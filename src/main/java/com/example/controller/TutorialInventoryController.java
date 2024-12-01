package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TutorialInventoryController {

    @FXML
    private StackPane tutorialDialog;

    @FXML
    private Label tutorialMessage;

    // Method to show the tutorial dialog
    private void showTutorialDialog(String message) {
        tutorialMessage.setText(message);
        tutorialDialog.setVisible(true);
    }

    // Method to close the tutorial dialog
    @FXML
    private void closeTutorialDialog() {
        tutorialDialog.setVisible(false);
    }

    // Methods for specific tutorial information
    @FXML
    private void showResourceInfo() {
        showTutorialDialog("Resources are points you can use to buy items. Earn more points by keeping your pet alive.");
    }

    @FXML
    private void showInventoryInfo() {
        showTutorialDialog("Your inventory contains items you’ve collected or purchased. Use them wisely to improve your pet’s stats!");
    }

    public void goBackTutorial(ActionEvent actionEvent) {SceneController.getInstance().switchToTutorial();
    }

}
