package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles updating fields of existing services in the database.
 */
public class ServiceUpdate {

    /**
     * Updates a specific field of a service.
     *
     * @param serviceID ID of the service to update.
     * @param field     Field name to update (e.g., "Cost", "ServiceDescription").
     * @param newValue  New value for the field.
     * @return true if the update was successful; false otherwise.
     */
    public boolean updateServiceField(int serviceID, String field, String newValue) {
        // Validate field name against allowed fields to prevent SQL injection
        if (!isValidField(field)) {
            System.err.println("Invalid field name: " + field);
            return false;
        }

        String updateSQL = "UPDATE services SET " + field + " = ? WHERE ServiceID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {

            if (con == null) {
                System.err.println("Database connection failed.");
                return false;
            }

            // Set parameters based on field type
            if (field.equalsIgnoreCase("Cost") || field.equalsIgnoreCase("CarID") || field.equalsIgnoreCase("ServiceID")) {
                // Numeric fields
                try {
                    if (field.equalsIgnoreCase("Cost")) {
                        pstmt.setDouble(1, Double.parseDouble(newValue));
                    } else {
                        pstmt.setInt(1, Integer.parseInt(newValue));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Invalid value format for field: " + field);
                    return false;
                }
            } else if (field.equalsIgnoreCase("ServiceDate")) {
                // Date field
                pstmt.setString(1, newValue); // Consider using java.sql.Date for date fields
            } else if (field.equalsIgnoreCase("ServiceDescription")) {
                // String field
                pstmt.setString(1, newValue);
            } else {
                // Other fields if any
                pstmt.setString(1, newValue);
            }

            pstmt.setInt(2, serviceID);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Service Updated. Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while updating service.");
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
        String[] validFields = {"CarID", "ServiceDate", "ServiceDescription", "Cost"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }
}
