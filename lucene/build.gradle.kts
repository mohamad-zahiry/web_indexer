plugins {
    kotlin("jvm")
}

group = "webindexer.lucene"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.lucene:lucene-core:9.9.1")
    implementation("org.apache.lucene:lucene-queryparser:9.9.1")
}

kotlin {
    jvmToolchain(17)
}