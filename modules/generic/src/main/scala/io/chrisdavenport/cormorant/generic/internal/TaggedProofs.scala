package io.chrisdavenport.cormorant.generic.internal

import io.chrisdavenport.cormorant.{Get, Put}
import shapeless.tag.@@
import cats.implicits._
import io.chrisdavenport.cormorant.implicits._
import shapeless.tag

import java.util.UUID

trait TaggedProofs {
  implicit def taggedStringGet[T]: Get[String @@ T] =
    stringGet.map(tag[T][String](_))

  implicit def taggedStringPut[T]: Put[String @@ T] =
    stringPut.contramap(_.asInstanceOf[String])

  implicit def taggedBoolGet[T]: Get[Boolean @@ T] =
    boolGet.map(tag[T][Boolean](_))

  implicit def taggedBoolPut[T]: Put[Boolean @@ T] =
    boolPut.contramap(_.asInstanceOf[Boolean])

  implicit def taggedCharGet[T]: Get[Char @@ T] =
    charGet.map(tag[T][Char](_))

  implicit def taggedCharPut[T]: Put[Char @@ T] =
    charPut.contramap(_.asInstanceOf[Char])

  implicit def taggedFloatGet[T]: Get[Float @@ T] =
    floatGet.map(tag[T][Float](_))

  implicit def taggedFloatPut[T]: Put[Float @@ T] =
    floatPut.contramap(_.asInstanceOf[Float])

  implicit def taggedDoubleGet[T]: Get[Double @@ T] =
    doubleGet.map(tag[T][Double](_))

  implicit def taggedDoublePut[T]: Put[Double @@ T] =
    doublePut.contramap(_.asInstanceOf[Double])

  implicit def taggedIntGet[T]: Get[Int @@ T] =
    intGet.map(tag[T][Int](_))

  implicit def taggedIntPut[T]: Put[Int @@ T] =
    intPut.contramap(_.asInstanceOf[Int])

  implicit def taggedByteGet[T]: Get[Byte @@ T] =
    byteGet.map(tag[T][Byte](_))

  implicit def taggedBytePut[T]: Put[Byte @@ T] =
    bytePut.contramap(_.asInstanceOf[Byte])

  implicit def taggedShortGet[T]: Get[Short @@ T] =
    shortGet.map(tag[T][Short](_))

  implicit def taggedShortPut[T]: Put[Short @@ T] =
    shortPut.contramap(_.asInstanceOf[Short])

  implicit def taggedLongGet[T]: Get[Long @@ T] =
    longGet.map(tag[T][Long](_))

  implicit def taggedLongPut[T]: Put[Long @@ T] =
    longPut.contramap(_.asInstanceOf[Long])

  implicit def taggedBigIntGet[T]: Get[BigInt @@ T] =
    bigIntGet.map(tag[T][BigInt](_))

  implicit def taggedBigIntPut[T]: Put[BigInt @@ T] =
    bigIntPut.contramap(_.asInstanceOf[BigInt])

  implicit def taggedBigDecimalGet[T]: Get[BigDecimal @@ T] =
    bigDecimalGet.map(tag[T][BigDecimal](_))

  implicit def taggedBigDecimalPut[T]: Put[BigDecimal @@ T] =
    bigDecimalPut.contramap(_.asInstanceOf[BigDecimal])

  implicit def taggedUuidGet[T]: Get[UUID @@ T] =
    uuidGet.map(tag[T][UUID](_))

  implicit def taggedUuidPut[T]: Put[UUID @@ T] =
    uuidPut.contramap(_.asInstanceOf[UUID])
}
