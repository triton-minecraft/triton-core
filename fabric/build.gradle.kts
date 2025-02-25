plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("fabric") {
            sourceSet(sourceSets["main"])
            sourceSet(sourceSets["client"])
        }
    }
}

repositories {
    // Add repositories for dependencies here if needed.
}

dependencies {
    implementation(project(":common"))
    compileOnly("net.luckperms:api:5.4")

    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modCompileOnly("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            mapOf(
                "version" to project.version,
                "minecraft_version" to project.property("minecraft_version"),
                "loader_version" to project.property("loader_version")
            )
        )
    }
}

val targetJavaVersion = 21
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    withSourcesJar()
}

tasks {
    jar {
        from("LICENSE") {
            rename { "${it}_${project.property("archivesBaseName")}" }
        }
    }

    // First make a dev JAR with embedded dependencies
    shadowJar {
        archiveClassifier.set("unmapped")
        archiveVersion.set("")

        mergeServiceFiles()

        dependencies {
            exclude(".*:.*:.*") // Exclude all dependencies
            include(project(":common")) // Include only the common module
        }
    }

    // Create a dedicated remap task for the shadow jar
    val remapShadowJar = register<net.fabricmc.loom.task.RemapJarTask>("remapShadowJar") {
        input.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set("")
    }

    build {
        dependsOn(remapJar)
        dependsOn(remapShadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }

        create<MavenPublication>("shadow") {
            project.shadow.component(this)
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()
        }
    }
}
