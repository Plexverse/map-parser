plugins {
    id("java")
    id("com.mineplex.sdk.plugin") version "1.21.9"
    id("maven-publish")
}

group = "net.plexverse"
version = "1.2"

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.opencollab.dev/main/")
    }
    maven {
        url = uri("https://s01.oss.sonatype.org/content/groups/public/")
    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        url = uri("https://repo.md-5.net/content/groups/public/")
    }
    maven {
        url = uri("https://mvn.lumine.io/repository/maven-public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.xenondevs.xyz/releases")
    }
}

dependencies {
    implementation("xyz.xenondevs.invui:invui:1.46")
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.projectlombok:lombok:1.18.30")
    implementation("com.oop:memory-store:4.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
}

tasks.register<Jar>("shadowJar") {
    archiveBaseName.set("MapParser")
    archiveVersion.set("1.2-SNAPSHOT")
    from(sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Copy>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.build {
    dependsOn("shadowJar")
}
