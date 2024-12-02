package com.example.model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Model class representing the complete state of the game at any given moment.
 * Implements the Singleton pattern to ensure a single current game state is maintained throughout the application.
 * The game state includes the player's pet, vital statistics, parental control status, and the timestamp of the last save.
 */
public class GameState {
    // Static reference to the current game state (Singleton pattern)
    private static GameState currentState;

    // Timestamp of when this game state was last saved
    private LocalDateTime savedAt;

    // The player's pet instance
    private Pet pet;

    // The pet's current vital statistics
    private VitalStats stats;

    // Indicates whether parental controls are active
    private boolean controlParent;

    // When the session started
    private LocalDateTime sessionStartTime;

    // Total time played
    private Duration totalPlayTime;

    /**
     * Constructor initializes a new game state with the current timestamp.
     * Sets the {@code savedAt} field to the current date and time.
     */
    public GameState() {
        this.controlParent = false;
        this.savedAt = LocalDateTime.now();
    }

    /**
     * Retrieves the current game state.
     * If no game state exists, a new instance is created and returned.
     *
     * @return The singleton instance of {@code GameState}.
     */
    public static GameState getCurrentState() {
        if (currentState == null) {
            currentState = new GameState();
        }
        return currentState;
    }

    /**
     * Sets the given game state as the current game state.
     * Replaces the existing singleton instance with the provided state.
     *
     * @param state The {@code GameState} to load as the current state.
     */
    public static void loadState(GameState state) {
        currentState = state;
    }

    /**
     * Gets the timestamp of the last time this game state was saved.
     *
     * @return A {@link LocalDateTime} representing the save timestamp.
     */
    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    /**
     * Sets the timestamp for when this game state was last saved.
     *
     * @param savedAt A {@link LocalDateTime} representing the new save timestamp.
     */
    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }

    /**
     * Gets the player's pet associated with this game state.
     *
     * @return The {@link Pet} instance representing the player's pet.
     */
    public Pet getPet() {
        return pet;
    }

    /**
     * Sets the player's pet for this game state.
     *
     * @param pet The {@link Pet} instance to associate with this game state.
     */
    public void setPet(Pet pet) {
        this.pet = pet;
    }

    /**
     * Gets the vital statistics of the player's pet.
     *
     * @return A {@link VitalStats} object representing the pet's current vital statistics.
     */
    public VitalStats getStats() {
        return stats;
    }

    /**
     * Sets the vital statistics for the player's pet.
     *
     * @param stats A {@link VitalStats} object containing the new statistics for the pet.
     */
    public void setStats(VitalStats stats) {
        this.stats = stats;
    }

    /**
     * Sets whether parental controls are active.
     *
     * @param controlParent {@code true} to enable parental controls, {@code false} to disable them.
     */
    public void setControlParent(boolean controlParent) {
        this.controlParent = controlParent;
    }

    /**
     * Checks whether parental controls are currently active.
     *
     * @return {@code true} if parental controls are enabled, {@code false} otherwise.
     */
    public boolean getControlParent() {
        return controlParent;
    }

    /**
     * Gets the total playtime accumulated in this game state.
     *
     * @return A {@link Duration} object representing the total playtime.
     */
    public Duration getTotalPlayTime() {
        return totalPlayTime;
    }

    /**
     * Sets the total playtime for this game state.
     *
     * @param totalPlayTime A {@link Duration} object representing the new total playtime.
     */
    public void setTotalPlayTime(Duration totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    /**
     * Gets the timestamp of when the current game session started.
     *
     * @return A {@link LocalDateTime} representing the session start time.
     */
    public LocalDateTime getSessionStartTime() {
        return sessionStartTime;
    }

    /**
     * Sets the timestamp for when the current game session started.
     *
     * @param sessionStartTime A {@link LocalDateTime} representing the session start time.
     */
    public void setSessionStartTime(LocalDateTime sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
}
