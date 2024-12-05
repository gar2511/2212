package com.example.util;

import com.example.model.GameState;
import com.example.model.UserPreferences;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private FileHandler fileHandler;
    private static final String TEST_SAVE_NAME = "test_save";
    private static final String TEST_PREFS_NAME = "preferences.json";

    @BeforeEach
    void setUp() {
        fileHandler = new FileHandler();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up save files and preferences after each test
        File[] saveFiles = fileHandler.getSaveFiles();
        if (saveFiles != null) {
            for (File file : saveFiles) {
                file.delete();
            }
        }

        File preferencesFile = new File("saves", TEST_PREFS_NAME);
        if (preferencesFile.exists()) {
            preferencesFile.delete();
        }
    }

    @Test
    void testSaveAndLoadGame() throws IOException {
        // Create a mock GameState
        GameState gameState = new GameState();
        gameState.setParentControlsEnabled(true);

        // Save the game state
        fileHandler.saveGame(TEST_SAVE_NAME, gameState);

        // Verify the file exists
        File saveFile = new File("saves", TEST_SAVE_NAME + ".json");
        assertTrue(saveFile.exists(), "Save file should exist after saving.");

        // Load the game state
        GameState loadedState = fileHandler.loadGame(TEST_SAVE_NAME);
        assertNotNull(loadedState, "Loaded game state should not be null.");
        assertTrue(loadedState.isParentControlsEnabled(), "Loaded game state should have parental controls enabled.");
    }

    @Test
    void testDeleteSave() throws IOException {
        // Create and save a mock GameState
        GameState gameState = new GameState();
        fileHandler.saveGame(TEST_SAVE_NAME, gameState);

        // Verify the file exists
        File saveFile = new File("saves", TEST_SAVE_NAME + ".json");
        assertTrue(saveFile.exists(), "Save file should exist before deletion.");

        // Delete the save file
        fileHandler.deleteSave(TEST_SAVE_NAME);

        // Verify the file does not exist
        assertFalse(saveFile.exists(), "Save file should not exist after deletion.");
    }

    @Test
    void testGetSaveFiles() throws IOException {
        // Save multiple mock game states
        fileHandler.saveGame(TEST_SAVE_NAME + "1", new GameState());
        fileHandler.saveGame(TEST_SAVE_NAME + "2", new GameState());

        // Retrieve save files
        File[] saveFiles = fileHandler.getSaveFiles();
        assertNotNull(saveFiles, "Save files array should not be null.");
        assertEquals(2, saveFiles.length, "There should be 2 save files in the saves directory.");
    }

    @Test
    void testSaveAndLoadPreferences() throws IOException {
        // Create and save user preferences
        UserPreferences preferences = new UserPreferences();
        preferences.setVolume(80.0);
        preferences.setParentControlsEnabled(true);
        fileHandler.savePreferences(preferences);

        // Verify the preferences file exists
        File preferencesFile = new File("saves", TEST_PREFS_NAME);
        assertTrue(preferencesFile.exists(), "Preferences file should exist after saving.");

        // Load the preferences
        UserPreferences loadedPreferences = fileHandler.loadPreferences();
        assertNotNull(loadedPreferences, "Loaded preferences should not be null.");
        assertEquals(80.0, loadedPreferences.getVolume(), "Loaded preferences should have a volume of 80.0.");
        assertTrue(loadedPreferences.isParentControlsEnabled(), "Loaded preferences should have parental controls enabled.");
    }

    @Test
    void testLoadPreferencesWhenFileMissing() throws IOException {
        // Ensure the preferences file does not exist
        File preferencesFile = new File("saves", TEST_PREFS_NAME);
        if (preferencesFile.exists()) {
            preferencesFile.delete();
        }

        // Load preferences
        UserPreferences loadedPreferences = fileHandler.loadPreferences();

        // Verify default preferences are returned
        assertNotNull(loadedPreferences, "Loaded preferences should not be null.");
        assertEquals(50.0, loadedPreferences.getVolume(), "Default preferences should have a volume of 50.0.");
        assertFalse(loadedPreferences.isParentControlsEnabled(), "Default preferences should have parental controls disabled.");
    }

    @Test
    void testLoadPreferencesWithCorruptedFile() throws IOException {
        // Create a corrupted preferences file
        File preferencesFile = new File("saves", TEST_PREFS_NAME);
        preferencesFile.getParentFile().mkdirs();
        Files.writeString(preferencesFile.toPath(), "corrupted content");

        // Load preferences
        UserPreferences loadedPreferences = fileHandler.loadPreferences();

        // Verify default preferences are returned and corrupted file is deleted
        assertNotNull(loadedPreferences, "Loaded preferences should not be null.");
        assertEquals(50.0, loadedPreferences.getVolume(), "Default preferences should have a volume of 50.0.");
        assertFalse(loadedPreferences.isParentControlsEnabled(), "Default preferences should have parental controls disabled.");
        assertFalse(preferencesFile.exists(), "Corrupted preferences file should be deleted.");
    }
}
