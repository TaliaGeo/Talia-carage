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

public class CustomerPaymentsReportUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private TextField customerIdField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Payments by Customer");

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

        // Load all payment data initially
        loadAllPaymentsData();

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
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

        Label title = new Label("Payments Made by Customer");
        title.setStyle("-fx-font-size: 20px; -fx-text-fill: #B22222; -fx-font-weight: bold;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        Label customerIdLabel = new Label("Customer ID:");
        customerIdField = new TextField();
        customerIdField.setPromptText("Enter Customer ID");

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-font-weight: bold;");
        searchButton.setOnAction(e -> searchPaymentsByCustomer());

        // Create Back Button
       
        searchBox.getChildren().addAll(customerIdLabel, customerIdField, searchButton);

        topSection.getChildren().addAll(title, searchBox);
        return topSection;
    }

    /**
     * Creates a styled "Back to Home" button.
     *
     * @param primaryStage The primary stage to handle navigation.
     * @return Styled Button.
     */
    private Button createBackButton(Stage primaryStage) {
        Button backButton = new Button("Back to Home");
        backButton.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        backButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-background-radius: 5;");
        backButton.setOnAction(e -> goBackToHome(primaryStage)); // Adjusted to use primaryStage
        return backButton;
    }

    /**
     * Navigates back to the Home Page.
     *
     * @param primaryStage The primary stage to switch scenes.
     */
    private void goBackToHome(Stage primaryStage) {
        HomePage homePage = new HomePage();
        try {
            homePage.start(primaryStage); // Reuse the same stage
            primaryStage.setMaximized(true); // Make sure the home page is maximized
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to navigate to Home Page.");
        }
    }

    private void setupTableView() {
        String[] columns = {"Payment ID", "Customer ID", "Order ID", "Payment Amount", "Payment Method"};

        for (int i = 0; i < columns.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(columns[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
            column.setPrefWidth(150);
            tableView.getColumns().add(column);
        }
    }

    private void loadAllPaymentsData() {
        // SQL Query: Join between customers, orders, and payments tables
        String sql = "SELECT p.PaymentID, c.CustomerID, o.OrderID, p.Amount, p.PaymentMethod " +
                     "FROM payments p " +
                     "JOIN orders o ON p.OrderID = o.OrderID " +
                     "JOIN customers c ON o.CustomerID = c.CustomerID";

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

    private void searchPaymentsByCustomer() {
        String customerIdText = customerIdField.getText().trim();

        // If no input, reload all data
        if (customerIdText.isEmpty()) {
            loadAllPaymentsData();
            return;
        }

        // SQL Query with JOIN and filter by CustomerID
        String sql = "SELECT p.PaymentID, c.CustomerID, o.OrderID, p.Amount, p.PaymentMethod " +
                     "FROM payments p " +
                     "JOIN orders o ON p.OrderID = o.OrderID " +
                     "JOIN customers c ON o.CustomerID = c.CustomerID " +
                     "WHERE c.CustomerID = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(customerIdText));

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
                showAlert("Info", "No payments found for Customer ID: " + customerIdText);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to fetch data: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid Customer ID. Please enter a numeric value.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
