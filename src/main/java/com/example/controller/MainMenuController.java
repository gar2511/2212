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
     * Switches to the save menu scene.
     */
    @FXML
    private void startGame() {
        PlayButtonSound();
        SceneController.getInstance().switchToSaveMenu();
    }

    /**
     * Handles the "Settings" button action.
     * Switches to the settings menu scene.
     */
    @FXML
    private void openSettings() {
        PlayButtonSound();
        SceneController.getInstance().switchToSettings();
    }
    @FXML
    private void openTutorial() {
        PlayButtonSound();
        SceneController.getInstance().switchToTutorial();
    }

    /**
     * Handles the "Exit" button action.
     * Shows the exit confirmation dialog.
     */
    @FXML
    private void exitGame() {
        PlayButtonSound();
        exitDialog.setVisible(true);
    }

    /**
     * Handles the confirmation of exit.
     * Closes the application.
     */
    @FXML
    private void confirmExit() {
        PlayButtonSound();

        App.getButtonSound().stop();
        App.getSoundPlayer().stop();
        App.getButtonSound().close();
        App.getSoundPlayer().close();

        Platform.exit();
    }

    /**
     * Handles the cancellation of exit.
     * Hides the exit confirmation dialog.
     */
    @FXML
    private void cancelExit() {
        PlayButtonSound();
        exitDialog.setVisible(false);
    }

}
