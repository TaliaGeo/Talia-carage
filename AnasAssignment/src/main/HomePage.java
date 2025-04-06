package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomePage extends Application {

    @Override
    public void start(Stage primaryStage) {
      
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-image: url('file:///C:/Users/Admin/eclipse-workspace/AnasAssignment/images/Screenshot%202024-12-19%20205923.png'); " +
                "-fx-background-size: cover; " +
                "-fx-background-position: center; " +
                "-fx-background-repeat: no-repeat;");
     


      
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.TOP_CENTER);
        headerBox.setPadding(new Insets(50, 10, 20, 10));

        Label mainTitle = new Label("Talia's Garage");
        mainTitle.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 60));
        mainTitle.setStyle("-fx-text-fill: #FFD700;");

        Label subTitle = new Label("Welcome to Talia Garage, where we turn car care into an art form,\n" +
                "ensuring your vehicle is always in peak condition. Drive in with confidence, drive out with satisfaction!");
        subTitle.setFont(Font.font("Arial", FontPosture.REGULAR, 20));
        subTitle.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        subTitle.setWrapText(true);

        headerBox.getChildren().addAll(mainTitle, subTitle);

       
        VBox sidebar = new VBox(20);
        sidebar.setAlignment(Pos.CENTER);
        sidebar.setPadding(new Insets(70));
        sidebar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        sidebar.setPrefWidth(350);
     
        Button employeeButton = createNavButton("Manage Employees", false);
        employeeButton.setOnAction(e -> openEmployeeManagement());

        Button customerButton = createNavButton("Manage Customers", false);
        customerButton.setOnAction(e -> openCustomerManagement());

        Button carButton = createNavButton("Manage Cars", false);
        carButton.setOnAction(e -> openCarManagement());

        Button orderButton = createNavButton("Manage Orders", false);
        orderButton.setOnAction(e -> openOrderManagement());

        Button paymentButton = createNavButton("Manage Payments", false);
        paymentButton.setOnAction(e -> openPaymentManagement());

        Button servicesButton = createNavButton("Manage Services", false);
        servicesButton.setOnAction(e -> openServiceManagement());

       
        Label reportsLabel = new Label("Reports");
        reportsLabel.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");
        reportsLabel.setAlignment(Pos.CENTER);
        reportsLabel.setMaxWidth(Double.MAX_VALUE);
        reportsLabel.setPadding(new Insets(10));

       
        Button reportA = createNavButton("Customer Payment", true);
        reportA.setOnAction(e -> CustomerPayments());

        Button reportB = createNavButton("Sales By Employee", true);
        reportB.setOnAction(e -> salsByemployee());

        Button reportC = createNavButton("Service History", true);
        reportC.setOnAction(e -> ServiceHistory());

        Button reportD = createNavButton("Service Revenue", true);
        reportD.setOnAction(e -> ServiceRevinue());

        Button reportE = createNavButton("Service Frequency", true);
        reportE.setOnAction(e -> serviceFrequncy());

        Button reportF = createNavButton("Service Report", true);
        reportF.setOnAction(e -> serviceReport());

        // Add buttons to the sidebar
        sidebar.getChildren().addAll(
                employeeButton, customerButton, carButton, orderButton, paymentButton, servicesButton,
                reportsLabel, reportA, reportB, reportC, reportD, reportE, reportF
        );

        // Add header and sidebar to the layout
        root.setTop(headerBox);
        root.setLeft(sidebar);

        // Scene setup
        Scene scene = new Scene(root);
        primaryStage.setTitle("Talia's Garage - Home Page");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); 
        primaryStage.show();
    }

    /**
     * Creates a styled navigation button.
     *
     * @param title    The text to display on the button.
     * @param isReport Indicates if the button is for reports (affects styling).
     * @return A styled Button.
     */
    private Button createNavButton(String title, boolean isReport) {
        Button button = new Button(title);
        button.setFont(Font.font("Arial", FontPosture.ITALIC, 16)); // Italic text
        button.setStyle(isReport
                ? "-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 5;"
                : "-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setPrefWidth(280); // Adjusted width to fit within the sidebar
        button.setPrefHeight(isReport ? 40 : 60); // Smaller height for report buttons
        button.setMaxWidth(Double.MAX_VALUE); // Allow the button to stretch horizontally if needed
        return button;
    }

    // Navigation methods to open different management UIs
    private void openEmployeeManagement() {
        try {
            new EmployeesManagementUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Employee Management UI.");
        }
    }

    private void openCustomerManagement() {
        try {
            new CustomerManagementUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Customer Management UI.");
        }
    }

    private void openCarManagement() {
        try {
            new CarManagementUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Car Management UI.");
        }
    }

    private void openOrderManagement() {
        try {
            new OrdersManagementUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Orders Management UI.");
        }
    }

    private void openPaymentManagement() {
        try {
            new PaymentsManagementUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Payments Management UI.");
        }
    }

    private void openServiceManagement() {
        try {
            new ServicesManagementUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Services Management UI.");
        }
    }

    // Methods to open report UIs
    private void CustomerPayments() {
        try {
            new CustomerPaymentsReportUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Customer Payments Report UI.");
        }
    }

    private void salsByemployee() {
        try {
            new SalesByEmployeeReportUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Sales By Employee Report UI.");
        }
    }

    private void ServiceHistory() {
        try {
            new ServiceHistoryReportUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Service History Report UI.");
        }
    }

    private void ServiceRevinue() {
        try {
            new ServiceRevenueReportUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Service Revenue Report UI.");
        }
    }

    private void serviceFrequncy() {
        try {
            new ServiceFrequencyReportUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Service Frequency Report UI.");
        }
    }

    private void serviceReport() {
        try {
            new ServiceReportUI().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Service Report UI.");
        }
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   The title of the alert.
     * @param message The content message of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Changed to ERROR for consistency
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
