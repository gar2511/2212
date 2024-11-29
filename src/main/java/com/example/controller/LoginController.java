package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import java.io.IOException;
import com.example.util.FileHandler;
import com.example.model.UserPreferences;
import com.example.components.CustomButton;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label userError;

    @FXML
    private Label passwordError;

    @FXML
    private CustomButton loginButton;

    private FileHandler fileHandler;
    private UserPreferences userPrefs;
    private Timeline fadeTimeline;

    @FXML
    public void initialize() {
        fileHandler = new FileHandler();
        try {
            userPrefs = fileHandler.loadPreferences();
        } catch (IOException e) {
            System.err.println("Failed to load preferences: " + e.getMessage());
            userPrefs = new UserPreferences();
        }

        // setup fade timeline
        fadeTimeline = new Timeline();
        fadeTimeline.setAutoReverse(false);

        // add listeners to validate input fields
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
    }

    private void validateInputs() {
        boolean isValid = !usernameField.getText().trim().isEmpty() 
                      && !passwordField.getText().trim().isEmpty();
        
        // stop any running animation
        fadeTimeline.stop();
        
        // setup new animation
        fadeTimeline.getKeyFrames().clear();
        KeyFrame keyFrame;
        if (isValid) {
            // fade in
            keyFrame = new KeyFrame(Duration.millis(300),
                new KeyValue(loginButton.opacityProperty(), 1.0));
        } else {
            // fade out
            keyFrame = new KeyFrame(Duration.millis(300),
                new KeyValue(loginButton.opacityProperty(), 0.7));
        }
        
        fadeTimeline.getKeyFrames().add(keyFrame);
        fadeTimeline.play();
        
        loginButton.setDisable(!isValid);
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