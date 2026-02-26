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
import java.util.HashMap;
import java.util.Map;

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
    
    public void deleteCartProductRecord(String productId, int orderId) {
        String sql = "DELETE FROM tbl_cart WHERE product_id = ? AND order_id = ?";
        Object[] values = { productId, orderId };

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
    
    public void deleteProductRecord(String productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        Object[] values = { productId };

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

    public int getSingleValue(String sql, int userId) {
        int id = -1;
        try (Connection conn = this.connectDB(); // Ensure this matches your connection method name
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1); // Get the first column (o_id)
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return id; // Returns the ID if found, otherwise returns -1
    }
    
    public int checkProductCart(String sql, String productId, int orderId) {
        System.out.println("Database Error: " + productId + " -- " + orderId);
        int id = -1;
        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);
            pstmt.setInt(2, orderId);
            
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return id; // Returns the ID if found, otherwise returns -1
    }
    
    public double getCartTotalAmount(int orderId) {
        double total = 0.0;
        
        String sql = "SELECT SUM(c.quantity * p.product_price) AS total_sum " +
                     "FROM tbl_cart c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "WHERE c.order_id = ?";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total_sum");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error calculating total: " + e.getMessage());
        }
        return total;
    }
    
    public Map<String, String> getProfileData(int userId) {
        Map<String, String> data = new HashMap<>();
        String sql = "SELECT firstname, lastname, email FROM user WHERE id = ?";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    data.put("firstname", rs.getString("firstname"));
                    data.put("lastname", rs.getString("lastname"));
                    data.put("email", rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching profile: " + e.getMessage());
        }
        return data;
    }

}
