package gui;

import dao.Userdao;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private MainFrame parent;

    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public RegisterPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel titleLabel = new JLabel("Crime Reporting System - Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(titleLabel, gbc);

        // Name
        JLabel nameLabel = new JLabel("Full Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(nameField, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        // Role
        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(roleLabel, gbc);

        String[] roles = {"Citizen", "Police"};
        roleComboBox = new JComboBox<>(roles);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(roleComboBox, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegistration());
        buttonPanel.add(registerButton);

        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> parent.navigateTo(MainFrame.LOGIN_PANEL));
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Add the form panel to the center
        add(formPanel, BorderLayout.CENTER);
    }

    private void handleRegistration() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = Userdao.registerUser(name, username, password, role);
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now log in.",
                    "Registration Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear fields
            nameField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            roleComboBox.setSelectedIndex(0);

            // Go back to login
            parent.navigateTo(MainFrame.LOGIN_PANEL);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registration failed. Username may already exist.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}