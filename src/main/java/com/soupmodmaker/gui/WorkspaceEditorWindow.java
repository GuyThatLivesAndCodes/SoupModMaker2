package com.soupmodmaker.gui;

import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.core.workspace.WorkspaceSettings;
import com.soupmodmaker.core.workspace.elements.ItemElement;
import com.soupmodmaker.generators.GeneratedMod;
import com.soupmodmaker.generators.impl.Forge1122Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

/**
 * Workspace editor window for editing mod projects.
 * Allows adding/removing items and generating mods.
 */
public class WorkspaceEditorWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceEditorWindow.class);

    // Workspace management
    private final WorkspaceManager.ProjectInfo projectInfo;
    private final WorkspaceManager workspaceManager;
    private final Workspace workspace;

    // Workspace settings fields
    private JTextField modNameField;
    private JTextField modIdField;
    private JTextField versionField;
    private JTextField authorField;
    private JTextArea descriptionArea;
    private JLabel minecraftVersionLabel;

    // Items table
    private DefaultTableModel itemsTableModel;
    private JTable itemsTable;

    public WorkspaceEditorWindow(WorkspaceManager.ProjectInfo projectInfo, WorkspaceManager workspaceManager) {
        this.projectInfo = projectInfo;
        this.workspaceManager = workspaceManager;
        this.workspace = projectInfo.getWorkspace();

        initializeUI();
        loadWorkspaceData();
    }

    private void initializeUI() {
        setTitle("SoupModMaker2 - Editing: " + workspace.getName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                closeEditor();
            }
        });
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel with workspace and items
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.add(createWorkspacePanel());
        centerPanel.add(createItemsPanel());
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel(workspace.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.WEST);

        JLabel pathLabel = new JLabel("Project: " + projectInfo.getPath());
        pathLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        pathLabel.setForeground(Color.GRAY);
        panel.add(pathLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createWorkspacePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Workspace Settings"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Mod Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Mod Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        modNameField = new JTextField();
        modNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { saveWorkspace(); }
        });
        panel.add(modNameField, gbc);

        // Row 0: Mod ID (read-only)
        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Mod ID:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        modIdField = new JTextField();
        modIdField.setEditable(false);
        modIdField.setBackground(new Color(240, 240, 240));
        panel.add(modIdField, gbc);

        // Row 1: Version
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Version:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        versionField = new JTextField();
        versionField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { saveWorkspace(); }
        });
        panel.add(versionField, gbc);

        // Row 1: Author
        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        authorField = new JTextField();
        authorField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { saveWorkspace(); }
        });
        panel.add(authorField, gbc);

        // Row 2: Minecraft Version (read-only display)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Minecraft Version:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        minecraftVersionLabel = new JLabel();
        minecraftVersionLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(minecraftVersionLabel, gbc);

        // Row 3: Description (spans full width)
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { saveWorkspace(); }
        });
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        panel.add(descScroll, gbc);

        return panel;
    }

    private JPanel createItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new TitledBorder("Items"));

        // Items table
        itemsTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Max Stack Size"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemsTable = new JTable(itemsTableModel);
        JScrollPane scrollPane = new JScrollPane(itemsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> showAddItemDialog());
        buttonPanel.add(addItemButton);

        JButton removeItemButton = new JButton("Remove Item");
        removeItemButton.addActionListener(e -> removeSelectedItem());
        buttonPanel.add(removeItemButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Left side - Save button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            saveWorkspace();
            JOptionPane.showMessageDialog(this, "Workspace saved!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        });
        leftPanel.add(saveButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> closeEditor());
        leftPanel.add(closeButton);

        panel.add(leftPanel, BorderLayout.WEST);

        // Right side - Generate button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton generateButton = new JButton("Generate Mod");
        generateButton.setFont(new Font("Arial", Font.BOLD, 14));
        generateButton.setPreferredSize(new Dimension(150, 40));
        generateButton.addActionListener(e -> generateMod());
        rightPanel.add(generateButton);

        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void loadWorkspaceData() {
        // Load settings into UI
        modNameField.setText(workspace.getName());
        modIdField.setText(workspace.getModId());
        versionField.setText(workspace.getSettings().getVersion());
        authorField.setText(workspace.getSettings().getAuthor());
        descriptionArea.setText(workspace.getSettings().getDescription());
        minecraftVersionLabel.setText(workspace.getMinecraftVersion() + " (" + workspace.getModLoader() + ")");

        // Load items into table
        itemsTableModel.setRowCount(0);
        for (var element : workspace.getElements()) {
            if (element instanceof ItemElement) {
                ItemElement item = (ItemElement) element;
                itemsTableModel.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getMaxStackSize()
                });
            }
        }
    }

    private void saveWorkspace() {
        // Update workspace from UI
        workspace.setName(modNameField.getText().trim());
        workspace.getSettings().setVersion(versionField.getText().trim());
        workspace.getSettings().setAuthor(authorField.getText().trim());
        workspace.getSettings().setDescription(descriptionArea.getText().trim());

        try {
            workspaceManager.saveWorkspace(projectInfo.getPath(), workspace);
            logger.debug("Workspace saved: {}", projectInfo.getName());
        } catch (Exception e) {
            logger.error("Failed to save workspace", e);
            JOptionPane.showMessageDialog(this,
                "Failed to save workspace:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeEditor() {
        // Save before closing
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

    private void showAddItemDialog() {
        JDialog dialog = new JDialog(this, "Add Item", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Item ID
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Item ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(15);
        dialog.add(idField, gbc);

        // Item Name
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("Item Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        dialog.add(nameField, gbc);

        // Max Stack Size
        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(new JLabel("Max Stack Size:"), gbc);
        gbc.gridx = 1;
        JSpinner stackSizeSpinner = new JSpinner(new SpinnerNumberModel(64, 1, 64, 1));
        dialog.add(stackSizeSpinner, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("Add");
        okButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int stackSize = (Integer) stackSizeSpinner.getValue();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            addItem(id, name, stackSize);
            dialog.dispose();
        });
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addItem(String id, String name, int maxStackSize) {
        ItemElement item = new ItemElement();
        item.setId(id);
        item.setName(name);
        item.setProperty("maxStackSize", maxStackSize);

        workspace.addElement(item);
        itemsTableModel.addRow(new Object[]{id, name, maxStackSize});
        saveWorkspace();

        logger.info("Added item: {} ({})", name, id);
    }

    private void removeSelectedItem() {
        int selectedRow = itemsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) itemsTableModel.getValueAt(selectedRow, 0);
        workspace.getElements().removeIf(element -> element.getId().equals(id));
        itemsTableModel.removeRow(selectedRow);
        saveWorkspace();

        logger.info("Removed item: {}", id);
    }

    private void generateMod() {
        // Validate
        if (workspace.getName().isEmpty() || workspace.getModId().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Mod Name and Mod ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
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
            return; // User cancelled
        }

        Path outputPath = chooser.getSelectedFile().toPath().resolve(workspace.getModId());

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
                    JOptionPane.showMessageDialog(WorkspaceEditorWindow.this,
                            "Mod generated successfully!\n" +
                                    "Files: " + result.getFiles().size() + "\n" +
                                    "Location: " + outputPath,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Mod generation completed: {}", outputPath);
                } catch (Exception ex) {
                    logger.error("Failed to generate mod", ex);
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
