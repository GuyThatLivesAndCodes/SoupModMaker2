package com.soupmodmaker.gui.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Console panel for showing build, generation, and run logs.
 */
public class ConsolePanel extends JPanel {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private JTextArea consoleArea;
    private JButton clearButton;

    public ConsolePanel() {
        initializeUI();
        log("Console ready.", LogLevel.INFO);
    }

    private void initializeUI() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Console");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 12f));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clear());
        headerPanel.add(clearButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Console text area
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        consoleArea.setLineWrap(false);

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void log(String message, LogLevel level) {
        SwingUtilities.invokeLater(() -> {
            String timestamp = LocalTime.now().format(TIME_FORMAT);
            String levelStr = String.format("%-7s", "[" + level.name() + "]");
            String formattedMessage = timestamp + " " + levelStr + " " + message + "\n";
            consoleArea.append(formattedMessage);

            // Auto-scroll to bottom
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        });
    }

    public void logInfo(String message) {
        log(message, LogLevel.INFO);
    }

    public void logSuccess(String message) {
        log(message, LogLevel.SUCCESS);
    }

    public void logWarning(String message) {
        log(message, LogLevel.WARNING);
    }

    public void logError(String message) {
        log(message, LogLevel.ERROR);
    }

    public void clear() {
        consoleArea.setText("");
        log("Console cleared.", LogLevel.INFO);
    }

    public enum LogLevel {
        INFO,
        SUCCESS,
        WARNING,
        ERROR
    }
}
