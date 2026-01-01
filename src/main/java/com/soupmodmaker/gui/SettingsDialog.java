package com.soupmodmaker.gui;

import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.core.workspace.WorkspaceSettings;

import javax.swing.*;
import java.awt.*;

/**
 * Settings dialog for workspace configuration.
 */
public class SettingsDialog extends JDialog {

    private final Workspace workspace;
    private final Runnable onSave;

    private JTextField modNameField;
    private JTextField versionField;
    private JTextField authorField;
    private JTextArea descriptionArea;
    private JLabel modIdLabel;
    private JLabel minecraftVersionLabel;

    public SettingsDialog(Window parent, Workspace workspace, Runnable onSave) {
        super(parent, "Workspace Settings", ModalityType.APPLICATION_MODAL);
        this.workspace = workspace;
        this.onSave = onSave;
        initializeUI();
        loadSettings();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 400);
        setLocationRelativeTo(getParent());

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Workspace Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Mod Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Mod Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        modNameField = new JTextField();
        formPanel.add(modNameField, gbc);

        // Mod ID (read-only)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Mod ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        modIdLabel = new JLabel();
        modIdLabel.setFont(modIdLabel.getFont().deriveFont(Font.BOLD));
        formPanel.add(modIdLabel, gbc);

        // Version
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Version:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        versionField = new JTextField();
        formPanel.add(versionField, gbc);

        // Author
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        authorField = new JTextField();
        formPanel.add(authorField, gbc);

        // Minecraft Version (read-only)
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(new JLabel("Minecraft Version:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        minecraftVersionLabel = new JLabel();
        minecraftVersionLabel.setFont(minecraftVersionLabel.getFont().deriveFont(Font.BOLD));
        formPanel.add(minecraftVersionLabel, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveSettings());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadSettings() {
        modNameField.setText(workspace.getName());
        modIdLabel.setText(workspace.getModId());
        versionField.setText(workspace.getSettings().getVersion());
        authorField.setText(workspace.getSettings().getAuthor());
        descriptionArea.setText(workspace.getSettings().getDescription());
        minecraftVersionLabel.setText(workspace.getMinecraftVersion() + " (" + workspace.getModLoader() + ")");
    }

    private void saveSettings() {
        workspace.setName(modNameField.getText().trim());

        WorkspaceSettings settings = workspace.getSettings();
        settings.setVersion(versionField.getText().trim());
        settings.setAuthor(authorField.getText().trim());
        settings.setDescription(descriptionArea.getText().trim());

        onSave.run();
        dispose();
    }
}
