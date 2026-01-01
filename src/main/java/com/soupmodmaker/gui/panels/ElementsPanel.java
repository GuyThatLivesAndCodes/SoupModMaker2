package com.soupmodmaker.gui.panels;

import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.core.workspace.WorkspaceElement;
import com.soupmodmaker.core.workspace.elements.ItemElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Create New Element", true);
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
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Create " + type, true);
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

        // Placeholder for other types
        WorkspaceElement element = new WorkspaceElement(type.toLowerCase());
        element.setId(id);
        element.setName(name);
        return element;
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

            // Editor content
            add(createEditorContent(), BorderLayout.CENTER);

            // Bottom buttons
            add(createButtonPanel(), BorderLayout.SOUTH);
        }

        private JPanel createEditorContent() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JLabel titleLabel = new JLabel("Edit " + element.getType() + ": " + element.getName());
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(titleLabel);
            panel.add(Box.createVerticalStrut(20));

            if (element instanceof ItemElement) {
                panel.add(createItemEditorFields((ItemElement) element));
            } else {
                JLabel comingSoonLabel = new JLabel("<html><i>Editor for " + element.getType() + " coming soon!</i></html>");
                comingSoonLabel.setForeground(Color.GRAY);
                comingSoonLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(comingSoonLabel);
            }

            return panel;
        }

        private JPanel createItemEditorFields(ItemElement item) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
            panel.add(new JLabel("Max Stack Size:"), gbc);

            gbc.gridx = 1; gbc.weightx = 1;
            JSpinner stackSizeSpinner = new JSpinner(new SpinnerNumberModel(item.getMaxStackSize(), 1, 64, 1));
            stackSizeSpinner.addChangeListener(e -> {
                item.setProperty("maxStackSize", stackSizeSpinner.getValue());
                hasUnsavedChanges = true;
            });
            panel.add(stackSizeSpinner, gbc);

            return panel;
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
