package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

/**
 * Handles searching for services in the database based on various criteria.
 */
public class ServiceSearch {

    /**
     * Searches for services based on provided parameters.
     *
     * @param serviceID         ID of the service.
     * @param carID             ID of the car.
     * @param serviceDate       Date of the service.
     * @param serviceDescription Description of the service.
     * @param cost              Cost of the service.
     * @return ObservableList containing the search results.
     */
    public ObservableList<ObservableList<String>> searchServices(
            String serviceID, String carID, String serviceDate, String serviceDescription, String cost) {

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        StringBuilder query = new StringBuilder("SELECT ServiceID, CarID, ServiceDate, ServiceDescription, Cost FROM services WHERE 1=1");
        ObservableList<Object> parameters = FXCollections.observableArrayList();

        if (!serviceID.isEmpty()) {
            query.append(" AND ServiceID = ?");
            try {
                parameters.add(Integer.parseInt(serviceID));
            } catch (NumberFormatException e) {
                // Invalid Service ID input; skip this filter
                System.err.println("Invalid Service ID format: " + serviceID);
            }
        }

        if (!carID.isEmpty()) {
            query.append(" AND CarID = ?");
            try {
                parameters.add(Integer.parseInt(carID));
            } catch (NumberFormatException e) {
                // Invalid Car ID input; skip this filter
                System.err.println("Invalid Car ID format: " + carID);
            }
        }

        if (!serviceDate.isEmpty()) {
            query.append(" AND ServiceDate LIKE ?");
            parameters.add("%" + serviceDate + "%");
        }

        if (!serviceDescription.isEmpty()) {
            query.append(" AND ServiceDescription LIKE ?");
            parameters.add("%" + serviceDescription + "%");
        }

        if (!cost.isEmpty()) {
            query.append(" AND Cost = ?");
            try {
                parameters.add(Double.parseDouble(cost));
            } catch (NumberFormatException e) {
                // Invalid Cost input; skip this filter
                System.err.println("Invalid Cost format: " + cost);
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
                System.out.println("No services found matching the criteria.");
            }

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while searching services.");
            e.printStackTrace();
        }

        return data;
    }
}
