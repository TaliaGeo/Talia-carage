package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles insertion of orders into the database.
 */
public class OrderInsert {

    /**
     * Inserts a new order into the database.
     *
     * @param orderID    Unique identifier for the order.
     * @param customerID ID of the customer placing the order.
     * @param employeeID ID of the employee handling the order.
     * @param carID      ID of the car being ordered.
     * @param quantity   Number of cars ordered.
     * @param totalPrice Total price of the order.
     * @param orderDate  Date of the order in YYYY-MM-DD format.
     * @return true if insertion was successful; false otherwise.
     */
    public boolean insertOrder(int orderID, int customerID, int employeeID, int carID, int quantity, double totalPrice, String orderDate) {
        String insertSQL = "INSERT INTO orders (OrderID, CustomerID, EmployeeID, CarID, Quantity, TotalPrice, OrderDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {

            if (con == null) {
                System.err.println("Database connection failed.");
                return false;
            }

            // Setting parameters for the query
            pstmt.setInt(1, orderID);
            pstmt.setInt(2, customerID);
            pstmt.setInt(3, employeeID);
            pstmt.setInt(4, carID);
            pstmt.setInt(5, quantity);
            pstmt.setDouble(6, totalPrice);
            pstmt.setString(7, orderDate);

            // Execute query and check if rows are affected
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Order Inserted. Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while inserting order.");
            e.printStackTrace();
            return false;
        }
    }
}
