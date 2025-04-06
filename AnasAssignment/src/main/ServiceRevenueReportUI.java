package main;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceRevenueReportUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private ComboBox<String> monthComboBox;
    private ComboBox<String> quarterComboBox;  // New ComboBox for Quarters

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Service Revenue Report");

        // Layout setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        VBox topSection = createTopSection();
        tableView.setPrefHeight(500);
        setupTableView();

        root.setTop(topSection);
        root.setCenter(tableView);

        // Load January data by default
        loadRevenueData("01");

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(20));
        topSection.setAlignment(Pos.CENTER);

        Label title = new Label("Service Revenue Report");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #B22222; -fx-font-weight: bold;");

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        // Month ComboBox
        Label monthLabel = new Label("Select Month:");
        monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        monthComboBox.setValue("January"); // Default to January

        // Quarter ComboBox for Quarterly Report
        Label quarterLabel = new Label("Select Quarter:");
        quarterComboBox = new ComboBox<>();
        quarterComboBox.getItems().addAll("Q1 (Jan-Mar)", "Q2 (Apr-Jun)", "Q3 (Jul-Sep)", "Q4 (Oct-Dec)");
        quarterComboBox.setValue("Q1 (Jan-Mar)"); // Default to Q1

        Button searchButton = new Button("Generate Report");
        searchButton.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-weight: bold;");
        searchButton.setOnAction(e -> generateReport());

        controls.getChildren().addAll(monthLabel, monthComboBox, quarterLabel, quarterComboBox, searchButton);

        // Add Back Button
        Button backButton = createBackButton();
        controls.getChildren().add(backButton);

        topSection.getChildren().addAll(title, controls);

        return topSection;
    }

    private void setupTableView() {
        String[] columns = {"Period", "Service Description", "Total Revenue"};

        for (int i = 0; i < columns.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
            column.setPrefWidth(250);
            tableView.getColumns().add(column);
        }
    }

    private void generateReport() {
        String selectedMonth = getMonthNumber(monthComboBox.getValue());
        String selectedQuarter = quarterComboBox.getValue();

        // Check if quarter is selected, then load quarterly data
        if (selectedQuarter != null && !selectedQuarter.isEmpty()) {
            loadQuarterlyRevenueData(selectedQuarter);
        } else {
            loadRevenueData(selectedMonth);
        }
    }

    private void loadRevenueData(String month) {
        String sql = "SELECT DATE_FORMAT(ServiceDate, '%Y-%m') AS Month, " +
                     "ServiceDescription, SUM(Cost) AS TotalRevenue " +
                     "FROM services " +
                     "WHERE MONTH(ServiceDate) = ? " +
                     "GROUP BY Month, ServiceDescription " +
                     "ORDER BY ServiceDescription";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, month); // Set the selected month as parameter
            ResultSet rs = stmt.executeQuery();

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("Month"));
                row.add(rs.getString("ServiceDescription"));
                row.add(String.format("%.2f", rs.getDouble("TotalRevenue")));
                data.add(row);
            }

            tableView.setItems(data);

            if (data.isEmpty()) {
                showAlert("Info", "No revenue data found for the selected month.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load revenue data: " + ex.getMessage());
        }
    }

    private void loadQuarterlyRevenueData(String quarter) {
        String quarterSQL = "SELECT CASE " +
                            "WHEN MONTH(ServiceDate) IN (1,2,3) THEN 'Q1 (Jan-Mar)' " +
                            "WHEN MONTH(ServiceDate) IN (4,5,6) THEN 'Q2 (Apr-Jun)' " +
                            "WHEN MONTH(ServiceDate) IN (7,8,9) THEN 'Q3 (Jul-Sep)' " +
                            "WHEN MONTH(ServiceDate) IN (10,11,12) THEN 'Q4 (Oct-Dec)' END AS Quarter, " +
                            "ServiceDescription, SUM(Cost) AS TotalRevenue " +
                            "FROM services " +
                            "WHERE CASE " +
                            "WHEN MONTH(ServiceDate) IN (1,2,3) THEN 'Q1 (Jan-Mar)' " +
                            "WHEN MONTH(ServiceDate) IN (4,5,6) THEN 'Q2 (Apr-Jun)' " +
                            "WHEN MONTH(ServiceDate) IN (7,8,9) THEN 'Q3 (Jul-Sep)' " +
                            "WHEN MONTH(ServiceDate) IN (10,11,12) THEN 'Q4 (Oct-Dec)' END = ? " +
                            "GROUP BY Quarter, ServiceDescription " +
                            "ORDER BY ServiceDescription";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(quarterSQL)) {

            stmt.setString(1, quarter); // Set the selected quarter as parameter
            ResultSet rs = stmt.executeQuery();

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("Quarter"));
                row.add(rs.getString("ServiceDescription"));
                row.add(String.format("%.2f", rs.getDouble("TotalRevenue")));
                data.add(row);
            }

            tableView.setItems(data);

            if (data.isEmpty()) {
                showAlert("Info", "No revenue data found for the selected quarter.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load revenue data: " + ex.getMessage());
        }
    }

    private String getMonthNumber(String monthName) {
        switch (monthName) {
            case "January": return "01";
            case "February": return "02";
            case "March": return "03";
            case "April": return "04";
            case "May": return "05";
            case "June": return "06";
            case "July": return "07";
            case "August": return "08";
            case "September": return "09";
            case "October": return "10";
            case "November": return "11";
            case "December": return "12";
            default: return "01"; // Default to January
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Creates a styled "Back" button.
     *
     * @return Styled Button.
     */
    private Button createBackButton() {
        Button backButton = new Button("Back to Home");
        backButton.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        backButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-background-radius: 5;");
        backButton.setOnAction(e -> goBackToHome());
        return backButton;
    }

    /**
     * Navigates back to the Home Page.
     */
    private void goBackToHome() {
        HomePage home = new HomePage();
        try {
            home.start(new Stage());
            // Close the current window
            ((Stage) tableView.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Home Page: " + e.getMessage());
        }
    }
}
