package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarInsert {
    public boolean insertCar(int CarID, String Make, String Model, int Year, double Price, int Stock, String VIN) {
        String insertSQL = "INSERT INTO cars (CarID, Make, Model, Year, Price, Stock, VIN) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
            pstmt.setInt(1, CarID);
            pstmt.setString(2, Make);
            pstmt.setString(3, Model);
            pstmt.setInt(4, Year);
            pstmt.setDouble(5, Price);
            pstmt.setInt(6, Stock);
            pstmt.setString(7, VIN);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
