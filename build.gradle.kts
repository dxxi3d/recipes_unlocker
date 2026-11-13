plugins {
    id("net.fabricmc.fabric-loom")
    id("me.modmuss50.mod-publish-plugin") version ("2.2.1")
}

repositories {
}

dependencies {
    minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
    implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")
}

tasks.processResources {
    val version = version
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    val projectName = project.name
    inputs.property("projectName", projectName)

    from("LICENSE") {
        rename { "${it}_$projectName" }
    }
}

publishMods {
    file = tasks.named<Jar>("jar").flatMap { it.archiveFile }
    displayName = "Recipes Unlocker ${project.version}"
    version = project.version.toString()
    changelog = providers.gradleProperty("changelog")
        .orElse(providers.environmentVariable("CHANGELOG"))
        .orElse("Release ${project.version}")
    type = STABLE
    modLoaders.add("fabric")
    dryRun = providers.gradleProperty("dryRun").map(String::toBoolean).orElse(false)

    curseforge {
        projectId = providers.gradleProperty("curseforge_id")
        accessToken = providers.gradleProperty("curseforge_token")
            .orElse(providers.gradleProperty("CURSEFORGE_TOKEN"))
            .orElse(providers.environmentVariable("CURSEFORGE_TOKEN"))
        minecraftVersions.addAll("26.1", "26.1.1", "26.1.2", "26.2", "26.3")
        client = false
        server = true
    }

    modrinth {
        projectId = providers.gradleProperty("modrinth_id")
        accessToken = providers.gradleProperty("modrinth_token")
            .orElse(providers.gradleProperty("MODRINTH_TOKEN"))
            .orElse(providers.environmentVariable("MODRINTH_TOKEN"))
        minecraftVersions.addAll("26.1", "26.1.1", "26.1.2", "26.2", "26.3")
        environment = SERVER_ONLY
    }
}
