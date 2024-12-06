package com.example.util;

import com.example.model.GameState;
import com.example.model.UserPreferences;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Utility class for managing game save files and user preferences.
 * Handles JSON serialization and deserialization for game state and preferences.
 */
public class FileHandler {

    // Directory where all save files will be stored
    private static final String SAVES_DIR = "saves";

    // File name for user preferences
    private static final String PREFS_FILE = "preferences.json";

    // ObjectMapper instance for JSON serialization/deserialization
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new {@code FileHandler} and ensures the saves directory exists.
     * Registers the {@link JavaTimeModule} to handle {@link java.time.LocalDateTime} serialization.
     */
    public FileHandler() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        createSavesDirectory();
    }

    /**
     * Creates the saves directory if it does not already exist.
     * Ensures the file structure required for saving and loading files is present.
     */
    private void createSavesDirectory() {
        File savesDir = new File(SAVES_DIR);
        if (!savesDir.exists()) {
            boolean created = savesDir.mkdirs();
            if (created) {
                System.out.println("Created saves directory: " + savesDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create saves directory: " + savesDir.getAbsolutePath());
            }
        }
    }

    /**
     * Saves a game state to a JSON file in the saves directory.
     *
     * @param saveName The name of the save file (without extension).
     * @param state    The {@link GameState} object to save.
     * @throws IOException if the save operation fails.
     */
    public void saveGame(String saveName, GameState state) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        objectMapper.writeValue(savePath.toFile(), state);
    }

    /**
     * Loads a game state from a JSON file in the saves directory.
     *
     * @param saveName The name of the save file (without extension).
     * @return The loaded {@link GameState} object.
     * @throws IOException if the load operation fails.
     */
    public GameState loadGame(String saveName) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        return objectMapper.readValue(savePath.toFile(), GameState.class);
    }

    /**
     * Deletes a save file from the saves directory if it exists.
     *
     * @param saveName The name of the save file (without extension).
     * @throws IOException if the delete operation fails.
     */
    public void deleteSave(String saveName) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        Files.deleteIfExists(savePath);
    }

    /**
     * Retrieves an array of all save files in the saves directory.
     *
     * @return An array of {@link File} objects representing save files, or {@code null} if the directory is empty.
     */
    public File[] getSaveFiles() {
        File savesDir = new File(SAVES_DIR);
        File[] files = savesDir.listFiles((dir, name) -> name.endsWith(".json"));
        System.out.println("Found save files: " + (files != null ? Arrays.toString(files) : "null"));
        return files;
    }

    /**
     * Saves user preferences to a JSON file.
     *
     * @param preferences The {@link UserPreferences} object to save.
     * @throws IOException if the save operation fails.
     */
    public void savePreferences(UserPreferences preferences) throws IOException {
        File preferencesFile = new File(SAVES_DIR, PREFS_FILE);
        objectMapper.writeValue(preferencesFile, preferences);
    }

    /**
     * Loads user preferences from a JSON file.
     * If the file does not exist or is corrupted, default preferences are returned.
     *
     * @return The loaded {@link UserPreferences} object, or default preferences if the file is missing or corrupted.
     * @throws IOException if an error occurs during loading.
     */
    public UserPreferences loadPreferences() throws IOException {
        File preferencesFile = new File(SAVES_DIR, PREFS_FILE);
        if (!preferencesFile.exists()) {
            return new UserPreferences(); // Return default preferences if file does not exist
        }

        try {
            return objectMapper.readValue(preferencesFile, UserPreferences.class);
        } catch (IOException e) {
            System.err.println("Error loading preferences: " + e.getMessage());
            preferencesFile.delete(); // Delete corrupted file
            return new UserPreferences();
        }
    }
}
