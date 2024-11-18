package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingsController {
    @FXML
    private Button backButton;

    @FXML
    private void goBack() {
        SceneController.getInstance().switchToMainMenu();
    }
}
