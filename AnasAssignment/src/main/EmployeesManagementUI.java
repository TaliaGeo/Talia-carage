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

public class EmployeesManagementUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private VBox mainContainer;
    private VBox actionContainer = new VBox(10);
    private Label messageLabel = new Label();

    // Add Employee Fields
    private TextField employeeIDField, firstNameField, lastNameField, positionField, salaryField, hireDateField;
    private Label employeeIDError, firstNameError, lastNameError, positionError, salaryError, hireDateError;

    // Find Employee Fields
    private TextField findEmployeeIDField, findFirstNameField, findLastNameField, findPositionField, findSalaryField, findHireDateField;

    // Update Employee Fields
    private TextField updateEmployeeIDField, updateFieldField, updateValueField;
    private Label updateEmployeeIDError, updateFieldError, updateValueError;

    // Update Salary Fields (similar to Update Price in CarManagementUI)
    private TextField adjustSalaryAmountField, adjustSalaryPercentageField;
    private Label adjustSalaryAmountError, adjustSalaryPercentageError;

    private EmployeeInsert employeeInsert = new EmployeeInsert();
    private EmployeeSearch employeeSearch = new EmployeeSearch();
    private EmployeeUpdate employeeUpdate = new EmployeeUpdate();
    private EmployeeSalaryUpdate employeeSalaryUpdate = new EmployeeSalaryUpdate();

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Employee Management");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Sidebar with buttons
        VBox sidebar = createSidebar();

        // Main container
        mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label title = new Label("Employee Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: #B22222;");

        tableView.setPrefHeight(400);
        setupTableColumns();
        loadData(employeeSearch.searchEmployees("", "", "", "", "", ""));

        messageLabel.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");

        mainContainer.getChildren().addAll(title, tableView, actionContainer, messageLabel);

        root.setLeft(sidebar);
        root.setCenter(mainContainer);

        Scene scene = new Scene(root);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #333333;");
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);

        Button addButton = createNavButton("Add Employee");
        addButton.setOnAction(e -> showAddEmployeeFields());

        Button findButton = createNavButton("Find Employee");
        findButton.setOnAction(e -> showFindEmployeeFields());

        Button updateButton = createNavButton("Update Employee");
        updateButton.setOnAction(e -> showUpdateEmployeeFields());

        Button updateSalaryButton = createNavButton("Update Salaries");
        updateSalaryButton.setOnAction(e -> showUpdateSalaryFields());

        Button backButton = createNavButton("Back");
        backButton.setOnAction(e -> goBackToHome());

        sidebar.getChildren().addAll(addButton, findButton, updateButton, updateSalaryButton, backButton);
        return sidebar;
    }

    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setPrefWidth(250);
        button.setPrefHeight(50);
        return button;
    }

    private void showAddEmployeeFields() {
        clearActionContainer();

        // Initialize fields and error labels
        employeeIDField = createTextField("Employee ID");
        employeeIDError = createErrorLabel();
        firstNameField = createTextField("First Name");
        firstNameError = createErrorLabel();
        lastNameField = createTextField("Last Name");
        lastNameError = createErrorLabel();
        positionField = createTextField("Position");
        positionError = createErrorLabel();
        salaryField = createTextField("Salary");
        salaryError = createErrorLabel();
        hireDateField = createTextField("Hire Date (YYYY-MM-DD)");
        hireDateError = createErrorLabel();

        Button saveButton = createActionButton("Save");
        saveButton.setOnAction(e -> insertEmployee());

        VBox fieldsBox = new VBox(5,
                new Label("Employee ID:"), employeeIDField, employeeIDError,
                new Label("First Name:"), firstNameField, firstNameError,
                new Label("Last Name:"), lastNameField, lastNameError,
                new Label("Position:"), positionField, positionError,
                new Label("Salary:"), salaryField, salaryError,
                new Label("Hire Date:"), hireDateField, hireDateError,
                saveButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    private void showFindEmployeeFields() {
        clearActionContainer();

        findEmployeeIDField = createTextField("Employee ID");
        findFirstNameField = createTextField("First Name");
        findLastNameField = createTextField("Last Name");
        findPositionField = createTextField("Position");
        findSalaryField = createTextField("Salary");
        findHireDateField = createTextField("Hire Date");

        Button searchButton = createActionButton("Search");
        searchButton.setOnAction(e -> searchEmployees());

        VBox fieldsBox = new VBox(10,
                new Label("Search Employees"),
                new Label("Employee ID:"), findEmployeeIDField,
                new Label("First Name:"), findFirstNameField,
                new Label("Last Name:"), findLastNameField,
                new Label("Position:"), findPositionField,
                new Label("Salary:"), findSalaryField,
                new Label("Hire Date:"), findHireDateField,
                searchButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    private void showUpdateEmployeeFields() {
        clearActionContainer();

        updateEmployeeIDField = createTextField("Employee ID");
        updateEmployeeIDError = createErrorLabel();
        updateFieldField = createTextField("Field to Update (e.g., Salary)");
        updateFieldError = createErrorLabel();
        updateValueField = createTextField("New Value");
        updateValueError = createErrorLabel();

        Button updateButton = createActionButton("Update");
        updateButton.setOnAction(e -> updateEmployeeField());

        VBox fieldsBox = new VBox(5,
                new Label("Employee ID:"), updateEmployeeIDField, updateEmployeeIDError,
                new Label("Field to Update:"), updateFieldField, updateFieldError,
                new Label("New Value:"), updateValueField, updateValueError,
                updateButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    private void showUpdateSalaryFields() {
        clearActionContainer();

        // Adjust by Amount Section
        Label amountLabel = new Label("Adjust Salaries by Amount:");
        adjustSalaryAmountField = createTextField("Enter amount (e.g., 500 or -500)");
        adjustSalaryAmountError = createErrorLabel();
        Button adjustAmountButton = createActionButton("Apply Amount");
        adjustAmountButton.setOnAction(e -> adjustSalariesByAmount());

        VBox amountBox = new VBox(5, amountLabel, adjustSalaryAmountField, adjustSalaryAmountError, adjustAmountButton);
        amountBox.setAlignment(Pos.CENTER_LEFT);
        amountBox.setPadding(new Insets(10));

        // Adjust by Percentage Section
        Label percentageLabel = new Label("Adjust Salaries by Percentage:");
        adjustSalaryPercentageField = createTextField("Enter % (e.g., 10 for +10%, -10 for -10%)");
        adjustSalaryPercentageError = createErrorLabel();
        Button adjustPercentageButton = createActionButton("Apply Percentage");
        adjustPercentageButton.setOnAction(e -> adjustSalariesByPercentage());

        VBox percentageBox = new VBox(5, percentageLabel, adjustSalaryPercentageField, adjustSalaryPercentageError, adjustPercentageButton);
        percentageBox.setAlignment(Pos.CENTER_LEFT);
        percentageBox.setPadding(new Insets(10));

        VBox fieldsBox = new VBox(20, amountBox, percentageBox);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    private void insertEmployee() {
        clearErrors();
        boolean isValid = true;

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

        // Validate Position
        String position = positionField.getText().trim();
        if (position.isEmpty()) {
            positionError.setText("Position is required.");
            isValid = false;
        }

        // Validate Salary
        String salaryText = salaryField.getText().trim();
        double salary = 0.0;
        if (salaryText.isEmpty()) {
            salaryError.setText("Salary is required.");
            isValid = false;
        } else {
            try {
                salary = Double.parseDouble(salaryText);
                if (salary < 0) {
                    salaryError.setText("Salary cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                salaryError.setText("Invalid Salary.");
                isValid = false;
            }
        }

        // Validate Hire Date
        String hireDate = hireDateField.getText().trim();
        if (hireDate.isEmpty()) {
            hireDateError.setText("Hire Date is required.");
            isValid = false;
        } else if (!hireDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            hireDateError.setText("Hire Date must be in YYYY-MM-DD format.");
            isValid = false;
        }

        if (isValid) {
            try {
                boolean success = employeeInsert.insertEmployee(empID, firstName, lastName, position, salary, hireDate);
                if (success) {
                    messageLabel.setText("Employee added successfully.");
                    loadData(employeeSearch.searchEmployees("", "", "", "", "", ""));
                    clearAddEmployeeFields();
                } else {
                    messageLabel.setText("Error adding employee. Please check the details.");
                }
            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    private void searchEmployees() {
        String empID = findEmployeeIDField.getText().trim();
        String firstName = findFirstNameField.getText().trim();
        String lastName = findLastNameField.getText().trim();
        String position = findPositionField.getText().trim();
        String salary = findSalaryField.getText().trim();
        String hireDate = findHireDateField.getText().trim();

        ObservableList<ObservableList<String>> results = employeeSearch.searchEmployees(empID, firstName, lastName, position, salary, hireDate);
        loadData(results);
        messageLabel.setText(results.isEmpty() ? "No employees found." : "Search results displayed.");
    }

    private void updateEmployeeField() {
        clearErrors();
        boolean isValid = true;

        // Validate Employee ID
        String empIDText = updateEmployeeIDField.getText().trim();
        int empID = 0;
        if (empIDText.isEmpty()) {
            updateEmployeeIDError.setText("Employee ID is required.");
            isValid = false;
        } else {
            try {
                empID = Integer.parseInt(empIDText);
                if (empID <= 0) {
                    updateEmployeeIDError.setText("Employee ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                updateEmployeeIDError.setText("Invalid Employee ID.");
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
                boolean success = employeeUpdate.updateEmployeeField(empID, field, value);
                if (success) {
                    messageLabel.setText("Employee updated successfully.");
                    loadData(employeeSearch.searchEmployees("", "", "", "", "", ""));
                    clearUpdateEmployeeFields();
                } else {
                    messageLabel.setText("Error updating employee. Please check the details.");
                }
            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    private void adjustSalariesByAmount() {
        clearErrors();
        boolean isValid = true;

        String amountText = adjustSalaryAmountField.getText().trim();
        double amount = 0.0;
        if (amountText.isEmpty()) {
            adjustSalaryAmountError.setText("Amount is required.");
            isValid = false;
        } else {
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                adjustSalaryAmountError.setText("Invalid amount.");
                isValid = false;
            }
        }

        if (isValid) {
            try {
                boolean success = employeeSalaryUpdate.adjustSalariesByAmount(amount);
                if (success) {
                    messageLabel.setText("Salaries adjusted successfully by " + amount + ".");
                    loadData(employeeSearch.searchEmployees("", "", "", "", "", ""));
                    adjustSalaryAmountField.clear();
                    System.out.println("Salaries adjusted by amount: " + amount);
                } else {
                    messageLabel.setText("Error adjusting salaries.");
                    System.err.println("Failed to adjust salaries by amount: " + amount);
                }
            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
                System.err.println("Validation Error: " + e.getMessage());
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
            System.err.println("Validation failed for salary adjustment by amount.");
        }
    }

    private void adjustSalariesByPercentage() { // Corrected method name
        clearErrors();
        boolean isValid = true;

        String percentageText = adjustSalaryPercentageField.getText().trim();
        double percentage = 0.0;
        if (percentageText.isEmpty()) {
            adjustSalaryPercentageError.setText("Percentage is required.");
            isValid = false;
        } else {
            try {
                percentage = Double.parseDouble(percentageText);
            } catch (NumberFormatException e) {
                adjustSalaryPercentageError.setText("Invalid percentage.");
                isValid = false;
            }
        }

        if (isValid) {
            try {
                boolean success = employeeSalaryUpdate.adjustSalariesByPercentage(percentage);
                if (success) {
                    messageLabel.setText("Salaries adjusted successfully by " + percentage + "%.");
                    loadData(employeeSearch.searchEmployees("", "", "", "", "", ""));
                    adjustSalaryPercentageField.clear();
                    System.out.println("Salaries adjusted by percentage: " + percentage + "%");
                } else {
                    messageLabel.setText("Error adjusting salaries.");
                    System.err.println("Failed to adjust salaries by percentage: " + percentage + "%");
                }
            } catch (IllegalArgumentException e) {
                messageLabel.setText(e.getMessage());
                System.err.println("Validation Error: " + e.getMessage());
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
            System.err.println("Validation failed for salary adjustment by percentage.");
        }
    }

    private void goBackToHome() {
        // Implement navigation to Home Page if applicable
        // new HomePage().start(new Stage());
        primaryStage.close();
    }

    private void loadData(ObservableList<ObservableList<String>> data) {
        tableView.setItems(data);
    }

    private void setupTableColumns() {
        tableView.getColumns().clear(); // Clear existing columns if any
        String[] columns = {"EmployeeID", "FirstName", "LastName", "Position", "Salary", "HireDate"};
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

    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px;");
        field.setMaxWidth(300);
        return field;
    }

    private Button createActionButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setPrefWidth(150);
        button.setPrefHeight(35);
        return button;
    }

    private Label createErrorLabel() {
        Label label = new Label();
        label.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        return label;
    }

    private void clearActionContainer() {
        actionContainer.getChildren().clear();
        messageLabel.setText("");
    }

    private void clearErrors() {
        // Clear Add Employee Errors
        if (employeeIDError != null) employeeIDError.setText("");
        if (firstNameError != null) firstNameError.setText("");
        if (lastNameError != null) lastNameError.setText("");
        if (positionError != null) positionError.setText("");
        if (salaryError != null) salaryError.setText("");
        if (hireDateError != null) hireDateError.setText("");

        // Clear Update Employee Errors
        if (updateEmployeeIDError != null) updateEmployeeIDError.setText("");
        if (updateFieldError != null) updateFieldError.setText("");
        if (updateValueError != null) updateValueError.setText("");

        // Clear Update Salary Errors
        if (adjustSalaryAmountError != null) adjustSalaryAmountError.setText("");
        if (adjustSalaryPercentageError != null) adjustSalaryPercentageError.setText("");
    }

    private boolean isValidField(String field) {
        // Define valid fields that can be updated
        String[] validFields = {"EmployeeID", "FirstName", "LastName", "Position", "Salary", "HireDate"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    private void clearAddEmployeeFields() {
        employeeIDField.clear();
        firstNameField.clear();
        lastNameField.clear();
        positionField.clear();
        salaryField.clear();
        hireDateField.clear();
    }

    private void clearUpdateEmployeeFields() {
        updateEmployeeIDField.clear();
        updateFieldField.clear();
        updateValueField.clear();
    }

    private void clearAdjustSalaryFields() {
        adjustSalaryAmountField.clear();
        adjustSalaryPercentageField.clear();
    }
}
