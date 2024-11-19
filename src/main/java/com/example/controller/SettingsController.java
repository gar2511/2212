package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

// Controller class for managing game settings and preferences
public class SettingsController {
    // FXML injected toggle button for parental controls
    @FXML
    private ToggleButton parentalControlsToggle;

    // Initializes the controller and sets up UI element listeners
    @FXML
    public void initialize() {
        // Add listener to handle state changes of the parental controls toggle
        parentalControlsToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            handleParentalControlsToggle(newVal);
        });
    }

    // Handles the toggling of parental controls
    // @param enabled true if parental controls should be enabled, false otherwise
    private void handleParentalControlsToggle(boolean enabled) {
        // TODO: implement parental controls functionality
        // Currently just logs the state change
        System.out.println("Parental controls " + (enabled ? "enabled" : "disabled"));
    }

    // Event handler for the back button
    // Returns the user to the main menu
    @FXML
    private void goBack() {
        SceneController.getInstance().switchToMainMenu();
    }
}
