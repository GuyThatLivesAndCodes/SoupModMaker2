plugins {
    java
    application
}

group = "com.soupmodmaker"
version = "0.1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // FreeMarker for template rendering
    implementation("org.freemarker:freemarker:2.3.32")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")

    // SLF4J logging API
    implementation("org.slf4j:slf4j-api:2.0.9")
    runtimeOnly("ch.qos.logback:logback-classic:1.4.11")

    // FlatLaf for modern dark theme
    implementation("com.formdev:flatlaf:3.2.5")

    // JUnit 5 for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // Default to GUI - use ./gradlew runCli for CLI mode
    mainClass.set("com.soupmodmaker.gui.SoupModMakerGUI")
}

// Add task to run CLI mode
tasks.register<JavaExec>("runCli") {
    group = "application"
    description = "Run SoupModMaker2 in CLI mode"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.soupmodmaker.cli.SoupModMakerCLI")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Create a fat JAR task (includes all dependencies)
tasks.register<Jar>("fatJar") {
    archiveBaseName.set("soupmodmaker2")
    archiveClassifier.set("all")
    archiveVersion.set(project.version.toString())

    manifest {
        attributes(
            "Main-Class" to "com.soupmodmaker.gui.SoupModMakerGUI",
            "Implementation-Title" to "SoupModMaker2",
            "Implementation-Version" to project.version
        )
    }

    // Include all runtime dependencies
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    // Include compiled classes
    with(tasks.jar.get())

    // Avoid duplicate META-INF files
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Make build also create the fat JAR
tasks.build {
    dependsOn("fatJar")
}

// Configure the regular JAR task to have a manifest
tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "com.soupmodmaker.gui.SoupModMakerGUI"
        )
    }
}
