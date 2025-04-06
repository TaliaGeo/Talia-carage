package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeInsert {
    public boolean insertEmployee(int employeeID, String firstName, String lastName, String position, double salary, String hireDate) throws IllegalArgumentException {
        // Validate inputs
        if (employeeID <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive.");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First Name is required.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name is required.");
        }
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Position is required.");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative.");
        }
        if (hireDate == null || !hireDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Hire Date must be in YYYY-MM-DD format.");
        }

        String insertSQL = "INSERT INTO employees (EmployeeID, FirstName, LastName, Position, Salary, HireDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
            pstmt.setInt(1, employeeID);
            pstmt.setString(2, firstName.trim());
            pstmt.setString(3, lastName.trim());
            pstmt.setString(4, position.trim());
            pstmt.setDouble(5, salary);
            pstmt.setString(6, hireDate.trim());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Log the exception (you might want to use a logger instead of printStackTrace)
            e.printStackTrace();
            return false;
        }
    }
}
