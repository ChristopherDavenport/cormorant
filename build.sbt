val Scala213 = "2.13.5"

ThisBuild / crossScalaVersions := Seq("2.12.13", Scala213)
ThisBuild / scalaVersion := crossScalaVersions.value.last

ThisBuild / githubWorkflowArtifactUpload := false

val Scala213Cond = s"matrix.scala == '$Scala213'"

def rubySetupSteps(cond: Option[String]) = Seq(
  WorkflowStep.Use(
    UseRef.Public("ruby", "setup-ruby", "v1"),
    name = Some("Setup Ruby"),
    params = Map("ruby-version" -> "2.6.0"),
    cond = cond),

  WorkflowStep.Run(
    List(
      "gem install saas",
      "gem install jekyll -v 3.2.1"),
    name = Some("Install microsite dependencies"),
    cond = cond))

ThisBuild / githubWorkflowBuildPreamble ++=
  rubySetupSteps(Some(Scala213Cond))

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(List("test", "mimaReportBinaryIssues")),

  WorkflowStep.Sbt(
    List("docs/makeMicrosite"),
    cond = Some(Scala213Cond)))

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")

// currently only publishing tags
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(RefPredicate.StartsWith(Ref.Tag("v")))

ThisBuild / githubWorkflowPublishPreamble ++=
  WorkflowStep.Use(UseRef.Public("olafurpg", "setup-gpg", "v3")) +: rubySetupSteps(None)

ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    List("ci-release"),
    name = Some("Publish artifacts to Sonatype"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}")),

  WorkflowStep.Sbt(
    List(s"++$Scala213", "docs/publishMicrosite"),
    name = Some("Publish microsite")
  )
)

inThisBuild(List(
  organization := "io.chrisdavenport",
  homepage := Some(url("https://github.com/ChristopherDavenport/cormorant")),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  developers := List(
    Developer(
      "ChristopherDavenport",
      "Christopher Davenport",
      "chris@christopherdavenport.tech",
      url("https://www.github.com/ChristopherDavenport")
    )
  )
))


lazy val cormorant = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .settings(skip in publish := true)
  .settings(commonSettings)
  .aggregate(core, generic, parser, refined, fs2, http4s, docs)


val catsV = "2.1.1"
val catsEffectV = "3.1.1"
val catsEffectTestV = "1.1.0"
val fs2V = "3.0.4"
val shapelessV = "2.3.3"
val http4sV = "0.23.0-RC1"
val catsScalacheckV = "0.3.0"
val munitV = "0.7.26"
val munitCatsEffectV = "1.0.3"
val scalacheckEffectV = "1.0.2"

lazy val core = project.in(file("modules/core"))
  .settings(commonSettings)
  .settings(
    name := "cormorant-core"
  )

lazy val generic = project.in(file("modules/generic"))
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "cormorant-generic",
    libraryDependencies ++= Seq(
      "com.chuusai" %% "shapeless" % "2.3.3"
    )
  )

lazy val parser = project.in(file("modules/parser"))
  .settings(commonSettings)
  .dependsOn(core % "compile;test->test")
  .settings(
    name := "cormorant-parser",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "atto-core" % "0.9.5"
    )
  )

lazy val refined = project.in(file("modules/refined"))
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "cormorant-refined",
    libraryDependencies ++= Seq(
      "eu.timepit" %% "refined" % "0.9.20",
    )
  )

lazy val fs2 = project.in(file("modules/fs2"))
  .settings(commonSettings)
  .dependsOn(core % "compile;test->test", parser)
  .settings(
    name := "cormorant-fs2",
    libraryDependencies ++= Seq(
      "co.fs2" %% "fs2-core" % fs2V,
      "co.fs2" %% "fs2-io"   % fs2V % Test
    )
  )

lazy val http4s = project.in(file("modules/http4s"))
  .settings(commonSettings)
  .dependsOn(core % "compile;test->test", parser, fs2)
  .settings(
    name := "cormorant-http4s",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-core"   % http4sV,
      "org.http4s" %% "http4s-dsl"    % http4sV % Test,
      "org.http4s" %% "http4s-client" % http4sV % Test
    )
  )

lazy val docs = project.in(file("modules"))
  .disablePlugins(MimaPlugin)
  .settings(skip in publish := true)
  .settings(commonSettings)
  .dependsOn(core, generic, parser, refined, fs2, http4s)
  .enablePlugins(MicrositesPlugin)
  .enablePlugins(MdocPlugin)
  .settings{
    import microsites._
    Seq(
      micrositeName := "cormorant",
      micrositeDescription := "CSV Library for Scala",
      micrositeAuthor := "Christopher Davenport",
      micrositeGithubOwner := "ChristopherDavenport",
      micrositeGithubRepo := "cormorant",
      micrositeBaseUrl := "/cormorant",
      micrositeDocumentationUrl := "https://www.javadoc.io/doc/io.chrisdavenport/cormorant-core_2.12",
      micrositeFooterText := None,
      micrositeHighlightTheme := "atom-one-light",
      micrositePalette := Map(
        "brand-primary" -> "#3e5b95",
        "brand-secondary" -> "#294066",
        "brand-tertiary" -> "#2d5799",
        "gray-dark" -> "#49494B",
        "gray" -> "#7B7B7E",
        "gray-light" -> "#E5E5E6",
        "gray-lighter" -> "#F4F3F4",
        "white-color" -> "#FFFFFF"
      ),
      libraryDependencies += "com.47deg" %% "github4s" % "0.28.1",
      micrositePushSiteWith := GitHub4s,
      micrositeGithubToken := sys.env.get("GITHUB_TOKEN"),
      micrositeExtraMdFiles := Map(
        file("CHANGELOG.md")        -> ExtraMdFileConfig("changelog.md", "page", Map("title" -> "changelog", "section" -> "changelog", "position" -> "100")),
        file("CODE_OF_CONDUCT.md")  -> ExtraMdFileConfig("code-of-conduct.md",   "page", Map("title" -> "code of conduct",   "section" -> "code of conduct",   "position" -> "101")),
        file("LICENSE")             -> ExtraMdFileConfig("license.md",   "page", Map("title" -> "license",   "section" -> "license",   "position" -> "102"))
      )
    )
  }

// General Settings
lazy val commonSettings = Seq(
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  testFrameworks += new TestFramework("munit.Framework"),

  libraryDependencies ++= Seq(
    "org.typelevel"               %% "cats-core"                  % catsV,
    "org.typelevel"               %% "cats-effect"                % catsEffectV,
    "org.scalameta"               %% "munit"                      % munitV        % Test,
    "org.scalameta"               %% "munit-scalacheck"           % munitV        % Test,
    "org.typelevel"               %% "munit-cats-effect-3"        % munitCatsEffectV % Test,
    "org.typelevel"               %% "scalacheck-effect-munit"    % scalacheckEffectV % Test,
    "io.chrisdavenport"           %% "cats-scalacheck"            % catsScalacheckV % Test,
  )
)
