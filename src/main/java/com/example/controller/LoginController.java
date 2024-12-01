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
import javafx.scene.layout.VBox;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label userError;

    @FXML
    private Label passwordError;

    @FXML
    private Label confirmPasswordError;

    @FXML
    private CustomButton loginButton;

    @FXML
    private StackPane userErrorIcon;

    @FXML
    private StackPane passwordErrorIcon;

    @FXML
    private StackPane confirmPasswordErrorIcon;

    @FXML
    private Label titleLabel;

    private FileHandler fileHandler;
    private UserPreferences userPrefs;
    private Timeline fadeTimeline;
    private boolean isCreationMode = true;

    @FXML
    public void initialize() {
        fileHandler = new FileHandler();
        try {
            userPrefs = fileHandler.loadPreferences();
            isCreationMode = !userPrefs.isParentControlsEnabled();
            
            updateUIForMode();
        } catch (IOException e) {
            System.err.println("Failed to load preferences: " + e.getMessage());
            userPrefs = new UserPreferences();
        }

        // Initialize button as disabled
        loginButton.setDisable(true);
        loginButton.setOpacity(0.5);

        // setup fade timeline
        fadeTimeline = new Timeline();
        fadeTimeline.setAutoReverse(false);

        // add listeners to validate input fields
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        if (isCreationMode) {
            confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> validateInputs());
        }

        // Initial validation
        validateInputs();
    }

    private void updateUIForMode() {
        titleLabel.setText(isCreationMode ? "Create Parent Account" : "Parent Login");
        loginButton.setText(isCreationMode ? "CREATE" : "LOGIN");
        
        // Hide both the field and its container
        confirmPasswordField.setVisible(isCreationMode);
        confirmPasswordField.setManaged(isCreationMode);
        ((VBox) confirmPasswordField.getParent().getParent()).setVisible(isCreationMode);
        ((VBox) confirmPasswordField.getParent().getParent()).setManaged(isCreationMode);
    }

    private void validateInputs() {
        boolean isValid = !usernameField.getText().trim().isEmpty() 
                      && !passwordField.getText().trim().isEmpty()
                      && (!isCreationMode || !confirmPasswordField.getText().trim().isEmpty());
        
        // stop any running animation
        fadeTimeline.stop();
        
        // setup new animation
        fadeTimeline.getKeyFrames().clear();
        KeyFrame keyFrame = new KeyFrame(
            Duration.millis(300),
            new KeyValue(loginButton.opacityProperty(), isValid ? 1.0 : 0.5, Interpolator.EASE_BOTH)
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
        if (isCreationMode) {
            handleCreate();
        } else {
            handleAuthentication();
        }
    }

    private void handleAuthentication() {
        // Reset error states
        userErrorIcon.setOpacity(0);
        passwordErrorIcon.setOpacity(0);
        usernameField.getStyleClass().remove("error");
        passwordField.getStyleClass().remove("error");

        // First check username
        if (!usernameField.getText().equals(userPrefs.getParentUsername())) {
            shakeField(usernameField, userErrorIcon);
            shakeField(passwordField, passwordErrorIcon);
            return;
        }
        
        // If username is correct, then check password
        if (!passwordField.getText().equals(userPrefs.getParentPassword())) {
            shakeField(passwordField, passwordErrorIcon);
            return;
        }
        
        // Both are correct
        SceneController.getInstance().switchToParentMenu();
    }

    private void handleCreate() {
        // Reset error states
        passwordErrorIcon.setOpacity(0);
        confirmPasswordErrorIcon.setOpacity(0);
        passwordField.getStyleClass().remove("error");
        confirmPasswordField.getStyleClass().remove("error");
        
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            shakeField(passwordField, passwordErrorIcon);
            shakeField(confirmPasswordField, confirmPasswordErrorIcon);
            return;
        }
        
        // Save the new credentials
        userPrefs.setParentUsername(usernameField.getText());
        userPrefs.setParentPassword(passwordField.getText());
        userPrefs.setParentControlsEnabled(true);
        
        try {
            fileHandler.savePreferences(userPrefs);
            SceneController.getInstance().switchToParentMenu();
        } catch (IOException e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() {
        System.out.println("Returning to the previous screen...");
        SceneController.getInstance().switchToSettings();
        // Logic to switch to the previous screen
    }
}
