plugins {
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.dokka") version "0.10.0"
    `maven-publish`
    signing
}

group = "pro.vdshb"
version = "0.7"

repositories {
    mavenCentral()
    maven(url = "https://dl.bintray.com/kotlin/dokka")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.rsocket:rsocket-core:1.0.0-RC5")
    implementation("org.slf4j:slf4j-api:1.7.29")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

//////////////////////////////// SONATYPE PUBLICATION ///////////////////////////////////
publishing {
    val ossrhUsername: String by project
    val ossrhPassword: String by project

    repositories {
        val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
        val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        val repoUrl = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        maven(url = repoUrl) {
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
    publications {
        create<MavenPublication>("mavenJava") {

            val binaryJar = components["java"]

            val dokkaJavadoc by tasks.creating(org.jetbrains.dokka.gradle.DokkaTask::class) {
                outputFormat = "javadoc"
                outputDirectory = "$buildDir/javadoc"
            }

            val javadocJar by tasks.creating(Jar::class) {
                dependsOn(dokkaJavadoc)
                archiveClassifier.set("javadoc")
                from("$buildDir/javadoc")
            }

            val sourcesJar by tasks.creating(Jar::class) {
                archiveClassifier.set("sources")
                from(sourceSets["main"].allSource)
            }

            artifacts {
                add("archives", javadocJar)
                add("archives", sourcesJar)
            }

            from(binaryJar)
            artifact(sourcesJar)
            artifact(javadocJar)

            pom {
                name.set("rsocket-java logger")
                description.set("Logger for rsocket-java library")
                url.set("https://github.com/vdshb/rsocket-java-logger")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("vdshb")
                        name.set("Vadim Shabanov")
                        email.set("opensource@vdshb.pro")
                    }
                }
                scm {
                    connection.set("scm:https://github.com/vdshb/rsocket-java-logger.git")
                    developerConnection.set("scm:https://github.com/vdshb")
                    url.set("https://github.com/vdshb/rsocket-java-logger")
                }
            }
        }
    }
//    afterEvaluate {
//        signing {
//            sign(publishing.publications["mavenJava"])
//        }
//    }
}
