import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter", "junit-jupiter")

    // Paper
    compileOnly("io.papermc.paper", "paper-api", "1.20.1-R0.1-SNAPSHOT")

    // Plugin
    compileOnly("io.github.miniplaceholders", "miniplaceholders-api", "2.1.0")

    // Config
    paperLibrary("org.spongepowered", "configurate-hocon", "4.1.2")
    paperLibrary("net.kyori", "adventure-serializer-configurate4", "4.12.0")

    // Localize
    paperLibrary("net.kyori.moonshine", "moonshine-standard", "2.0.4")

    // Command
    paperLibrary("cloud.commandframework", "cloud-paper", "1.8.3")
    paperLibrary("cloud.commandframework", "cloud-minecraft-extras", "1.8.3")

    // Dependency Injection
    paperLibrary("com.google.inject", "guice", "7.0.0")

    // Database
    paperLibrary("com.zaxxer", "HikariCP", "5.0.1")
    paperLibrary("org.jdbi", "jdbi3-core", "3.41.1")
    paperLibrary("org.jdbi", "jdbi3-sqlobject", "3.41.1")
    paperLibrary("org.flywaydb", "flyway-core", "9.22.1")
    paperLibrary("org.flywaydb", "flyway-mysql", "9.22.1")
}

paper {
    generateLibrariesJson = true

    authors = listOf("Unitarou")
    version = "1.0-SNAPSHOT"
    description = "PaperAPIを使った中規模以上のプラグインの設計例"
    foliaSupported = false
    apiVersion = "1.20"

    val mainPackage = "com.github.namiuni.modernexample"
    main = "$mainPackage.ModernExample"
    bootstrapper = "$mainPackage.ModernExampleBootstrap"
    loader = "$mainPackage.ModernExamplePluginLoader"

    serverDependencies {
        register("MiniPlaceholders") {
            required = false
        }
    }

    permissions {
    }
}

tasks {
    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        this.options.release.set(17)
    }

    shadowJar {
        this.archiveClassifier.set(null as String?)
        this.archiveVersion.set(paper.version)
    }

    runServer {
        minecraftVersion("1.20.1")
    }

    test {
        useJUnitPlatform()
    }
}
