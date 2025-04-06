package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class CarSearch {
    public ObservableList<ObservableList<String>> searchCars(String carID, String make, String model,
                                                             String year, String price, String stock, String vin) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        StringBuilder query = new StringBuilder("SELECT CarID, Make, Model, Year, Price, Stock, VIN FROM cars WHERE 1=1");

        // Numeric fields (exact match if parseable)
        if (!carID.isEmpty()) {
            try {
                Integer.parseInt(carID);
                query.append(" AND CarID = ").append(carID);
            } catch (NumberFormatException ignored) {}
        }

        if (!year.isEmpty()) {
            try {
                Integer.parseInt(year);
                query.append(" AND Year = ").append(year);
            } catch (NumberFormatException ignored) {}
        }

        if (!price.isEmpty()) {
            try {
                Double.parseDouble(price);
                query.append(" AND Price = ").append(price);
            } catch (NumberFormatException ignored) {}
        }

        if (!stock.isEmpty()) {
            try {
                Integer.parseInt(stock);
                query.append(" AND Stock = ").append(stock);
            } catch (NumberFormatException ignored) {}
        }

        // Text fields: LIKE search
        if (!make.isEmpty()) {
            query.append(" AND Make LIKE '%").append(make).append("%'");
        }
        if (!model.isEmpty()) {
            query.append(" AND Model LIKE '%").append(model).append("%'");
        }
        if (!vin.isEmpty()) {
            query.append(" AND VIN LIKE '%").append(vin).append("%'");
        }

        try (Connection con = DBUtil.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {

            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i=1; i<=columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
