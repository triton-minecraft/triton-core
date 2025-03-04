buildscript {
    repositories {
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        mavenCentral()
    }
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    id("eclipse")
    id("idea")
    id("net.minecraftforge.gradle") version "[6.0.16,6.2)"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}


apply(plugin = "org.spongepowered.mixin")

group = project.property("mod_group_id") as String
version = project.property("mod_version") as String

base {
    archivesName = project.property("mod_id") as String
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

minecraft {
    val mapping_channel: String by project
    val mapping_version: String by project

    mappings(mapping_channel, mapping_version)

    reobf = false
    copyIdeResources.set(true)

    runs {
        configureEach {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
        }

        create("client") {
            property("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("server") {
            property("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
            args("--nogui")
        }

        create("gameTestServer") {
            property("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
        }

        create("data") {
            workingDirectory(project.file("run-data"))
            args(
                "--mod", project.property("mod_id") as String,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }
    }
}

extensions.configure<org.spongepowered.asm.gradle.plugins.MixinExtension> {
    add(sourceSets.main.get(), "${project.property("mod_id")}.refmap.json")
    config("${project.property("mod_id")}.mixins.json")
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
    }
}

repositories {
}

dependencies {
    implementation(project(":common"))
    compileOnly("net.luckperms:api:5.4")

    val minecraft_version: String by project
    val forge_version: String by project
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    implementation("net.sf.jopt-simple:jopt-simple:5.0.4") {
        version {
            strictly("5.0.4")
        }
    }
}

tasks.named<ProcessResources>("processResources") {
    val minecraft_version: String by project
    val minecraft_version_range: String by project
    val forge_version: String by project
    val forge_version_range: String by project
    val loader_version_range: String by project
    val mod_id: String by project
    val mod_name: String by project
    val mod_license: String by project
    val mod_version: String by project
    val mod_authors: String by project
    val mod_description: String by project

    val replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "forge_version" to forge_version,
        "forge_version_range" to forge_version_range,
        "loader_version_range" to loader_version_range,
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_license" to mod_license,
        "mod_version" to mod_version,
        "mod_authors" to mod_authors,
        "mod_description" to mod_description
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + mapOf("project" to project))
    }
}

tasks.named<Jar>("jar") {
    dependsOn("shadowJar")
    manifest {
        attributes(mapOf(
            "Specification-Title" to project.property("mod_id"),
            "Specification-Vendor" to project.property("mod_authors"),
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to project.property("mod_authors"),
        ))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    dependencies {
        exclude(".*:.*:.*")
        include(project(":common"))
    }
}

sourceSets.forEach {
    val dir = layout.buildDirectory.dir("sourcesSets/${it.name}")
    it.output.resourcesDir = dir.get().asFile
    it.java.destinationDirectory.set(dir)
}