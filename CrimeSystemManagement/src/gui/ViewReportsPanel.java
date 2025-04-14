package gui;

import model.CrimeReport;
import model.Person;
import system.CrimeSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewReportsPanel extends JPanel {
    private MainFrame parent;
    private JTable reportsTable;
    private DefaultTableModel tableModel;
    private JTextArea reportDetailsArea;
    private JLabel accessInfoLabel;

    public ViewReportsPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        // Create menu panel
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.NORTH);

        // Create table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Crime Reports"));

        // Add access information label
        accessInfoLabel = new JLabel();
        updateAccessInfoLabel();
        tablePanel.add(accessInfoLabel, BorderLayout.NORTH);

        // Table model
        String[] columnNames = {"ID", "Title", "Date", "Reporter"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        reportsTable = new JTable(tableModel);
        reportsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedReport();
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(reportsTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        // Report details area
        reportDetailsArea = new JTextArea(10, 30);
        reportDetailsArea.setEditable(false);
        reportDetailsArea.setLineWrap(true);
        reportDetailsArea.setWrapStyleWord(true);
        JScrollPane detailsScrollPane = new JScrollPane(reportDetailsArea);
        detailsScrollPane.setBorder(BorderFactory.createTitledBorder("Report Details"));

        // Create a split pane
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                tablePanel,
                detailsScrollPane
        );
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);

        // Add status panel at the bottom
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Logged in as: " + parent.getLoggedInUser().getName() +
                " (" + parent.getLoggedInUser().getRole() + ")");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // Load reports
        loadReports();
    }

    private void updateAccessInfoLabel() {
        Person currentUser = parent.getLoggedInUser();
        if (currentUser.getRole().equals("Police")) {
            accessInfoLabel.setText("Access Level: Showing all reports (Police Officer)");
            accessInfoLabel.setForeground(new Color(0, 100, 0)); // Dark green
        } else {
            accessInfoLabel.setText("Access Level: Showing only your reports (Citizen)");
            accessInfoLabel.setForeground(new Color(128, 0, 0)); // Dark red
        }
        accessInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        accessInfoLabel.setFont(accessInfoLabel.getFont().deriveFont(Font.BOLD));
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton newReportButton = new JButton("New Report");
        newReportButton.addActionListener(e -> parent.navigateTo(MainFrame.REPORTING_PANEL));
        menuPanel.add(newReportButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadReports());
        menuPanel.add(refreshButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        menuPanel.add(logoutButton);

        return menuPanel;
    }

    private void loadReports() {
        // Clear the existing data
        tableModel.setRowCount(0);
        reportDetailsArea.setText("");

        try {
            // Get current user
            Person currentUser = parent.getLoggedInUser();
            boolean isPolice = currentUser.getRole().equals("Police");
            String currentUserName = currentUser.getName();

            // Read reports from file
            List<String[]> reports = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("reports.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length >= 6) {
                        // Only add reports that the user is allowed to see
                        String reporterName = parts[4];

                        // Police can see all reports, citizens can only see their own
                        if (isPolice || reporterName.equals(currentUserName)) {
                            reports.add(parts);
                        }
                    }
                }
            }

            // Add reports to the table
            for (String[] report : reports) {
                String id = report[0];
                String title = report[1];
                String date = report[3];
                String reporter = report[4] + " (" + report[5] + ")";

                tableModel.addRow(new Object[]{id, title, date, reporter});
            }

            if (reports.isEmpty()) {
                if (isPolice) {
                    reportDetailsArea.setText("No reports available in the system.");
                } else {
                    reportDetailsArea.setText("You haven't submitted any reports yet.");
                }
            }

        } catch (IOException e) {
            reportDetailsArea.setText("Error loading reports: " + e.getMessage());
        }
    }

    private void displaySelectedReport() {
        int selectedRow = reportsTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String reportId = tableModel.getValueAt(selectedRow, 0).toString();

                // Find the complete report from the file
                try (BufferedReader br = new BufferedReader(new FileReader("reports.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(";");
                        if (parts.length >= 6 && parts[0].equals(reportId)) {
                            // Format the report details
                            StringBuilder sb = new StringBuilder();
                            sb.append("ID: ").append(parts[0]).append("\n");
                            sb.append("Title: ").append(parts[1]).append("\n");
                            sb.append("Description: ").append(parts[2]).append("\n");
                            sb.append("Date: ").append(parts[3]).append("\n");
                            sb.append("Reporter: ").append(parts[4]).append(" (").append(parts[5]).append(")\n");

                            reportDetailsArea.setText(sb.toString());
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                reportDetailsArea.setText("Error reading report details: " + e.getMessage());
            }
        } else {
            reportDetailsArea.setText("");
        }
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