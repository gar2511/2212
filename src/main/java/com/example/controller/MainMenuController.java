package com.example.controller;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

/**
 * Controller class for the main menu interface.
 * Manages scene transitions and user interactions on the main menu screen.
 */
public class MainMenuController {

    @FXML
    private StackPane exitDialog;

    /**
     * Handles the "Load Game" button action.
     * Switches to the save menu scene.
     */
    @FXML
    private void startGame() {
        SceneController.getInstance().switchToSaveMenu();
    }

    /**
     * Handles the "Settings" button action.
     * Switches to the settings menu scene.
     */
    @FXML
    private void openSettings() {
        SceneController.getInstance().switchToSettings();
    }

    /**
     * Handles the "Exit" button action.
     * Shows the exit confirmation dialog.
     */
    @FXML
    private void exitGame() {
        exitDialog.setVisible(true);
    }

    /**
     * Handles the confirmation of exit.
     * Closes the application.
     */
    @FXML
    private void confirmExit() {
        Platform.exit();
    }

    /**
     * Handles the cancellation of exit.
     * Hides the exit confirmation dialog.
     */
    @FXML
    private void cancelExit() {
        exitDialog.setVisible(false);
    }
}
