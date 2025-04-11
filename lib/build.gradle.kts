plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    id("jacoco")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
    }
}
dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)
    testImplementation("org.assertj:assertj-core:3.24.2")
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
    testImplementation(kotlin("test"))
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
