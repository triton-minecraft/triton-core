rootProject.name = "triton-core"
include("common")
include("spigot")
include("velocity")
include("minestom")
include("fabric")

project(":spigot").name = "triton-core-spigot"
project(":velocity").name = "triton-core-velocity"
project(":minestom").name = "triton-core-minestom"
project(":fabric").name = "triton-core-fabric"

pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
    }
}