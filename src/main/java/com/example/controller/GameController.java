package com.example.controller;

import javafx.fxml.FXML;

// Controller class responsible for managing the game scene/view
public class GameController {

    // Constructor - called when the game scene is initialized
    public GameController() {
        System.out.println("GameController initialized");
    }

    // Event handler for the back button
    // Returns the user to the main menu when clicked
    @FXML
    private void goBack() {
        SceneController.getInstance().switchToMainMenu();
    }
}
