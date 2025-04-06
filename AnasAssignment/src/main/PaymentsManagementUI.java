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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * User Interface for managing payments.
 */
public class PaymentsManagementUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private VBox mainContainer;
    private VBox actionContainer = new VBox(10);
    private Label messageLabel = new Label();

    // Add Payment Fields
    private TextField paymentIDField, orderIDField, amountField, paymentDateField, paymentMethodField;
    private Label paymentIDError, orderIDError, amountError, paymentDateError, paymentMethodError;

    // Find Payment Fields
    private TextField findPaymentIDField, findOrderIDField, findPaymentDateField, findPaymentMethodField, findAmountField;

    // Update Payment Fields
    private TextField updatePaymentIDField, updateFieldField, updateValueField;
    private Label updatePaymentIDError, updateFieldError, updateValueError;

    private PaymentInsert paymentInsert = new PaymentInsert();
    private PaymentSearch paymentSearch = new PaymentSearch();
    private PaymentUpdate paymentUpdate = new PaymentUpdate();

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Payments Management");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFFFFF;");

        // Sidebar with buttons
        VBox sidebar = createSidebar();

        // Main container
        mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label title = new Label("Payments Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #B22222;");

        tableView.setPrefHeight(400);
        setupTableColumns();
        loadData(paymentSearch.searchPayments("", "", "", "", ""));

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

        Button addButton = createNavButton("Add Payment");
        addButton.setOnAction(e -> showAddPaymentFields());

        Button findButton = createNavButton("Find Payment");
        findButton.setOnAction(e -> showFindPaymentFields());

        Button updateButton = createNavButton("Update Payment");
        updateButton.setOnAction(e -> showUpdatePaymentFields());

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
     * Displays the fields required to add a new payment.
     */
    private void showAddPaymentFields() {
        clearActionContainer();

        // Initialize fields and error labels
        paymentIDField = createTextField("Payment ID");
        paymentIDError = createErrorLabel();
        orderIDField = createTextField("Order ID");
        orderIDError = createErrorLabel();
        amountField = createTextField("Amount");
        amountError = createErrorLabel();
        paymentDateField = createTextField("Payment Date (YYYY-MM-DD)");
        paymentDateError = createErrorLabel();
        paymentMethodField = createTextField("Payment Method");
        paymentMethodError = createErrorLabel();

        Button saveButton = createActionButton("Save");
        saveButton.setOnAction(e -> insertPayment());

        VBox fieldsBox = new VBox(5,
                new Label("Payment ID:"), paymentIDField, paymentIDError,
                new Label("Order ID:"), orderIDField, orderIDError,
                new Label("Amount:"), amountField, amountError,
                new Label("Payment Date:"), paymentDateField, paymentDateError,
                new Label("Payment Method:"), paymentMethodField, paymentMethodError,
                saveButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to find/search payments.
     */
    private void showFindPaymentFields() {
        clearActionContainer();

        findPaymentIDField = createTextField("Payment ID");
        findOrderIDField = createTextField("Order ID");
        findPaymentDateField = createTextField("Payment Date");
        findPaymentMethodField = createTextField("Payment Method");
        findAmountField = createTextField("Amount");

        Button searchButton = createActionButton("Search");
        searchButton.setOnAction(e -> searchPayments());

        VBox fieldsBox = new VBox(10,
                new Label("Search Payments"),
                new Label("Payment ID:"), findPaymentIDField,
                new Label("Order ID:"), findOrderIDField,
                new Label("Payment Date:"), findPaymentDateField,
                new Label("Payment Method:"), findPaymentMethodField,
                new Label("Amount:"), findAmountField,
                searchButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to update an existing payment.
     */
    private void showUpdatePaymentFields() {
        clearActionContainer();

        updatePaymentIDField = createTextField("Payment ID");
        updatePaymentIDError = createErrorLabel();
        updateFieldField = createTextField("Field to Update (e.g., Amount)");
        updateFieldError = createErrorLabel();
        updateValueField = createTextField("New Value");
        updateValueError = createErrorLabel();

        Button updateButton = createActionButton("Update");
        updateButton.setOnAction(e -> updatePaymentField());

        VBox fieldsBox = new VBox(5,
                new Label("Payment ID:"), updatePaymentIDField, updatePaymentIDError,
                new Label("Field to Update:"), updateFieldField, updateFieldError,
                new Label("New Value:"), updateValueField, updateValueError,
                updateButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Inserts a new payment after validating inputs.
     */
    private void insertPayment() {
        clearErrors();
        boolean isValid = true;

        // Validate Payment ID
        String payIDText = paymentIDField.getText().trim();
        int payID = 0;
        if (payIDText.isEmpty()) {
            paymentIDError.setText("Payment ID is required.");
            isValid = false;
        } else {
            try {
                payID = Integer.parseInt(payIDText);
                if (payID <= 0) {
                    paymentIDError.setText("Payment ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                paymentIDError.setText("Invalid Payment ID.");
                isValid = false;
            }
        }

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

        // Validate Amount
        String amtText = amountField.getText().trim();
        double amt = 0.0;
        if (amtText.isEmpty()) {
            amountError.setText("Amount is required.");
            isValid = false;
        } else {
            try {
                amt = Double.parseDouble(amtText);
                if (amt < 0) {
                    amountError.setText("Amount cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                amountError.setText("Invalid Amount.");
                isValid = false;
            }
        }

        // Validate Payment Date
        String date = paymentDateField.getText().trim();
        if (date.isEmpty()) {
            paymentDateError.setText("Payment Date is required.");
            isValid = false;
        } else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            paymentDateError.setText("Payment Date must be in YYYY-MM-DD format.");
            isValid = false;
        }

        // Validate Payment Method
        String method = paymentMethodField.getText().trim();
        if (method.isEmpty()) {
            paymentMethodError.setText("Payment Method is required.");
            isValid = false;
        }

        if (isValid) {
            try {
                boolean success = paymentInsert.insertPayment(payID, ordID, amt, date, method);
                if (success) {
                    messageLabel.setText("Payment added successfully.");
                    loadData(paymentSearch.searchPayments("", "", "", "", "")); // Refresh table
                    clearAddPaymentFields();
                } else {
                    messageLabel.setText("Error adding payment. Please check the details.");
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
     * Searches for payments based on input criteria.
     */
    private void searchPayments() {
        String payID = findPaymentIDField.getText().trim();
        String ordID = findOrderIDField.getText().trim();
        String payDate = findPaymentDateField.getText().trim();
        String payMethod = findPaymentMethodField.getText().trim();
        String amt = findAmountField.getText().trim();

        ObservableList<ObservableList<String>> results = paymentSearch.searchPayments(payID, ordID, payDate, payMethod, amt);
        loadData(results);
        messageLabel.setText(results.isEmpty() ? "No payments found." : "Search results displayed.");
    }

    /**
     * Updates a specific field of an existing payment after validation.
     */
    private void updatePaymentField() {
        clearErrors();
        boolean isValid = true;

        // Validate Payment ID
        String payIDText = updatePaymentIDField.getText().trim();
        int payID = 0;
        if (payIDText.isEmpty()) {
            updatePaymentIDError.setText("Payment ID is required.");
            isValid = false;
        } else {
            try {
                payID = Integer.parseInt(payIDText);
                if (payID <= 0) {
                    updatePaymentIDError.setText("Payment ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                updatePaymentIDError.setText("Invalid Payment ID.");
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
                boolean success = paymentUpdate.updatePaymentField(payID, field, value);
                if (success) {
                    messageLabel.setText("Payment updated successfully.");
                    loadData(paymentSearch.searchPayments("", "", "", "", ""));
                    clearUpdatePaymentFields();
                } else {
                    messageLabel.setText("Error updating payment. Please check the details.");
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
        String[] columns = {"PaymentID", "OrderID", "Amount", "PaymentDate", "PaymentMethod"};
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
        // Clear Add Payment Errors
        if (paymentIDError != null) paymentIDError.setText("");
        if (orderIDError != null) orderIDError.setText("");
        if (amountError != null) amountError.setText("");
        if (paymentDateError != null) paymentDateError.setText("");
        if (paymentMethodError != null) paymentMethodError.setText("");

        // Clear Update Payment Errors
        if (updatePaymentIDError != null) updatePaymentIDError.setText("");
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
        String[] validFields = {"OrderID", "Amount", "PaymentDate", "PaymentMethod"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all fields in the Add Payment section.
     */
    private void clearAddPaymentFields() {
        paymentIDField.clear();
        orderIDField.clear();
        amountField.clear();
        paymentDateField.clear();
        paymentMethodField.clear();
    }

    /**
     * Clears all fields in the Update Payment section.
     */
    private void clearUpdatePaymentFields() {
        updatePaymentIDField.clear();
        updateFieldField.clear();
        updateValueField.clear();
    }
}
