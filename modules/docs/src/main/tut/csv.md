---
layout: docs
title:  "CSV Model"
position: 1
---

# CSV Model

Cormorants CSV Model is a simple protocol for dealing with a fairly simple specification that has
several small points of difficulty that need to be addressed. The primary component of this model
is a sealed trait called CSV which represent valid components and constructors of CSV's.

It important to note that the specification tells us to treat all white space as significant and a
part of the header/field so modifying and managing your whitespace may need to happen within
model transformations before `Read` or after `Write` if you have custom white space needs.

The specification tells us that each row should have the same number of columns but the
specification gives no way to specify this. In this model, this is capable of summarizing all
allowable states so we have opted towards permissiveness which following it more directly
in all the concrete implementations.

First we handle imports..

```tut:silent
import io.chrisdavenport.cormorant.CSV
```

## CSV.Complete

A `CSV.Complete` is what we generally think of as a CSV. It has a `Headers` row, and a `Rows`.
This means that we are able to use the headers in this piece of the model to build our types,
something we are unable to do with any of the other components of the model.

```tut:book
CSV.Complete(
  CSV.Headers(
    List(CSV.Header("Favorite Color"), CSV.Header("Favorite Number"))
  ),
  CSV.Rows(
    List(
      CSV.Row(List(CSV.Field("Green"), CSV.Field("1"))),
      CSV.Row(List(CSV.Field("Yellow"), CSV.Field("3")))
    )
  )
)
```

## CSV.Rows

A `CSV.Rows` consists of a `List` of rows. With this we can construct structural matches to
a given type at each row for conversion from the model.

```tut:book
CSV.Rows(
  List(
      CSV.Row(List(CSV.Field("Monkey"), CSV.Field("9"))),
      CSV.Row(List(CSV.Field("Dog"), CSV.Field("1")))
    )
)
```

## CSV.Headers

A `CSV.Headers` is a `List` of `CSV.Header` which is something we can construct for positional
guidance or can be used to extract values out of a CSV.

```tut:book
CSV.Headers(
  List(CSV.Header("Favorite Color"), CSV.Header("Favorite Number"))
)
```

## CSV.Row

## CSV.Header

## CSV.Field
