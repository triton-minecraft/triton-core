plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    maven("https://repo.hypera.dev/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":common"))
    implementation("net.minestom:minestom-snapshots:698af959c8")
    implementation("ch.qos.logback:logback-classic:1.5.3")
    implementation("dev.lu15:luckperms-minestom:5.4-SNAPSHOT")
    implementation("org.mongodb:mongodb-driver-legacy:4.5.0")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Minestom has a minimum Java version of 21
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dev.kyriji.minestom.DevServer" // Change this to your main class
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("") // Prevent the -all suffix on the shadowjar file.
    }
}