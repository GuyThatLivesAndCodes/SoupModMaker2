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

        // Generate README
        generateReadme(workspace, outputPath, generatedMod);

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
        // Create gradle wrapper directory
        Path wrapperDir = outputPath.resolve("gradle/wrapper");
        Files.createDirectories(wrapperDir);

        // Create gradle-wrapper.properties for Gradle 4.10.3 (required for Forge 1.12.2)
        String wrapperProperties = "distributionBase=GRADLE_USER_HOME\n" +
                "distributionPath=wrapper/dists\n" +
                "distributionUrl=https\\://services.gradle.org/distributions/gradle-4.10.3-bin.zip\n" +
                "zipStoreBase=GRADLE_USER_HOME\n" +
                "zipStorePath=wrapper/dists\n";

        Path propsPath = wrapperDir.resolve("gradle-wrapper.properties");
        Files.writeString(propsPath, wrapperProperties);
        generatedMod.addFile(new GeneratedMod.GeneratedFile("gradle/wrapper/gradle-wrapper.properties", wrapperProperties, propsPath));

        // Create gradlew scripts
        String gradlewUnix = "#!/usr/bin/env sh\n\n" +
                "##############################################################################\n" +
                "##\n" +
                "##  Gradle start up script for UN*X\n" +
                "##\n" +
                "##############################################################################\n\n" +
                "# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.\n" +
                "DEFAULT_JVM_OPTS='\"\"'\n\n" +
                "APP_NAME=\"Gradle\"\n" +
                "APP_BASE_NAME=`basename \"$0\"`\n\n" +
                "# Determine the Java command to use to start the JVM.\n" +
                "if [ -n \"$JAVA_HOME\" ] ; then\n" +
                "    if [ -x \"$JAVA_HOME/jre/sh/java\" ] ; then\n" +
                "        JAVACMD=\"$JAVA_HOME/jre/sh/java\"\n" +
                "    else\n" +
                "        JAVACMD=\"$JAVA_HOME/bin/java\"\n" +
                "    fi\n" +
                "else\n" +
                "    JAVACMD=\"java\"\n" +
                "fi\n\n" +
                "exec \"$JAVACMD\" $JAVA_OPTS $GRADLE_OPTS \"-Dorg.gradle.appname=$APP_BASE_NAME\" -classpath \"gradle/wrapper/gradle-wrapper.jar\" org.gradle.wrapper.GradleWrapperMain \"$@\"\n";

        String gradlewWindows = "@if \"%DEBUG%\" == \"\" @echo off\r\n" +
                "@rem Gradle startup script for Windows\r\n\r\n" +
                "set DIRNAME=%~dp0\r\n" +
                "if \"%DIRNAME%\" == \"\" set DIRNAME=.\r\n" +
                "set APP_BASE_NAME=%~n0\r\n" +
                "set DEFAULT_JVM_OPTS=\r\n\r\n" +
                "@rem Find java.exe\r\n" +
                "if defined JAVA_HOME goto findJavaFromJavaHome\r\n\r\n" +
                "set JAVA_EXE=java.exe\r\n" +
                "%JAVA_EXE% -version >NUL 2>&1\r\n" +
                "if \"%ERRORLEVEL%\" == \"0\" goto init\r\n\r\n" +
                "echo ERROR: JAVA_HOME is not set and no 'java' command could be found\r\n" +
                "exit /b 1\r\n\r\n" +
                ":findJavaFromJavaHome\r\n" +
                "set JAVA_HOME=%JAVA_HOME:\"=%\r\n" +
                "set JAVA_EXE=%JAVA_HOME%/bin/java.exe\r\n\r\n" +
                ":init\r\n" +
                "\"%JAVA_EXE%\" %DEFAULT_JVM_OPTS% %JAVA_OPTS% -classpath \"gradle\\wrapper\\gradle-wrapper.jar\" org.gradle.wrapper.GradleWrapperMain %*\r\n";

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

        logger.debug("Generated gradle wrapper for Gradle 4.10.3 (Forge 1.12.2 compatible)");
    }

    private void generateReadme(Workspace workspace, Path outputPath, GeneratedMod generatedMod) throws Exception {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("modId", workspace.getModId());
        dataModel.put("modName", workspace.getName());
        dataModel.put("version", workspace.getSettings().getVersion());
        dataModel.put("author", workspace.getSettings().getAuthor());
        dataModel.put("description", workspace.getSettings().getDescription());

        String content = templateRenderer.render("forge-1.12.2/README.md.ftl", dataModel);

        String relativePath = "README.md";
        Path filePath = outputPath.resolve(relativePath);

        Files.writeString(filePath, content);
        generatedMod.addFile(new GeneratedMod.GeneratedFile(relativePath, content, filePath));

        logger.debug("Generated README.md");
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
