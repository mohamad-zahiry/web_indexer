plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.webindexer"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Package(ktor): Build user friendly API
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.7")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.7")
    implementation("io.ktor:ktor-server-default-headers-jvm:2.3.7")
    // Package(skrapeit): Extract webpage data from fetched HTML
    implementation("it.skrape:skrapeit:1.1.5")
    // Package(lucene core): Index documents for searching
    implementation("org.apache.lucene:lucene-core:9.9.1")
}

kotlin {
    jvmToolchain(17)
}