package dao;

import model.Citizen;
import model.PoliceOfficer;
import model.Person;
import Util.DatabaseUtil;

import java.sql.*;

public class Userdao {

    public static Person login(String username, String password) throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        String query = "SELECT name, role FROM users WHERE username=? AND password=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("name");
            String role = rs.getString("role");
            if (role.equalsIgnoreCase("Citizen")) {
                return new Citizen(name);
            } else {
                return new PoliceOfficer(name);
            }
        }
        return null;
    }

    // Method to register a new user
    public static boolean registerUser(String name, String username, String password, String role) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO users (name, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, role);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }
}
