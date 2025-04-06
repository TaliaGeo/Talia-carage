package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeSalaryUpdate {

    /**
     * Adjusts the salary of all employees by a fixed amount.
     *
     * @param amount The amount to add (positive) or subtract (negative) from each employee's salary.
     * @return true if at least one row was updated; false otherwise.
     */
    public boolean adjustSalariesByAmount(double amount) {
        String updateSQL = "UPDATE employees SET Salary = Salary + ?";
        return executeUpdate(updateSQL, amount);
    }

    /**
     * Adjusts the salary of all employees by a percentage.
     *
     * @param percentage The percentage to increase (positive) or decrease (negative) each employee's salary.
     * @return true if at least one row was updated; false otherwise.
     */
    public boolean adjustSalariesByPercentage(double percentage) {
        String updateSQL = "UPDATE employees SET Salary = Salary * (1 + ? / 100)";
        return executeUpdate(updateSQL, percentage);
    }

    /**
     * Executes an update statement with a single double parameter.
     *
     * @param sql   The SQL update statement.
     * @param param The parameter to set in the PreparedStatement.
     * @return true if at least one row was updated; false otherwise.
     */
    private boolean executeUpdate(String sql, double param) {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            if (con == null) {
                System.err.println("Failed to establish a database connection.");
                return false;
            }

            // Ensure auto-commit is enabled
            if (!con.getAutoCommit()) {
                con.setAutoCommit(true);
            }

            pstmt.setDouble(1, param);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while adjusting salaries.");
            e.printStackTrace();
            return false;
        }
    }
}
