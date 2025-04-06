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
 * User Interface for managing services.
 */
public class ServicesManagementUI extends Application {
    private TableView<ObservableList<String>> tableView = new TableView<>();
    private VBox mainContainer;
    private VBox actionContainer = new VBox(10);
    private Label messageLabel = new Label();

    // Add Service Fields
    private TextField serviceIDField, carIDField, serviceDateField, serviceDescriptionField, costField;
    private Label serviceIDError, carIDError, serviceDateError, serviceDescriptionError, costError;

    // Find Service Fields
    private TextField findServiceIDField, findCarIDField, findServiceDateField, findServiceDescriptionField, findCostField;

    // Update Service Fields
    private TextField updateServiceIDField, updateFieldField, updateValueField;
    private Label updateServiceIDError, updateFieldError, updateValueError;

    private ServiceInsert serviceInsert = new ServiceInsert();
    private ServiceSearch serviceSearch = new ServiceSearch();
    private ServiceUpdate serviceUpdate = new ServiceUpdate();

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Services Management");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Sidebar with buttons
        VBox sidebar = createSidebar();

        // Main container
        mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label title = new Label("Services Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #B22222;");

        tableView.setPrefHeight(400);
        setupTableColumns();
        loadData(serviceSearch.searchServices("", "", "", "", ""));

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

        Button addButton = createNavButton("Add Service");
        addButton.setOnAction(e -> showAddServiceFields());

        Button findButton = createNavButton("Find Service");
        findButton.setOnAction(e -> showFindServiceFields());

