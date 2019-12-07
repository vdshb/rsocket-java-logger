plugins {
    kotlin("jvm") version "1.3.61"
}

group = "pro.vdshb"
version = "0.6"

repositories {
    mavenCentral()
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