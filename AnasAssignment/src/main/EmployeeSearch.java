package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class EmployeeSearch {
    public ObservableList<ObservableList<String>> searchEmployees(
            String employeeID, String firstName, String lastName, String position, String salary, String hireDate) {

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        StringBuilder query = new StringBuilder("SELECT * FROM employees WHERE 1=1");
        int paramIndex = 1;

        // Using PreparedStatement to prevent SQL injection
        // Building dynamic query with parameters
        try (Connection con = DBUtil.getConnection()) {
            StringBuilder sql = new StringBuilder("SELECT * FROM employees WHERE 1=1");
            if (!employeeID.isEmpty()) {
                try {
                    Integer.parseInt(employeeID);
                    sql.append(" AND EmployeeID = ?");
                } catch (NumberFormatException ignored) {}
            }
            if (!firstName.isEmpty()) {
                sql.append(" AND FirstName LIKE ?");
            }
            if (!lastName.isEmpty()) {
                sql.append(" AND LastName LIKE ?");
            }
            if (!position.isEmpty()) {
                sql.append(" AND Position LIKE ?");
            }
            if (!salary.isEmpty()) {
                try {
                    Double.parseDouble(salary);
                    sql.append(" AND Salary = ?");
                } catch (NumberFormatException ignored) {}
            }
            if (!hireDate.isEmpty()) {
                sql.append(" AND HireDate LIKE ?");
            }

            try (PreparedStatement pstmt = con.prepareStatement(sql.toString())) {
                if (!employeeID.isEmpty()) {
                    try {
                        pstmt.setInt(paramIndex++, Integer.parseInt(employeeID));
                    } catch (NumberFormatException ignored) {}
                }
                if (!firstName.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + firstName.trim() + "%");
                }
                if (!lastName.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + lastName.trim() + "%");
                }
                if (!position.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + position.trim() + "%");
                }
                if (!salary.isEmpty()) {
                    try {
                        pstmt.setDouble(paramIndex++, Double.parseDouble(salary));
                    } catch (NumberFormatException ignored) {}
                }
                if (!hireDate.isEmpty()) {
                    pstmt.setString(paramIndex++, "%" + hireDate.trim() + "%");
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    int columnCount = rs.getMetaData().getColumnCount();
                    while (rs.next()) {
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int i = 1; i <= columnCount; i++) {
                            row.add(rs.getString(i));
                        }
                        data.add(row);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }
}
