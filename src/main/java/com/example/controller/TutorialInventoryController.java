package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import static com.example.App.PlayButtonSound;

public class TutorialInventoryController {

    @FXML
    private StackPane tutorialDialog;

    @FXML
    private Label tutorialMessage;

    // Method to show the tutorial dialog
    private void showTutorialDialog(String message) {
        PlayButtonSound();
        tutorialMessage.setText(message);
        tutorialDialog.setVisible(true);
    }

    // Method to close the tutorial dialog
    @FXML
    private void closeTutorialDialog() {
        PlayButtonSound();
        tutorialDialog.setVisible(false);
    }

    // Methods for specific tutorial information
    @FXML
    private void showResourceInfo() {
        PlayButtonSound();
        showTutorialDialog("Resources are points you can use to buy items. Earn more points by keeping your pet alive.");
    }

    @FXML
    private void showInventoryInfo() {
        PlayButtonSound();
        showTutorialDialog("Your inventory contains items you’ve collected or purchased. Use them wisely to improve your pet’s stats!");
    }

    public void goBackTutorial(ActionEvent actionEvent) {
        PlayButtonSound();
        SceneController.getInstance().switchToTutorial();
    }

}
