package main;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CarManagementUI extends Application {

    private TableView<ObservableList<String>> tableView = new TableView<>();
    private VBox mainContainer;
    private VBox actionContainer = new VBox(10);
    private Label messageLabel = new Label();

    // Add Car Fields
    private TextField carIDField, makeField, modelField, yearField, priceField, stockField, vinField;
    private Label carIDError, makeError, modelError, yearError, priceError, stockError, vinError;

    // Find Car Fields
    private TextField findCarIDField, findMakeField, findModelField, findYearField, findPriceField, findStockField, findVinField;

    // Update Car Fields
    private TextField updateCarIDField, updateField, updateValue;
    private Label updateCarIDError, updateFieldError, updateValueError;

    // Update Price Fields
    private TextField adjustAmountField, adjustPercentageField;
    private Label adjustAmountError, adjustPercentageError;

    private CarInsert carInsert = new CarInsert();
    private CarUpdate carUpdate = new CarUpdate();
    private CarSearch carSearch = new CarSearch();
    private CarPriceUpdate carPriceUpdate = new CarPriceUpdate();

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Car Management");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Sidebar with buttons
        VBox sidebar = createSidebar();

        // Main container
        mainContainer = new VBox(10);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(20));

        Label title = new Label("Car Management");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setStyle("-fx-text-fill: #B22222;");

        tableView.setPrefHeight(400);
        setupTableColumns();
        loadData(carSearch.searchCars("", "", "", "", "", "", ""));

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

        Button addButton = createNavButton("Add Car");
        addButton.setOnAction(e -> showAddCarFields());

        Button findButton = createNavButton("Find Car");
        findButton.setOnAction(e -> showFindCarFields());

        Button updateButton = createNavButton("Update Car");
        updateButton.setOnAction(e -> showUpdateCarFields());

        Button updatePriceButton = createNavButton("Update Prices");
        updatePriceButton.setOnAction(e -> showUpdatePriceFields());

        Button backButton = createNavButton("Back");
        backButton.setOnAction(e -> goBackToHome());

        sidebar.getChildren().addAll(addButton, findButton, updateButton, updatePriceButton, backButton);
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

    private void showAddCarFields() {
        clearActionContainer();

        carIDField = createTextField("Car ID");
        carIDError = createErrorLabel();
        makeField = createTextField("Make");
        makeError = createErrorLabel();
        modelField = createTextField("Model");
        modelError = createErrorLabel();
        yearField = createTextField("Year");
        yearError = createErrorLabel();
        priceField = createTextField("Price");
        priceError = createErrorLabel();
        stockField = createTextField("Stock");
        stockError = createErrorLabel();
        vinField = createTextField("VIN");
        vinError = createErrorLabel();

        Button saveButton = createActionButton("Save");
        saveButton.setOnAction(e -> insertCar());

        VBox fieldsBox = new VBox(5, carIDField, carIDError, makeField, makeError, modelField, modelError,
                yearField, yearError, priceField, priceError, stockField, stockError, vinField, vinError, saveButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    private void showFindCarFields() {
        clearActionContainer();

        findCarIDField = createTextField("Car ID");
        findMakeField = createTextField("Make");
        findModelField = createTextField("Model");
        findYearField = createTextField("Year");
        findPriceField = createTextField("Price");
        findStockField = createTextField("Stock");
        findVinField = createTextField("VIN");

        Button searchButton = createActionButton("Search");
        searchButton.setOnAction(e -> searchCars());

        VBox fieldsBox = new VBox(10, findCarIDField, findMakeField, findModelField, findYearField,
                findPriceField, findStockField, findVinField, searchButton);
        fieldsBox.setAlignment(Pos.CENTER);
        actionContainer.getChildren().add(fieldsBox);
    }

    private void showUpdateCarFields() {
        clearActionContainer();

        updateCarIDField = createTextField("Car ID");
        updateCarIDError = createErrorLabel();
        updateField = createTextField("Field to Update (e.g., Make)");
        updateFieldError = createErrorLabel();
        updateValue = createTextField("New Value");
        updateValueError = createErrorLabel();

        Button updateButton = createActionButton("Update");
        updateButton.setOnAction(e -> updateCarField());

        VBox fieldsBox = new VBox(5, updateCarIDField, updateCarIDError, updateField, updateFieldError,
                updateValue, updateValueError, updateButton);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    private void showUpdatePriceFields() {
        clearActionContainer();

        // Adjust by Amount Section
        Label amountLabel = new Label("Adjust Prices by Amount:");
        adjustAmountField = createTextField("Enter amount (e.g., 100 or -50)");
        adjustAmountError = createErrorLabel();
        Button adjustAmountButton = createActionButton("Apply Amount");
        adjustAmountButton.setOnAction(e -> adjustPricesByAmount());

        VBox amountBox = new VBox(5, amountLabel, adjustAmountField, adjustAmountError, adjustAmountButton);
        amountBox.setAlignment(Pos.CENTER_LEFT);
        amountBox.setPadding(new Insets(10));

        // Adjust by Percentage Section
        Label percentageLabel = new Label("Adjust Prices by Percentage:");
        adjustPercentageField = createTextField("Enter % (e.g., 10 for +10%, -10 for -10%)");
        adjustPercentageError = createErrorLabel();
        Button adjustPercentageButton = createActionButton("Apply Percentage");
        adjustPercentageButton.setOnAction(e -> adjustPricesByPercentage());

        VBox percentageBox = new VBox(5, percentageLabel, adjustPercentageField, adjustPercentageError, adjustPercentageButton);
        percentageBox.setAlignment(Pos.CENTER_LEFT);
        percentageBox.setPadding(new Insets(10));

        VBox fieldsBox = new VBox(20, amountBox, percentageBox);
        fieldsBox.setAlignment(Pos.CENTER);
        fieldsBox.setPadding(new Insets(20));

        actionContainer.getChildren().add(fieldsBox);
    }

    private void insertCar() {
        clearErrors();
        boolean isValid = true;

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

        // Validate Make
        String make = makeField.getText().trim();
        if (make.isEmpty()) {
            makeError.setText("Make is required.");
            isValid = false;
        }

        // Validate Model
        String model = modelField.getText().trim();
        if (model.isEmpty()) {
            modelError.setText("Model is required.");
            isValid = false;
        }

        // Validate Year
        String yearText = yearField.getText().trim();
        int year = 0;
        if (yearText.isEmpty()) {
            yearError.setText("Year is required.");
            isValid = false;
        } else {
            try {
                year = Integer.parseInt(yearText);
                if (year < 1886 || year > 9999) { // First car invented in 1886
                    yearError.setText("Enter a valid year.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                yearError.setText("Invalid Year.");
                isValid = false;
            }
        }

        // Validate Price
        String priceText = priceField.getText().trim();
        double price = 0.0;
        if (priceText.isEmpty()) {
            priceError.setText("Price is required.");
            isValid = false;
        } else {
            try {
                price = Double.parseDouble(priceText);
                if (price < 0) {
                    priceError.setText("Price cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                priceError.setText("Invalid Price.");
                isValid = false;
            }
        }

        // Validate Stock
        String stockText = stockField.getText().trim();
        int stock = 0;
        if (stockText.isEmpty()) {
            stockError.setText("Stock is required.");
            isValid = false;
        } else {
            try {
                stock = Integer.parseInt(stockText);
                if (stock < 0) {
                    stockError.setText("Stock cannot be negative.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                stockError.setText("Invalid Stock.");
                isValid = false;
            }
        }

        // Validate VIN
        String vin = vinField.getText().trim();
        if (vin.isEmpty()) {
            vinError.setText("VIN is required.");
            isValid = false;
        } else if (vin.length() != 17) { // Standard VIN length
            vinError.setText("VIN must be 17 characters.");
            isValid = false;
        }

        if (isValid) {
            boolean success = carInsert.insertCar(carID, make, model, year, price, stock, vin);
            if (success) {
                messageLabel.setText("Car added successfully.");
                loadData(carSearch.searchCars("", "", "", "", "", "", ""));
                clearAddCarFields();
            } else {
                messageLabel.setText("Error adding car. Please check the details.");
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    private void searchCars() {
        ObservableList<ObservableList<String>> results = carSearch.searchCars(
                findCarIDField.getText().trim(),
                findMakeField.getText().trim(),
                findModelField.getText().trim(),
                findYearField.getText().trim(),
                findPriceField.getText().trim(),
                findStockField.getText().trim(),
                findVinField.getText().trim()
        );
        loadData(results);
        messageLabel.setText(results.isEmpty() ? "No cars found." : "Search results displayed.");
    }

    private void updateCarField() {
        clearErrors();
        boolean isValid = true;

        // Validate Car ID
        String carIDText = updateCarIDField.getText().trim();
        int carID = 0;
        if (carIDText.isEmpty()) {
            updateCarIDError.setText("Car ID is required.");
            isValid = false;
        } else {
            try {
                carID = Integer.parseInt(carIDText);
                if (carID <= 0) {
                    updateCarIDError.setText("Car ID must be positive.");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                updateCarIDError.setText("Invalid Car ID.");
                isValid = false;
            }
        }

        // Validate Field to Update
        String field = updateField.getText().trim();
        if (field.isEmpty()) {
            updateFieldError.setText("Field to update is required.");
            isValid = false;
        } else if (!isValidField(field)) {
            updateFieldError.setText("Invalid field name.");
            isValid = false;
        }

        // Validate New Value
        String value = updateValue.getText().trim();
        if (value.isEmpty()) {
            updateValueError.setText("New value is required.");
            isValid = false;
        }

        if (isValid) {
            boolean success = carUpdate.updateCarField(carID, field, value);
            if (success) {
                messageLabel.setText("Car updated successfully.");
                loadData(carSearch.searchCars("", "", "", "", "", "", ""));
                clearUpdateCarFields();
            } else {
                messageLabel.setText("Error updating car. Please check the details.");
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    private void adjustPricesByAmount() {
        clearErrors();
        boolean isValid = true;

        String amountText = adjustAmountField.getText().trim();
        double amount = 0.0;
        if (amountText.isEmpty()) {
            adjustAmountError.setText("Amount is required.");
            isValid = false;
        } else {
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                adjustAmountError.setText("Invalid amount.");
                isValid = false;
            }
        }

        if (isValid) {
            boolean success = carPriceUpdate.adjustPricesByAmount(amount);
            if (success) {
                messageLabel.setText("Prices adjusted successfully.");
                loadData(carSearch.searchCars("", "", "", "", "", "", ""));
                clearAdjustAmountFields();
            } else {
                messageLabel.setText("Error adjusting prices.");
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    private void adjustPricesByPercentage() {
        clearErrors();
        boolean isValid = true;

        String percentageText = adjustPercentageField.getText().trim();
        double percentage = 0.0;
        if (percentageText.isEmpty()) {
            adjustPercentageError.setText("Percentage is required.");
            isValid = false;
        } else {
            try {
                percentage = Double.parseDouble(percentageText);
            } catch (NumberFormatException e) {
                adjustPercentageError.setText("Invalid percentage.");
                isValid = false;
            }
        }

        if (isValid) {
            boolean success = carPriceUpdate.adjustPricesByPercentage(percentage);
            if (success) {
                messageLabel.setText("Prices adjusted successfully.");
                loadData(carSearch.searchCars("", "", "", "", "", "", ""));
                clearAdjustPercentageFields();
            } else {
                messageLabel.setText("Error adjusting prices.");
            }
        } else {
            messageLabel.setText("Please correct the highlighted errors.");
        }
    }

    private void goBackToHome() {
        // Assuming HomePage is another JavaFX Application or Stage
        new HomePage().start(new Stage());
        primaryStage.close();
    }

    private void loadData(ObservableList<ObservableList<String>> data) {
        tableView.setItems(data);
    }

    private void setupTableColumns() {
        tableView.getColumns().clear(); // Clear existing columns if any
        String[] columns = {"CarID", "Make", "Model", "Year", "Price", "Stock", "VIN"};
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
        // Clear Add Car Errors
        if (carIDError != null) carIDError.setText("");
        if (makeError != null) makeError.setText("");
        if (modelError != null) modelError.setText("");
        if (yearError != null) yearError.setText("");
        if (priceError != null) priceError.setText("");
        if (stockError != null) stockError.setText("");
        if (vinError != null) vinError.setText("");

        // Clear Update Car Errors
        if (updateCarIDError != null) updateCarIDError.setText("");
        if (updateFieldError != null) updateFieldError.setText("");
        if (updateValueError != null) updateValueError.setText("");

        // Clear Update Price Errors
        if (adjustAmountError != null) adjustAmountError.setText("");
        if (adjustPercentageError != null) adjustPercentageError.setText("");
    }

    private boolean isValidField(String field) {
        // Define valid fields that can be updated
        String[] validFields = {"Make", "Model", "Year", "Price", "Stock", "VIN"};
        for (String validField : validFields) {
            if (validField.equalsIgnoreCase(field)) {
                return true;
            }
        }
        return false;
    }

    private void clearAddCarFields() {
        carIDField.clear();
        makeField.clear();
        modelField.clear();
        yearField.clear();
        priceField.clear();
        stockField.clear();
        vinField.clear();
    }

    private void clearUpdateCarFields() {
        updateCarIDField.clear();
        updateField.clear();
        updateValue.clear();
    }

    private void clearAdjustAmountFields() {
        adjustAmountField.clear();
    }

    private void clearAdjustPercentageFields() {
        adjustPercentageField.clear();
    }
}
