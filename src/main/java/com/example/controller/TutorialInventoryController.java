package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import static com.example.App.PlayButtonSound;

/**
 * Controller class for managing the inventory tutorial.
 * Provides information about resources, inventory, and related gameplay mechanics.
 */
public class TutorialInventoryController {

    @FXML
    private StackPane tutorialDialog;

    @FXML
    private Label tutorialMessage;

    /**
     * Displays the tutorial dialog with the specified message.
     *
     * @param message The message to display in the tutorial dialog.
     */
    private void showTutorialDialog(String message) {
        PlayButtonSound();
        tutorialMessage.setText(message);
        tutorialDialog.setVisible(true);
    }

    /**
     * Closes the currently visible tutorial dialog.
     */
    @FXML
    private void closeTutorialDialog() {
        PlayButtonSound();
        tutorialDialog.setVisible(false);
    }

    /**
     * Shows the tutorial dialog explaining resources.
     */
    @FXML
    private void showResourceInfo() {
        PlayButtonSound();
        showTutorialDialog("Resources are points you can use to buy items. Earn more points by keeping your pet alive.");
    }

    /**
     * Shows the tutorial dialog explaining inventory management.
     */
    @FXML
    private void showInventoryInfo() {
        PlayButtonSound();
        showTutorialDialog("Your inventory contains items you’ve collected or purchased. Use them wisely to improve your pet’s stats!");
    }

    /**
     * Navigates back to the main tutorial screen.
     *
     * @param actionEvent The event triggered by the back button action.
     */
    public void goBackTutorial(ActionEvent actionEvent) {
        PlayButtonSound();
        SceneController.getInstance().switchToTutorial();
    }
}
