plugins {
    kotlin("jvm") version "1.9.21"
}

group = "webindexer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Local modules
    implementation(project(":crawler"))
    implementation(project(":lucene"))
    // Third party modules
    implementation(platform("org.http4k:http4k-bom:5.12.2.1"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-format-jackson")
}

kotlin {
    jvmToolchain(17)
}