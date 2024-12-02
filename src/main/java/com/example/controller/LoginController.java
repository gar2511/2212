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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.Node;

import static com.example.App.PlayButtonSound;

/**
 * Controller class responsible for handling the login and PIN creation process.
 * Supports both login (authentication) and PIN creation modes.
 */
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

    @FXML
    private HBox pinDotsContainer;

    private Circle[] dots;
    private StringBuilder currentPin = new StringBuilder();

    private FileHandler fileHandler;
    private UserPreferences userPrefs;
    private Timeline fadeTimeline;
    private boolean isCreationMode = true;
    private String firstPin = null;
    private boolean isShaking = false;

    /**
     * Initializes the controller after the FXML file is loaded.
     * Sets up the UI based on the mode (PIN creation or login) and initializes event listeners.
     */
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

        // Initialize each dot with default styling
        for (Circle dot : dots) {
            dot.setRadius(6);
            dot.setFill(Color.valueOf("rgba(255, 255, 255, 0.3)"));
            dot.setStroke(Color.WHITE);
            dot.setStrokeWidth(1);
        }

        // Add key event handler for the pin input
        pinDotsContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::handleKeyPress);
            }
        });
    }

    /**
     * Updates the UI based on whether the controller is in creation or login mode.
     */
    private void updateUIForMode() {
        titleLabel.setText(isCreationMode ? "Create Parent PIN" : "Parent Login");
    }

    /**
     * Handles the login button click event.
     * Determines the appropriate action based on the current mode (creation or login).
     */
    @FXML
    private void handleLogin() {
        if (isCreationMode) {
            handleCreate();
        } else {
            handleAuthentication();
        }
    }

    /**
     * Handles PIN authentication.
     * If the entered PIN does not match the stored PIN, shakes the input field to indicate an error.
     */
    private void handleAuthentication() {
        if (!currentPin.toString().equals(userPrefs.getParentPassword())) {
            shakeDotsContainer();
            return;
        }
        SceneController.getInstance().switchToParentMenu();
    }

    /**
     * Handles PIN creation, including confirming the PIN and saving it to preferences.
     */
    private void handleCreate() {
        if (firstPin == null) {
            firstPin = currentPin.toString();
            titleLabel.setText("Confirm PIN");
            clearPin();
            return;
        }

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

    /**
     * Navigates back to the previous screen.
     */
    @FXML
    private void goBack() {
        PlayButtonSound();
        SceneController.getInstance().switchToSettings();
    }

    /**
     * Handles button clicks for entering digits in the PIN.
     *
     * @param event The ActionEvent triggered by the button click.
     */
    @FXML
    private void handlePinButton(ActionEvent event) {
        PlayButtonSound();
        if (isShaking || currentPin.length() >= 6) return;

        CustomButton button = (CustomButton) event.getSource();
        String digit = button.getText();
        currentPin.append(digit);
        updatePinDots();

        if (currentPin.length() == 6) {
            validatePin();
        }
    }

    /**
     * Handles the backspace key for PIN input.
     */
    @FXML
    private void handleBackspace() {
        PlayButtonSound();
        if (isShaking || currentPin.length() == 0) return;

        currentPin.setLength(currentPin.length() - 1);
        updatePinDots();
    }

    /**
     * Updates the PIN dot indicators based on the length of the entered PIN.
     */
    private void updatePinDots() {
        for (int i = 0; i < dots.length; i++) {
            if (i < currentPin.length()) {
                dots[i].setFill(Color.WHITE);
            } else {
                dots[i].setFill(Color.valueOf("rgba(255, 255, 255, 0.3)"));
            }
        }
    }

    /**
     * Validates the entered PIN based on the current mode.
     */
    private void validatePin() {
        if (isCreationMode) {
            handleCreate();
        } else {
            handleAuthentication();
        }
    }

    /**
     * Clears the entered PIN and updates the PIN dot indicators.
     */
    private void clearPin() {
        currentPin.setLength(0);
        updatePinDots();
    }

    /**
     * Shakes the PIN dot container to indicate an error.
     */
    private void shakeDotsContainer() {
        isShaking = true;

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
                new KeyFrame(Duration.millis(500), new KeyValue(pinDotsContainer.translateXProperty(), 0))
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

    /**
     * Handles key presses for PIN input and backspace.
     *
     * @param event The KeyEvent triggered by the user.
     */
    private void handleKeyPress(KeyEvent event) {
        if (isShaking) return;

        String key = event.getText();
        if (key.matches("[0-9]")) {
            if (currentPin.length() < 6) {
                currentPin.append(key);
                updatePinDots();
                if (currentPin.length() == 6) {
                    validatePin();
                }
            }
        } else if (event.getCode() == KeyCode.BACK_SPACE) {
            if (currentPin.length() > 0) {
                currentPin.setLength(currentPin.length() - 1);
                updatePinDots();
            }
        }
    }
}
