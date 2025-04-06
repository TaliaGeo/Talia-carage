package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles updating fields of existing payments in the database.
 */
public class PaymentUpdate {

    /**
     * Updates a specific field of a payment.
     *
     * @param paymentID ID of the payment to update.
     * @param field     Field name to update (e.g., "Amount", "PaymentMethod").
     * @param newValue  New value for the field.
     * @return true if the update was successful; false otherwise.
     */
    public boolean updatePaymentField(int paymentID, String field, String newValue) {
        // Validate field name against allowed fields to prevent SQL injection
        if (!isValidField(field)) {
            System.err.println("Invalid field name: " + field);
            return false;
        }

        String updateSQL = "UPDATE payments SET " + field + " = ? WHERE PaymentID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

            if (con == null) {
                System.err.println("Database connection failed.");
                return false;
            }

            // Set parameters based on field type
            if (field.equalsIgnoreCase("Amount") || field.equalsIgnoreCase("OrderID") || field.equalsIgnoreCase("PaymentID")) {
                // Numeric fields
                try {
                    if (field.equalsIgnoreCase("Amount")) {
                        pstmt.setDouble(1, Double.parseDouble(newValue));
                    } else {
                        pstmt.setInt(1, Integer.parseInt(newValue));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid value format for field: " + field);
                    return false;
                }
            } else if (field.equalsIgnoreCase("PaymentDate")) {
                // Date field
                pstmt.setString(1, newValue); // Consider using java.sql.Date for date fields
            } else if (field.equalsIgnoreCase("PaymentMethod")) {
                // String field
                pstmt.setString(1, newValue);
            } else {
                // Other fields if any
                pstmt.setString(1, newValue);
            }

            pstmt.setInt(2, paymentID);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Payment Updated. Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while updating payment.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates if the provided field name is allowed to be updated.
     *
     * @param field Field name to validate.
     * @return true if valid; false otherwise.
     */
    private boolean isValidField(String field) {
        String[] validFields = {"OrderID", "Amount", "PaymentDate", "PaymentMethod"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }
}
