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
 * Main window for SoupModMaker2 GUI.
 * Provides interface for workspace creation and mod generation.
 */
public class MainWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);

    // Workspace settings fields
    private JTextField modNameField;
    private JTextField modIdField;
    private JTextField versionField;
    private JTextField authorField;
    private JTextArea descriptionArea;
    private JComboBox<String> minecraftVersionCombo;

    // Items table
    private DefaultTableModel itemsTableModel;
    private JTable itemsTable;

    // Current workspace
    private Workspace workspace;

    public MainWindow() {
        initializeWorkspace();
        initializeUI();
    }

    private void initializeWorkspace() {
        workspace = new Workspace();
        workspace.setName("MyMod");
        workspace.setModId("mymod");

        WorkspaceSettings settings = new WorkspaceSettings();
        settings.setVersion("1.0.0");
        settings.setAuthor("YourName");
        settings.setDescription("A cool Minecraft mod!");
        settings.setJavaPackage("com.example.mymod");
        workspace.setSettings(settings);

        workspace.setMinecraftVersion("1.12.2");
        workspace.setModLoader("forge");
    }

    private void initializeUI() {
        setTitle("SoupModMaker2 - Better Minecraft Mod Maker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null); // Center on screen

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

        // Bottom panel with generate button
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("SoupModMaker2");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.WEST);

        JLabel subtitleLabel = new JLabel("Create Minecraft mods with clean, professional code");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

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
        modNameField = new JTextField(workspace.getName());
        panel.add(modNameField, gbc);

        // Row 0: Mod ID
        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Mod ID:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        modIdField = new JTextField(workspace.getModId());
        panel.add(modIdField, gbc);

        // Row 1: Version
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Version:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        versionField = new JTextField(workspace.getSettings().getVersion());
        panel.add(versionField, gbc);

        // Row 1: Author
        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        authorField = new JTextField(workspace.getSettings().getAuthor());
        panel.add(authorField, gbc);

        // Row 2: Minecraft Version
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Minecraft Version:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        minecraftVersionCombo = new JComboBox<>(new String[]{"1.12.2", "1.8.9", "1.7.10"});
        minecraftVersionCombo.setSelectedItem(workspace.getMinecraftVersion());
        panel.add(minecraftVersionCombo, gbc);

        // Row 3: Description (spans full width)
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        descriptionArea = new JTextArea(workspace.getSettings().getDescription(), 3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
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
                return false; // Make table read-only
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton generateButton = new JButton("Generate Mod");
        generateButton.setFont(new Font("Arial", Font.BOLD, 14));
        generateButton.setPreferredSize(new Dimension(150, 40));
        generateButton.addActionListener(e -> generateMod());
        panel.add(generateButton);

        return panel;
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

        logger.info("Removed item: {}", id);
    }

    private void generateMod() {
        // Update workspace from UI fields
        workspace.setName(modNameField.getText().trim());
        workspace.setModId(modIdField.getText().trim());
        workspace.setMinecraftVersion((String) minecraftVersionCombo.getSelectedItem());

        WorkspaceSettings settings = workspace.getSettings();
        settings.setVersion(versionField.getText().trim());
        settings.setAuthor(authorField.getText().trim());
        settings.setDescription(descriptionArea.getText().trim());
        settings.setJavaPackage("com.example." + workspace.getModId().toLowerCase());

        // Validate
        if (workspace.getName().isEmpty() || workspace.getModId().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Mod Name and Mod ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Mod generated successfully!\n" +
                                    "Files: " + result.getFiles().size() + "\n" +
                                    "Location: " + outputPath,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    logger.info("Mod generation completed: {}", outputPath);
                } catch (Exception ex) {
                    logger.error("Failed to generate mod", ex);
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Failed to generate mod:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }
}
