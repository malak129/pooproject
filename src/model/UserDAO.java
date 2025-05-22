package model;

import java.io.IOException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User getUserByUsername(String username) throws SQLException, IOException {
           String sql = "SELECT * FROM users WHERE username = ?";
           try (Connection conn = DataConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setString(1, username);
               ResultSet rs = pstmt.executeQuery();
               if (rs.next()) {
                   return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"),rs.getInt("score"));
               }
           }
           return null;
       }
    public void addUser(User user) throws SQLException, IOException {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
        }
    }
    public void updateUserScore(int userId, int newScore) throws SQLException, IOException {
        String query = "UPDATE users SET score = ? WHERE id = ?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, newScore);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
    public User getUserById(int userId) throws SQLException, IOException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("score")
                    );
                }
            }
        }
        return null;
    }
}


