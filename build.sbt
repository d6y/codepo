name := "Code Point"

version := "1.1.0"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

seq(webSettings :_*)

libraryDependencies ++= {
  Seq(
	  "org.specs2" %% "specs2" % "2.0" % "test"
  )
}

EclipseKeys.withSource := true

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

