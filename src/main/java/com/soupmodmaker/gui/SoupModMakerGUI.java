package com.soupmodmaker.gui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI application entry point for SoupModMaker2.
 * Provides a graphical interface for creating and managing Minecraft mod workspaces.
 */
public class SoupModMakerGUI {

    public static void main(String[] args) {
        // Set FlatLaf dark theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());

            // Set modern font
            Font defaultFont = new Font("Segoe UI", Font.PLAIN, 13);
            UIManager.put("defaultFont", defaultFont);
        } catch (Exception e) {
            // Fall back to system look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // Use default
            }
        }

        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainMenuWindow mainMenu = new MainMenuWindow();
            mainMenu.setVisible(true);
        });
    }
}
