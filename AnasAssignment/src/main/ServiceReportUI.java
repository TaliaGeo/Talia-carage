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

public class ServiceReportUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private TextField carIdField, customerIdField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Service Reports");

        // Layout setup
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        VBox topSection = createTopSection();
        tableView.setPrefHeight(500);
        setupTableView();

        root.setTop(topSection);
        root.setCenter(tableView);

        // Scene setup
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        // Load all records immediately when the UI is opened
        loadAllServices();
    }

    /**
     * Creates the top section containing the title, search fields, and buttons.
     *
     * @return VBox containing the top section.
     */
    private VBox createTopSection() {
        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(20));
        topSection.setAlignment(Pos.CENTER);

        // Title
        Label title = new Label("Service Report");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #B22222; -fx-font-weight: bold;");

        // Search Fields and Button
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);

        Label carIdLabel = new Label("Car ID:");
        carIdField = new TextField();
        carIdField.setPromptText("Enter Car ID");

        Label customerIdLabel = new Label("Customer ID:");
        customerIdField = new TextField();
        customerIdField.setPromptText("Enter Customer ID");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-weight: bold;");
        searchButton.setOnAction(e -> searchServices());

        controls.getChildren().addAll(carIdLabel, carIdField, customerIdLabel, customerIdField, searchButton);

        // Add Back Button
        Button backButton = createBackButton();
        controls.getChildren().add(backButton);

        // Add title and controls to the top section
        topSection.getChildren().addAll(title, controls);

        return topSection;
    }

    private void setupTableView() {
        String[] columns = {"Service ID", "Car ID", "Car Make", "Car Model", "Service Date", "Description", "Cost"};

        for (int i = 0; i < columns.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
            tableView.getColumns().add(column);
        }
    }

    private void searchServices() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        String sql = "SELECT s.ServiceID, s.CarID, c.Make, c.Model, s.ServiceDate, s.ServiceDescription, s.Cost " +
                     "FROM services s " +
                     "JOIN cars c ON s.CarID = c.CarID " +
                     "JOIN customers cust ON s.CustomerID = cust.CustomerID " +
                     "WHERE (? IS NULL OR s.CarID = ?) AND (? IS NULL OR s.CustomerID = ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String carId = carIdField.getText().trim();
            String customerId = customerIdField.getText().trim();

            stmt.setString(1, carId.isEmpty() ? null : carId);
            stmt.setString(2, carId.isEmpty() ? null : carId);
            stmt.setString(3, customerId.isEmpty() ? null : customerId);
            stmt.setString(4, customerId.isEmpty() ? null : customerId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            // Update TableView with the fetched data
            tableView.setItems(data);

            if (data.isEmpty()) {
                showAlert("Info", "No services found for the given search criteria.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to search services: " + ex.getMessage());
        }
    }

    // New method to load all services when the UI is opened
    private void loadAllServices() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        String sql = "SELECT s.ServiceID, s.CarID, c.Make, c.Model, s.ServiceDate, s.ServiceDescription, s.Cost " +
                     "FROM services s " +
                     "JOIN cars c ON s.CarID = c.CarID " +
                     "JOIN customers cust ON s.CustomerID = cust.CustomerID";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }

            // Update TableView with the fetched data
            tableView.setItems(data);

            if (data.isEmpty()) {
                showAlert("Info", "No services found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load services: " + ex.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
