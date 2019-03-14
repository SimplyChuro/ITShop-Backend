name := "ITShop"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.8"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)


// Default
libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "1.4.197"


// Database
libraryDependencies += javaJdbc
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"


// Amazon Web Services
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.475"
libraryDependencies += "net.java.dev.jets3t" % "jets3t" % "0.9.4"


// Mail
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"