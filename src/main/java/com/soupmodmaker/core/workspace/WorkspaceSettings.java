package com.soupmodmaker.core.workspace;

/**
 * Settings for a workspace including mod metadata and configuration.
 */
public class WorkspaceSettings {
    private String javaPackage;
    private String author;
    private String description;
    private String version;
    private String license;

    public WorkspaceSettings() {
        this.version = "1.0.0";
        this.license = "MIT";
    }

    // Getters and Setters

    public String getJavaPackage() {
        return javaPackage;
    }

    public void setJavaPackage(String javaPackage) {
        this.javaPackage = javaPackage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return "WorkspaceSettings{" +
                "javaPackage='" + javaPackage + '\'' +
                ", author='" + author + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
