package com.example.controller;

import javafx.fxml.FXML;

public class GameController {

    public GameController() {
        System.out.println("GameController initialized");
    }

    @FXML
    private void goBack() {
        SceneController.getInstance().switchToMainMenu();
    }
}
