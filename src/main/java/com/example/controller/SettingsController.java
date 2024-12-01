package com.example.controller;

import com.example.App;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
//import javafx.scene.control.Slider;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;
import javafx.scene.Node;
import com.example.components.CustomButton;
import com.example.components.CustomSlider;
import com.example.components.CustomToggle;
import javafx.scene.control.Label;
import com.example.model.UserPreferences;
import com.example.util.FileHandler;
import java.io.IOException;

/**
 * Controller class for managing game settings and preferences.
 * Handles parental controls, volume adjustments, and user interactions with the settings menu.
 */
public class SettingsController {
    @FXML
    private ToggleButton parentalControlsToggle;

    @FXML
    private CustomSlider volumeSlider;

    @FXML
    private Label volumeLabel;

    @FXML
    private CustomButton parentalControlsButton;
    @FXML
    private CustomButton configureButton;
    @FXML
    private Label parentalStatusLabel;

    private Rectangle coloredTrack;
    private UserPreferences userPrefs;
    private FileHandler fileHandler;

    /**
     * Initializes the settings menu.
     * - Sets up listeners for parental controls and volume adjustments.
     * - Configures a custom colored track for the volume slider.
     */
    @FXML
    public void initialize() {
        fileHandler = new FileHandler();

        // Load preferences
        try {
            userPrefs = fileHandler.loadPreferences();
        } catch (IOException e) {
            System.err.println("Failed to load preferences: " + e.getMessage());
            userPrefs = new UserPreferences();
        }

        // Wrap all UI operations in Platform.runLater to ensure FXML elements are initialized
        Platform.runLater(() -> {
            updateParentalControlsUI(userPrefs.isParentControlsEnabled());

            // Set initial values from preferences
            if (volumeSlider != null && volumeLabel != null) {
                volumeSlider.setValue(userPrefs.getVolume());
                volumeLabel.setText((int)userPrefs.getVolume() + "%");
            }

            if (parentalControlsToggle != null && parentalStatusLabel != null) {
                parentalControlsToggle.setSelected(userPrefs.isParentControlsEnabled());
                parentalStatusLabel.setText(userPrefs.isParentControlsEnabled() ? "Enabled" : "Disabled");
            }

            // Add listeners for preferences changes
            if (parentalControlsToggle != null) {
                parentalControlsToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    handleParentalControlsToggle(newVal);
                    if (parentalStatusLabel != null) {
                        parentalStatusLabel.setText(newVal ? "Enabled" : "Disabled");
                    }
                    userPrefs.setParentControlsEnabled(newVal);
                    savePreferences();
                });
            }

            if (volumeSlider != null) {
                volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    handleVolumeChange(newVal.intValue());
                    if (volumeLabel != null) {
                        volumeLabel.setText(newVal.intValue() + "%");
                    }
                    userPrefs.setVolume(newVal.doubleValue());
                    savePreferences();
                });
            }

            setupVolumeSlider();
        });
    }

    /**
     * Configures a custom colored track on the volume slider.
     * The track visually represents the current volume level and updates dynamically.
     */
    private void setupVolumeSlider() {
        // Create a colored rectangle to represent the filled portion of the track
        coloredTrack = new Rectangle();
        coloredTrack.getStyleClass().add("colored-track");
        coloredTrack.setHeight(8);
        coloredTrack.setY(4);

        // Locate the track node within the slider
        Node track = volumeSlider.lookup(".track");
        if (track instanceof StackPane) {
            // Add the colored track as a child of the track node
            ((StackPane) track).getChildren().add(coloredTrack);

            // Perform the initial update of the colored track
            updateColoredTrack();

            // Add listeners to update the track when slider width or value changes
            volumeSlider.widthProperty().addListener((obs, oldVal, newVal) -> updateColoredTrack());
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                updateColoredTrack();
                // Only update the label text, not the track text
                if (volumeLabel != null) {
                    volumeLabel.setText(newVal.intValue() + "%");
                }
            });
        }
    }

    /**
     * Updates the width of the colored track based on the current volume slider value.
     * The track dynamically reflects the percentage of the slider's value.
     */
    private void updateColoredTrack() {
        if (coloredTrack != null) {
            double width = volumeSlider.getWidth() - 16; // Account for slider padding
            double percentage = (volumeSlider.getValue() - volumeSlider.getMin()) /
                    (volumeSlider.getMax() - volumeSlider.getMin());
            coloredTrack.setWidth(Math.max(0, width * percentage));
        }
    }

    /**
     * Handles changes to the parental controls toggle state.
     *
     * @param enabled {@code true} if parental controls are enabled, {@code false} otherwise.
     */
    private void handleParentalControlsToggle(boolean enabled) {
        if (parentalControlsToggle != null) {
            System.out.println("Parental controls " + (enabled ? "enabled" : "disabled"));
        }
    }

    /**
     * Handles changes to the volume slider value.
     *
     * @param volume The new volume level as an integer between the slider's minimum and maximum.
     */
    private void handleVolumeChange(int volume) {
        float value = (float) volume / 100f;
        App.getSoundPlayer().setVolume(value);
        System.out.println("Volume changed to: " + volume);
    }

    /**
     * Returns the user to the main menu.
     */
    @FXML
    private void goBack() {
        SceneController.getInstance().switchToMainMenu();
    }

    @FXML
    private void goParent(){ SceneController.getInstance().switchToLoginParent(); }

    private void savePreferences() {
        try {
            fileHandler.savePreferences(userPrefs);
        } catch (IOException e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }

    private void updateParentalControlsUI(boolean enabled) {
        parentalStatusLabel.setText(enabled ? "Enabled" : "Disabled");
        parentalControlsButton.setVisible(!enabled);
        configureButton.setVisible(enabled);
    }

    @FXML
    private void handleParentalControls() {
        SceneController.getInstance().switchToLoginParent();
    }
}
