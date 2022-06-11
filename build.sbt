import sbt.Resolver

val jsScalaVersion = "2.13.8"
val jvmScalaVersion = "2.13.8"

val endpoints4sVersion = "1.7.0"
val akkaActorVersion = "2.6.15"
val akkaHttpVersion = "10.2.6"
val openApiVersion = "4.1.0"

val serverProjectName = "akkaHttpServer"

val LOCAL_HOST = "BASE_URL" -> "http://127.0.0.1:80"
val REMOTE_HOST = "BASE_URL" -> "http://127.0.0.1:80"

lazy val shared =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .enablePlugins(BuildInfoPlugin)
    .settings(
      name := "shared",
      description := "",
      resolvers += Resolver.bintrayRepo("hmil", "maven"),
      libraryDependencies ++= Seq(
        "org.endpoints4s" %%% "algebra" % endpoints4sVersion,
        "org.endpoints4s" %%% "json-schema-generic" % endpoints4sVersion
      )
    )
    .jsSettings(scalaVersion := jsScalaVersion, buildInfoKeys ++= Seq[BuildInfoKey](LOCAL_HOST))
    .jvmSettings(scalaVersion := jvmScalaVersion, buildInfoKeys ++= Seq[BuildInfoKey](LOCAL_HOST))
val sharedJS = shared.js
val sharedJVM = shared.jvm

lazy val akkaHttpServer = (project in file(serverProjectName))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "akkaHttpServer",
    scalaVersion := jvmScalaVersion,
    description := "",
    resolvers += "Sonatype OSS Snapshots".at("https://oss.sonatype.org/content/repositories/snapshots"),
    maintainer := "rickyoung@optionax.com",
    Compile / resourceDirectory := baseDirectory.value / "src/main/resources",
    Compile / mainClass := Some("endpoints4s.akkahttp.server.MyApiServer"),
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.akka" %% "akka-actor-typed" % akkaActorVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaActorVersion,
      "org.endpoints4s" %% "akka-http-server" % "6.1.0",
      "org.endpoints4s" %% "openapi" % openApiVersion
    )
  )
  .dependsOn(sharedJVM)
val serverBaseDir: SettingKey[File] = akkaHttpServer / baseDirectory
val serverResourcesDir: String = "src/main/resources/assets/js"

lazy val akkaHttpClient = (project in file("akkaHttpClient"))
  .settings(
    name := "akkaHttpClient",
    scalaVersion := jvmScalaVersion,
    libraryDependencies ++= Seq(
      "org.endpoints4s" %% "akka-http-client" % "5.1.0",
      "com.typesafe.akka" %% "akka-stream" % akkaActorVersion
    )
  )
  .dependsOn(sharedJVM)

lazy val xhrClient = project
  .enablePlugins(ScalaJSPlugin)
  .settings(
    Compile / fastOptJS / artifactPath := serverBaseDir.value / serverResourcesDir / "xhrClient-opt.js",
    Compile / fullOptJS / artifactPath := serverBaseDir.value / serverResourcesDir / "xhrClient-opt.js",
    resolvers += Resolver.bintrayRepo("hmil", "maven"),
    Compile / mainClass := Some("endpoints4s.xhr.MyXhrApp"),
    scalaJSUseMainModuleInitializer := true,
    scalaVersion := jsScalaVersion,
    libraryDependencies ++= Seq(
      "org.endpoints4s" %%% "xhr-client" % "5.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.1.0"
    )
  )
  .dependsOn(sharedJS)

lazy val fetchClient = project
  .enablePlugins(ScalaJSPlugin)
  .settings(
    Compile / fastOptJS / artifactPath := serverBaseDir.value / serverResourcesDir / "fetchClient-opt.js",
    Compile / fullOptJS / artifactPath := serverBaseDir.value / serverResourcesDir / "fetchClient-opt.js",
    resolvers += Resolver.bintrayRepo("hmil", "maven"),
    Compile / mainClass := Some("endpoints4s.fetch.MyXhrApp"),
    scalaJSUseMainModuleInitializer := true,
    scalaVersion := jsScalaVersion,
    libraryDependencies ++= Seq(
      "org.endpoints4s" %%% "fetch-client" % "2.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.1.0"
    )
  )
  .dependsOn(sharedJS)

lazy val wxFacade = project
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaVersion := jsScalaVersion,
    libraryDependencies ++= Seq(
      "org.julienrf" %%% "faithful" % "2.0.0",
      "org.julienrf" %%% "faithful-cats" % "2.0.0"
    )
  )

lazy val endpoints4sWx = project
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaVersion := jsScalaVersion,
    libraryDependencies ++= Seq(
      "org.endpoints4s" %%% "algebra" % endpoints4sVersion,
      "org.endpoints4s" %%% "openapi" % openApiVersion
    )
  )

lazy val wxClient = project
  .enablePlugins(ScalaJSPlugin)
  .settings(
    Compile / fastOptJS / artifactPath := baseDirectory.value / "miniProgram" / "pages" / "main-opt.js",
    Compile / fullOptJS / artifactPath := baseDirectory.value / "miniProgram" / "pages" / "main-opt.js",
    name := "example",
    version := "0.1",
    resolvers += Resolver.bintrayRepo("hmil", "maven"),
    scalaVersion := jsScalaVersion,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.CommonJSModule)
    },
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies ++= Seq(
    )
  )
  .dependsOn(endpoints4sWx)
  .dependsOn(sharedJS)
  .dependsOn(wxFacade)
