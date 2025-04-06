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
 * User Interface for managing customers.
 */
public class CustomerManagementUI extends Application {
    private TableView<ObservableList<String>> tableView = new TableView<>();
    private VBox mainContainer;
    private VBox actionContainer = new VBox(10);
    private Label messageLabel = new Label();

    // Add Customer Fields
    private TextField customerIDField, firstNameField, lastNameField, emailField, phoneField, addressField, cityField, zipCodeField;
    private Label customerIDError, firstNameError, lastNameError, emailError, phoneError, addressError, cityError, zipCodeError;

    // Find Customer Fields
    private TextField findCustomerIDField, findFirstNameField, findLastNameField, findEmailField, findPhoneField, findAddressField, findCityField, findZipCodeField;

    // Update Customer Fields
    private TextField updateCustomerIDField, updateFieldField, updateValueField;
    private Label updateCustomerIDError, updateFieldError, updateValueError;

    private CustomerInsert customerInsert = new CustomerInsert();
    private CustomerSearch customerSearch = new CustomerSearch();
    private CustomerUpdate customerUpdate = new CustomerUpdate();

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Customer Management");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Sidebar with buttons
        VBox sidebar = createSidebar();

        // Main container
        mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label title = new Label("Customer Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #B22222;");

        tableView.setPrefHeight(400);
        setupTableColumns();
        loadData(customerSearch.searchCustomers("", "", "", "", "", "", "", ""));

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

        Button addButton = createNavButton("Add Customer");
        addButton.setOnAction(e -> showAddCustomerFields());

        Button findButton = createNavButton("Find Customer");
        findButton.setOnAction(e -> showFindCustomerFields());

