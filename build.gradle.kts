plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.1.0"
}

group = "net.plexverse"
version = "1.2.0"

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
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    implementation("xyz.xenondevs.invui:invui:2.0.0-alpha.15")
    implementation("com.google.code.gson:gson:2.10.1")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.oop:memory-store:4.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("commons-io:commons-io:2.15.1")
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

tasks.named<Copy>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    filesMatching("plugin.yml") {
        filter { line ->
            line.replace("@version@", project.version.toString())
        }
    }
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveBaseName.set("map-parser")
    archiveVersion.set("${project.version}")
    archiveClassifier.set("")
    mergeServiceFiles()
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    
    // Include all dependencies - Paper's reflection rewriter will handle NMS access
    configurations = listOf(project.configurations.getByName("runtimeClasspath"))
}

// Make shadowJar the default jar
tasks.named<Jar>("jar") {
    enabled = false
}

tasks.build {
    dependsOn("shadowJar")
}
