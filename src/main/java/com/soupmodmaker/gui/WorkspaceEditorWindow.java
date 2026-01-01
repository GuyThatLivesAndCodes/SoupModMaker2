package com.soupmodmaker.gui;

import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.generators.GeneratedMod;
import com.soupmodmaker.generators.impl.Forge1122Generator;
import com.soupmodmaker.gui.panels.ConsolePanel;
import com.soupmodmaker.gui.panels.ElementsPanel;
import com.soupmodmaker.gui.panels.ResourcesPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

/**
 * Modern workspace editor with panel-based IDE-like interface.
 */
public class WorkspaceEditorWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceEditorWindow.class);

    private final WorkspaceManager.ProjectInfo projectInfo;
    private final WorkspaceManager workspaceManager;
    private final Workspace workspace;

    private ElementsPanel elementsPanel;
    private ResourcesPanel resourcesPanel;
    private ConsolePanel consolePanel;
    private JTabbedPane rightTabbedPane;

    public WorkspaceEditorWindow(WorkspaceManager.ProjectInfo projectInfo, WorkspaceManager workspaceManager) {
        this.projectInfo = projectInfo;
        this.workspaceManager = workspaceManager;
        this.workspace = projectInfo.getWorkspace();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("SoupModMaker2 - " + workspace.getName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                closeEditor();
            }
        });
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Top toolbar
        mainPanel.add(createToolbar(), BorderLayout.NORTH);

        // Center + Right panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(700);
        splitPane.setLeftComponent(createCenterPanel());
        splitPane.setRightComponent(createRightPanel());

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Bottom action bar
        mainPanel.add(createActionBar(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Left: Project info
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        JLabel projectNameLabel = new JLabel(workspace.getName());
        projectNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        leftPanel.add(projectNameLabel);

        JLabel versionLabel = new JLabel("v" + workspace.getSettings().getVersion());
        versionLabel.setForeground(Color.GRAY);
        leftPanel.add(versionLabel);

        JLabel mcVersionLabel = new JLabel("| MC " + workspace.getMinecraftVersion());
        mcVersionLabel.setForeground(Color.GRAY);
        leftPanel.add(mcVersionLabel);

        toolbar.add(leftPanel, BorderLayout.WEST);

        // Right: Action buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        JButton settingsButton = new JButton("⚙ Settings");
        settingsButton.addActionListener(e -> openSettings());
        rightPanel.add(settingsButton);

        JButton saveButton = new JButton("💾 Save");
        saveButton.addActionListener(e -> saveWorkspace());
        rightPanel.add(saveButton);

        toolbar.add(rightPanel, BorderLayout.EAST);

        return toolbar;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 5));

        // Project overview
        JPanel overviewPanel = new JPanel();
        overviewPanel.setLayout(new BoxLayout(overviewPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("<html><h1>" + workspace.getName() + "</h1></html>");
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        overviewPanel.add(welcomeLabel);

        overviewPanel.add(Box.createVerticalStrut(10));

        JLabel descLabel = new JLabel("<html><p>" + workspace.getSettings().getDescription() + "</p></html>");
        descLabel.setForeground(Color.GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        overviewPanel.add(descLabel);

        overviewPanel.add(Box.createVerticalStrut(20));

        JLabel instructionsLabel = new JLabel("<html>" +
            "<p><b>Getting Started:</b></p>" +
            "<ul>" +
            "<li>Use the <b>Elements</b> tab on the right to add items, blocks, and entities</li>" +
            "<li>Use the <b>Resources</b> tab to manage textures and sounds</li>" +
            "<li>Check the <b>Console</b> tab for build logs and output</li>" +
            "<li>Click <b>Generate Mod</b> when you're ready to export</li>" +
            "</ul>" +
            "</html>");
        instructionsLabel.setForeground(Color.GRAY);
        instructionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        overviewPanel.add(instructionsLabel);

        panel.add(overviewPanel, BorderLayout.NORTH);

        return panel;
    }

    private JComponent createRightPanel() {
        rightTabbedPane = new JTabbedPane();
        rightTabbedPane.setBorder(new EmptyBorder(10, 5, 10, 10));

        // Elements tab
        elementsPanel = new ElementsPanel(workspace, this::saveWorkspace);
        rightTabbedPane.addTab("📦 Elements", elementsPanel);

        // Resources tab
        resourcesPanel = new ResourcesPanel();
        rightTabbedPane.addTab("🎨 Resources", resourcesPanel);

        // Console tab
        consolePanel = new ConsolePanel();
        rightTabbedPane.addTab("📋 Console", consolePanel);

        return rightTabbedPane;
    }

    private JPanel createActionBar() {
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Left: Close button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton closeButton = new JButton("← Back to Projects");
        closeButton.addActionListener(e -> closeEditor());
        leftPanel.add(closeButton);

        actionBar.add(leftPanel, BorderLayout.WEST);

        // Right: Generate button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateButton = new JButton("🚀 Generate Mod");
        generateButton.setFont(generateButton.getFont().deriveFont(Font.BOLD, 14f));
        generateButton.setPreferredSize(new Dimension(150, 35));
        generateButton.addActionListener(e -> generateMod());
        rightPanel.add(generateButton);

        actionBar.add(rightPanel, BorderLayout.EAST);

        return actionBar;
    }

    private void openSettings() {
        SettingsDialog dialog = new SettingsDialog(this, workspace, () -> {
            saveWorkspace();
            setTitle("SoupModMaker2 - " + workspace.getName());
            consolePanel.logInfo("Settings saved");
        });
        dialog.setVisible(true);
    }

    private void saveWorkspace() {
        try {
            workspaceManager.saveWorkspace(projectInfo.getPath(), workspace);
            logger.debug("Workspace saved: {}", projectInfo.getName());
        } catch (Exception e) {
            logger.error("Failed to save workspace", e);
            consolePanel.logError("Failed to save workspace: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Failed to save workspace:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeEditor() {
        saveWorkspace();

        // Return to main menu
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof MainMenuWindow && !window.isVisible()) {
                window.setVisible(true);
                break;
            }
        }

        dispose();
    }

    private void generateMod() {
        // Validate
        if (workspace.getName().isEmpty() || workspace.getModId().isEmpty()) {
            consolePanel.logError("Mod name and ID cannot be empty");
            JOptionPane.showMessageDialog(this,
                "Please fill in Mod Name and Mod ID in Settings",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save workspace before generating
        saveWorkspace();

        // Choose output directory
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Choose Output Directory");
        chooser.setCurrentDirectory(new File("generated"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            consolePanel.logInfo("Generation cancelled by user");
            return;
        }

        Path outputPath = chooser.getSelectedFile().toPath().resolve(workspace.getModId());

        // Switch to console tab
        rightTabbedPane.setSelectedComponent(consolePanel);

        consolePanel.logInfo("Starting mod generation...");
        consolePanel.logInfo("Target: " + workspace.getMinecraftVersion() + " " + workspace.getModLoader());

        // Generate mod in background
        SwingWorker<GeneratedMod, Void> worker = new SwingWorker<>() {
            @Override
            protected GeneratedMod doInBackground() throws Exception {
                logger.info("Generating mod: {}", workspace.getName());
                Forge1122Generator generator = new Forge1122Generator();
                return generator.generate(workspace, outputPath);
            }

            @Override
            protected void done() {
                try {
                    GeneratedMod result = get();
                    consolePanel.logSuccess("Mod generated successfully!");
                    consolePanel.logInfo("Generated " + result.getFiles().size() + " files");
                    consolePanel.logInfo("Location: " + outputPath);

                    JOptionPane.showMessageDialog(WorkspaceEditorWindow.this,
                            "Mod generated successfully!\n" +
                                    "Files: " + result.getFiles().size() + "\n" +
                                    "Location: " + outputPath,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    logger.info("Mod generation completed: {}", outputPath);
                } catch (Exception ex) {
                    logger.error("Failed to generate mod", ex);
                    consolePanel.logError("Generation failed: " + ex.getMessage());

                    JOptionPane.showMessageDialog(WorkspaceEditorWindow.this,
                            "Failed to generate mod:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }
}
