package com.example;

import com.example.util.SoundPlayer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.util.FileHandler;
import com.example.model.UserPreferences;

import java.io.IOException;
import java.util.Objects;

/**
 * Main application class that initializes and launches the JavaFX application.
 */
public class App extends Application {

    private static SoundPlayer music;  // Background music player
    private static SoundPlayer buttonSound;  // Button click sound player

    /**
     * Provides access to the global instance of the music sound player.
     *
     * @return the global music sound player instance.
     */
    public static SoundPlayer getSoundPlayer() {
        return music;
    }

    /**
     * Provides access to the global instance of the button sound player.
     *
     * @return the global button sound player instance.
     */
    public static SoundPlayer getButtonSound() {
        return buttonSound;
    }

    /**
     * Initializes the sound system, loading preferences and configuring
     * sound players for background music and button sounds.
     */
    private void initializeSound() {
        try {
            // Load preferences
            FileHandler fileHandler = new FileHandler();
            UserPreferences prefs = fileHandler.loadPreferences();

            // Initialize music sound player
            music = new SoundPlayer();
            music.setFile(0);

            // Apply saved volume before playing
            float savedVolume = (float) prefs.getVolume() / 100f;
            music.setVolume(savedVolume);

            // Play and loop background music
            music.play();
            music.loop();

            // Initialize button sound
            buttonSound = new SoundPlayer();
            buttonSound.setFile(1);  // Index 1 is water.wav

        } catch (IOException e) {
            // Handle case where preferences cannot be loaded
            System.err.println("Failed to load preferences: " + e.getMessage());

            // Initialize sound players with default settings
            music = new SoundPlayer();
            music.setFile(0);
            music.setVolume(0.5f);  // Default 50% volume
            music.play();
            music.loop();

            buttonSound = new SoundPlayer();
            buttonSound.setFile(1);
        }
    }

    /**
     * The entry point for the JavaFX application.
     * This method initializes the main menu scene and sets up the primary stage.
     *
     * @param stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        try {
            // Load the main menu FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_menu.fxml"));

            // Create the main scene with initial dimensions
            Scene scene = new Scene(loader.load(), 1600, 900);

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
            // Handle errors during startup
            e.printStackTrace();
            // TODO: Add proper error handling/user notification
        }
    }

    /**
     * Application entry point.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        launch();  // Launch the JavaFX application
    }

    /**
     * Plays the button click sound.
     * This method resets the button sound to the beginning and plays it at the current music volume.
     */
    public static void PlayButtonSound() {
        buttonSound.flush();
        buttonSound.setZeroPosition();

        // Get the current volume from the music player and apply it to button sound
        float currentVolume = music.getVolume();
        buttonSound.setVolume(currentVolume);
        buttonSound.play();
    }
}