        Button updateButton = createNavButton("Update Customer");
        updateButton.setOnAction(e -> showUpdateCustomerFields());

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
     * Displays the fields required to add a new customer.
     */
    private void showAddCustomerFields() {
        clearActionContainer();

        // Initialize fields and error labels
        customerIDField = createTextField("Customer ID");
        customerIDError = createErrorLabel();
        firstNameField = createTextField("First Name");
        firstNameError = createErrorLabel();
        lastNameField = createTextField("Last Name");
        lastNameError = createErrorLabel();
        emailField = createTextField("Email");
        emailError = createErrorLabel();
        phoneField = createTextField("Phone");
        phoneError = createErrorLabel();
        addressField = createTextField("Address");
        addressError = createErrorLabel();
        cityField = createTextField("City");
        cityError = createErrorLabel();
        zipCodeField = createTextField("Zip Code");
        zipCodeError = createErrorLabel();

        Button saveButton = createActionButton("Save");
        saveButton.setOnAction(e -> insertCustomer());

        VBox fieldsBox = new VBox(5,
                new Label("Customer ID:"), customerIDField, customerIDError,
                new Label("First Name:"), firstNameField, firstNameError,
                new Label("Last Name:"), lastNameField, lastNameError,
                new Label("Email:"), emailField, emailError,
                new Label("Phone:"), phoneField, phoneError,
                new Label("Address:"), addressField, addressError,
                new Label("City:"), cityField, cityError,
                new Label("Zip Code:"), zipCodeField, zipCodeError,
                saveButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to find/search customers.
     */
    private void showFindCustomerFields() {
        clearActionContainer();

        // Initialize search fields
        findCustomerIDField = createTextField("Customer ID");
        findFirstNameField = createTextField("First Name");
        findLastNameField = createTextField("Last Name");
        findEmailField = createTextField("Email");
        findPhoneField = createTextField("Phone");
        findAddressField = createTextField("Address");
        findCityField = createTextField("City");
        findZipCodeField = createTextField("Zip Code");

        Button searchButton = createActionButton("Search");
        searchButton.setOnAction(e -> searchCustomers());

        VBox fieldsBox = new VBox(10,
                new Label("Search Customers"),
                new Label("Customer ID:"), findCustomerIDField,
                new Label("First Name:"), findFirstNameField,
                new Label("Last Name:"), findLastNameField,
                new Label("Email:"), findEmailField,
                new Label("Phone:"), findPhoneField,
                new Label("Address:"), findAddressField,
                new Label("City:"), findCityField,
                new Label("Zip Code:"), findZipCodeField,
                searchButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to update an existing customer.
     */
    private void showUpdateCustomerFields() {
        clearActionContainer();

        updateCustomerIDField = createTextField("Customer ID");
        updateCustomerIDError = createErrorLabel();
        updateFieldField = createTextField("Field to Update (e.g., Email)");
        updateFieldError = createErrorLabel();
        updateValueField = createTextField("New Value");
        updateValueError = createErrorLabel();

        Button updateButton = createActionButton("Update");
        updateButton.setOnAction(e -> updateCustomerField());

        VBox fieldsBox = new VBox(5,
                new Label("Customer ID:"), updateCustomerIDField, updateCustomerIDError,
                new Label("Field to Update:"), updateFieldField, updateFieldError,
                new Label("New Value:"), updateValueField, updateValueError,
                updateButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Inserts a new customer after validating inputs.
     */
    private void insertCustomer() {
        clearErrors();
        boolean isValid = true;

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

        // Validate First Name
        String firstName = firstNameField.getText().trim();
        if (firstName.isEmpty()) {
            firstNameError.setText("First Name is required.");
            isValid = false;
        }

        // Validate Last Name
        String lastName = lastNameField.getText().trim();
        if (lastName.isEmpty()) {
            lastNameError.setText("Last Name is required.");
            isValid = false;
        }

        // Validate Email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            emailError.setText("Email is required.");
            isValid = false;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            emailError.setText("Invalid Email format.");
            isValid = false;
        }

        // Validate Phone
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            phoneError.setText("Phone is required.");
            isValid = false;
        } else if (!phone.matches("\\d{10}")) {
            phoneError.setText("Phone must be 10 digits.");
            isValid = false;
        }

        // Validate Address
        String address = addressField.getText().trim();
        if (address.isEmpty()) {
            addressError.setText("Address is required.");
            isValid = false;
        }

        // Validate City
        String city = cityField.getText().trim();
        if (city.isEmpty()) {
            cityError.setText("City is required.");
            isValid = false;
        }

        // Validate Zip Code
        String zipCode = zipCodeField.getText().trim();
        if (zipCode.isEmpty()) {
            zipCodeError.setText("Zip Code is required.");
            isValid = false;
        } else if (!zipCode.matches("\\d{5}")) {
            zipCodeError.setText("Zip Code must be 5 digits.");
            isValid = false;
        }

        if (isValid) {
            try {
                boolean success = customerInsert.insertCustomer(custID, firstName, lastName, email, phone, address, city, zipCode);
                if (success) {
                    messageLabel.setText("Customer added successfully.");
                    loadData(customerSearch.searchCustomers("", "", "", "", "", "", "", ""));
                    clearAddCustomerFields();
                } else {
                    messageLabel.setText("Error adding customer. Please check the details.");
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
     * Searches for customers based on input criteria.
     */
    private void searchCustomers() {
        String custID = findCustomerIDField.getText().trim();
        String firstName = findFirstNameField.getText().trim();
        String lastName = findLastNameField.getText().trim();
        String email = findEmailField.getText().trim();
        String phone = findPhoneField.getText().trim();
        String address = findAddressField.getText().trim();
        String city = findCityField.getText().trim();
        String zipCode = findZipCodeField.getText().trim();

        // Build search parameters
        String field = "";
        String value = "";

        if (!custID.isEmpty()) {
            field = "CustomerID";
            value = custID;
        } else if (!firstName.isEmpty()) {
            field = "FirstName";
            value = firstName;
        } else if (!lastName.isEmpty()) {
            field = "LastName";
            value = lastName;
        } else if (!email.isEmpty()) {
            field = "Email";
            value = email;
        } else if (!phone.isEmpty()) {
            field = "Phone";
            value = phone;
        } else if (!address.isEmpty()) {
            field = "Address";
            value = address;
        } else if (!city.isEmpty()) {
            field = "City";
            value = city;
        } else if (!zipCode.isEmpty()) {
            field = "ZipCode";
            value = zipCode;
        }

        ObservableList<ObservableList<String>> results = customerSearch.searchCustomers("", field, value, "", "", "", "", "");
        loadData(results);
        messageLabel.setText(results.isEmpty() ? "No customers found." : "Search results displayed.");
    }

    /**
     * Updates a specific field of an existing customer after validation.
     */
    private void updateCustomerField() {
        clearErrors();
        boolean isValid = true;

        // Validate Customer ID
        String custIDText = updateCustomerIDField.getText().trim();
        int custID = 0;
        if (custIDText.isEmpty()) {
            updateCustomerIDError.setText("Customer ID is required.");
            isValid = false;
        } else {
            try {
                custID = Integer.parseInt(custIDText);
                if (custID <= 0) {
                    updateCustomerIDError.setText("Customer ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                updateCustomerIDError.setText("Invalid Customer ID.");
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
        } else {
            // Additional validation based on field
            if (field.equalsIgnoreCase("Email") && !value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                updateValueError.setText("Invalid Email format.");
                isValid = false;
            }
            if (field.equalsIgnoreCase("Phone") && !value.matches("\\d{10}")) {
                updateValueError.setText("Phone must be 10 digits.");
                isValid = false;
            }
            if (field.equalsIgnoreCase("ZipCode") && !value.matches("\\d{5}")) {
                updateValueError.setText("Zip Code must be 5 digits.");
                isValid = false;
            }
        }

        if (isValid) {
            try {
                boolean success = customerUpdate.updateCustomerField(custID, field, value);
                if (success) {
                    messageLabel.setText("Customer updated successfully.");
                    loadData(customerSearch.searchCustomers("", "", "", "", "", "", "", ""));
                    clearUpdateCustomerFields();
                } else {
                    messageLabel.setText("Error updating customer. Please check the details.");
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
        String[] columns = {"CustomerID", "FirstName", "LastName", "Email", "Phone", "Address", "City", "ZipCode"};
        for (int i = 0; i < columns.length; i++) {
            final int colIndex = i;
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(columns[i]);
            col.setCellValueFactory(param -> {
                if (param.getValue().size() > colIndex) {
                    return new SimpleStringProperty(param.getValue().get(colIndex));
                } else {
                    return new SimpleStringProperty(""); // Prevent IndexOutOfBoundsException
                }
            });
            col.setPrefWidth(120); // Optional: set preferred width
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
        // Clear Add Customer Errors
        if (customerIDError != null) customerIDError.setText("");
        if (firstNameError != null) firstNameError.setText("");
        if (lastNameError != null) lastNameError.setText("");
        if (emailError != null) emailError.setText("");
        if (phoneError != null) phoneError.setText("");
        if (addressError != null) addressError.setText("");
        if (cityError != null) cityError.setText("");
        if (zipCodeError != null) zipCodeError.setText("");

        // Clear Update Customer Errors
        if (updateCustomerIDError != null) updateCustomerIDError.setText("");
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
        String[] validFields = {"FirstName", "LastName", "Email", "Phone", "Address", "City", "ZipCode"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all fields in the Add Customer section.
     */
    private void clearAddCustomerFields() {
        customerIDField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        cityField.clear();
        zipCodeField.clear();
    }

    /**
     * Clears all fields in the Update Customer section.
     */
    private void clearUpdateCustomerFields() {
        updateCustomerIDField.clear();
        updateFieldField.clear();
        updateValueField.clear();
    }
}
