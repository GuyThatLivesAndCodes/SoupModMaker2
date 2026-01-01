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

    // JUnit 5 for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.soupmodmaker.cli.SoupModMakerCLI")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
