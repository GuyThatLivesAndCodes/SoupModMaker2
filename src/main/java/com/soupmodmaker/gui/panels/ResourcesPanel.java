package com.soupmodmaker.gui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Resources panel for textures, sounds, models, etc.
 */
public class ResourcesPanel extends JPanel {

    public ResourcesPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Placeholder content
        JLabel placeholderLabel = new JLabel("<html><center>" +
            "<h2>Resources</h2>" +
            "<p>Manage textures, sounds, and models for your mod</p>" +
            "<br><p><i>Coming soon!</i></p>" +
            "</center></html>");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setForeground(Color.GRAY);

        add(placeholderLabel, BorderLayout.CENTER);

        // Future categories
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.add(createCategoryButton("Textures", "16x16, 32x32, etc."));
        categoryPanel.add(createCategoryButton("Sounds", "OGG audio files"));
        categoryPanel.add(createCategoryButton("Models", "JSON models"));

        add(categoryPanel, BorderLayout.SOUTH);
    }

    private JButton createCategoryButton(String title, String description) {
        JButton button = new JButton("<html><b>" + title + "</b><br><small>" + description + "</small></html>");
        button.setEnabled(false);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }
}
