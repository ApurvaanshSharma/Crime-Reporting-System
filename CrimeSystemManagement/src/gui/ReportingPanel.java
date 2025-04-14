package gui;

import model.CrimeReport;
import model.Person;
import system.CrimeSystem;
import threads.ReportSaver;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportingPanel extends JPanel {
    private MainFrame parent;
    private Person user;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField dateField;

    public ReportingPanel(MainFrame parent, Person user) {
        this.parent = parent;
        this.user = user;

        setLayout(new BorderLayout());

        // Create menu panel for navigation
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.NORTH);

        // Create report form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel panelTitleLabel = new JLabel("Submit Crime Report");
        panelTitleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(panelTitleLabel, gbc);

        // Report Title
        JLabel titleLabel = new JLabel("Crime Title:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(titleLabel, gbc);

        titleField = new JTextField(30);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(titleField, gbc);

        // Description
        JLabel descLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(descLabel, gbc);

        descriptionArea = new JTextArea(10, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(scrollPane, gbc);

        // Date
        JLabel dateLabel = new JLabel("Date (dd-mm-yyyy):");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(dateLabel, gbc);

        // Current date as default
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = sdf.format(new Date());

        dateField = new JTextField(currentDate, 10);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(dateField, gbc);

        // Submit button
        JButton submitButton = new JButton("Submit Report");
        submitButton.addActionListener(e -> submitReport());

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        // Add the form panel to the center
        add(formPanel, BorderLayout.CENTER);

        // Add status panel at the bottom
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Logged in as: " + user.getName() + " (" + user.getRole() + ")");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton viewReportsButton = new JButton("View Reports");
        viewReportsButton.addActionListener(e -> parent.navigateTo(MainFrame.VIEW_REPORTS_PANEL));
        menuPanel.add(viewReportsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        menuPanel.add(logoutButton);

        return menuPanel;
    }

    private void submitReport() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String date = dateField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required.",
                    "Submission Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate date format
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use dd-mm-yyyy format.",
                    "Submission Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create and add the report
        CrimeReport report = new CrimeReport(title, description, date, user);
        parent.getCrimeSystem().addReport(report);

        // Create and start a thread to save the report asynchronously
        new ReportSaver(report, parent.getCrimeSystem()).start();

        JOptionPane.showMessageDialog(this,
                "Report submitted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        // Clear the form fields
        titleField.setText("");
        descriptionArea.setText("");
        // Keep the date as is
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            parent.navigateTo(MainFrame.LOGIN_PANEL);
        }
    }
}