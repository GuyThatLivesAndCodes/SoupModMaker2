package com.soupmodmaker.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI application entry point for SoupModMaker2.
 * Provides a graphical interface for creating and managing Minecraft mod workspaces.
 */
public class SoupModMakerGUI {

    public static void main(String[] args) {
        // Set system look and feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default look and feel
        }

        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainMenuWindow mainMenu = new MainMenuWindow();
            mainMenu.setVisible(true);
        });
    }
}
