package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerUpdate {

    /**
     * Updates a specific field of a customer identified by CustomerID.
     *
     * @param customerID The ID of the customer to update.
     * @param field      The field to update (e.g., Email).
     * @param newValue   The new value for the field.
     * @return true if the update was successful; false otherwise.
     */
    public boolean updateCustomerField(int customerID, String field, String newValue) {
        // Whitelist of updatable fields to prevent SQL injection
        String[] validFields = {"FirstName", "LastName", "Email", "Phone", "Address", "City", "ZipCode"};
        boolean isValidField = false;
        String sanitizedField = null;

        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                sanitizedField = validField; // Ensure correct casing
                isValidField = true;
                break;
            }
        }

        if (!isValidField) {
            System.err.println("Invalid field name for update: " + field);
            return false;
        }

        String updateSQL = "UPDATE customers SET " + sanitizedField + " = ? WHERE CustomerID = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

            // Set the new value based on the field type
            switch (sanitizedField) {
                case "FirstName":
                case "LastName":
                case "Email":
                case "Phone":
                case "Address":
                case "City":
                case "ZipCode":
                    pstmt.setString(1, newValue);
                    break;
                default:
                    // Should not reach here due to prior validation
                    pstmt.setString(1, newValue);
            }

            pstmt.setInt(2, customerID);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected by updateCustomerField: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating customer field:");
            e.printStackTrace();
            return false;
        }
    }
}
