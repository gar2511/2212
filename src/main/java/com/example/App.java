package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10); // 10 is spacing between elements
        root.getStyleClass().add("root");
        
        Button button = new Button("Click Me!");
        button.getStyleClass().add("custom-button");
        
        root.getChildren().add(button);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/main.css")).toExternalForm());
        
        stage.setTitle("JavaFX Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
} 