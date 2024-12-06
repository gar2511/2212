package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @BeforeEach
    void resetGameState() {
        // Reset the singleton instance before each test
        GameState.loadState(null);
    }

    @Test
    void testSingletonBehavior() {
        GameState firstInstance = GameState.getCurrentState();
        GameState secondInstance = GameState.getCurrentState();

        assertSame(firstInstance, secondInstance, "GameState should follow the singleton pattern.");
    }

    @Test
    void testLoadState() {
        GameState newState = new GameState();
        GameState.loadState(newState);

        assertSame(newState, GameState.getCurrentState(), "GameState.loadState() should replace the current singleton instance.");
    }

    @Test
    void testSavedAtTimestamp() {
        GameState gameState = GameState.getCurrentState();
        LocalDateTime now = LocalDateTime.now();
        gameState.setSavedAt(now);

        assertEquals(now, gameState.getSavedAt(), "The savedAt timestamp should match the value set.");
    }

    @Test
    void testSetAndGetPet() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = new Pet();
        pet.setName("Buddy");
        pet.setSpecies("Dog");

        gameState.setPet(pet);

        assertNotNull(gameState.getPet(), "The pet should not be null after being set.");
        assertEquals("Buddy", gameState.getPet().getName(), "The pet's name should match the value set.");
        assertEquals("Dog", gameState.getPet().getSpecies(), "The pet's species should match the value set.");
    }

    @Test
    void testSetAndGetStats() {
        GameState gameState = GameState.getCurrentState();
        VitalStats stats = new VitalStats();
        stats.increaseHealth(50);
        stats.increaseEnergy(30);

        gameState.setStats(stats);

        assertNotNull(gameState.getStats(), "The stats should not be null after being set.");
        assertEquals(50, gameState.getStats().getHealth(), "The health stat should match the value set.");
        assertEquals(30, gameState.getStats().getEnergy(), "The energy stat should match the value set.");
    }

    @Test
    void testParentalControls() {
        GameState gameState = GameState.getCurrentState();
        gameState.setParentControlsEnabled(true);

        assertTrue(gameState.isParentControlsEnabled(), "Parental controls should be enabled when set to true.");

        gameState.setParentControlsEnabled(false);

        assertFalse(gameState.isParentControlsEnabled(), "Parental controls should be disabled when set to false.");
    }

    @Test
    void testControlParentFlag() {
        GameState gameState = GameState.getCurrentState();
        gameState.setControlParent(true);

        assertTrue(gameState.getControlParent(), "ControlParent flag should be true when set to true.");

        gameState.setControlParent(false);

        assertFalse(gameState.getControlParent(), "ControlParent flag should be false when set to false.");
    }
}
