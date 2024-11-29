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

// Utility class for managing game save files and persistence
public class FileHandler {
    // Directory where all save files will be stored
    private static final String SAVES_DIR = "saves";
    
    // ObjectMapper instance for JSON serialization/deserialization
    private final ObjectMapper objectMapper;

    // Constructor initializes JSON mapper and ensures save directory exists
    public FileHandler() {
        objectMapper = new ObjectMapper();
        // Register JavaTimeModule to handle LocalDateTime serialization
        objectMapper.registerModule(new JavaTimeModule());
        createSavesDirectory();
    }

    // Creates the saves directory if it doesn't exist
    // Called during initialization to ensure proper file structure
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

    // Saves a game state to a JSON file
    // @param saveName The name of the save file (without extension)
    // @param state The GameState object to save
    // @throws IOException if the save operation fails
    public void saveGame(String saveName, GameState state) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        objectMapper.writeValue(savePath.toFile(), state);
    }

    // Loads a game state from a JSON file
    // @param saveName The name of the save file (without extension)
    // @return The loaded GameState object
    // @throws IOException if the load operation fails
    public GameState loadGame(String saveName) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        return objectMapper.readValue(savePath.toFile(), GameState.class);
    }

    // Deletes a save file if it exists
    // @param saveName The name of the save file (without extension)
    // @throws IOException if the delete operation fails
    public void deleteSave(String saveName) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        Files.deleteIfExists(savePath);
    }

    // Gets an array of all save files in the saves directory
    // @return Array of File objects representing save files, or null if directory is empty
    public File[] getSaveFiles() {
        File savesDir = new File(SAVES_DIR);
        File[] files = savesDir.listFiles((dir, name) -> name.endsWith(".json"));
        System.out.println("Found save files: " + (files != null ? Arrays.toString(files) : "null"));
        return files;
    }

    private static final String PREFS_FILE = "preferences.json";

    public void savePreferences(UserPreferences prefs) throws IOException {
        createSavesDirectory(); // Ensure directory exists
        Path prefsPath = Paths.get(SAVES_DIR, PREFS_FILE);
        objectMapper.writeValue(prefsPath.toFile(), prefs);
    }

    public UserPreferences loadPreferences() throws IOException {
        Path prefsPath = Paths.get(SAVES_DIR, PREFS_FILE);
        if (Files.exists(prefsPath)) {
            return objectMapper.readValue(prefsPath.toFile(), UserPreferences.class);
        }
        return new UserPreferences(); // return default preferences if file doesn't exist
    }
}