package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import com.example.util.FileHandler;
import com.example.model.UserPreferences;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label userError;

    @FXML
    private Label passwordError;

    private FileHandler fileHandler;
    private UserPreferences userPrefs;

    @FXML
    public void initialize() {
        fileHandler = new FileHandler();
        try {
            userPrefs = fileHandler.loadPreferences();
        } catch (IOException e) {
            System.err.println("Failed to load preferences: " + e.getMessage());
            userPrefs = new UserPreferences();
        }
    }

    @FXML
    private void handleLogin() {
        // Clear previous error messages
        userError.setVisible(false);
        passwordError.setVisible(false);

        // Validate user input
        boolean valid = true;
        if (!usernameField.getText().equals(userPrefs.getParentUsername())) {
            userError.setVisible(true);
            valid = false;
        }

        if (!passwordField.getText().equals(userPrefs.getParentPassword())) {
            passwordError.setVisible(true);
            valid = false;
        }

        if (valid) {
            SceneController.getInstance().switchToParentMenu();
        }
    }

    @FXML
    private void goBack() {
        System.out.println("Returning to the previous screen...");
        SceneController.getInstance().switchToSettings();
        // Logic to switch to the previous screen
    }
}