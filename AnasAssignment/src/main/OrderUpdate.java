package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles updating fields of existing orders in the database.
 */
public class OrderUpdate {

    /**
     * Updates a specific field of an order.
     *
     * @param orderId ID of the order to update.
     * @param field   Field name to update (e.g., "Quantity", "TotalPrice").
     * @param newValue New value for the field.
     * @return true if the update was successful; false otherwise.
     */
    public boolean updateOrderField(int orderId, String field, String newValue) {
        // Validate field name against allowed fields to prevent SQL injection
        if (!isValidField(field)) {
            System.err.println("Invalid field name: " + field);
            return false;
        }

        String updateSQL = "UPDATE orders SET " + field + " = ? WHERE OrderId = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

            if (con == null) {
                System.err.println("Database connection failed.");
                return false;
            }

            // Set parameters based on field type
            if (field.equalsIgnoreCase("TotalPrice") || field.equalsIgnoreCase("Quantity") || field.equalsIgnoreCase("CarID") ||
                field.equalsIgnoreCase("CustomerID") || field.equalsIgnoreCase("EmployeeID")) {
                // Numeric fields
                try {
                    if (field.equalsIgnoreCase("TotalPrice")) {
                        pstmt.setDouble(1, Double.parseDouble(newValue));
                    } else {
                        pstmt.setInt(1, Integer.parseInt(newValue));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid value format for field: " + field);
                    return false;
                }
            } else if (field.equalsIgnoreCase("OrderDate")) {
                // Date field
                pstmt.setString(1, newValue); // Consider using java.sql.Date for date fields
            } else {
                // Other fields (e.g., description)
                pstmt.setString(1, newValue);
            }

            pstmt.setInt(2, orderId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Order Updated. Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while updating order.");
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
        String[] validFields = {"CustomerID", "EmployeeID", "CarID", "Quantity", "TotalPrice", "OrderDate"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }
}
