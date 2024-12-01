package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

public class LoginController {

    @FXML
    private PasswordField pinField;

    @FXML
    private PasswordField confirmPinField;

    @FXML
    private StackPane pinErrorIcon;

    @FXML
    private StackPane confirmPinErrorIcon;

    @FXML
    private Label titleLabel;

    @FXML
    private Circle dot1, dot2, dot3, dot4, dot5, dot6;
    private Circle[] dots;
    private StringBuilder currentPin = new StringBuilder();

    private FileHandler fileHandler;
    private UserPreferences userPrefs;
    private Timeline fadeTimeline;
    private boolean isCreationMode = true;

    @FXML
    private HBox pinDotsContainer;

    private String firstPin = null;

    private boolean isShaking = false;

    @FXML
    public void initialize() {
        fileHandler = new FileHandler();
        try {
            userPrefs = fileHandler.loadPreferences();
            isCreationMode = userPrefs.getParentPassword().isEmpty();
            
            updateUIForMode();
        } catch (IOException e) {
            System.err.println("Failed to load preferences: " + e.getMessage());
            userPrefs = new UserPreferences();
        }

        dots = new Circle[]{dot1, dot2, dot3, dot4, dot5, dot6};
        
        // Initialize each dot
        for (Circle dot : dots) {
            dot.setRadius(6);
            dot.setFill(Color.valueOf("rgba(255, 255, 255, 0.3)"));
            dot.setStroke(Color.WHITE);
            dot.setStrokeWidth(1);
        }
    }

    private void updateUIForMode() {
        titleLabel.setText(isCreationMode ? "Create Parent PIN" : "Parent Login");
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
        if (!currentPin.toString().equals(userPrefs.getParentPassword())) {
            shakeDotsContainer();
            return;
        }
        SceneController.getInstance().switchToParentMenu();
    }

    private void handleCreate() {
        // Store the first PIN entry if we don't have it yet
        if (firstPin == null) {
            firstPin = currentPin.toString();
            titleLabel.setText("Confirm PIN");
            clearPin();
            return;
        }
        
        // Validate the confirmation PIN
        if (!firstPin.equals(currentPin.toString())) {
            shakeDotsContainer();
            titleLabel.setText("Create Parent PIN");
            firstPin = null;
            return;
        }
        
        userPrefs.setParentPassword(currentPin.toString());
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

    @FXML
    private void handlePinButton(ActionEvent event) {
        if (isShaking || currentPin.length() >= 6) return;
        
        CustomButton button = (CustomButton) event.getSource();
        String digit = button.getText();
        currentPin.append(digit);
        updatePinDots();
        
        if (currentPin.length() == 6) {
            validatePin();
        }
    }

    @FXML
    private void handleBackspace() {
        if (isShaking || currentPin.length() == 0) return;
        
        currentPin.setLength(currentPin.length() - 1);
        updatePinDots();
    }

    private void updatePinDots() {
        for (int i = 0; i < dots.length; i++) {
            if (i < currentPin.length()) {
                dots[i].setFill(Color.WHITE);
                // dots[i].setScaleX(1.2);
                // dots[i].setScaleY(1.2);
            } else {
                dots[i].setFill(Color.valueOf("rgba(255, 255, 255, 0.3)"));
                // dots[i].setScaleX(1.0);
                // dots[i].setScaleY(1.0);
            }
        }
    }

    private void validatePin() {
        if (isCreationMode) {
            handleCreate();
        } else {
            handleAuthentication();
        }
    }

    private void clearPin() {
        currentPin.setLength(0);
        updatePinDots();
    }

    private void shakeDotsContainer() {
        isShaking = true;
        
        // make all dots filled red since we only validate with 6 digits
        for (Circle dot : dots) {
            dot.setFill(Color.valueOf("rgba(231, 76, 60, 1)"));
            dot.setStroke(Color.valueOf("rgba(231, 76, 60, 1)"));
        }

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(pinDotsContainer.translateXProperty(), 0)),
            new KeyFrame(Duration.millis(100), new KeyValue(pinDotsContainer.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(200), new KeyValue(pinDotsContainer.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(300), new KeyValue(pinDotsContainer.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(400), new KeyValue(pinDotsContainer.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(500), new KeyValue(pinDotsContainer.translateXProperty(), -10)),
            new KeyFrame(Duration.millis(600), new KeyValue(pinDotsContainer.translateXProperty(), 10)),
            new KeyFrame(Duration.millis(700), new KeyValue(pinDotsContainer.translateXProperty(), 0))
        );
        
        timeline.setOnFinished(e -> {
            for (Circle dot : dots) {
                dot.setFill(Color.valueOf("rgba(255, 255, 255, 0.3)"));
                dot.setStroke(Color.WHITE);
            }
            clearPin();
            isShaking = false;
        });
        
        timeline.play();
    }
}
