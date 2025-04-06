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

public class SalesByEmployeeReportUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private TextField employeeIdField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sales by Employee");

        // Layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        VBox topSection = createTopSection(primaryStage);
        tableView.setPrefHeight(400);

        // Setup table columns
        setupTableView();

        // Add to root pane
        root.setTop(topSection);
        root.setCenter(tableView);

        // Load all data initially
        loadAllSalesData();

        // **Remove fixed size from Scene**
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Start maximized
        primaryStage.show();
    }

    /**
     * Creates the top section containing the title, search box, and back button.
     *
     * @param primaryStage The primary stage to handle navigation.
     * @return VBox containing the top section.
     */
    private VBox createTopSection(Stage primaryStage) {
        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(20));
        topSection.setAlignment(Pos.CENTER);

        Label title = new Label("Sales Completed by Employee");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #B22222; -fx-font-weight: bold;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        Label employeeIdLabel = new Label("Employee ID:");
        employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-weight: bold;");
        searchButton.setOnAction(e -> searchOrdersByEmployee());

        // Create Back Button
   

        searchBox.getChildren().addAll(employeeIdLabel, employeeIdField, searchButton);

        topSection.getChildren().addAll(title, searchBox);
        return topSection;
    }

    /**
     * Creates a styled "Back to Home" button.
     *
     * @param primaryStage The primary stage to handle navigation.
     * @return Styled Button.
     */
   

    private void setupTableView() {
        String[] columns = {"Order ID", "Employee ID", "Customer ID", "Order Date", "Total Price"};

        for (int i = 0; i < columns.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
            column.setPrefWidth(150);
            tableView.getColumns().add(column);
        }
    }

    private void loadAllSalesData() {
        String sql = "SELECT OrderID, EmployeeID, CustomerID, OrderDate, TotalPrice FROM orders";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            tableView.setItems(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load data: " + ex.getMessage());
        }
    }

    private void searchOrdersByEmployee() {
        String employeeIdText = employeeIdField.getText().trim();

        // Check if the Employee ID is empty
        if (employeeIdText.isEmpty()) {
            loadAllSalesData(); // Reload all data if no input
            return;
        }

        String sql = "SELECT OrderID, EmployeeID, CustomerID, OrderDate, TotalPrice " +
                     "FROM orders " +
                     "WHERE EmployeeID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(employeeIdText));

            ResultSet rs = stmt.executeQuery();
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            tableView.setItems(data);
            if (data.isEmpty()) {
                showAlert("Info", "No results found for Employee ID: " + employeeIdText);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to fetch data: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid Employee ID. Please enter a numeric value.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert;
        if (title.equalsIgnoreCase("Error")) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
