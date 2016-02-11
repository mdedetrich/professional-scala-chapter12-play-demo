import com.typesafe.sbt.rjs.Import.RjsKeys
import sbt.Keys._
import sbt.Project.projectToRef

lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"

lazy val scalatagsV = "0.5.4"
lazy val reactV = "0.14.3"

lazy val server = (project in file("jvm")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(rjs, digest, scalaJSProd, gzip),
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  routesGenerator := InjectedRoutesGenerator,
  RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:")),
  RjsKeys.mainModule := "main",
  RjsKeys.webJarCdns := Map.empty,
  DigestKeys.algorithms += "md5",
  includeFilter in digest := "*.js",
  includeFilter in gzip := "*.html" || "*.css" || "*.js",
  libraryDependencies ++= Seq(
    filters,
    cache
  )
).enablePlugins(PlayScala,SbtWeb)
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(sharedJvm)
  

lazy val client = (project in file("js")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.1",
    "com.lihaoyi" %%% "scalarx" % "0.2.8",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
    "com.lihaoyi" %%% "upickle" % "0.3.4",
    "com.github.japgolly.scalajs-react" %%% "core" % "0.10.4"
  ),
  jsDependencies ++= Seq(
    "org.webjars.bower" % "react" % reactV / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
    "org.webjars.bower" % "react" % reactV / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "React"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsSettings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "scalatags" % scalatagsV
    )
  ).jvmSettings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "scalatags" % scalatagsV
    )
  ).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
