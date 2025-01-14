plugins {
    id("java")
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.mongodb:mongodb-driver-legacy:4.5.0")
    compileOnly("net.luckperms:api:5.4")
}
