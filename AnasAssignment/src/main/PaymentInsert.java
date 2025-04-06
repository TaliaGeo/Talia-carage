package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles insertion of payments into the database.
 */
public class PaymentInsert {

    /**
     * Inserts a new payment into the database.
     *
     * @param paymentID     Unique identifier for the payment.
     * @param orderID       ID of the associated order.
     * @param amount        Amount of the payment.
     * @param paymentDate   Date of the payment in YYYY-MM-DD format.
     * @param paymentMethod Method of payment (e.g., Credit Card, Cash).
     * @return true if insertion was successful; false otherwise.
     */
    public boolean insertPayment(int paymentID, int orderID, double amount, String paymentDate, String paymentMethod) {
        String insertSQL = "INSERT INTO payments (PaymentID, OrderID, Amount, PaymentDate, PaymentMethod) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
             
            if (con == null) {
                System.err.println("Database connection failed.");
                return false;
            }

            // Setting parameters for the query
            pstmt.setInt(1, paymentID);
            pstmt.setInt(2, orderID);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, paymentDate);
            pstmt.setString(5, paymentMethod);

            // Execute query and check if rows are affected
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Payment Inserted. Rows affected: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while inserting payment.");
            e.printStackTrace();
            return false;
        }
    }
}
