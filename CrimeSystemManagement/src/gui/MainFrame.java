package gui;

import model.Person;
import system.CrimeSystem;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private ReportingPanel reportingPanel;
    private ViewReportsPanel viewReportsPanel;

    private Person loggedInUser;
    private CrimeSystem crimeSystem;

    public static final String LOGIN_PANEL = "Login Panel";
    public static final String REGISTER_PANEL = "Register Panel";
    public static final String REPORTING_PANEL = "Reporting Panel";
    public static final String VIEW_REPORTS_PANEL = "View Reports Panel";

    public MainFrame() {
        // Initialize the crime system
        crimeSystem = new CrimeSystem();

        // Set up the JFrame
        setTitle("Crime Reporting System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create the panels
        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);

        // Add panels to the card layout
        cardPanel.add(loginPanel, LOGIN_PANEL);
        cardPanel.add(registerPanel, REGISTER_PANEL);

        // ReportingPanel and ViewReportsPanel will be created after login

        // Add the card panel to the frame
        add(cardPanel);

        // Show the login panel first
        cardLayout.show(cardPanel, LOGIN_PANEL);
    }

    public void navigateTo(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public void setLoggedInUser(Person user) {
        this.loggedInUser = user;

        // Now create the panels that need the user info
        reportingPanel = new ReportingPanel(this, user);
        viewReportsPanel = new ViewReportsPanel(this);

        // Add these panels to the card layout
        cardPanel.add(reportingPanel, REPORTING_PANEL);
        cardPanel.add(viewReportsPanel, VIEW_REPORTS_PANEL);

        // Navigate to the reporting panel
        navigateTo(REPORTING_PANEL);
    }

    public Person getLoggedInUser() {
        return loggedInUser;
    }

    public CrimeSystem getCrimeSystem() {
        return crimeSystem;
    }
}