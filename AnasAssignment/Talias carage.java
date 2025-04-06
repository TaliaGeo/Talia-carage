package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User Interface for user login.
 */
public class LoginUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-image: url('file:///C:/Users/Admin/eclipse-workspace/AnasAssignment/images/Screenshot%202024-12-17%20230403.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center;" +
                "-fx-background-repeat: no-repeat;");

        VBox loginBox = new VBox(10);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, black, 10, 0, 0, 0);");

        Label title = new Label("LOGIN");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: white;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);
        usernameField.setPrefHeight(40);
        usernameField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-padding: 8;");
        Label usernameError = new Label();
        usernameError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-padding: 8;");
        Label passwordError = new Label();
        passwordError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button loginButton = new Button("LOGIN");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        loginButton.setStyle("-fx-background-color: #B22222; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10 15;");

        loginButton.setOnAction(e -> {
            usernameError.setText("");
            passwordError.setText("");

            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty()) {
                usernameError.setText("Username cannot be empty.");
            } else if (password.isEmpty()) {
                passwordError.setText("Password cannot be empty.");
            } else {
                if (authenticate(username, password)) {
                    switchToHomePage(primaryStage);
                } else {
                    if (!isUsernameValid(username)) {
                        usernameError.setText("Username not found.");
                    } else {
                        passwordError.setText("Incorrect password.");
                    }
                }
            }
        });

        loginBox.getChildren().addAll(
                title,
                usernameField, usernameError,
                passwordField, passwordError,
                loginButton
        );

        root.getChildren().add(loginBox);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Car Company - Login");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Start maximized
        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM user_account WHERE username = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isUsernameValid(String username) {
        String query = "SELECT * FROM user_account WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void switchToHomePage(Stage primaryStage) {
        try {
            HomePage homePage = new HomePage();
            homePage.start(primaryStage);
            primaryStage.setFullScreen(true); // Make sure the home page is in full screen
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to navigate to Home Page.");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
