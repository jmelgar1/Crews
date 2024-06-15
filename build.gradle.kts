plugins {
  `java-library`
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.playpro.com")
        maven(url = "https://maven.enginehub.org/repo/")
        maven(url = "https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT") {
        exclude("junit", "junit")
    }
    compileOnly("net.dv8tion:JDA:5.0.0-beta.13")
    compileOnly("net.coreprotect:coreprotect:22.0")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.15")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "bukkit")
    }
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(21)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}
