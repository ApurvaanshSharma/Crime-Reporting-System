import gui.MainFrame;
import Util.DatabaseUtil;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Test database connection first
        System.out.println("Testing database connection...");
        if (!DatabaseUtil.testConnection()) {
            JOptionPane.showMessageDialog(null,
                    "Failed to connect to database. Please check your connection parameters.",
                    "Database Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Launch the GUI
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}