plugins {
    kotlin("jvm")
}

group = "webindexer.crawler"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("it.skrape:skrapeit:1.1.5")
}

kotlin {
    jvmToolchain(17)
}