package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainMenuController {

    @FXML
    private void startNewGame() {
        SceneController.getInstance().switchToGame();
    }

    @FXML
    private void loadGame() {

    }

    @FXML
    private void openSettings() {

    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }

}
