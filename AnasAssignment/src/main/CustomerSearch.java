package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Handles searching for customers in the database based on various criteria.
 */
public class CustomerSearch {

    /**
     * Searches for customers based on provided criteria.
     * If a criterion is empty, it is ignored in the search.
     *
     * @param serviceID Placeholder parameter for consistency across modules (unused).
     * @param field     The field to search by (e.g., "CustomerID", "FirstName").
     * @param searchValue The value to search for.
     * @param unused1    Placeholder for future use.
     * @param unused2    Placeholder for future use.
     * @param unused3    Placeholder for future use.
     * @param unused4    Placeholder for future use.
     * @param unused5    Placeholder for future use.
     * @return An ObservableList of ObservableList containing the search results.
     */
    public ObservableList<ObservableList<String>> searchCustomers(
            String serviceID, String field, String searchValue,
            String unused1, String unused2, String unused3, String unused4, String unused5
    ) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        // If no field is specified, return all customers
        if (field == null || field.isEmpty()) {
            field = "";
            searchValue = "";
        }

        StringBuilder query = new StringBuilder("SELECT * FROM customers WHERE 1=1");
        ObservableList<Object> parameters = FXCollections.observableArrayList();

        if (!field.isEmpty() && !searchValue.isEmpty()) {
            // Whitelist fields to prevent SQL injection
            String sanitizedField = sanitizeField(field);
            if (sanitizedField != null) {
                if (sanitizedField.equalsIgnoreCase("CustomerID")) {
                    query.append(" AND ").append(sanitizedField).append(" = ?");
                    try {
                        parameters.add(Integer.parseInt(searchValue));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid CustomerID format: " + searchValue);
                        return data; // Return empty list if CustomerID is invalid
                    }
                } else {
                    query.append(" AND ").append(sanitizedField).append(" LIKE ?");
                    parameters.add("%" + searchValue + "%");
                }
            }
        }

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query.toString())) {

            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

        } catch (SQLException e) {
            System.err.println("Error searching customers:");
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Sanitizes the field name to prevent SQL injection.
     *
     * @param field The field name to sanitize.
     * @return The sanitized field name if valid; null otherwise.
     */
    private String sanitizeField(String field) {
        String[] validFields = {"CustomerID", "FirstName", "LastName", "Email", "Phone", "Address", "City", "ZipCode"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return validField; // Return the correctly cased field name
            }
        }
        System.err.println("Invalid search field: " + field);
        return null;
    }
}
