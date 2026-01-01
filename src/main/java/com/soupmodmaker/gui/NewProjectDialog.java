package com.soupmodmaker.gui;

import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.core.workspace.WorkspaceSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

/**
 * Dialog for creating a new workspace project.
 */
public class NewProjectDialog extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger(NewProjectDialog.class);

    private final WorkspaceManager workspaceManager;

    private JTextField modNameField;
    private JTextField modIdField;
    private JTextField authorField;
    private JComboBox<String> minecraftVersionCombo;
    private JComboBox<String> modLoaderCombo;

    private boolean projectCreated = false;

    public NewProjectDialog(Frame parent, WorkspaceManager workspaceManager) {
        super(parent, "Create New Project", true);
        this.workspaceManager = workspaceManager;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 350);
        setLocationRelativeTo(getParent());
        setResizable(false);

        // Form panel
        add(createFormPanel(), BorderLayout.CENTER);

        // Buttons panel
        add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Create New Mod Project");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Mod Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Mod Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        modNameField = new JTextField("My Awesome Mod");
        modNameField.addActionListener(e -> generateModId());
        modNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                if (modIdField.getText().isEmpty() || modIdField.getText().equals("myawesomemod")) {
                    generateModId();
                }
            }
        });
        panel.add(modNameField, gbc);

        // Mod ID
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Mod ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        modIdField = new JTextField("myawesomemod");
        panel.add(modIdField, gbc);

        // Author
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        authorField = new JTextField(System.getProperty("user.name", "YourName"));
        panel.add(authorField, gbc);

        // Minecraft Version
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        panel.add(new JLabel("Minecraft Version:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        minecraftVersionCombo = new JComboBox<>(new String[]{
            "1.12.2",
            "1.8.9",
            "1.7.10"
        });
        panel.add(minecraftVersionCombo, gbc);

        // Mod Loader
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(new JLabel("Mod Loader:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        modLoaderCombo = new JComboBox<>(new String[]{
            "Forge"
        });
        panel.add(modLoaderCombo, gbc);

        // Info label
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><i>You'll be able to add items, blocks, and other elements after creating the project.</i></html>");
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel, gbc);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton createButton = new JButton("Create Project");
        createButton.setFont(new Font("Arial", Font.BOLD, 13));
        createButton.addActionListener(e -> createProject());
        panel.add(createButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        return panel;
    }

    private void generateModId() {
        String modName = modNameField.getText().trim();
        if (!modName.isEmpty()) {
            String modId = modName.toLowerCase()
                                  .replaceAll("[^a-z0-9]", "")
                                  .replaceAll("\\s+", "");
            modIdField.setText(modId);
        }
    }

    private void createProject() {
        String modName = modNameField.getText().trim();
        String modId = modIdField.getText().trim();
        String author = authorField.getText().trim();
        String minecraftVersion = (String) minecraftVersionCombo.getSelectedItem();
        String modLoader = ((String) modLoaderCombo.getSelectedItem()).toLowerCase();

        // Validate
        if (modName.isEmpty() || modId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in Mod Name and Mod ID",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!modId.matches("[a-z0-9_]+")) {
            JOptionPane.showMessageDialog(this,
                "Mod ID can only contain lowercase letters, numbers, and underscores",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create workspace
        Workspace workspace = new Workspace();
        workspace.setName(modName);
        workspace.setModId(modId);
        workspace.setMinecraftVersion(minecraftVersion);
        workspace.setModLoader(modLoader);

        WorkspaceSettings settings = new WorkspaceSettings();
        settings.setVersion("1.0.0");
        settings.setAuthor(author);
        settings.setDescription("A cool Minecraft mod!");
        settings.setJavaPackage("com.example." + modId);
        workspace.setSettings(settings);

        // Create project
        try {
            Path projectPath = workspaceManager.createProject(modId, workspace);

            JOptionPane.showMessageDialog(this,
                "Project created successfully!\n" +
                "Location: " + projectPath,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            projectCreated = true;
            logger.info("Created new project: {}", modName);

            // Open the project editor immediately
            WorkspaceManager.ProjectInfo projectInfo = new WorkspaceManager.ProjectInfo(
                modId,
                projectPath,
                workspace
            );

            WorkspaceEditorWindow editor = new WorkspaceEditorWindow(projectInfo, workspaceManager);
            editor.setVisible(true);

            // Close this dialog
            dispose();
        } catch (Exception e) {
            logger.error("Failed to create project", e);
            JOptionPane.showMessageDialog(this,
                "Failed to create project:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isProjectCreated() {
        return projectCreated;
    }
}
