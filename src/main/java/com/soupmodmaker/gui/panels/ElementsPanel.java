package com.soupmodmaker.gui.panels;

import com.soupmodmaker.core.workspace.Workspace;
import com.soupmodmaker.core.workspace.elements.ItemElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Elements panel showing items, blocks, entities, etc.
 */
public class ElementsPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ElementsPanel.class);

    private final Workspace workspace;
    private final Runnable onWorkspaceChanged;

    private JList<String> categoryList;
    private DefaultListModel<String> categoryListModel;
    private DefaultTableModel elementsTableModel;
    private JTable elementsTable;

    private String selectedCategory = "Items";

    public ElementsPanel(Workspace workspace, Runnable onWorkspaceChanged) {
        this.workspace = workspace;
        this.onWorkspaceChanged = onWorkspaceChanged;
        initializeUI();
        refreshElements();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Left: Category list
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(120);

        JPanel categoryPanel = createCategoryPanel();
        splitPane.setLeftComponent(categoryPanel);

        // Right: Elements table
        JPanel elementsPanel = createElementsPanel();
        splitPane.setRightComponent(elementsPanel);

        add(splitPane, BorderLayout.CENTER);

        // Bottom: Action buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JLabel label = new JLabel("Categories");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 12f));
        panel.add(label, BorderLayout.NORTH);

        categoryListModel = new DefaultListModel<>();
        categoryListModel.addElement("Items");
        categoryListModel.addElement("Blocks");
        categoryListModel.addElement("Entities");

        categoryList = new JList<>(categoryListModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setSelectedIndex(0);
        categoryList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedCategory = categoryList.getSelectedValue();
                refreshElements();
            }
        });

        JScrollPane scrollPane = new JScrollPane(categoryList);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createElementsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JLabel label = new JLabel("Elements");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 12f));
        panel.add(label, BorderLayout.NORTH);

        elementsTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Properties"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        elementsTable = new JTable(elementsTableModel);
        elementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        elementsTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(elementsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JButton addButton = new JButton("+ Add");
        addButton.addActionListener(e -> addElement());
        panel.add(addButton);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> editElement());
        panel.add(editButton);

        JButton removeButton = new JButton("- Remove");
        removeButton.addActionListener(e -> removeElement());
        panel.add(removeButton);

        return panel;
    }

    public void refreshElements() {
        elementsTableModel.setRowCount(0);

        if ("Items".equals(selectedCategory)) {
            for (var element : workspace.getElements()) {
                if (element instanceof ItemElement) {
                    ItemElement item = (ItemElement) element;
                    String properties = "Max Stack: " + item.getMaxStackSize();
                    elementsTableModel.addRow(new Object[]{
                        item.getId(),
                        item.getName(),
                        properties
                    });
                }
            }
        } else if ("Blocks".equals(selectedCategory)) {
            // Future: Show blocks
            elementsTableModel.addRow(new Object[]{"", "No blocks yet", "Coming soon!"});
        } else if ("Entities".equals(selectedCategory)) {
            // Future: Show entities
            elementsTableModel.addRow(new Object[]{"", "No entities yet", "Coming soon!"});
        }
    }

    private void addElement() {
        if ("Items".equals(selectedCategory)) {
            showAddItemDialog();
        } else {
            JOptionPane.showMessageDialog(this,
                selectedCategory + " are not implemented yet",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editElement() {
        int selectedRow = elementsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an element to edit",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
            "Edit functionality coming soon!",
            "Coming Soon",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeElement() {
        int selectedRow = elementsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select an element to remove",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!"Items".equals(selectedCategory)) {
            return;
        }

        String id = (String) elementsTableModel.getValueAt(selectedRow, 0);
        workspace.getElements().removeIf(element -> element.getId().equals(id));
        refreshElements();
        onWorkspaceChanged.run();

        logger.info("Removed item: {}", id);
    }

    private void showAddItemDialog() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Add Item", true);
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
                JOptionPane.showMessageDialog(dialog,
                    "Please fill in all fields",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
        refreshElements();
        onWorkspaceChanged.run();

        logger.info("Added item: {} ({})", name, id);
    }
}
