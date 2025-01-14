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
    implementation("com.github.big-minecraft:bmc-api:97074e9964")
    implementation("org.mongodb:mongodb-driver-legacy:4.5.0")
    compileOnly("net.luckperms:api:5.4")
}
