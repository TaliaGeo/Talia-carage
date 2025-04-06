package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Handles searching for payments in the database based on various criteria.
 */
public class PaymentSearch {

    /**
     * Searches for payments based on provided parameters.
     *
     * @param paymentID     ID of the payment.
     * @param orderID       ID of the associated order.
     * @param paymentDate   Date of the payment.
     * @param paymentMethod Method of payment.
     * @param amount        Amount of the payment.
     * @return ObservableList containing the search results.
     */
    public ObservableList<ObservableList<String>> searchPayments(
            String paymentID, String orderID, String paymentDate, String paymentMethod, String amount) {

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        StringBuilder query = new StringBuilder("SELECT PaymentID, OrderID, PaymentDate, PaymentMethod, Amount FROM payments WHERE 1=1");
        ObservableList<Object> parameters = FXCollections.observableArrayList();

        if (!paymentID.isEmpty()) {
            query.append(" AND PaymentID = ?");
            try {
                parameters.add(Integer.parseInt(paymentID));
            } catch (NumberFormatException e) {
                // Invalid Payment ID input; skip this filter
            }
        }

        if (!orderID.isEmpty()) {
            query.append(" AND OrderID = ?");
            try {
                parameters.add(Integer.parseInt(orderID));
            } catch (NumberFormatException e) {
                // Invalid Order ID input; skip this filter
            }
        }

        if (!paymentDate.isEmpty()) {
            query.append(" AND PaymentDate LIKE ?");
            parameters.add("%" + paymentDate + "%");
        }

        if (!paymentMethod.isEmpty()) {
            query.append(" AND PaymentMethod LIKE ?");
            parameters.add("%" + paymentMethod + "%");
        }

        if (!amount.isEmpty()) {
            query.append(" AND Amount = ?");
            try {
                parameters.add(Double.parseDouble(amount));
            } catch (NumberFormatException e) {
                // Invalid Amount input; skip this filter
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
                System.out.println("No payments found matching the criteria.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while searching payments.");
            e.printStackTrace();
        }

        return data;
    }
}
