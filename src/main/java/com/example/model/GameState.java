package com.example.model;

import java.time.LocalDateTime;

public class GameState {
    private static GameState currentState;

    private LocalDateTime savedAt;
    private Pet pet;
    private VitalStats stats;

    public GameState() {
        this.savedAt = LocalDateTime.now();
    }

    public static GameState getCurrentState() {
        if (currentState == null) {
            currentState = new GameState();
        }
        return currentState;
    }

    public static void loadState(GameState state) {
        currentState = state;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public VitalStats getStats() {
        return stats;
    }

    public void setStats(VitalStats stats) {
        this.stats = stats;
    }
}