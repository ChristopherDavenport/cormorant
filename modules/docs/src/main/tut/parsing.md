---
layout: docs
title:  "Parsing CSV"
position: 2
---

# Parsing CSV's

Cormorant includes a parsing modules which is a wrapper around an [atto](http://tpolecat.github.io/atto/) parser for CSV's.

Parsing is not part of the `cormorant-core` module, so you will need to include a dependency on the `cormorant-parser` module in your build:

```scala
libraryDependencies += "io.chrisdavenport" %% "cormorant-parser" % cormorantVersion
```

Parsing is done like the following.

```tut:book
import io.chrisdavenport.cormorant._, io.chrisdavenport.cormorant.parser._

val rawCSV : String = """header1,header2,header3,header4
yellow,3,teacher,monkey
green,6,carpenter,elephant
"""

val parseResult = parseComplete(rawCSV)
```

However parsing might fail, the result is an `Either` with an `io.chrisdavenport.cormorant.Error.ParseFailure` on the left side.
In the example above, the input was a valid CSV, so the result was a `Right` containing the CSV representation. We use `Either` as this is a pure way to represent our failure states.

However, what happens when we parse an invalid csv:
```tut:book
val badCSV : String = "I'm not a CSV"

parseComplete(badCSV)
```

As we mentioning in the section on our model, there are several components you may be looking to parse.

```tut:book
// parseHeaders - A List of Headers
val headers: String = "Title,Name,Phone Number,Extension"
parseHeaders(headers)

// parseRow - A list of fields
val row : String = "yellow,green,blue,magenta"
parseRow(row)

// parseRows - A list of rows
val rows: String = """yellow,1,monkey
green,2,giraffe
blue,3,lion"""
parseRows(rows)
```