package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import javafx.animation.Interpolator;
import java.io.IOException;
import com.example.util.FileHandler;
import com.example.model.UserPreferences;
import com.example.components.CustomButton;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextInputControl;

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

    @FXML
    private StackPane userErrorIcon;

    @FXML
    private StackPane passwordErrorIcon;

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
        KeyFrame keyFrame = new KeyFrame(
            Duration.millis(300),
            new KeyValue(loginButton.opacityProperty(), isValid ? 1.0 : 0.7)
        );
        
        fadeTimeline.getKeyFrames().add(keyFrame);
        fadeTimeline.play();
        
        loginButton.setDisable(!isValid);
    }

    private void shakeField(TextInputControl field, StackPane errorIcon) {
        field.getStyleClass().add("error");
        errorIcon.setOpacity(1);
        
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(field.translateXProperty(), 0)),
            new KeyFrame(Duration.millis(100), new KeyValue(field.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(200), new KeyValue(field.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(300), new KeyValue(field.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(400), new KeyValue(field.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(500), new KeyValue(field.translateXProperty(), 0))
        );
        timeline.play();
    }

    @FXML
    private void handleLogin() {
        // Reset error states
        userErrorIcon.setOpacity(0);
        passwordErrorIcon.setOpacity(0);
        usernameField.getStyleClass().remove("error");
        passwordField.getStyleClass().remove("error");

        boolean valid = true;
        if (!usernameField.getText().equals(userPrefs.getParentUsername())) {
            shakeField(usernameField, userErrorIcon);
            valid = false;
        }

        if (!passwordField.getText().equals(userPrefs.getParentPassword())) {
            shakeField(passwordField, passwordErrorIcon);
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