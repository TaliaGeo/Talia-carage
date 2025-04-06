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

public class ServiceFrequencyReportUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private ComboBox<String> carModelComboBox, customerComboBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Service Frequency Report");

        // Root layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        VBox topSection = createTopSection();
        tableView.setPrefHeight(500);
        setupTableView();

        root.setTop(topSection);
        root.setCenter(tableView);

        // Load initial data for all car models and customers
        loadCarModelFrequency();
        loadCustomerFrequency();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(15);
        topSection.setPadding(new Insets(20));
        topSection.setAlignment(Pos.CENTER);

        Label title = new Label("Service Frequency Report");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #B22222; -fx-font-weight: bold;");

        HBox controls = new HBox(20);
        controls.setAlignment(Pos.CENTER);

        Label carModelLabel = new Label("Select Car Model:");
        carModelComboBox = new ComboBox<>();
        carModelComboBox.setPromptText("Choose a Car Model");
        carModelComboBox.setOnAction(e -> loadCarModelFrequency());

        Label customerLabel = new Label("Select Customer Category:");
        customerComboBox = new ComboBox<>();
        customerComboBox.setPromptText("Choose a Customer");
        customerComboBox.setOnAction(e -> loadCustomerFrequency());

        Button resetButton = new Button("Reset");
        resetButton.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-weight: bold;");
        resetButton.setOnAction(e -> resetFilters());

        controls.getChildren().addAll(carModelLabel, carModelComboBox, customerLabel, customerComboBox, resetButton);

        // Add Back Button
        Button backButton = createBackButton();
        controls.getChildren().add(backButton);

        topSection.getChildren().addAll(title, controls);

        loadComboBoxData();

        return topSection;
    }

    private void setupTableView() {
        tableView.getColumns().clear();

        String[] columns = {"Category", "Service Count"};
        for (int i = 0; i < columns.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
            column.setPrefWidth(250);
            tableView.getColumns().add(column);
        }
    }

    private void loadCarModelFrequency() {
        String carModelFilter = carModelComboBox.getValue();
        String sql = "SELECT c.Model AS Category, COUNT(s.ServiceID) AS ServiceCount " +
                     "FROM services s " +
                     "JOIN cars c ON s.CarID = c.CarID ";

        if (carModelFilter != null && !carModelFilter.isEmpty()) {
            sql += "WHERE c.Model = ? ";
        }
        sql += "GROUP BY c.Model ORDER BY ServiceCount DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (carModelFilter != null && !carModelFilter.isEmpty()) {
                stmt.setString(1, carModelFilter);
            }

            populateTable(stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load car model frequency: " + ex.getMessage());
        }
    }

    private void loadCustomerFrequency() {
        String customerFilter = customerComboBox.getValue();
        String sql = "SELECT CONCAT(c.FirstName, ' ', c.LastName) AS Category, COUNT(s.ServiceID) AS ServiceCount " +
                     "FROM services s " +
                     "JOIN customers c ON s.CustomerID = c.CustomerID ";

        if (customerFilter != null && !customerFilter.isEmpty()) {
            sql += "WHERE CONCAT(c.FirstName, ' ', c.LastName) = ? ";
        }
        sql += "GROUP BY c.CustomerID ORDER BY ServiceCount DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (customerFilter != null && !customerFilter.isEmpty()) {
                stmt.setString(1, customerFilter);
            }

            populateTable(stmt);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load customer frequency: " + ex.getMessage());
        }
    }

    private void populateTable(PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        while (rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add(rs.getString("Category"));
            row.add(String.valueOf(rs.getInt("ServiceCount")));
            data.add(row);
        }

        tableView.setItems(data);

        if (data.isEmpty()) {
            showAlert("Info", "No data found for the selected filter.");
        }
    }

    private void loadComboBoxData() {
        try (Connection conn = DBUtil.getConnection()) {
            // Load car models
            String carModelSQL = "SELECT DISTINCT Model FROM cars";
            ResultSet rsCarModels = conn.createStatement().executeQuery(carModelSQL);
            while (rsCarModels.next()) {
                carModelComboBox.getItems().add(rsCarModels.getString("Model"));
            }

            // Load customer names
            String customerSQL = "SELECT DISTINCT CONCAT(FirstName, ' ', LastName) AS CustomerName FROM customers";
            ResultSet rsCustomers = conn.createStatement().executeQuery(customerSQL);
            while (rsCustomers.next()) {
                customerComboBox.getItems().add(rsCustomers.getString("CustomerName"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load ComboBox data: " + ex.getMessage());
        }
    }

    private void resetFilters() {
        carModelComboBox.setValue(null);
        customerComboBox.setValue(null);
        loadCarModelFrequency();
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
