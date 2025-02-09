plugins {
    id("java")
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("dev.kyriji:bmc-api:0.0.0")
    compileOnly("org.mongodb:mongodb-driver-legacy:4.5.0")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.google.code.gson:gson:2.8.8")
}
