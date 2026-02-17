/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Juliana Ritz Magat
 */
public class config {
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load the SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:food.db"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }
    
    public int addOrderRecord(String sql, Object... values) {
        int generatedId = -1;

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1); // This is your New ID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }
    
    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }
    
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
    
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }
    
    public void deleteRecord(String userId) {
        String sql = "DELETE FROM user WHERE id = ?";
        Object[] values = { userId };

        try (Connection conn = connectDB();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Record deleted successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
    
    public void deleteCartItem(String itemId) {
        String sql = "DELETE FROM tbl_cart WHERE id = ?";
        Object[] values = { itemId };

        try (Connection conn = connectDB();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            int rowsDeleted = pstmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Record deleted successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }
    
//    public boolean authenticate(String sql, Object... values) {
//    try (Connection conn = connectDB();
//         PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//        for (int i = 0; i < values.length; i++) {
//            pstmt.setObject(i + 1, values[i]);
//        }
//
//        try (ResultSet rs = pstmt.executeQuery()) {
//            if (rs.next()) {
//                return true;
//            }
//        }
//    } catch (SQLException e) {
//        System.out.println("Login Error: " + e.getMessage());
//    }
//    return false;
//}
    
    public boolean authenticate(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    int id = rs.getInt("id");   // DB column name
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String email = rs.getString("email");
                    String type = rs.getString("type");
                    String status = rs.getString("status");

                    // SAVE SESSION
                    Session.setUser(id, firstname, lastname, status, email, type);

                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }
        return false;
    }

    public void displayData(String sql, javax.swing.JTable table) {
        try (Connection conn = connectDB();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            // This line automatically maps the Resultset to your JTable
            table.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException e) {
            System.out.println("Error displaying data: " + e.getMessage());
        }
    }
    
    public String getUserType(String sql, String email, String password, String status) {
        String userType = null;
        try (Connection conn = connectDB();
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.setString(2, password);
            pst.setString(3, status);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    userType = rs.getString("type"); // column name from your DB
                    int id = rs.getInt("id");   // DB column name
                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String type = rs.getString("type");

                    // SAVE SESSION
                    Session.setUser(id, firstname, lastname, status, email, type);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userType;
    }
    
public java.util.List<java.util.Map<String, Object>> fetchRecords(String sqlQuery, Object... values) {
    java.util.List<java.util.Map<String, Object>> records = new java.util.ArrayList<>();

    try (Connection conn = this.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            java.util.Map<String, Object> row = new java.util.HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(metaData.getColumnName(i), rs.getObject(i));
            }
            records.add(row);
        }

    } catch (SQLException e) {
        System.out.println("Error fetching records: " + e.getMessage());
    }

    return records;
}

}
