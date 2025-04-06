package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles insertion of customers into the database.
 */
public class CustomerInsert {

    /**
     * Inserts a new customer into the database.
     *
     * @param customerID Unique identifier for the customer.
     * @param firstName  First name of the customer.
     * @param lastName   Last name of the customer.
     * @param email      Email address of the customer.
     * @param phone      Phone number of the customer.
     * @param address    Address of the customer.
     * @param city       City of the customer.
     * @param zipCode    ZIP code of the customer.
     * @return true if insertion was successful; false otherwise.
     */
    public boolean insertCustomer(int customerID, String firstName, String lastName, String email,
                                  String phone, String address, String city, String zipCode) {
        String insertSQL = "INSERT INTO customers (CustomerID, FirstName, LastName, Email, Phone, Address, City, ZipCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(insertSQL)) {

            if (con == null) {
                System.err.println("Database connection failed.");
                return false;
            }

            // Setting parameters for the query
            pstmt.setInt(1, customerID);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setString(6, address);
            pstmt.setString(7, city);
            pstmt.setString(8, zipCode);

            // Execute query and check if rows are affected
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Customer Inserted. Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception occurred while inserting customer.");
            e.printStackTrace();
            return false;
        }
    }
}
