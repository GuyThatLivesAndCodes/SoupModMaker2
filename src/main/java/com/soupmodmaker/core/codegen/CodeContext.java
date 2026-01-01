package com.soupmodmaker.core.codegen;

import com.soupmodmaker.core.workspace.Workspace;

import java.util.HashMap;
import java.util.Map;

/**
 * Context information for code generation.
 * Provides access to workspace settings and helper data.
 */
public class CodeContext {
    private final Workspace workspace;
    private final Map<String, Object> data;

    public CodeContext(Workspace workspace) {
        this.workspace = workspace;
        this.data = new HashMap<>();
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getJavaPackage() {
        return workspace.getSettings().getJavaPackage();
    }

    public String getModId() {
        return workspace.getModId();
    }

    public String getMinecraftVersion() {
        return workspace.getMinecraftVersion();
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Map<String, Object> getData() {
        return data;
    }
}
