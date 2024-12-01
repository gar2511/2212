package com.example.model;

import java.time.LocalDateTime;

/**
 * Model class representing the complete state of the game at any given moment.
 * Implements the Singleton pattern to ensure a single current game state is maintained throughout the application.
 * The game state includes the player's pet, vital statistics, and the timestamp of the last save.
 */
public class GameState {
    // Static reference to the current game state (Singleton pattern)
    private static GameState currentState;

    // The player's pet instance
    private Pet pet;

    /**
     * Constructor initializes a new game state with the current timestamp.
     * Sets the {@code savedAt} field to the current date and time.
     */
    public GameState() {
        // Timestamp of when this game state was last saved
        LocalDateTime.now();
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

}
