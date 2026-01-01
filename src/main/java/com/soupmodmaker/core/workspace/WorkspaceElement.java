package com.soupmodmaker.core.workspace;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for all workspace elements (items, blocks, entities, etc.)
 * Each element represents a mod component that will be generated.
 */
public abstract class WorkspaceElement {
    private String type;      // item, block, entity, etc.
    private String id;        // Unique identifier (registry name)
    private String name;      // Display name
    private Map<String, Object> properties;

    public WorkspaceElement(String type) {
        this.type = type;
        this.properties = new HashMap<>();
    }

    public WorkspaceElement(String type, String id, String name) {
        this(type);
        this.id = id;
        this.name = name;
    }

    // Getters and Setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkspaceElement that = (WorkspaceElement) o;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public String toString() {
        return "WorkspaceElement{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
