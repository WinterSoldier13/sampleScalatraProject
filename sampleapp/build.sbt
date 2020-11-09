val ScalatraVersion = "2.7.0"

organization := "com.github.wintersoldier13"

name := "SampleApp"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.1"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.28.v20200408" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
)
//ScalaLikeJDBC
libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "3.5.0",
  "com.h2database"  %  "h2"                % "1.4.200",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
)
//MYSQL driver
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.22" % Runtime

////SWAGGER
//libraryDependencies ++= Seq(  "org.scalatra" %% "scalatra-json" % "2.3.2", "org.json4s"   %% "json4s-native" % "3.2.11",)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
