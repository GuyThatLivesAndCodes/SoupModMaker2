package com.soupmodmaker.generators;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generated mod with all its files.
 */
public class GeneratedMod {
    private final String modId;
    private final String modName;
    private final Path outputPath;
    private final List<GeneratedFile> files;
    private boolean buildSuccessful;
    private Path jarPath;

    public GeneratedMod(String modId, String modName, Path outputPath) {
        this.modId = modId;
        this.modName = modName;
        this.outputPath = outputPath;
        this.files = new ArrayList<>();
        this.buildSuccessful = false;
    }

    public void addFile(GeneratedFile file) {
        files.add(file);
    }

    // Getters and Setters

    public String getModId() {
        return modId;
    }

    public String getModName() {
        return modName;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public List<GeneratedFile> getFiles() {
        return files;
    }

    public boolean isBuildSuccessful() {
        return buildSuccessful;
    }

    public void setBuildSuccessful(boolean buildSuccessful) {
        this.buildSuccessful = buildSuccessful;
    }

    public Path getJarPath() {
        return jarPath;
    }

    public void setJarPath(Path jarPath) {
        this.jarPath = jarPath;
    }

    @Override
    public String toString() {
        return "GeneratedMod{" +
                "modId='" + modId + '\'' +
                ", modName='" + modName + '\'' +
                ", files=" + files.size() +
                ", buildSuccessful=" + buildSuccessful +
                '}';
    }

    /**
     * Represents a single generated file.
     */
    public static class GeneratedFile {
        private final String relativePath;
        private final String content;
        private final Path absolutePath;

        public GeneratedFile(String relativePath, String content, Path absolutePath) {
            this.relativePath = relativePath;
            this.content = content;
            this.absolutePath = absolutePath;
        }

        public String getRelativePath() {
            return relativePath;
        }

        public String getContent() {
            return content;
        }

        public Path getAbsolutePath() {
            return absolutePath;
        }

        @Override
        public String toString() {
            return "GeneratedFile{" +
                    "relativePath='" + relativePath + '\'' +
                    '}';
        }
    }
}
