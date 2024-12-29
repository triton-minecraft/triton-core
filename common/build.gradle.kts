plugins {
    id("java")
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:mongodb-driver-sync:5.2.1")
}
