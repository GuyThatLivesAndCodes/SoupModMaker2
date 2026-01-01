buildscript {
    repositories {
        jcenter()
        maven { url = "https://files.minecraftforge.net/maven" }
    }
    dependencies {
        classpath 'net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT'
    }
}

apply plugin: 'net.minecraftforge.gradle.forge'

version = "${version}"
group = "${package}"
archivesBaseName = "${modId}"

sourceCompatibility = targetCompatibility = '1.8'
compileJava {
    sourceCompatibility = targetCompatibility = '1.8'
}

minecraft {
    version = "1.12.2-14.23.5.2859"
    runDir = "run"
    mappings = "stable_39"
}

dependencies {
    // Add dependencies here if needed
}

processResources {
    inputs.property "version", project.version
    inputs.property "mcversion", "1.12.2"

    from(sourceSets.main.resources.srcDirs) {
        include 'mcmod.info'
        expand 'version':project.version, 'mcversion':"1.12.2"
    }

    from(sourceSets.main.resources.srcDirs) {
        exclude 'mcmod.info'
    }
}
