package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button button = new Button("Hover me");
        button.getStyleClass().add("menu-button");

        StackPane root = new StackPane(button);
        Scene scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/test.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Test Transition");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}