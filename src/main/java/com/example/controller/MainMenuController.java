package com.example.controller;

import com.example.App;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import com.example.components.CustomButton;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

import static com.example.App.PlayButtonSound;

/**
 * Controller class for the main menu interface.
 * Manages scene transitions and user interactions on the main menu screen.
 */
public class MainMenuController {

    @FXML
    private CustomButton loadGameButton;

    @FXML
    private CustomButton settingsButton;

    @FXML
    private CustomButton exitButton;

    @FXML
    private StackPane exitDialog;

    /**
     * Handles the "Load Game" button action.
     * Plays a button sound and switches to the save menu scene.
     */
    @FXML
    private void startGame() {
        PlayButtonSound();
        SceneController.getInstance().switchToSaveMenu();
    }

    /**
     * Handles the "Settings" button action.
     * Plays a button sound and switches to the settings menu scene.
     */
    @FXML
    private void openSettings() {
        PlayButtonSound();
        SceneController.getInstance().switchToSettings();
    }

    /**
     * Handles the "Tutorial" button action.
     * Plays a button sound and switches to the tutorial scene.
     */
    @FXML
    private void openTutorial() {
        PlayButtonSound();
        SceneController.getInstance().switchToTutorial();
    }

    /**
     * Handles the "Exit" button action.
     * Plays a button sound and displays the exit confirmation dialog.
     */
    @FXML
    private void exitGame() {
        PlayButtonSound();
        exitDialog.setVisible(true);
    }

    /**
     * Handles the confirmation of exit.
     * Stops all audio players and closes the application.
     */
    @FXML
    private void confirmExit() {
        PlayButtonSound();

        // Stop and close audio resources
        App.getButtonSound().stop();
        App.getSoundPlayer().stop();
        App.getButtonSound().close();
        App.getSoundPlayer().close();

        // Exit the application
        Platform.exit();
    }

    /**
     * Handles the cancellation of the exit process.
     * Plays a button sound and hides the exit confirmation dialog.
     */
    @FXML
    private void cancelExit() {
        PlayButtonSound();
        exitDialog.setVisible(false);
    }
}
