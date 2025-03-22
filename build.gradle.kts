plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("antlr")
}

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4:4.13.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("cli.CliKt")
}

sourceSets["main"].java.srcDir("build/generated-src/antlr/main")

tasks.named("compileKotlin") {
    dependsOn("generateGrammarSource")
}

tasks.generateGrammarSource {
    arguments.addAll(listOf("-visitor"))
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "cli.CliKt"
    }
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "cli.CliKt"
    }
}

tasks.shadowJar {
    archiveFileName.set("DML-all.jar")
}
