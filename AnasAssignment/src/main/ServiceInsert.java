package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceInsert {
    public boolean insertService(int serviceID, int carID, String serviceDate, String serviceDescription, double cost) {
        String insertSQL = "INSERT INTO services (ServiceID, CarID, ServiceDate, ServiceDescription, Cost) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
            pstmt.setInt(1, serviceID);
            pstmt.setInt(2, carID);
            pstmt.setString(3, serviceDate);
            pstmt.setString(4, serviceDescription);
            pstmt.setDouble(5, cost);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
