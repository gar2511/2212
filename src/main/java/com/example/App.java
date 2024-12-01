package com.example;

import com.example.controller.SettingsController;
import com.example.util.FileHandler;
import com.example.util.SoundPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

// Main application class that initializes and launches the JavaFX application
public class App extends Application {

    private static SoundPlayer music;

    // Allow the program to access the global instance of sound player
    public static SoundPlayer getSoundPlayer() {
        return music;
    }

    // Helper Function
    private void initializeSound() {
        // Play and initialize background music
        music = new SoundPlayer();
        music.setFile(0);   // set to file 0 in the array, background music
        music.play();
        music.loop();
    }

    @Override
    public void start(Stage stage) {
        try {
            // Load the main menu FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_menu.fxml"));

            // Create the main scene with initial dimensions
            Scene scene = new Scene(loader.load(), 1000, 600);

            // Load and apply the CSS stylesheet for menu styling
            scene.getStylesheets().add(
                Objects.requireNonNull(
                    getClass().getResource("/styles/menu.css")
                ).toExternalForm()
            );

            // Configure the primary stage/window
            stage.setTitle("NEOPETS 2.0");  // Set window title
            stage.setScene(scene);          // Set the main scene
            stage.show();                   // Display the window

            initializeSound();

        } catch (IOException e) {
            // Log any errors that occur during startup
            e.printStackTrace();
            // TODO: Add proper error handling/user notification
        }
    }

    // Application entry point
    public static void main(String[] args) {
        launch();  // Launch the JavaFX application
    }
}
