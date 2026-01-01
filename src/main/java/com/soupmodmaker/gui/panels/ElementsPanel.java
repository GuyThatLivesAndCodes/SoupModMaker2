package com.soupmodmaker.gui.panels;

import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.core.workspace.WorkspaceElement;
import com.soupmodmaker.core.workspace.elements.ItemElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * MCreator-style Elements panel with grid view, subtabs, and element editors.
 */
public class ElementsPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ElementsPanel.class);

    private final Workspace workspace;
    private final Runnable onWorkspaceChanged;

    private JPanel elementsGrid;
    private JTabbedPane editorTabs;
    private JPanel elementsViewPanel;

    private WorkspaceElement selectedElement = null;
    private final Map<WorkspaceElement, ElementEditorPanel> openEditors = new HashMap<>();

    public ElementsPanel(Workspace workspace, Runnable onWorkspaceChanged) {
        this.workspace = workspace;
        this.onWorkspaceChanged = onWorkspaceChanged;
        initializeUI();
        refreshElementsGrid();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Main split: sidebar + content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(150);

        // Left sidebar with action buttons
        splitPane.setLeftComponent(createSidebar());

        // Right content area (tabs or grid)
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Editor tabs (hidden initially)
        editorTabs = new JTabbedPane();
        editorTabs.setVisible(false);

        // Elements grid view
        elementsViewPanel = createElementsView();

        contentPanel.add(editorTabs, BorderLayout.CENTER);
        contentPanel.add(elementsViewPanel, BorderLayout.CENTER);

        splitPane.setRightComponent(contentPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Actions");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 12f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(10));

        // Create button
        JButton createButton = new JButton("+ Create");
        createButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        createButton.addActionListener(e -> showCreateElementDialog());
        sidebar.add(createButton);
        sidebar.add(Box.createVerticalStrut(5));

        // Edit button
        JButton editButton = new JButton("Edit");
        editButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        editButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        editButton.addActionListener(e -> editSelectedElement());
        sidebar.add(editButton);
        sidebar.add(Box.createVerticalStrut(5));

        // Rename button
        JButton renameButton = new JButton("Rename");
        renameButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        renameButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        renameButton.addActionListener(e -> renameSelectedElement());
        sidebar.add(renameButton);
        sidebar.add(Box.createVerticalStrut(5));

        // Delete button
        JButton deleteButton = new JButton("- Delete");
        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        deleteButton.setForeground(new Color(200, 50, 50));
        deleteButton.addActionListener(e -> deleteSelectedElement());
        sidebar.add(deleteButton);

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JPanel createElementsView() {
        JPanel viewPanel = new JPanel(new BorderLayout(5, 5));
        viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Workspace Elements");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        viewPanel.add(titleLabel, BorderLayout.NORTH);

        // Scrollable grid of elements
        elementsGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        JScrollPane scrollPane = new JScrollPane(elementsGrid);
        scrollPane.setBorder(null);
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        return viewPanel;
    }

    private void refreshElementsGrid() {
        elementsGrid.removeAll();

        if (workspace.getElements().isEmpty()) {
            JLabel emptyLabel = new JLabel("<html><center>No elements yet<br><br>Click '+ Create' to add an element</center></html>");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            elementsGrid.add(emptyLabel);
        } else {
            for (WorkspaceElement element : workspace.getElements()) {
                elementsGrid.add(createElementCard(element));
            }
        }

        elementsGrid.revalidate();
        elementsGrid.repaint();
    }

    private JPanel createElementCard(WorkspaceElement element) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(150, 100));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Type icon/label
        String typeIcon = getElementTypeIcon(element);
        JLabel iconLabel = new JLabel(typeIcon, SwingConstants.CENTER);
        iconLabel.setFont(iconLabel.getFont().deriveFont(Font.BOLD, 24f));
        card.add(iconLabel, BorderLayout.CENTER);

        // Element name
        JLabel nameLabel = new JLabel(element.getName(), SwingConstants.CENTER);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 12f));
        card.add(nameLabel, BorderLayout.SOUTH);

        // Click to select
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectElement(element, card);
                if (e.getClickCount() == 2) {
                    editSelectedElement();
                }
            }
        });

        return card;
    }

    private String getElementTypeIcon(WorkspaceElement element) {
        if (element instanceof ItemElement) return "🎁";
        return "📦";
    }

    private void selectElement(WorkspaceElement element, JPanel card) {
        selectedElement = element;

        // Visual feedback
        for (Component comp : elementsGrid.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(null);
            }
        }
        card.setBackground(new Color(100, 150, 200, 50));
    }

    private void showCreateElementDialog() {
        // Element type selection dialog
        String[] elementTypes = {"Item", "Block", "Tool", "Armor", "Food", "Entity"};

        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Create New Element", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JLabel titleLabel = new JLabel("Select Element Type");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(new EmptyBorder(15, 15, 10, 15));
        dialog.add(titleLabel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        gridPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        for (String type : elementTypes) {
            JButton typeButton = new JButton("<html><center>" + getTypeIcon(type) + "<br>" + type + "</center></html>");
            typeButton.setPreferredSize(new Dimension(100, 80));
            typeButton.addActionListener(e -> {
                dialog.dispose();
                showCreateElementNameDialog(type);
            });
            gridPanel.add(typeButton);
        }

        dialog.add(gridPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private String getTypeIcon(String type) {
        return switch (type) {
            case "Item" -> "🎁";
            case "Block" -> "🧱";
            case "Tool" -> "⚒";
            case "Armor" -> "🛡";
            case "Food" -> "🍖";
            case "Entity" -> "🐑";
            default -> "📦";
        };
    }

    private void showCreateElementNameDialog(String type) {
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Create " + type, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Create New " + type);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        dialog.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        dialog.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        JTextField nameField = new JTextField(15);
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = name.toLowerCase().replaceAll("[^a-z0-9]", "_");
            WorkspaceElement element = createElementOfType(type, id, name);
            workspace.addElement(element);
            refreshElementsGrid();
            onWorkspaceChanged.run();
            dialog.dispose();

            // Open editor for new element
            openElementEditor(element);
        });
        buttonPanel.add(createButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, gbc);
        dialog.setVisible(true);
    }

    private WorkspaceElement createElementOfType(String type, String id, String name) {
        // For now, only Items are implemented
        if ("Item".equals(type)) {
            ItemElement item = new ItemElement();
            item.setId(id);
            item.setName(name);
            item.setProperty("maxStackSize", 64);
            return item;
        }

        // Use ItemElement as placeholder for other types until they're implemented
        ItemElement placeholder = new ItemElement();
        placeholder.setId(id);
        placeholder.setName(name + " (" + type + ")");
        placeholder.setProperty("elementType", type);
        placeholder.setProperty("maxStackSize", 64);
        return placeholder;
    }

    private void editSelectedElement() {
        if (selectedElement == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an element to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        openElementEditor(selectedElement);
    }

    private void openElementEditor(WorkspaceElement element) {
        // Check if already open
        if (openEditors.containsKey(element)) {
            editorTabs.setSelectedComponent(openEditors.get(element));
            showEditorTabs();
            return;
        }

        // Create new editor
        ElementEditorPanel editor = new ElementEditorPanel(element, () -> {
            onWorkspaceChanged.run();
            refreshElementsGrid();
        }, () -> closeEditor(element));

        openEditors.put(element, editor);
        editorTabs.addTab(element.getName(), editor);
        editorTabs.setSelectedComponent(editor);

        showEditorTabs();
    }

    private void closeEditor(WorkspaceElement element) {
        ElementEditorPanel editor = openEditors.remove(element);
        if (editor != null) {
            editorTabs.remove(editor);
        }

        if (editorTabs.getTabCount() == 0) {
            hideEditorTabs();
        }
    }

    private void showEditorTabs() {
        elementsViewPanel.setVisible(false);
        editorTabs.setVisible(true);
    }

    private void hideEditorTabs() {
        editorTabs.setVisible(false);
        elementsViewPanel.setVisible(true);
    }

    private void renameSelectedElement() {
        if (selectedElement == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an element to rename",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newName = JOptionPane.showInputDialog(this,
            "Enter new name:",
            selectedElement.getName());

        if (newName != null && !newName.trim().isEmpty()) {
            selectedElement.setName(newName.trim());
            refreshElementsGrid();
            onWorkspaceChanged.run();

            // Update tab name if open
            if (openEditors.containsKey(selectedElement)) {
                int index = editorTabs.indexOfComponent(openEditors.get(selectedElement));
                if (index >= 0) {
                    editorTabs.setTitleAt(index, newName.trim());
                }
            }
        }
    }

    private void deleteSelectedElement() {
        if (selectedElement == null) {
            JOptionPane.showMessageDialog(this,
                "Please select an element to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete element '" + selectedElement.getName() + "'?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Close editor if open
            if (openEditors.containsKey(selectedElement)) {
                closeEditor(selectedElement);
            }

            workspace.getElements().remove(selectedElement);
            selectedElement = null;
            refreshElementsGrid();
            onWorkspaceChanged.run();
        }
    }

    /**
     * Element editor panel with Save/Save & Close buttons.
     * Uses a consistent structure across all element types.
     */
    private class ElementEditorPanel extends JPanel {
        private final WorkspaceElement element;
        private final Runnable onSave;
        private final Runnable onClose;

        private boolean hasUnsavedChanges = false;

        public ElementEditorPanel(WorkspaceElement element, Runnable onSave, Runnable onClose) {
            this.element = element;
            this.onSave = onSave;
            this.onClose = onClose;
            initializeUI();
        }

        private void initializeUI() {
            setLayout(new BorderLayout(10, 10));
            setBorder(new EmptyBorder(15, 15, 15, 15));

            // Scrollable editor content
            JScrollPane scrollPane = new JScrollPane(createEditorContent());
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            add(scrollPane, BorderLayout.CENTER);

            // Bottom buttons
            add(createButtonPanel(), BorderLayout.SOUTH);
        }

        private JPanel createEditorContent() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            // Header with element type icon and name
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            String elementType = getElementTypeFromElement(element);
            JLabel iconLabel = new JLabel(getTypeIcon(elementType));
            iconLabel.setFont(iconLabel.getFont().deriveFont(24f));
            headerPanel.add(iconLabel);

            JLabel titleLabel = new JLabel(element.getName());
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
            headerPanel.add(titleLabel);

            JLabel typeLabel = new JLabel("(" + elementType + ")");
            typeLabel.setForeground(Color.GRAY);
            headerPanel.add(typeLabel);

            headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(headerPanel);
            panel.add(Box.createVerticalStrut(15));

            // General Properties (common to all types)
            panel.add(createGeneralPropertiesSection());
            panel.add(Box.createVerticalStrut(10));

            // Type-specific properties
            if (element instanceof ItemElement) {
                panel.add(createItemPropertiesSection((ItemElement) element));
            } else {
                panel.add(createPlaceholderPropertiesSection(elementType));
            }

            return panel;
        }

        private String getElementTypeFromElement(WorkspaceElement element) {
            Object type = element.getProperty("elementType");
            if (type != null) {
                return type.toString();
            }
            return element instanceof ItemElement ? "Item" : "Unknown";
        }

        private JPanel createGeneralPropertiesSection() {
            JPanel section = createSection("General Properties");

            // Display Name
            addPropertyField(section, "Display Name:", element.getName(), value -> {
                element.setName(value);
                hasUnsavedChanges = true;
            });

            // Internal ID (read-only)
            JTextField idField = new JTextField(element.getId());
            idField.setEditable(false);
            idField.setForeground(Color.GRAY);
            addPropertyRow(section, "Internal ID:", idField);

            return section;
        }

        private JPanel createItemPropertiesSection(ItemElement item) {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Visual Properties
            JPanel visualSection = createSection("Visual Properties");
            String texture = getStringProperty(item, "texture", "");
            addPropertyField(visualSection, "Texture:", texture,
                value -> {
                    item.setProperty("texture", value);
                    hasUnsavedChanges = true;
                });
            container.add(visualSection);
            container.add(Box.createVerticalStrut(10));

            // Behavior Properties
            JPanel behaviorSection = createSection("Behavior Properties");

            // Max Stack Size
            addSpinnerField(behaviorSection, "Max Stack Size:",
                item.getMaxStackSize(), 1, 64, 1,
                value -> {
                    item.setProperty("maxStackSize", value);
                    hasUnsavedChanges = true;
                });

            // Durability
            int durability = getIntProperty(item, "durability", 0);
            addSpinnerField(behaviorSection, "Durability:",
                durability, 0, 10000, 1,
                value -> {
                    item.setProperty("durability", value);
                    hasUnsavedChanges = true;
                });

            // Creative Tab
            String[] creativeTabs = {"Miscellaneous", "Building Blocks", "Decorations",
                                     "Redstone", "Transportation", "Food", "Tools",
                                     "Combat", "Brewing", "Materials"};
            String creativeTab = getStringProperty(item, "creativeTab", "Miscellaneous");
            addComboBoxField(behaviorSection, "Creative Tab:", creativeTabs, creativeTab,
                value -> {
                    item.setProperty("creativeTab", value);
                    hasUnsavedChanges = true;
                });

            // Is Food
            boolean isFood = getBooleanProperty(item, "isFood", false);
            addCheckboxField(behaviorSection, "Is Food", isFood,
                value -> {
                    item.setProperty("isFood", value);
                    hasUnsavedChanges = true;
                });

            container.add(behaviorSection);
            return container;
        }

        // Helper methods to safely get properties with defaults
        private String getStringProperty(WorkspaceElement element, String key, String defaultValue) {
            Object value = element.getProperty(key);
            return value != null ? value.toString() : defaultValue;
        }

        private int getIntProperty(WorkspaceElement element, String key, int defaultValue) {
            Object value = element.getProperty(key);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return defaultValue;
        }

        private boolean getBooleanProperty(WorkspaceElement element, String key, boolean defaultValue) {
            Object value = element.getProperty(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            return defaultValue;
        }

        private JPanel createPlaceholderPropertiesSection(String elementType) {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel section = createSection(elementType + " Properties");

            // Show type-specific placeholder properties based on element type
            switch (elementType) {
                case "Block" -> addBlockPlaceholderFields(section);
                case "Tool" -> addToolPlaceholderFields(section);
                case "Armor" -> addArmorPlaceholderFields(section);
                case "Food" -> addFoodPlaceholderFields(section);
                case "Entity" -> addEntityPlaceholderFields(section);
                default -> {
                    JLabel label = new JLabel("<html><i>Properties for " + elementType + " will be available soon!</i></html>");
                    label.setForeground(Color.GRAY);
                    section.add(label);
                }
            }

            container.add(section);
            return container;
        }

        private void addBlockPlaceholderFields(JPanel section) {
            addPropertyField(section, "Texture:", "", value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Hardness:", 1.5, 0, 100, 0.5, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Resistance:", 10.0, 0, 1000, 1, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Light Level:", 0, 0, 15, 1, value -> hasUnsavedChanges = true);
            addCheckboxField(section, "Needs Tool to Harvest", false, value -> hasUnsavedChanges = true);
        }

        private void addToolPlaceholderFields(JPanel section) {
            addPropertyField(section, "Texture:", "", value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Durability:", 250, 1, 10000, 1, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Efficiency:", 6.0, 0, 20, 0.5, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Damage:", 4.0, 0, 20, 0.5, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Harvest Level:", 2, 0, 5, 1, value -> hasUnsavedChanges = true);
        }

        private void addArmorPlaceholderFields(JPanel section) {
            addPropertyField(section, "Texture:", "", value -> hasUnsavedChanges = true);
            String[] armorSlots = {"Helmet", "Chestplate", "Leggings", "Boots"};
            addComboBoxField(section, "Armor Slot:", armorSlots, "Helmet", value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Armor Points:", 2, 0, 20, 1, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Toughness:", 0.0, 0, 10, 0.5, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Durability:", 100, 1, 10000, 1, value -> hasUnsavedChanges = true);
        }

        private void addFoodPlaceholderFields(JPanel section) {
            addPropertyField(section, "Texture:", "", value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Hunger Restored:", 4, 0, 20, 1, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Saturation:", 0.6, 0, 2, 0.1, value -> hasUnsavedChanges = true);
            addCheckboxField(section, "Always Edible", false, value -> hasUnsavedChanges = true);
            addCheckboxField(section, "Fast to Eat", false, value -> hasUnsavedChanges = true);
        }

        private void addEntityPlaceholderFields(JPanel section) {
            addPropertyField(section, "Model:", "", value -> hasUnsavedChanges = true);
            addPropertyField(section, "Texture:", "", value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Health:", 20.0, 1, 1000, 1, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Damage:", 2.0, 0, 50, 0.5, value -> hasUnsavedChanges = true);
            addSpinnerField(section, "Speed:", 0.25, 0, 2, 0.05, value -> hasUnsavedChanges = true);
        }

        private JPanel createSection(String title) {
            JPanel section = new JPanel();
            section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
            section.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12)
            ));
            section.setAlignmentX(Component.LEFT_ALIGNMENT);
            return section;
        }

        private void addPropertyField(JPanel section, String label, String initialValue,
                                      java.util.function.Consumer<String> onChange) {
            JTextField textField = new JTextField(initialValue);
            textField.addActionListener(e -> onChange.accept(textField.getText()));
            textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void changedUpdate(javax.swing.event.DocumentEvent e) { onChange.accept(textField.getText()); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { onChange.accept(textField.getText()); }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { onChange.accept(textField.getText()); }
            });
            addPropertyRow(section, label, textField);
        }

        private void addSpinnerField(JPanel section, String label, Number initialValue,
                                     Number min, Number max, Number step,
                                     java.util.function.Consumer<Number> onChange) {
            SpinnerNumberModel model;
            if (initialValue instanceof Double || min instanceof Double || max instanceof Double) {
                model = new SpinnerNumberModel(initialValue.doubleValue(),
                    min.doubleValue(), max.doubleValue(), step.doubleValue());
            } else {
                model = new SpinnerNumberModel(initialValue.intValue(),
                    min.intValue(), max.intValue(), step.intValue());
            }

            JSpinner spinner = new JSpinner(model);
            spinner.addChangeListener(e -> onChange.accept((Number) spinner.getValue()));
            addPropertyRow(section, label, spinner);
        }

        private void addComboBoxField(JPanel section, String label, String[] options,
                                      String initialValue, java.util.function.Consumer<String> onChange) {
            JComboBox<String> comboBox = new JComboBox<>(options);
            comboBox.setSelectedItem(initialValue);
            comboBox.addActionListener(e -> onChange.accept((String) comboBox.getSelectedItem()));
            addPropertyRow(section, label, comboBox);
        }

        private void addCheckboxField(JPanel section, String label, boolean initialValue,
                                      java.util.function.Consumer<Boolean> onChange) {
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.setSelected(initialValue);
            checkBox.addActionListener(e -> onChange.accept(checkBox.isSelected()));
            section.add(checkBox);
            section.add(Box.createVerticalStrut(5));
        }

        private void addPropertyRow(JPanel section, String labelText, JComponent component) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

            JLabel label = new JLabel(labelText);
            label.setPreferredSize(new Dimension(150, 25));
            row.add(label, BorderLayout.WEST);

            component.setPreferredSize(new Dimension(250, 25));
            row.add(component, BorderLayout.CENTER);

            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            section.add(row);
            section.add(Box.createVerticalStrut(5));
        }

        private JPanel createButtonPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton saveButton = new JButton("💾 Save");
            saveButton.addActionListener(e -> save());
            panel.add(saveButton);

            JButton saveAndCloseButton = new JButton("💾 Save & Close");
            saveAndCloseButton.addActionListener(e -> {
                save();
                onClose.run();
            });
            panel.add(saveAndCloseButton);

            JButton closeButton = new JButton("✕ Close");
            closeButton.addActionListener(e -> attemptClose());
            panel.add(closeButton);

            return panel;
        }

        private void save() {
            onSave.run();
            hasUnsavedChanges = false;
        }

        private void attemptClose() {
            if (hasUnsavedChanges) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes. Close anyway?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            onClose.run();
        }
    }
}
