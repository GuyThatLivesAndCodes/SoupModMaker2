package com.soupmodmaker.core.workspace;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soupmodmaker.core.workspace.elements.ItemElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Handles loading and saving workspace JSON files.
 * Responsible for serialization/deserialization of workspace data.
 */
public class WorkspaceLoader {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceLoader.class);
    private final Gson gson;

    public WorkspaceLoader() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Load a workspace from a JSON file.
     *
     * @param path Path to the workspace JSON file
     * @return The loaded Workspace object
     * @throws IOException if file cannot be read
     */
    public Workspace load(Path path) throws IOException {
        logger.info("Loading workspace from: {}", path);

        try (FileReader reader = new FileReader(path.toFile())) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            Workspace workspace = new Workspace();

            // Parse workspace metadata
            if (root.has("version")) {
                workspace.setVersion(root.get("version").getAsString());
            }

            if (root.has("workspace")) {
                JsonObject workspaceObj = root.getAsJsonObject("workspace");

                if (workspaceObj.has("name")) {
                    workspace.setName(workspaceObj.get("name").getAsString());
                }
                if (workspaceObj.has("modId")) {
                    workspace.setModId(workspaceObj.get("modId").getAsString());
                }
                if (workspaceObj.has("minecraftVersion")) {
                    workspace.setMinecraftVersion(workspaceObj.get("minecraftVersion").getAsString());
                }
                if (workspaceObj.has("modLoader")) {
                    workspace.setModLoader(workspaceObj.get("modLoader").getAsString());
                }

                // Parse settings
                if (workspaceObj.has("settings")) {
                    WorkspaceSettings settings = gson.fromJson(
                            workspaceObj.get("settings"),
                            WorkspaceSettings.class
                    );
                    workspace.setSettings(settings);
                }
            }

            // Parse elements
            if (root.has("elements")) {
                for (JsonElement elementJson : root.getAsJsonArray("elements")) {
                    JsonObject elementObj = elementJson.getAsJsonObject();
                    String type = elementObj.get("type").getAsString();

                    WorkspaceElement element = parseElement(type, elementObj);
                    if (element != null) {
                        workspace.addElement(element);
                    }
                }
            }

            logger.info("Loaded workspace: {} with {} elements",
                    workspace.getName(), workspace.getElements().size());

            return workspace;
        }
    }

    /**
     * Parse a workspace element based on its type.
     */
    private WorkspaceElement parseElement(String type, JsonObject elementObj) {
        WorkspaceElement element;

        switch (type) {
            case "item":
                element = new ItemElement();
                break;
            // Future: Add more element types here
            default:
                logger.warn("Unknown element type: {}", type);
                return null;
        }

        // Parse common fields
        if (elementObj.has("id")) {
            element.setId(elementObj.get("id").getAsString());
        }
        if (elementObj.has("name")) {
            element.setName(elementObj.get("name").getAsString());
        }

        // Parse properties
        if (elementObj.has("properties")) {
            JsonObject propsObj = elementObj.getAsJsonObject("properties");
            for (String key : propsObj.keySet()) {
                JsonElement value = propsObj.get(key);

                // Convert JSON value to Java object
                Object javaValue;
                if (value.isJsonPrimitive()) {
                    if (value.getAsJsonPrimitive().isBoolean()) {
                        javaValue = value.getAsBoolean();
                    } else if (value.getAsJsonPrimitive().isNumber()) {
                        javaValue = value.getAsNumber();
                    } else {
                        javaValue = value.getAsString();
                    }
                } else {
                    javaValue = value.toString();
                }

                element.setProperty(key, javaValue);
            }
        }

        return element;
    }

    /**
     * Save a workspace to a JSON file.
     *
     * @param workspace The workspace to save
     * @param path      Path where to save the JSON file
     * @throws IOException if file cannot be written
     */
    public void save(Workspace workspace, Path path) throws IOException {
        logger.info("Saving workspace to: {}", path);

        JsonObject root = new JsonObject();
        root.addProperty("version", workspace.getVersion());

        // Workspace metadata
        JsonObject workspaceObj = new JsonObject();
        workspaceObj.addProperty("name", workspace.getName());
        workspaceObj.addProperty("modId", workspace.getModId());
        workspaceObj.addProperty("minecraftVersion", workspace.getMinecraftVersion());
        workspaceObj.addProperty("modLoader", workspace.getModLoader());

        // Settings
        workspaceObj.add("settings", gson.toJsonTree(workspace.getSettings()));

        root.add("workspace", workspaceObj);

        // Elements
        root.add("elements", gson.toJsonTree(workspace.getElements()));

        // Write to file
        try (FileWriter writer = new FileWriter(path.toFile())) {
            gson.toJson(root, writer);
        }

        logger.info("Workspace saved successfully");
    }
}
