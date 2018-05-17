import sbt.Resolver

enablePlugins(JavaAppPackaging)

name := "auth-service"
organization := "com.github.zheniatrochun"
version := "1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val scalaTestV  = "2.2.6"
  val akkaHttpVersion = "10.0.9"
  val akkaV = "2.5.3"
  val slickV = "3.2.0"

  Seq(
    "io.jsonwebtoken" % "jjwt" % "0.9.0",
    "com.typesafe.akka"           %% "akka-actor"                           % akkaV,
    "com.typesafe.akka"           %% "akka-stream"                          % akkaV,
    "com.typesafe.akka"           %% "akka-http"                            % akkaHttpVersion,
    "com.typesafe.akka"           %% "akka-http-spray-json"                 % akkaHttpVersion,
    "com.typesafe.akka"           %% "akka-http-testkit"                    % akkaHttpVersion,
    "com.typesafe.akka"           %% "akka-testkit"                         % akkaV,
    "com.jason-goodwin"           %% "authentikat-jwt"                      % "0.4.5",
    "com.github.t3hnar"           %% "scala-bcrypt"                         % "3.0",
    "com.typesafe.slick"          %% "slick"                                % slickV,
    "com.typesafe.slick"          %% "slick-hikaricp"                       % slickV,
    "com.byteslounge"             %% "slick-repo"                           % "1.4.3",
    "org.postgresql"               % "postgresql"                           % "42.2.1",
    "com.h2database"               % "h2"                                   % "1.3.175",
    "net.debasishg"               %% "redisclient"                          % "3.5",
    "joda-time"                    % "joda-time"                            % "2.9.9",
    "org.joda"                     % "joda-convert"                         % "1.8.1",
    "com.typesafe.scala-logging"  %% "scala-logging"                        % "3.1.0",
    "ch.qos.logback"               % "logback-classic"                      % "1.1.3",
    "org.scalatest"               %% "scalatest"                            % scalaTestV    % "test"
  )
}

Revolver.settings
resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += "typesave" at "http://repo.typesafe.com/typesafe/releases"