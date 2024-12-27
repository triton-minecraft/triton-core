plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(project(":common"))
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
}

tasks.shadowJar {
    archiveClassifier.set("") // This removes the '-all' suffix from the output jar
}

tasks.build {
    dependsOn(tasks.shadowJar)
}