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

public class ServiceHistoryReportUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private TextField carIdField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Detailed History of Service Costs for Specific Vehicles");

        // Root layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        VBox topSection = createTopSection();
        tableView.setPrefHeight(500);
        setupTableView();

        root.setTop(topSection);
        root.setCenter(tableView);

        // Load default data with all car service histories
        loadServiceHistoryData(null);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(20));
        topSection.setAlignment(Pos.CENTER);

        Label title = new Label("Service History Report");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #B22222; -fx-font-weight: bold;");

        HBox inputSection = new HBox(10);
        inputSection.setAlignment(Pos.CENTER);

        Label carIdLabel = new Label("Enter Car ID:");
        carIdField = new TextField();
        carIdField.setPromptText("Car ID");
        carIdField.setMaxWidth(150);

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-weight: bold;");
        searchButton.setOnAction(e -> {
            String carId = carIdField.getText().trim();
            if (!carId.isEmpty()) {
                loadServiceHistoryData(carId);
            } else {
                showAlert("Error", "Please enter a valid Car ID.");
            }
        });

        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-weight: bold;");
        resetButton.setOnAction(e -> {
            carIdField.clear();
            loadServiceHistoryData(null);
        });

        inputSection.getChildren().addAll(carIdLabel, carIdField, searchButton, resetButton);

        // Add Back Button
        Button backButton = createBackButton();
        inputSection.getChildren().add(backButton);

        topSection.getChildren().addAll(title, inputSection);

        return topSection;
    }

    private void setupTableView() {
        tableView.getColumns().clear();

        String[] columnNames = {"Service Date", "Service Description", "Cost"};
        for (int i = 0; i < columnNames.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
            column.setPrefWidth(250);
            tableView.getColumns().add(column);
        }
    }

    private void loadServiceHistoryData(String carId) {
        String sql = "SELECT s.ServiceDate, s.ServiceDescription, s.Cost " +
                     "FROM services s " +
                     "JOIN cars c ON s.CarID = c.CarID ";

        if (carId != null && !carId.isEmpty()) {
            sql += "WHERE c.CarID = ?";
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (carId != null && !carId.isEmpty()) {
                stmt.setInt(1, Integer.parseInt(carId));
            }

            populateTable(stmt);

        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load service history: " + ex.getMessage());
        }
    }

    private void populateTable(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(rs.getString("ServiceDate"));
            row.add(rs.getString("ServiceDescription"));
            row.add(String.format("%.2f", rs.getDouble("Cost")));
            data.add(row);
        }

        tableView.setItems(data);

        if (data.isEmpty()) {
            showAlert("Info", "No service history found for the given Car ID.");
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