        Button updateButton = createNavButton("Update Service");
        updateButton.setOnAction(e -> showUpdateServiceFields());

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
     * Displays the fields required to add a new service.
     */
    private void showAddServiceFields() {
        clearActionContainer();

        // Initialize fields and error labels
        serviceIDField = createTextField("Service ID");
        serviceIDError = createErrorLabel();
        carIDField = createTextField("Car ID");
        carIDError = createErrorLabel();
        serviceDateField = createTextField("Service Date (YYYY-MM-DD)");
        serviceDateError = createErrorLabel();
        serviceDescriptionField = createTextField("Service Description");
        serviceDescriptionError = createErrorLabel();
        costField = createTextField("Cost");
        costError = createErrorLabel();

        Button saveButton = createActionButton("Save");
        saveButton.setOnAction(e -> insertService());

        VBox fieldsBox = new VBox(5,
                new Label("Service ID:"), serviceIDField, serviceIDError,
                new Label("Car ID:"), carIDField, carIDError,
                new Label("Service Date:"), serviceDateField, serviceDateError,
                new Label("Service Description:"), serviceDescriptionField, serviceDescriptionError,
                new Label("Cost:"), costField, costError,
                saveButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to find/search services.
     */
    private void showFindServiceFields() {
        clearActionContainer();

        findServiceIDField = createTextField("Service ID");
        findCarIDField = createTextField("Car ID");
        findServiceDateField = createTextField("Service Date");
        findServiceDescriptionField = createTextField("Service Description");
        findCostField = createTextField("Cost");

        Button searchButton = createActionButton("Search");
        searchButton.setOnAction(e -> searchServices());

        VBox fieldsBox = new VBox(10,
                new Label("Search Services"),
                new Label("Service ID:"), findServiceIDField,
                new Label("Car ID:"), findCarIDField,
                new Label("Service Date:"), findServiceDateField,
                new Label("Service Description:"), findServiceDescriptionField,
                new Label("Cost:"), findCostField,
                searchButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Displays the fields required to update an existing service.
     */
    private void showUpdateServiceFields() {
        clearActionContainer();

        updateServiceIDField = createTextField("Service ID");
        updateServiceIDError = createErrorLabel();
        updateFieldField = createTextField("Field to Update (e.g., Cost)");
        updateFieldError = createErrorLabel();
        updateValueField = createTextField("New Value");
        updateValueError = createErrorLabel();

        Button updateButton = createActionButton("Update");
        updateButton.setOnAction(e -> updateServiceField());

        VBox fieldsBox = new VBox(5,
                new Label("Service ID:"), updateServiceIDField, updateServiceIDError,
                new Label("Field to Update:"), updateFieldField, updateFieldError,
                new Label("New Value:"), updateValueField, updateValueError,
                updateButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    /**
     * Inserts a new service after validating inputs.
     */
    private void insertService() {
        clearErrors();
        boolean isValid = true;

        // Validate Service ID
        String sidText = serviceIDField.getText().trim();
        int sid = 0;
        if (sidText.isEmpty()) {
            serviceIDError.setText("Service ID is required.");
            isValid = false;
        } else {
            try {
                sid = Integer.parseInt(sidText);
                if (sid <= 0) {
                    serviceIDError.setText("Service ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                serviceIDError.setText("Invalid Service ID.");
                isValid = false;
            }
        }

        // Validate Car ID
        String cidText = carIDField.getText().trim();
        int cid = 0;
        if (cidText.isEmpty()) {
            carIDError.setText("Car ID is required.");
            isValid = false;
        } else {
            try {
                cid = Integer.parseInt(cidText);
                if (cid <= 0) {
                    carIDError.setText("Car ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                carIDError.setText("Invalid Car ID.");
                isValid = false;
            }
        }

        // Validate Service Date
        String date = serviceDateField.getText().trim();
        if (date.isEmpty()) {
            serviceDateError.setText("Service Date is required.");
            isValid = false;
        } else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            serviceDateError.setText("Service Date must be in YYYY-MM-DD format.");
            isValid = false;
        }

        // Validate Service Description
        String desc = serviceDescriptionField.getText().trim();
        if (desc.isEmpty()) {
            serviceDescriptionError.setText("Service Description is required.");
            isValid = false;
        }

        // Validate Cost
        String costText = costField.getText().trim();
        double cost = 0.0;
        if (costText.isEmpty()) {
            costError.setText("Cost is required.");
            isValid = false;
        } else {
            try {
                cost = Double.parseDouble(costText);
                if (cost < 0) {
                    costError.setText("Cost cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                costError.setText("Invalid Cost.");
                isValid = false;
            }
        }

        if (isValid) {
            try {
                boolean success = serviceInsert.insertService(sid, cid, date, desc, cost);
                if (success) {
                    messageLabel.setText("Service added successfully.");
                    loadData(serviceSearch.searchServices("", "", "", "", ""));
                    clearAddServiceFields();
                } else {
                    messageLabel.setText("Error adding service. Please check the details.");
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
     * Searches for services based on input criteria.
     */
    private void searchServices() {
        String sid = findServiceIDField.getText().trim();
        String cid = findCarIDField.getText().trim();
        String sDate = findServiceDateField.getText().trim();
        String sDesc = findServiceDescriptionField.getText().trim();
        String sCost = findCostField.getText().trim();

        ObservableList<ObservableList<String>> results = serviceSearch.searchServices(sid, cid, sDate, sDesc, sCost);
        loadData(results);
        messageLabel.setText(results.isEmpty() ? "No services found." : "Search results displayed.");
    }

    /**
     * Updates a specific field of an existing service after validation.
     */
    private void updateServiceField() {
        clearErrors();
        boolean isValid = true;

        // Validate Service ID
        String sidText = updateServiceIDField.getText().trim();
        int sid = 0;
        if (sidText.isEmpty()) {
            updateServiceIDError.setText("Service ID is required.");
            isValid = false;
        } else {
            try {
                sid = Integer.parseInt(sidText);
                if (sid <= 0) {
                    updateServiceIDError.setText("Service ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                updateServiceIDError.setText("Invalid Service ID.");
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
                boolean success = serviceUpdate.updateServiceField(sid, field, value);
                if (success) {
                    messageLabel.setText("Service updated successfully.");
                    loadData(serviceSearch.searchServices("", "", "", "", ""));
                    clearUpdateServiceFields();
                } else {
                    messageLabel.setText("Error updating service. Please check the details.");
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
        String[] columns = {"ServiceID", "CarID", "ServiceDate", "ServiceDescription", "Cost"};
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
        // Clear Add Service Errors
        if (serviceIDError != null) serviceIDError.setText("");
        if (carIDError != null) carIDError.setText("");
        if (serviceDateError != null) serviceDateError.setText("");
        if (serviceDescriptionError != null) serviceDescriptionError.setText("");
        if (costError != null) costError.setText("");

        // Clear Update Service Errors
        if (updateServiceIDError != null) updateServiceIDError.setText("");
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
        String[] validFields = {"CarID", "ServiceDate", "ServiceDescription", "Cost"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clears all fields in the Add Service section.
     */
    private void clearAddServiceFields() {
        serviceIDField.clear();
        carIDField.clear();
        serviceDateField.clear();
        serviceDescriptionField.clear();
        costField.clear();
    }

    /**
     * Clears all fields in the Update Service section.
     */
    private void clearUpdateServiceFields() {
        updateServiceIDField.clear();
        updateFieldField.clear();
        updateValueField.clear();
    }
}
