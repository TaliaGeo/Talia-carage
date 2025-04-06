package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Handles searching for orders in the database based on various criteria.
 */
public class OrderSearch {

    /**
     * Searches for orders based on provided parameters.
     *
     * @param orderId    ID of the order.
     * @param carId      ID of the car.
     * @param customerId ID of the customer.
     * @param employeeId ID of the employee.
     * @param quantity   Quantity ordered.
     * @param totalPrice Total price of the order.
     * @return ObservableList containing the search results.
     */
    public ObservableList<ObservableList<String>> searchOrders(
            String orderId, String carId, String customerId, String employeeId, String quantity, String totalPrice) {

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        StringBuilder query = new StringBuilder("SELECT OrderId, CarID, CustomerID, EmployeeID, Quantity, TotalPrice, OrderDate FROM orders WHERE 1=1");
        ObservableList<Object> parameters = FXCollections.observableArrayList();

        if (!orderId.isEmpty()) {
            query.append(" AND OrderId = ?");
            try {
                parameters.add(Integer.parseInt(orderId));
            } catch (NumberFormatException e) {
                // Invalid Order ID input; skip this filter
            }
        }

        if (!carId.isEmpty()) {
            query.append(" AND CarID = ?");
            try {
                parameters.add(Integer.parseInt(carId));
            } catch (NumberFormatException e) {
                // Invalid Car ID input; skip this filter
            }
        }

        if (!customerId.isEmpty()) {
            query.append(" AND CustomerID = ?");
            try {
                parameters.add(Integer.parseInt(customerId));
            } catch (NumberFormatException e) {
                // Invalid Customer ID input; skip this filter
            }
        }

        if (!employeeId.isEmpty()) {
            query.append(" AND EmployeeID = ?");
            try {
                parameters.add(Integer.parseInt(employeeId));
            } catch (NumberFormatException e) {
                // Invalid Employee ID input; skip this filter
            }
        }

        if (!quantity.isEmpty()) {
            query.append(" AND Quantity = ?");
            try {
                parameters.add(Integer.parseInt(quantity));
            } catch (NumberFormatException e) {
                // Invalid Quantity input; skip this filter
            }
        }

        if (!totalPrice.isEmpty()) {
            query.append(" AND TotalPrice = ?");
            try {
                parameters.add(Double.parseDouble(totalPrice));
            } catch (NumberFormatException e) {
                // Invalid Total Price input; skip this filter
            }
        }

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query.toString())) {

            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            if (!hasData) {
                System.out.println("No orders found matching the criteria.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while searching orders.");
            e.printStackTrace();
        }

        return data;
    }
}
