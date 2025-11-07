plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.morphox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(22)
}