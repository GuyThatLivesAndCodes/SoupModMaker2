package com.soupmodmaker.generators.impl;

import com.soupmodmaker.core.templates.TemplateRenderer;
import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.core.workspace.WorkspaceElement;
import com.soupmodmaker.core.workspace.elements.ItemElement;
import com.soupmodmaker.generators.GeneratedMod;
import com.soupmodmaker.generators.VersionGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generator for Forge 1.12.2 mods.
 * Creates a complete, buildable Forge mod project.
 */
public class Forge1122Generator implements VersionGenerator {
    private static final Logger logger = LoggerFactory.getLogger(Forge1122Generator.class);
    private final TemplateRenderer templateRenderer;

    public Forge1122Generator() {
        this.templateRenderer = new TemplateRenderer();
    }

    @Override
    public String getSupportedVersion() {
        return "1.12.2";
    }

    @Override
    public String getModLoader() {
        return "forge";
    }

    @Override
    public GeneratedMod generate(Workspace workspace, Path outputPath) throws Exception {
        logger.info("Generating Forge 1.12.2 mod: {}", workspace.getName());

        GeneratedMod generatedMod = new GeneratedMod(
                workspace.getModId(),
                workspace.getName(),
                outputPath
        );

        // Create directory structure
        createDirectoryStructure(outputPath, workspace);

        // Generate main mod class
        generateMainModClass(workspace, outputPath, generatedMod);

        // Generate item registry
        generateItemRegistry(workspace, outputPath, generatedMod);

        // Generate item classes
        generateItems(workspace, outputPath, generatedMod);

        // Generate mcmod.info
        generateMcModInfo(workspace, outputPath, generatedMod);

        // Generate build.gradle
        generateBuildGradle(workspace, outputPath, generatedMod);

        // Generate gradle wrapper files
        generateGradleWrapper(outputPath, generatedMod);

        logger.info("Generated {} files for mod: {}", generatedMod.getFiles().size(), workspace.getName());

        return generatedMod;
    }

    private void createDirectoryStructure(Path outputPath, Workspace workspace) throws IOException {
        String packagePath = workspace.getSettings().getJavaPackage().replace('.', '/');

        // Create source directories
        Path srcMain = outputPath.resolve("src/main/java/" + packagePath);
        Path srcMainItem = srcMain.resolve("item");
        Path srcResources = outputPath.resolve("src/main/resources");

        Files.createDirectories(srcMain);
        Files.createDirectories(srcMainItem);
        Files.createDirectories(srcResources);

        logger.debug("Created directory structure at: {}", outputPath);
    }

    private void generateMainModClass(Workspace workspace, Path outputPath, GeneratedMod generatedMod) throws Exception {
        String className = capitalize(workspace.getModId());
        String packageName = workspace.getSettings().getJavaPackage();

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("package", packageName);
        dataModel.put("className", className);
        dataModel.put("modId", workspace.getModId());
        dataModel.put("modName", workspace.getName());
        dataModel.put("version", workspace.getSettings().getVersion());
        dataModel.put("author", workspace.getSettings().getAuthor());

        String content = templateRenderer.render("forge-1.12.2/MainMod.java.ftl", dataModel);

        String relativePath = "src/main/java/" + packageName.replace('.', '/') + "/" + className + ".java";
        Path filePath = outputPath.resolve(relativePath);

        Files.writeString(filePath, content);
        generatedMod.addFile(new GeneratedMod.GeneratedFile(relativePath, content, filePath));

        logger.debug("Generated main mod class: {}", className);
    }

    private void generateItemRegistry(Workspace workspace, Path outputPath, GeneratedMod generatedMod) throws Exception {
        // Get all items from workspace
        List<Map<String, Object>> itemDataList = new ArrayList<>();

        for (WorkspaceElement element : workspace.getElements()) {
            if (element instanceof ItemElement) {
                ItemElement item = (ItemElement) element;
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("id", item.getId());
                itemData.put("className", "Item" + capitalize(item.getId()));
                itemDataList.add(itemData);
            }
        }

        if (itemDataList.isEmpty()) {
            logger.debug("No items to register");
            return;
        }

        String packageName = workspace.getSettings().getJavaPackage();
        String className = capitalize(workspace.getModId()) + "Items";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("package", packageName);
        dataModel.put("modId", workspace.getModId());
        dataModel.put("modName", workspace.getName());
        dataModel.put("items", itemDataList);

        String content = templateRenderer.render("forge-1.12.2/ItemRegistry.java.ftl", dataModel);

        String relativePath = "src/main/java/" + packageName.replace('.', '/') + "/item/" + className + ".java";
        Path filePath = outputPath.resolve(relativePath);

        Files.writeString(filePath, content);
        generatedMod.addFile(new GeneratedMod.GeneratedFile(relativePath, content, filePath));

        logger.debug("Generated item registry with {} items", itemDataList.size());
    }

