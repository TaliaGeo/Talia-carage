package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeUpdate {
    public boolean updateEmployeeField(int employeeID, String field, String newValue) throws IllegalArgumentException {
        String[] allowedFields = {"EmployeeID", "FirstName", "LastName", "Position", "Salary", "HireDate"};
        boolean validField = false;
        String formattedField = "";

        // Validate the field to update
        for (String f : allowedFields) {
            if (f.equalsIgnoreCase(field)) {
                formattedField = f;
                validField = true;
                break;
            }
        }
        if (!validField) {
            throw new IllegalArgumentException("Invalid field name.");
        }

        // Additional validation based on the field type
        switch (formattedField) {
            case "EmployeeID":
                try {
                    int empID = Integer.parseInt(newValue);
                    if (empID <= 0) {
                        throw new IllegalArgumentException("Employee ID must be positive.");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Employee ID.");
                }
                break;
            case "Salary":
                try {
                    double salary = Double.parseDouble(newValue);
                    if (salary < 0) {
                        throw new IllegalArgumentException("Salary cannot be negative.");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Salary.");
                }
                break;
            case "HireDate":
                if (!newValue.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new IllegalArgumentException("Hire Date must be in YYYY-MM-DD format.");
                }
                break;
            default:
                if (newValue == null || newValue.trim().isEmpty()) {
                    throw new IllegalArgumentException(formattedField + " cannot be empty.");
                }
                break;
        }

        String updateSQL = "UPDATE employees SET " + formattedField + " = ? WHERE EmployeeID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {
            // Set parameters based on the field type
            if (formattedField.equals("Salary")) {
                pstmt.setDouble(1, Double.parseDouble(newValue));
            } else if (formattedField.equals("EmployeeID")) {
                pstmt.setInt(1, Integer.parseInt(newValue));
            } else {
                pstmt.setString(1, newValue.trim());
            }
            pstmt.setInt(2, employeeID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log the exception (consider using a logger)
            e.printStackTrace();
            return false;
        }
    }
}
