plugins {
    id("java")
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.big-minecraft:bmc-api:7563425c0b")
    compileOnly("org.mongodb:mongodb-driver-legacy:4.5.0")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.google.code.gson:gson:2.8.8")
}
