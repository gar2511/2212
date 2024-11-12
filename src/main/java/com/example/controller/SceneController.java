package com.example.controller;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SceneController {
    private static SceneController instance;
    
    private SceneController() {}
    
    public static SceneController getInstance() {
        if (instance == null) {
            instance = new SceneController();
        }
        return instance;
    }

    public void switchToGame() {
        loadFXML("game.fxml");
    }

    private void loadFXML(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/" + fxml));
            Stage stage = (Stage) getCurrentScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxml);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Scene getCurrentScene() {
        return Stage.getWindows().stream()
                .filter(Window::isShowing)
                .map(window -> ((Stage) window).getScene())
                .findFirst()
                .orElse(null);
    }
}
