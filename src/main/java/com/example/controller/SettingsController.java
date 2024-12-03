package com.example.controller;

import com.example.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import com.example.components.CustomButton;
import com.example.components.CustomSlider;
import com.example.model.UserPreferences;
import com.example.util.FileHandler;

import java.io.IOException;

import static com.example.App.PlayButtonSound;

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

        Platform.runLater(() -> {
            updateParentalControlsUI(userPrefs.isParentControlsEnabled());
            handleVolumeChange((int) userPrefs.getVolume());

            if (volumeSlider != null && volumeLabel != null) {
                volumeSlider.setValue(userPrefs.getVolume());
                volumeLabel.setText((int) userPrefs.getVolume() + "%");
            }

            if (parentalControlsToggle != null && parentalStatusLabel != null) {
                parentalControlsToggle.setSelected(userPrefs.isParentControlsEnabled());
                parentalStatusLabel.setText(userPrefs.isParentControlsEnabled() ? "Enabled" : "Disabled");
            }

            if (parentalControlsToggle != null) {
                parentalControlsToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    handleParentalControlsToggle(newVal);
                    parentalStatusLabel.setText(newVal ? "Enabled" : "Disabled");
                    userPrefs.setParentControlsEnabled(newVal);
                    savePreferences();
                });
            }

            if (volumeSlider != null) {
                volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                    handleVolumeChange(newVal.intValue());
                    volumeLabel.setText(newVal.intValue() + "%");
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
        coloredTrack = new Rectangle();
        coloredTrack.getStyleClass().add("colored-track");
        coloredTrack.setHeight(8);
        coloredTrack.setY(4);

        Node track = volumeSlider.lookup(".track");
        if (track instanceof StackPane) {
            ((StackPane) track).getChildren().add(coloredTrack);
            updateColoredTrack();

            volumeSlider.widthProperty().addListener((obs, oldVal, newVal) -> updateColoredTrack());
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                updateColoredTrack();
                if (volumeLabel != null) {
                    volumeLabel.setText(newVal.intValue() + "%");
                }
            });
        }
    }

    /**
     * Updates the width of the colored track based on the current volume slider value.
     */
    private void updateColoredTrack() {
        if (coloredTrack != null) {
            double width = volumeSlider.getWidth() - 16;
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
        PlayButtonSound();
        userPrefs.setParentControlsEnabled(enabled);
        updateParentalControlsUI(enabled);
        savePreferences();
        System.out.println("Parental controls " + (enabled ? "enabled" : "disabled"));
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
     * Saves the user preferences to the file.
     */
    private void savePreferences() {
        try {
            fileHandler.savePreferences(userPrefs);
        } catch (IOException e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }

    /**
     * Updates the UI based on the state of parental controls.
     *
     * @param enabled {@code true} if parental controls are enabled, {@code false} otherwise.
     */
    private void updateParentalControlsUI(boolean enabled) {
        if (enabled) {
            parentalStatusLabel.setText("Enabled");
            parentalControlsButton.setVisible(false);
            parentalControlsButton.setManaged(false);
            configureButton.setVisible(true);
            configureButton.setManaged(true);
        } else {
            if (userPrefs.getParentPassword().isEmpty()) {
                parentalStatusLabel.setText("No active profile");
                parentalControlsButton.setVisible(true);
                parentalControlsButton.setManaged(true);
                configureButton.setVisible(false);
                configureButton.setManaged(false);
            } else {
                parentalStatusLabel.setText("Disabled");
                parentalControlsButton.setVisible(false);
                parentalControlsButton.setManaged(false);
                configureButton.setVisible(true);
                configureButton.setManaged(true);
            }
        }
    }

    /**
     * Navigates back to the main menu.
     */
    @FXML
    private void goBack() {
        PlayButtonSound();
        SceneController.getInstance().switchToMainMenu();
    }

    /**
     * Navigates to the parental controls login screen.
     */
    @FXML
    private void goParent() {
        PlayButtonSound();
        SceneController.getInstance().switchToLoginParent();
    }

    /**
     * Handles the parental controls button action.
     * Navigates to the parental controls login screen.
     */
    @FXML
    private void handleParentalControls() {
        SceneController.getInstance().switchToLoginParent();
    }
}
