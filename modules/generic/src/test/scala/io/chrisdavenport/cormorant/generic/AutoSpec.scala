package io.chrisdavenport.cormorant.generic

import cats.data._
import _root_.io.chrisdavenport.cormorant._
import _root_.io.chrisdavenport.cormorant.implicits._
import _root_.io.chrisdavenport.cormorant.generic.auto._
import shapeless.tag.@@
import shapeless.{tag => stag}

class AutoSpec extends munit.FunSuite {

  test("encode a row with Write automatically") {
    case class Example(i: Int, s: String, b: Int)

    val encoded = Example(1,"Hello",73).writeRow
    val expected = CSV.Row(NonEmptyList.of(CSV.Field("1"), CSV.Field("Hello"), CSV.Field("73")))
    assertEquals(encoded, expected)
  }

  test("encode a row with tagged types automatically") {
    sealed trait A
    sealed trait B
    sealed trait C

    case class Example(i: Int @@ A, s: String @@ B, b: Long @@ C)

    val encoded = Example(stag[A][Int](1), stag[B][String]("Hello"), stag[C][Long](73L)).writeRow
    val expected = CSV.Row(NonEmptyList.of(CSV.Field("1"), CSV.Field("Hello"), CSV.Field("73")))

    assertEquals(encoded, expected)
  }

  test("encode a comple with LabelledWrite automatically") {
    case class Example(i: Int, s: Option[String], b: Int)

    val encoded = List(Example(1, Option("Hello"), 73)).writeComplete
    val expected = CSV.Complete(
      CSV.Headers(NonEmptyList.of(CSV.Header("i"), CSV.Header("s"), CSV.Header("b"))),
      CSV.Rows(List(CSV.Row(NonEmptyList.of(CSV.Field("1"), CSV.Field("Hello"), CSV.Field("73")))))
    )
    assertEquals(encoded, expected)
  }

  test("read a row with read automatically") {
    case class Example(i: Int, s: Option[String], b: Int)
    val from = CSV.Row(NonEmptyList.of(CSV.Field("1"), CSV.Field("Hello"), CSV.Field("73")))
    val expected = Example(1, Some("Hello"), 73)
    assertEquals(from.readRow[Example], Right(expected))
  }

  test("read a row with tagged types automatically") {
    sealed trait A
    sealed trait B
    sealed trait C

    case class Example(i: Int @@ A, s: String @@ B, b: Long @@ C)
    val from = CSV.Row(NonEmptyList.of(CSV.Field("1"), CSV.Field("Hello"), CSV.Field("73")))

    val expected = Example(stag[A][Int](1), stag[B][String]("Hello"), stag[C][Long](73L))
    assertEquals(from.readRow[Example], Right(expected))
  }

  test("read a row with labelledread automatically") {
    import cats.syntax.either._

    case class Example(i: Int, s: Option[String], b: Int)

    // Notice That the order is different than the example above
    val fromCSV = CSV.Complete(
      CSV.Headers(NonEmptyList.of(CSV.Header("b"), CSV.Header("s"), CSV.Header("i"))),
      CSV.Rows(List(CSV.Row(NonEmptyList.of(CSV.Field("73"), CSV.Field("Hello"), CSV.Field("1")))))
    )

    val expected = List(Example(1, Option("Hello"), 73)).map(Either.right)
    assertEquals(fromCSV.readLabelled[Example], expected)
  }
}