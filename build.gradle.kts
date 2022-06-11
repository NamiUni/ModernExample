import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import xyz.jpenilla.runpaper.task.RunServerTask

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"

    // Gradleでビルドしたプラグインと一緒にPaperサーバーを実行できる
    // https://github.com/jpenilla/run-paper
    id("xyz.jpenilla.run-paper") version "1.0.6"

    // Gradleでplugin.ymlを生成できる
    // https://github.com/Minecrell/plugin-yml
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "com.github.namiuni"
version = "0.1"

repositories {
    mavenLocal()
    mavenCentral()

    // Paper
    maven("https://repo.papermc.io/repository/maven-public/")

    // TaskChain
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    // Paper
    implementation("io.papermc", "paperlib", "1.0.7")
    compileOnly("io.papermc.paper", "paper-api", "1.18.2-R0.1-SNAPSHOT")

    // Command
    implementation("cloud.commandframework", "cloud-paper", "1.6.1")
    implementation("cloud.commandframework", "cloud-annotations", "1.6.1")

    // Config
    implementation("org.spongepowered", "configurate-hocon", "4.1.2")
    implementation("net.kyori", "adventure-serializer-configurate4", "4.10.1")

    // Event
    implementation("net.kyori", "event-api","5.0.0-SNAPSHOT")

    // Utils
    implementation("com.google.inject", "guice", "5.1.0")
    implementation("co.aikar", "taskchain-bukkit", "3.7.2")

}

bukkit {
    name = rootProject.name
    version = project.version as String
    main = "com.github.namiuni.modernexample.ModernExample"
    apiVersion = "1.18"
    description = "お手本プラグイン"
    author = "Unitarou"
    website = "https://github.com/NamiUni"
}

tasks {
    withType<ShadowJar> {
        this.archiveClassifier.set(null as String?)
        relocate("io.papermc.lib", "com.github.namiuni.modernexample.paperlib")
        relocate("co.aikar.taskchain", "com.github.namiuni.modernexample.taskchain")
    }

    withType<RunServerTask> {
        this.minecraftVersion("1.18.2")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
