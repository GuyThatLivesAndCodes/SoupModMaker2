package com.soupmodmaker.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Main menu window for SoupModMaker2.
 * Shows list of workspace projects and provides options to create, open, or delete projects.
 */
public class MainMenuWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuWindow.class);

    private final WorkspaceManager workspaceManager;
    private DefaultTableModel projectsTableModel;
    private JTable projectsTable;
    private List<WorkspaceManager.ProjectInfo> projects;

    public MainMenuWindow() {
        this.workspaceManager = new WorkspaceManager();
        initializeUI();
        refreshProjectsList();
    }

    private void initializeUI() {
        setTitle("SoupModMaker2 - Project Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Projects table
        mainPanel.add(createProjectsPanel(), BorderLayout.CENTER);

        // Buttons
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("SoupModMaker2");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        panel.add(titleLabel, BorderLayout.WEST);

        JLabel pathLabel = new JLabel("Projects: " + workspaceManager.getProjectsDirectory());
        pathLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        pathLabel.setForeground(Color.GRAY);
        panel.add(pathLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createProjectsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JLabel label = new JLabel("Your Mod Projects:");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);

        // Projects table
        projectsTableModel = new DefaultTableModel(
            new String[]{"Project Name", "Mod ID", "Minecraft Version", "Author"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        projectsTable = new JTable(projectsTableModel);
        projectsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectsTable.setRowHeight(25);
        projectsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        projectsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        projectsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        projectsTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        // Double-click to open
        projectsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openSelectedProject();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(projectsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton newButton = new JButton("Create New Project");
        newButton.setFont(new Font("Arial", Font.BOLD, 13));
        newButton.addActionListener(e -> createNewProject());
        panel.add(newButton);

        JButton openButton = new JButton("Open Project");
        openButton.setFont(new Font("Arial", Font.BOLD, 13));
        openButton.addActionListener(e -> openSelectedProject());
        panel.add(openButton);

        JButton deleteButton = new JButton("Delete Project");
        deleteButton.setForeground(new Color(180, 0, 0));
        deleteButton.addActionListener(e -> deleteSelectedProject());
        panel.add(deleteButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshProjectsList());
        panel.add(refreshButton);

        return panel;
    }

    private void refreshProjectsList() {
        projectsTableModel.setRowCount(0);
        projects = workspaceManager.listProjects();

        if (projects.isEmpty()) {
            projectsTableModel.addRow(new Object[]{"No projects yet. Click 'Create New Project' to get started!", "", "", ""});
        } else {
            for (WorkspaceManager.ProjectInfo project : projects) {
                projectsTableModel.addRow(new Object[]{
                    project.getWorkspace().getName(),
                    project.getWorkspace().getModId(),
                    project.getWorkspace().getMinecraftVersion(),
                    project.getWorkspace().getSettings().getAuthor()
                });
            }
        }

        logger.info("Loaded {} projects", projects.size());
    }

    private void createNewProject() {
        NewProjectDialog dialog = new NewProjectDialog(this, workspaceManager);
        dialog.setVisible(true);

        // Refresh list after dialog closes
        refreshProjectsList();
    }

    private void openSelectedProject() {
        int selectedRow = projectsTable.getSelectedRow();

        if (selectedRow == -1 || projects.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select a project to open",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        WorkspaceManager.ProjectInfo project = projects.get(selectedRow);
        logger.info("Opening project: {}", project.getName());

        // Open workspace editor
        WorkspaceEditorWindow editor = new WorkspaceEditorWindow(project, workspaceManager);
        editor.setVisible(true);

        // Hide main menu while editor is open
        setVisible(false);
    }

    private void deleteSelectedProject() {
        int selectedRow = projectsTable.getSelectedRow();

        if (selectedRow == -1 || projects.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select a project to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        WorkspaceManager.ProjectInfo project = projects.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the project:\n" +
            project.getWorkspace().getName() + " (" + project.getName() + ")?\n\n" +
            "This action cannot be undone!",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                workspaceManager.deleteProject(project.getPath());
                JOptionPane.showMessageDialog(this,
                    "Project deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                refreshProjectsList();
                logger.info("Deleted project: {}", project.getName());
            } catch (Exception e) {
                logger.error("Failed to delete project", e);
                JOptionPane.showMessageDialog(this,
                    "Failed to delete project:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
