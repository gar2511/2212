package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import javafx.scene.effect.ColorAdjust;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

public class MainMenuController {
    @FXML
    private Button newGameButton;
    @FXML
    private Button loadGameButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        // Create hover animations for each button
        setupHoverAnimation(newGameButton);
        setupHoverAnimation(loadGameButton);
        setupHoverAnimation(settingsButton);
        setupHoverAnimation(exitButton);
    }

    private void setupHoverAnimation(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), button);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        button.setEffect(colorAdjust);

        Timeline colorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.brightnessProperty(), 0)),
                new KeyFrame(Duration.millis(100), new KeyValue(colorAdjust.brightnessProperty(), -0.15))
        );

        Timeline reverseColorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.brightnessProperty(), -0.15)),
                new KeyFrame(Duration.millis(100), new KeyValue(colorAdjust.brightnessProperty(), 0))
        );

        button.setOnMouseEntered(e -> {
            scaleUp.play();
            colorTimeline.play();
        });

        button.setOnMouseExited(e -> {
            scaleDown.play();
            reverseColorTimeline.play();
        });
    }

    @FXML
    private void startNewGame() {
        SceneController.getInstance().switchToGame();
    }

    @FXML
    private void loadGame() {
        SceneController.getInstance().switchToSaveMenu();
    }

    @FXML
    private void openSettings() {
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }
}
