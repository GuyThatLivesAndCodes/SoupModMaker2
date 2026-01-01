package com.soupmodmaker.gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.soupmodmaker.core.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Manages workspace projects in the user's Documents folder.
 * Handles creation, loading, saving, and deletion of workspace projects.
 */
public class WorkspaceManager {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceManager.class);
    private static final String PROJECTS_DIR_NAME = "SoupModMaker2/Projects";
    private static final String WORKSPACE_FILE = "workspace.json";

    private final Path projectsDirectory;
    private final Gson gson;

    public WorkspaceManager() {
        // Get user's documents directory
        String userHome = System.getProperty("user.home");
        String documentsPath = Paths.get(userHome, "Documents").toString();

        // Try to find Documents folder (cross-platform)
        Path docsPath = Paths.get(documentsPath);
        if (!Files.exists(docsPath)) {
            // Fallback to user home if Documents doesn't exist
            docsPath = Paths.get(userHome);
        }

        this.projectsDirectory = docsPath.resolve(PROJECTS_DIR_NAME);
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Create projects directory if it doesn't exist
        try {
            Files.createDirectories(projectsDirectory);
            logger.info("Projects directory: {}", projectsDirectory);
        } catch (IOException e) {
            logger.error("Failed to create projects directory", e);
        }
    }

    /**
     * Get the projects directory path.
     */
    public Path getProjectsDirectory() {
        return projectsDirectory;
    }

    /**
     * List all workspace projects.
     */
    public List<ProjectInfo> listProjects() {
        List<ProjectInfo> projects = new ArrayList<>();

        if (!Files.exists(projectsDirectory)) {
            return projects;
        }

        try (Stream<Path> paths = Files.list(projectsDirectory)) {
            paths.filter(Files::isDirectory)
                 .forEach(projectDir -> {
                     Path workspaceFile = projectDir.resolve(WORKSPACE_FILE);
                     if (Files.exists(workspaceFile)) {
                         try {
                             Workspace workspace = loadWorkspace(projectDir);
                             projects.add(new ProjectInfo(
                                 projectDir.getFileName().toString(),
                                 projectDir,
                                 workspace
                             ));
                         } catch (Exception e) {
                             logger.warn("Failed to load project: {}", projectDir, e);
                         }
                     }
                 });
        } catch (IOException e) {
            logger.error("Failed to list projects", e);
        }

        return projects;
    }

    /**
     * Create a new workspace project.
     */
    public Path createProject(String projectName, Workspace workspace) throws IOException {
        // Sanitize project name for file system
        String sanitizedName = projectName.replaceAll("[^a-zA-Z0-9-_]", "_").toLowerCase();
        Path projectDir = projectsDirectory.resolve(sanitizedName);

        // Check if project already exists
        if (Files.exists(projectDir)) {
            throw new IOException("Project already exists: " + projectName);
        }

        // Create project directory
        Files.createDirectories(projectDir);

        // Save workspace
        saveWorkspace(projectDir, workspace);

        logger.info("Created project: {}", projectDir);
        return projectDir;
    }

    /**
     * Load a workspace from a project directory.
     */
    public Workspace loadWorkspace(Path projectDir) throws IOException {
        Path workspaceFile = projectDir.resolve(WORKSPACE_FILE);

        if (!Files.exists(workspaceFile)) {
            throw new IOException("Workspace file not found: " + workspaceFile);
        }

        String json = Files.readString(workspaceFile);
        return gson.fromJson(json, Workspace.class);
    }

    /**
     * Save a workspace to a project directory.
     */
    public void saveWorkspace(Path projectDir, Workspace workspace) throws IOException {
        Path workspaceFile = projectDir.resolve(WORKSPACE_FILE);
        String json = gson.toJson(workspace);
        Files.writeString(workspaceFile, json);
        logger.debug("Saved workspace: {}", workspaceFile);
    }

    /**
     * Delete a project directory.
     */
    public void deleteProject(Path projectDir) throws IOException {
        if (!projectDir.startsWith(projectsDirectory)) {
            throw new IllegalArgumentException("Cannot delete project outside projects directory");
        }

        // Delete all files recursively
        try (Stream<Path> paths = Files.walk(projectDir)) {
            paths.sorted((a, b) -> b.compareTo(a)) // Reverse order to delete files before directories
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         logger.warn("Failed to delete: {}", path, e);
                     }
                 });
        }

        logger.info("Deleted project: {}", projectDir);
    }

    /**
     * Information about a workspace project.
     */
    public static class ProjectInfo {
        private final String name;
        private final Path path;
        private final Workspace workspace;

        public ProjectInfo(String name, Path path, Workspace workspace) {
            this.name = name;
            this.path = path;
            this.workspace = workspace;
        }

        public String getName() {
            return name;
        }

        public Path getPath() {
            return path;
        }

        public Workspace getWorkspace() {
            return workspace;
        }

        @Override
        public String toString() {
            return workspace.getName() + " (" + name + ")";
        }
    }
}
