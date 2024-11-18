package com.example.util;

import com.example.model.GameState;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {
    private static final String SAVES_DIR = "saves";
    private final ObjectMapper objectMapper;

    public FileHandler() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        createSavesDirectory();
    }

    private void createSavesDirectory() {
        try {
            Files.createDirectories(Paths.get(SAVES_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame(String saveName, GameState state) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        objectMapper.writeValue(savePath.toFile(), state);
    }

    public GameState loadGame(String saveName) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        return objectMapper.readValue(savePath.toFile(), GameState.class);
    }

    public void deleteSave(String saveName) throws IOException {
        Path savePath = Paths.get(SAVES_DIR, saveName + ".json");
        Files.deleteIfExists(savePath);
    }

    public File[] getSaveFiles() {
        File savesDir = new File(SAVES_DIR);
        return savesDir.listFiles((dir, name) -> name.endsWith(".json"));
    }
}