    private void generateItems(Workspace workspace, Path outputPath, GeneratedMod generatedMod) throws Exception {
        String packageName = workspace.getSettings().getJavaPackage();

        for (WorkspaceElement element : workspace.getElements()) {
            if (element instanceof ItemElement) {
                ItemElement item = (ItemElement) element;

                String className = "Item" + capitalize(item.getId());

                Map<String, Object> dataModel = new HashMap<>();
                dataModel.put("package", packageName);
                dataModel.put("className", className);
                dataModel.put("itemId", item.getId());
                dataModel.put("itemName", item.getName());
                dataModel.put("maxStackSize", item.getMaxStackSize());

                String content = templateRenderer.render("forge-1.12.2/Item.java.ftl", dataModel);

                String relativePath = "src/main/java/" + packageName.replace('.', '/') + "/item/" + className + ".java";
                Path filePath = outputPath.resolve(relativePath);

                Files.writeString(filePath, content);
                generatedMod.addFile(new GeneratedMod.GeneratedFile(relativePath, content, filePath));

                logger.debug("Generated item: {}", className);
            }
        }
    }

    private void generateMcModInfo(Workspace workspace, Path outputPath, GeneratedMod generatedMod) throws Exception {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("modId", workspace.getModId());
        dataModel.put("modName", workspace.getName());
        dataModel.put("description", workspace.getSettings().getDescription());
        dataModel.put("version", workspace.getSettings().getVersion());
        dataModel.put("author", workspace.getSettings().getAuthor());

        String content = templateRenderer.render("forge-1.12.2/mcmod.info.ftl", dataModel);

        String relativePath = "src/main/resources/mcmod.info";
        Path filePath = outputPath.resolve(relativePath);

        Files.writeString(filePath, content);
        generatedMod.addFile(new GeneratedMod.GeneratedFile(relativePath, content, filePath));

        logger.debug("Generated mcmod.info");
    }

    private void generateBuildGradle(Workspace workspace, Path outputPath, GeneratedMod generatedMod) throws Exception {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("modId", workspace.getModId());
        dataModel.put("version", workspace.getSettings().getVersion());
        dataModel.put("package", workspace.getSettings().getJavaPackage());

        String content = templateRenderer.render("forge-1.12.2/build.gradle.ftl", dataModel);

        String relativePath = "build.gradle";
        Path filePath = outputPath.resolve(relativePath);

        Files.writeString(filePath, content);
        generatedMod.addFile(new GeneratedMod.GeneratedFile(relativePath, content, filePath));

        logger.debug("Generated build.gradle");
    }

    private void generateGradleWrapper(Path outputPath, GeneratedMod generatedMod) throws IOException {
        // Create gradlew scripts (basic versions - user should run gradle wrapper)
        String gradlewUnix = "#!/bin/bash\ngradle \"$@\"\n";
        String gradlewWindows = "@echo off\r\ngradle %*\r\n";

        Path gradlewPath = outputPath.resolve("gradlew");
        Path gradlewBatPath = outputPath.resolve("gradlew.bat");

        Files.writeString(gradlewPath, gradlewUnix);
        Files.writeString(gradlewBatPath, gradlewWindows);

        // Make gradlew executable on Unix
        try {
            gradlewPath.toFile().setExecutable(true);
        } catch (Exception e) {
            logger.warn("Could not set gradlew as executable: {}", e.getMessage());
        }

        generatedMod.addFile(new GeneratedMod.GeneratedFile("gradlew", gradlewUnix, gradlewPath));
        generatedMod.addFile(new GeneratedMod.GeneratedFile("gradlew.bat", gradlewWindows, gradlewBatPath));

        logger.debug("Generated gradle wrapper scripts");
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
