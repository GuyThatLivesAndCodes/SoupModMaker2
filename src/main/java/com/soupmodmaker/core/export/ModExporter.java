package com.soupmodmaker.core.export;

import com.soupmodmaker.generators.GeneratedMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exports generated mods by building them with Gradle.
 * Handles the compilation and JAR packaging process.
 */
public class ModExporter {
    private static final Logger logger = LoggerFactory.getLogger(ModExporter.class);

    /**
     * Build a generated mod using Gradle.
     *
     * @param generatedMod The generated mod to build
     * @return true if build succeeded, false otherwise
     */
    public boolean build(GeneratedMod generatedMod) {
        logger.info("Building mod: {}", generatedMod.getModName());

        Path modPath = generatedMod.getOutputPath();

        try {
            // Check if build.gradle exists
            Path buildGradle = modPath.resolve("build.gradle");
            if (!Files.exists(buildGradle)) {
                logger.error("build.gradle not found at: {}", buildGradle);
                return false;
            }

            // Run gradle build
            boolean success = runGradleBuild(modPath);

            if (success) {
                // Find the generated JAR
                Path jarPath = findGeneratedJar(modPath);
                if (jarPath != null) {
                    generatedMod.setJarPath(jarPath);
                    generatedMod.setBuildSuccessful(true);
                    logger.info("Build successful! JAR at: {}", jarPath);
                    return true;
                } else {
                    logger.warn("Build completed but JAR not found");
                    return false;
                }
            } else {
                logger.error("Build failed");
                return false;
            }

        } catch (Exception e) {
            logger.error("Error building mod: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Run Gradle build command.
     */
    private boolean runGradleBuild(Path modPath) throws Exception {
        List<String> command = new ArrayList<>();

        // Use gradlew if available, otherwise use system gradle
        Path gradlew = modPath.resolve("gradlew");
        if (Files.exists(gradlew)) {
            command.add(gradlew.toAbsolutePath().toString());
        } else {
            command.add("gradle");
        }

        command.add("build");
        command.add("--no-daemon");
        command.add("--stacktrace");

        logger.info("Running: {}", String.join(" ", command));

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(modPath.toFile());
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // Read output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.debug("[Gradle] {}", line);
            }
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            logger.info("Gradle build completed successfully");
            return true;
        } else {
            logger.error("Gradle build failed with exit code: {}", exitCode);
            return false;
        }
    }

    /**
     * Find the generated JAR file in the build output.
     */
    private Path findGeneratedJar(Path modPath) throws Exception {
        Path buildLibs = modPath.resolve("build/libs");

        if (!Files.exists(buildLibs)) {
            return null;
        }

        List<Path> jars = Files.list(buildLibs)
                .filter(p -> p.toString().endsWith(".jar"))
                .filter(p -> !p.toString().contains("sources"))
                .filter(p -> !p.toString().contains("javadoc"))
                .collect(Collectors.toList());

        if (jars.isEmpty()) {
            return null;
        }

        // Return the first JAR (or the one without -sources/-javadoc)
        return jars.get(0);
    }

    /**
     * Clean the build directory.
     */
    public void clean(Path modPath) {
        try {
            Path buildDir = modPath.resolve("build");
            if (Files.exists(buildDir)) {
                deleteRecursively(buildDir);
                logger.info("Cleaned build directory");
            }
        } catch (Exception e) {
            logger.warn("Error cleaning build directory: {}", e.getMessage());
        }
    }

    private void deleteRecursively(Path path) throws Exception {
        if (Files.isDirectory(path)) {
            Files.list(path).forEach(child -> {
                try {
                    deleteRecursively(child);
                } catch (Exception e) {
                    logger.warn("Error deleting: {}", child, e);
                }
            });
        }
        Files.deleteIfExists(path);
    }
}
