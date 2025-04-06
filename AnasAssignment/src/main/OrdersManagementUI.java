package main;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * User Interface for managing orders.
 */
public class OrdersManagementUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private VBox mainContainer;
    private VBox actionContainer = new VBox(10);
    private Label messageLabel = new Label();

    // Add Order Fields
    private TextField orderIDField, customerIDField, employeeIDField, carIDField, quantityField, totalPriceField, orderDateField;
    private Label orderIDError, customerIDError, employeeIDError, carIDError, quantityError, totalPriceError, orderDateError;

    // Find Order Fields
    private TextField findOrderIDField, findCarIDField, findCustomerIDField, findEmployeeIDField, findQuantityField, findTotalPriceField;

    // Update Order Fields
    private TextField updateOrderIDField, updateFieldField, updateValueField;
    private Label updateOrderIDError, updateFieldError, updateValueError;

    private OrderInsert orderInsert = new OrderInsert();
    private OrderSearch orderSearch = new OrderSearch();
    private OrderUpdate orderUpdate = new OrderUpdate();

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Order Management");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Sidebar with buttons
        VBox sidebar = createSidebar();

        // Main container
        mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label title = new Label("Order Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #B22222;");

        tableView.setPrefHeight(400);
        setupTableColumns();
        loadData(orderSearch.searchOrders("", "", "", "", "", ""));

        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        mainContainer.getChildren().addAll(title, tableView, actionContainer, messageLabel);

        root.setLeft(sidebar);
        root.setCenter(mainContainer);

        Scene scene = new Scene(root);
        stage.setMaximized(true); // Full-screen mode
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates the sidebar with navigation buttons.
     *
     * @return VBox containing the sidebar.
     */
    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #333333;");
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);

        Button addButton = createNavButton("Add Order");
        addButton.setOnAction(e -> showAddOrderFields());

        Button findButton = createNavButton("Find Order");
        findButton.setOnAction(e -> showFindOrderFields());

        Button updateButton = createNavButton("Update Order");
        updateButton.setOnAction(e -> showUpdateOrderFields());

        Button backButton = createNavButton("Back");
        backButton.setOnAction(e -> goBackToHome());

        sidebar.getChildren().addAll(addButton, findButton, updateButton, backButton);
        return sidebar;
    }

    /**
     * Creates a styled navigation button.
     *
     * @param text Text to display on the button.
     * @return Styled Button.
     */
    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setPrefWidth(250);
        button.setPrefHeight(50);
        return button;
    }

    /**
     * Displays the fields required to add a new order.
     */
    private void showAddOrderFields() {
        clearActionContainer();

        // Initialize fields and error labels
        orderIDField = createTextField("Order ID");
        orderIDError = createErrorLabel();
        customerIDField = createTextField("Customer ID");
        customerIDError = createErrorLabel();
        employeeIDField = createTextField("Employee ID");
        employeeIDError = createErrorLabel();
        carIDField = createTextField("Car ID");
        carIDError = createErrorLabel();
        quantityField = createTextField("Quantity");
        quantityError = createErrorLabel();
        totalPriceField = createTextField("Total Price");
        totalPriceError = createErrorLabel();
        orderDateField = createTextField("Order Date (YYYY-MM-DD)");
        orderDateError = createErrorLabel();

        Button saveButton = createActionButton("Save");
        saveButton.setOnAction(e -> insertOrder());

        VBox fieldsBox = new VBox(5,
                new Label("Order ID:"), orderIDField, orderIDError,
                new Label("Customer ID:"), customerIDField, customerIDError,
                new Label("Employee ID:"), employeeIDField, employeeIDError,
                new Label("Car ID:"), carIDField, carIDError,
                new Label("Quantity:"), quantityField, quantityError,
                new Label("Total Price:"), totalPriceField, totalPriceError,
                new Label("Order Date:"), orderDateField, orderDateError,
                saveButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to find/search orders.
     */
    private void showFindOrderFields() {
        clearActionContainer();

        findOrderIDField = createTextField("Order ID");
        findCarIDField = createTextField("Car ID");
        findCustomerIDField = createTextField("Customer ID");
        findEmployeeIDField = createTextField("Employee ID");
        findQuantityField = createTextField("Quantity");
        findTotalPriceField = createTextField("Total Price");

        Button searchButton = createActionButton("Search");
        searchButton.setOnAction(e -> searchOrders());

        VBox fieldsBox = new VBox(10,
                new Label("Search Orders"),
                new Label("Order ID:"), findOrderIDField,
                new Label("Car ID:"), findCarIDField,
                new Label("Customer ID:"), findCustomerIDField,
                new Label("Employee ID:"), findEmployeeIDField,
                new Label("Quantity:"), findQuantityField,
                new Label("Total Price:"), findTotalPriceField,
                searchButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to update an existing order.
     */
    private void showUpdateOrderFields() {
        clearActionContainer();

        updateOrderIDField = createTextField("Order ID");
        updateOrderIDError = createErrorLabel();
        updateFieldField = createTextField("Field to Update (e.g., Quantity)");
        updateFieldError = createErrorLabel();
        updateValueField = createTextField("New Value");
        updateValueError = createErrorLabel();

        Button updateButton = createActionButton("Update");
        updateButton.setOnAction(e -> updateOrderField());

        VBox fieldsBox = new VBox(5,
                new Label("Order ID:"), updateOrderIDField, updateOrderIDError,
                new Label("Field to Update:"), updateFieldField, updateFieldError,
                new Label("New Value:"), updateValueField, updateValueError,
                updateButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Inserts a new order after validating inputs.
     */
    private void insertOrder() {
        clearErrors();
        boolean isValid = true;

        // Validate Order ID
        String ordIDText = orderIDField.getText().trim();
        int ordID = 0;
        if (ordIDText.isEmpty()) {
            orderIDError.setText("Order ID is required.");
            isValid = false;
        } else {
            try {
                ordID = Integer.parseInt(ordIDText);
                if (ordID <= 0) {
                    orderIDError.setText("Order ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                orderIDError.setText("Invalid Order ID.");
                isValid = false;
            }
        }

        // Validate Customer ID
        String custIDText = customerIDField.getText().trim();
        int custID = 0;
        if (custIDText.isEmpty()) {
            customerIDError.setText("Customer ID is required.");
            isValid = false;
        } else {
            try {
                custID = Integer.parseInt(custIDText);
                if (custID <= 0) {
                    customerIDError.setText("Customer ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                customerIDError.setText("Invalid Customer ID.");
                isValid = false;
            }
        }

        // Validate Employee ID
        String empIDText = employeeIDField.getText().trim();
        int empID = 0;
        if (empIDText.isEmpty()) {
            employeeIDError.setText("Employee ID is required.");
            isValid = false;
        } else {
            try {
                empID = Integer.parseInt(empIDText);
                if (empID <= 0) {
                    employeeIDError.setText("Employee ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                employeeIDError.setText("Invalid Employee ID.");
                isValid = false;
            }
        }

        // Validate Car ID
        String carIDText = carIDField.getText().trim();
        int carID = 0;
        if (carIDText.isEmpty()) {
            carIDError.setText("Car ID is required.");
            isValid = false;
        } else {
            try {
                carID = Integer.parseInt(carIDText);
                if (carID <= 0) {
                    carIDError.setText("Car ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                carIDError.setText("Invalid Car ID.");
                isValid = false;
            }
        }

        // Validate Quantity
        String qtyText = quantityField.getText().trim();
        int qty = 0;
        if (qtyText.isEmpty()) {
            quantityError.setText("Quantity is required.");
            isValid = false;
        } else {
            try {
                qty = Integer.parseInt(qtyText);
                if (qty <= 0) {
                    quantityError.setText("Quantity must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                quantityError.setText("Invalid Quantity.");
                isValid = false;
            }
        }

        // Validate Total Price
        String priceText = totalPriceField.getText().trim();
        double price = 0.0;
        if (priceText.isEmpty()) {
            totalPriceError.setText("Total Price is required.");
            isValid = false;
        } else {
            try {
                price = Double.parseDouble(priceText);
                if (price < 0) {
                    totalPriceError.setText("Total Price cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                totalPriceError.setText("Invalid Total Price.");
                isValid = false;
            }
        }

        // Validate Order Date
        String date = orderDateField.getText().trim();
        if (date.isEmpty()) {
            orderDateError.setText("Order Date is required.");
            isValid = false;
        } else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            orderDateError.setText("Order Date must be in YYYY-MM-DD format.");
            isValid = false;
        }

        if (isValid) {
            try {
                boolean success = orderInsert.insertOrder(ordID, custID, empID, carID, qty, price, date);
                if (success) {
                    messageLabel.setText("Order added successfully.");
                    loadData(orderSearch.searchOrders("", "", "", "", "", ""));
                    clearAddOrderFields();
                } else {
                    messageLabel.setText("Error adding order. Please check the details.");
                }
            } catch (Exception e) {
                messageLabel.setText("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    /**
     * Searches for orders based on input criteria.
     */
    private void searchOrders() {
        String ordID = findOrderIDField.getText().trim();
        String carID = findCarIDField.getText().trim();
        String custID = findCustomerIDField.getText().trim();
        String empID = findEmployeeIDField.getText().trim();
        String qty = findQuantityField.getText().trim();
        String price = findTotalPriceField.getText().trim();

        ObservableList<ObservableList<String>> results = orderSearch.searchOrders(ordID, carID, custID, empID, qty, price);
        loadData(results);
        messageLabel.setText(results.isEmpty() ? "No orders found." : "Search results displayed.");
    }

    /**
     * Updates a specific field of an existing order after validation.
     */
    private void updateOrderField() {
        clearErrors();
        boolean isValid = true;

        // Validate Order ID
        String ordIDText = updateOrderIDField.getText().trim();
        int ordID = 0;
        if (ordIDText.isEmpty()) {
            updateOrderIDError.setText("Order ID is required.");
            isValid = false;
        } else {
            try {
                ordID = Integer.parseInt(ordIDText);
                if (ordID <= 0) {
                    updateOrderIDError.setText("Order ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                updateOrderIDError.setText("Invalid Order ID.");
                isValid = false;
            }
        }

        // Validate Field to Update
        String field = updateFieldField.getText().trim();
        if (field.isEmpty()) {
            updateFieldError.setText("Field to update is required.");
            isValid = false;
        } else if (!isValidField(field)) {
            updateFieldError.setText("Invalid field name.");
            isValid = false;
        }

        // Validate New Value
        String value = updateValueField.getText().trim();
        if (value.isEmpty()) {
            updateValueError.setText("New value is required.");
            isValid = false;
        }

        if (isValid) {
            try {
                boolean success = orderUpdate.updateOrderField(ordID, field, value);
                if (success) {
                    messageLabel.setText("Order updated successfully.");
                    loadData(orderSearch.searchOrders("", "", "", "", "", ""));
                    clearUpdateOrderFields();
                } else {
                    messageLabel.setText("Error updating order. Please check the details.");
                }
            } catch (Exception e) {
                messageLabel.setText("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    /**
     * Navigates back to the home page.
     */
    private void goBackToHome() {
        // Implement navigation to Home Page if applicable
        // new HomePage().start(new Stage());
        primaryStage.close();
    }

    /**
     * Loads data into the TableView.
     *
     * @param data Data to load.
     */
    private void loadData(ObservableList<ObservableList<String>> data) {
        tableView.setItems(data);
    }

    /**
     * Sets up the columns for the TableView.
     */
    private void setupTableColumns() {
        tableView.getColumns().clear(); // Clear existing columns if any
        String[] columns = {"OrderID", "CustomerID", "EmployeeID", "CarID", "Quantity", "TotalPrice", "OrderDate"};
        for (int i = 0; i < columns.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(columns[i]);
            col.setCellValueFactory(param -> {
                if (param.getValue().size() > colIndex) {
                    return new SimpleStringProperty(param.getValue().get(colIndex));
                } else {
                    return new SimpleStringProperty("");
                }
            });
            col.setPrefWidth(100); // Optional: set preferred width
            tableView.getColumns().add(col);
        }
    }

    /**
     * Creates a styled TextField with a prompt.
     *
     * @param prompt Prompt text.
     * @return Styled TextField.
     */
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px;");
        field.setMaxWidth(300);
        return field;
    }

    /**
     * Creates a styled action button.
     *
     * @param text Text to display on the button.
     * @return Styled Button.
     */
    private Button createActionButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setPrefWidth(150);
        button.setPrefHeight(35);
        return button;
    }

    /**
     * Creates a styled error label.
     *
     * @return Styled Label.
     */
    private Label createErrorLabel() {
        Label label = new Label();
        label.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        return label;
    }

    /**
     * Clears the action container and resets the message label.
     */
    private void clearActionContainer() {
        actionContainer.getChildren().clear();
        messageLabel.setText("");
    }

    /**
     * Clears all error labels.
     */
    private void clearErrors() {
        // Clear Add Order Errors
        if (orderIDError != null) orderIDError.setText("");
        if (customerIDError != null) customerIDError.setText("");
        if (employeeIDError != null) employeeIDError.setText("");
        if (carIDError != null) carIDError.setText("");
        if (quantityError != null) quantityError.setText("");
        if (totalPriceError != null) totalPriceError.setText("");
        if (orderDateError != null) orderDateError.setText("");

        // Clear Update Order Errors
        if (updateOrderIDError != null) updateOrderIDError.setText("");
        if (updateFieldError != null) updateFieldError.setText("");
        if (updateValueError != null) updateValueError.setText("");
    }

    /**
     * Validates if the provided field name is allowed to be updated.
     *
     * @param field Field name to validate.
     * @return true if valid; false otherwise.
     */
    private boolean isValidField(String field) {
        String[] validFields = {"CustomerID", "EmployeeID", "CarID", "Quantity", "TotalPrice", "OrderDate"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all fields in the Add Order section.
     */
    private void clearAddOrderFields() {
        orderIDField.clear();
        customerIDField.clear();
        employeeIDField.clear();
        carIDField.clear();
        quantityField.clear();
        totalPriceField.clear();
        orderDateField.clear();
    }

    /**
     * Clears all fields in the Update Order section.
     */
    private void clearUpdateOrderFields() {
        updateOrderIDField.clear();
        updateFieldField.clear();
        updateValueField.clear();
    }
}
