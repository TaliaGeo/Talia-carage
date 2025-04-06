package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerDelete {
    public boolean deleteCustomer(int customerID) {
        String deleteSQL = "DELETE FROM customers WHERE CustomerID = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, customerID);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
