plugins {
    kotlin("jvm") version "2.0.0"
    application
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.elbekD:kt-telegram-bot:1.4.0")
    implementation("io.ktor:ktor-client-java:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.github.cdimascio:dotenv-kotlin:6.5.1")
}

application {
    mainClass.set("BotKt")
}