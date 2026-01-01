package com.soupmodmaker.core.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a SoupModMaker2 workspace containing all mod elements and settings.
 * This is the main container for a mod project.
 */
public class Workspace {
    private String version; // Workspace format version
    private String name;
    private String modId;
    private String minecraftVersion;
    private String modLoader; // forge, neoforge, fabric
    private WorkspaceSettings settings;
    private List<WorkspaceElement> elements;

    public Workspace() {
        this.version = "1.0.0";
        this.elements = new ArrayList<>();
        this.settings = new WorkspaceSettings();
    }

    public Workspace(String name, String modId, String minecraftVersion, String modLoader) {
        this();
        this.name = name;
        this.modId = modId;
        this.minecraftVersion = minecraftVersion;
        this.modLoader = modLoader;
    }

    // Getters and Setters

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public String getModLoader() {
        return modLoader;
    }

    public void setModLoader(String modLoader) {
        this.modLoader = modLoader;
    }

    public WorkspaceSettings getSettings() {
        return settings;
    }

    public void setSettings(WorkspaceSettings settings) {
        this.settings = settings;
    }

    public List<WorkspaceElement> getElements() {
        return elements;
    }

    public void setElements(List<WorkspaceElement> elements) {
        this.elements = elements;
    }

    public void addElement(WorkspaceElement element) {
        this.elements.add(element);
    }

    public void removeElement(WorkspaceElement element) {
        this.elements.remove(element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workspace workspace = (Workspace) o;
        return Objects.equals(modId, workspace.modId) &&
                Objects.equals(minecraftVersion, workspace.minecraftVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modId, minecraftVersion);
    }

    @Override
    public String toString() {
        return "Workspace{" +
                "name='" + name + '\'' +
                ", modId='" + modId + '\'' +
                ", minecraftVersion='" + minecraftVersion + '\'' +
                ", modLoader='" + modLoader + '\'' +
                ", elements=" + elements.size() +
                '}';
    }
}
