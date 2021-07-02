package io.chrisdavenport.cormorant.generic

import io.chrisdavenport.cormorant._
import shapeless._

object semiauto 
  extends internal.LabelledReadProofs 
  with internal.LabelledWriteProofs
  with internal.ReadProofs
  with internal.WriteProofs
  with internal.TaggedProofs {

  def deriveWrite[A, R](
      implicit gen: Generic.Aux[A, R],
      enc: Write[R]
  ): Write[A] = new Write[A] {
    def write(a: A): CSV.Row = Write[gen.Repr].write(gen.to(a))
  }


  def deriveLabelledWrite[A, H <: HList](
      implicit gen: LabelledGeneric.Aux[A, H],
      hlw: Lazy[LabelledWrite[H]]): LabelledWrite[A] = new LabelledWrite[A] {
    val writeH: LabelledWrite[H] = hlw.value
    def headers: CSV.Headers = writeH.headers
    def write(a: A): CSV.Row = writeH.write(gen.to(a))
  }

  def deriveRead[A, R](
      implicit gen: Generic.Aux[A, R],
      R: Lazy[Read[R]]
  ): Read[A] = new Read[A] {
    def readPartial(a: CSV.Row): Either[Error.DecodeFailure, Either[(CSV.Row, A), A]] ={
      R.value.readPartial(a).map{
        case Left((csv, r)) => Left((csv, gen.from(r)))
        case Right(r) => Right(gen.from(r))
      }
    }
  }

  def deriveLabelledRead[A, H <: HList](
      implicit gen: LabelledGeneric.Aux[A, H],
      hlw: Lazy[LabelledRead[H]]): LabelledRead[A] = new LabelledRead[A] {
    val readH: LabelledRead[H] = hlw.value
    def read(a: CSV.Row, h: CSV.Headers): Either[Error.DecodeFailure, A] =
      readH.read(a, h).map(gen.from(_))
  }

}