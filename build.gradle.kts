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
    implementation(project("crawler")) {
        implementation("it.skrape:skrapeit:1.1.5")
    }
    implementation(project("lucene")) {
        implementation("org.apache.lucene:lucene-core:9.9.1")
        implementation("org.apache.lucene:lucene-queryparser:9.9.1")
    }
    // Third party modules
    implementation(platform("org.http4k:http4k-bom:5.12.2.1"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-format-jackson")
}

kotlin {
    jvmToolchain(17)
}