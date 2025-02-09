plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("maven-publish")
}

group = "dev.kyriji"
version = "0.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.hypera.dev/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":common"))
    compileOnly("net.minestom:minestom-snapshots:698af959c8")
//    compileOnly("ch.qos.logback:logback-classic:1.5.3")
    compileOnly("dev.lu15:luckperms-minestom:5.4-SNAPSHOT")
    compileOnly("org.mongodb:mongodb-driver-legacy:4.5.0")
    compileOnly("com.zaxxer:HikariCP:4.0.3")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:3.3.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dev.kyriji.minestom.TritonCoreMinestom"
            attributes["Module-Name"] = "triton-core"
            attributes["Module-Dependencies"] = "triton-dependencies"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            project.shadow.component(this)

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}