package com.soupmodmaker.generators;

import com.soupmodmaker.core.workspace.Workspace;

import java.nio.file.Path;
import java.util.List;

/**
 * Interface for version-specific mod generators.
 * Each implementation handles code generation for a specific Minecraft version and mod loader.
 */
public interface VersionGenerator {

    /**
     * Get the Minecraft version this generator supports.
     * @return Version string (e.g., "1.12.2")
     */
    String getSupportedVersion();

    /**
     * Get the mod loader this generator supports.
     * @return Mod loader name (e.g., "forge", "neoforge", "fabric")
     */
    String getModLoader();

    /**
     * Generate a complete mod from a workspace.
     *
     * @param workspace The workspace to generate from
     * @param outputPath Path where to generate the mod files
     * @return GeneratedMod containing generation results
     * @throws Exception if generation fails
     */
    GeneratedMod generate(Workspace workspace, Path outputPath) throws Exception;

    /**
     * Check if this generator is compatible with the given workspace.
     *
     * @param workspace The workspace to check
     * @return true if compatible, false otherwise
     */
    default boolean isCompatible(Workspace workspace) {
        return getSupportedVersion().equals(workspace.getMinecraftVersion()) &&
               getModLoader().equals(workspace.getModLoader());
    }
}
