package io.chrisdavenport.cormorant.generic

import io.chrisdavenport.cormorant._
import shapeless._

/**
 * Fully Automatic Derivation of Any Product Type
  **/
object auto 
  extends internal.LabelledReadProofs 
  with internal.LabelledWriteProofs
  with internal.ReadProofs
  with internal.WriteProofs
  with internal.TaggedProofs {

  implicit def deriveWrite[A, R](
    implicit gen: Generic.Aux[A, R],
    enc: Write[R]
  ): Write[A] = semiauto.deriveWrite[A, R]

  implicit def deriveLabelledWrite[A, H <: HList](
    implicit gen: LabelledGeneric.Aux[A, H],
    hlw: Lazy[LabelledWrite[H]]
  ): LabelledWrite[A] = semiauto.deriveLabelledWrite[A, H]

  implicit def deriveRead[A, R](
    implicit gen: Generic.Aux[A, R],
    R: Lazy[Read[R]]
  ): Read[A] = semiauto.deriveRead[A, R]

  implicit def deriveLabelledRead[A, H <: HList](
    implicit gen: LabelledGeneric.Aux[A, H],
    hlw: Lazy[LabelledRead[H]]): LabelledRead[A] = semiauto.deriveLabelledRead

}
