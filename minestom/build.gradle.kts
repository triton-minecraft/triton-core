plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("net.minestom:minestom-snapshots:698af959c8")
    implementation("ch.qos.logback:logback-classic:1.5.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Minestom has a minimum Java version of 21
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.example.Main" // Change this to your main class
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