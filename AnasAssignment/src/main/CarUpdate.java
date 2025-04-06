package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarUpdate {
    public boolean updateCarField(int carID, String field, String newValue) {
        String[] allowedFields = {"CarID","Make","Model","Year","Price","Stock","VIN"};
        boolean validField = false;
        for (String f : allowedFields) {
            if (f.equalsIgnoreCase(field)) {
                field = f; 
                validField = true;
                break;
            }
        }
        if (!validField) {
            return false; 
        }

        String updateSQL = "UPDATE cars SET " + field + " = ? WHERE CarID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(updateSQL)) {
            pstmt.setString(1, newValue);
            pstmt.setInt(2, carID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
