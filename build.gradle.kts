plugins {
    id("java")
    id("com.gtnewhorizons.retrofuturagradle") version "1.4.0"
}

group = "cn.davidma.tinymobfarm"
version = "1.0.6-ce"

base {
    archivesName.set("TinyMobFarm-CE")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

minecraft {
    mcVersion.set("1.12.2")
    username.set("Developer")
}

repositories {
    maven {
        name = "GTNH Maven"
        url = uri("https://nexus.gtnewhorizons.com/repository/public/")
    }
    mavenCentral()
}

dependencies {
    patchedMinecraft("me.eigenraven.java8unsupported:java-8-unsupported-shim:1.0.0")
}

tasks.processResources.configure {
    inputs.property("version", project.version)
    inputs.property("mcversion", minecraft.mcVersion.get())

    filesMatching("mcmod.info") {
        expand(
            mapOf(
                "version" to project.version,
                "mcversion" to minecraft.mcVersion.get()
            )
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}
