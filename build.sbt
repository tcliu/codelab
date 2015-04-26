// import play.Project._

name := "liteapp"

version := "1.0.0"

scalaVersion := "2.10.3"

retrieveManaged := true

// playScalaSettings
       
val springVersion = "4.0.0.RELEASE"
val springBootVersion = "0.5.0.M7"
val slf4jVersion = "1.7.5"
val logbackVersion = "1.0.13"
val jettyVersion = "8.1.14.v20131031"
// val jettyVersion = "9.1.0.v20131115"

resolvers ++= Seq(
	"Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
	"Spring IO Repository" at "http://repo.spring.io/libs-milestone",
	"Spring IO Snapshots" at  "http://repo.spring.io/snapshot",
	"Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
	"Nexus Repository" at "https://code.lds.org/nexus/content/groups/main-repo"
)

// Scala
libraryDependencies ++= Seq(
	"org.scala-lang" % "scala-library" % scalaVersion.value,
	"org.scala-lang" % "scala-reflect" % scalaVersion.value,
	"com.jsuereth" %% "scala-arm" % "1.3"
)

// Apache Commons
libraryDependencies ++= Seq(
	"org.apache.commons" % "commons-lang3" % "3.1",
	"commons-beanutils" % "commons-beanutils" % "1.8.3",
	"commons-cli" % "commons-cli" % "1.2",
	"commons-collections" % "commons-collections" % "3.2.1",
	"commons-io" % "commons-io" % "2.4",
	"commons-fileupload" % "commons-fileupload" % "1.3"
)

// Guava
libraryDependencies ++= "com.google.guava" % "guava" % "15.0"	
	
// Spring
libraryDependencies ++= Seq(
	"org.springframework" % "spring-aop" % springVersion,
	"org.springframework" % "spring-core" % springVersion,
	"org.springframework" % "spring-beans" % springVersion,
	"org.springframework" % "spring-context" % springVersion,
	"org.springframework" % "spring-jdbc" % springVersion,
	"org.springframework" % "spring-messaging" % springVersion,
	"org.springframework" % "spring-tx" % springVersion,
	"org.springframework" % "spring-web" % springVersion,
	"org.springframework" % "spring-webmvc" % springVersion,
	"org.springframework" % "spring-websocket" % springVersion
)

// Spring Boot
libraryDependencies ++= Seq(
	"org.springframework.boot" % "spring-boot-starter" % springBootVersion excludeAll( 
		ExclusionRule(organization = "org.springframework.boot", artifact = "spring-boot-starter-tomcat"),
		ExclusionRule(artifact = "javax.servlet-api")
	),
	"org.springframework.boot" % "spring-boot-starter-jetty" % springBootVersion % "runtime",
	"org.springframework.boot" % "spring-boot-starter-actuator" % springBootVersion % "runtime",
	"org.springframework.boot" % "spring-boot-starter-security" % springBootVersion % "runtime"
)

// Java EE
libraryDependencies ++= Seq(
	"javax.servlet" % "javax.servlet-api" % "3.1.0"
)

// Data conversion
libraryDependencies ++= Seq(
	"com.fasterxml.jackson.core" % "jackson-databind" % "2.3.0",
	"com.fasterxml" % "jackson-module-scala" % "1.9.3" excludeAll(
		ExclusionRule(
	),
	"com.lambdaworks" %% "jacks" % "2.2.3",
	"org.apache.poi" % "poi" % "3.10-beta2",
	"org.apache.poi" % "poi-ooxml" % "3.10-beta2",
	"net.sf.opencsv" % "opencsv" % "2.3"
)

// Templating
libraryDependencies ++= Seq(
	"org.thymeleaf" % "thymeleaf-spring4" % "2.1.2.RELEASE",
	"org.lesscss" % "lesscss" % "1.3.3"
)

// JDBC
libraryDependencies ++= Seq(
	"com.oracle" % "ojdbc6" % "11.2.0.3"
)

// Jetty
libraryDependencies ++= Seq(
	"org.eclipse.jetty" % "jetty-util" % jettyVersion,
	"org.eclipse.jetty" % "jetty-webapp" % jettyVersion,
	"org.eclipse.jetty" % "jetty-jsp" % jettyVersion,
	"org.eclipse.jetty" % "jetty-servlet" % jettyVersion,
	"org.eclipse.jetty" % "jetty-server" % jettyVersion,
	"org.eclipse.jetty" % "jetty-security" % jettyVersion,
	"org.eclipse.jetty" % "jetty-plus" % jettyVersion
)

// Logging
libraryDependencies ++= Seq(
	"org.slf4j" % "slf4j-api" % slf4jVersion,
	"org.slf4j" % "slf4j-ext" % slf4jVersion,
	"ch.qos.logback" % "logback-classic" % logbackVersion % "runtime",
	"com.typesafe" %% "scalalogging-slf4j" % "1.1.0-SNAPSHOT",
	"commons-logging" % "commons-logging" % "1.1.3" % "runtime"
)

// Testing
libraryDependencies ++= Seq(
	"junit" % "junit" % "4.11" % "test",
	"org.scalatest" %% "scalatest" % "2.0" % "test"
)
	