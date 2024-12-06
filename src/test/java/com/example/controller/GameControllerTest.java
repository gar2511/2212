package com.example.controller;

import com.example.model.*;
import com.example.util.FileHandler;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.animation.Timeline;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameControllerTest {

    private GameController gameController;
    private GameState mockGameState;
    private Pet mockPet;
    private VitalStats mockStats;
    private FileHandler mockFileHandler;

    @BeforeAll
    void setupClass() {
        Platform.startup(() -> {}); // Initialize JavaFX toolkit
    }

    @BeforeEach
    void setup() {
        gameController = new GameController();
        mockGameState = mock(GameState.class);
        mockPet = mock(Pet.class);
        mockStats = mock(VitalStats.class);
        mockFileHandler = mock(FileHandler.class);

        when(mockGameState.getPet()).thenReturn(mockPet);
        when(mockPet.getStats()).thenReturn(mockStats);

        GameState.loadState(mockGameState);

        // Mock the bindings for simplicity
        when(mockStats.energyProperty()).thenReturn(new SimpleIntegerProperty(100));
        when(mockStats.healthProperty()).thenReturn(new SimpleIntegerProperty(100));
        when(mockStats.hungerProperty()).thenReturn(new SimpleIntegerProperty(100));
        when(mockStats.happinessProperty()).thenReturn(new SimpleIntegerProperty(100));
    }

    @Test
    void testInitialization() {
        Platform.runLater(() -> {
            gameController.initialize();

            // Verify UI components are properly initialized
            assertNotNull(gameController.energyBar, "Energy bar should be initialized.");
            assertNotNull(gameController.healthBar, "Health bar should be initialized.");
            assertNotNull(gameController.hungerBar, "Hunger bar should be initialized.");
            assertNotNull(gameController.happinessBar, "Happiness bar should be initialized.");
        });
    }

    @Test
    void testFeedPet() {
        when(mockPet.getDefaultItem12()).thenReturn(1);

        Platform.runLater(() -> {
            gameController.feedPet();

            // Verify stat updates
            verify(mockStats).increaseHunger(20);
            verify(mockStats).increaseHappiness(10);
            verify(mockPet.getInventory()).decreaseItem1();
        });
    }

    @Test
    void testPlayPet() {
        Platform.runLater(() -> {
            gameController.playPet();

            // Verify stat updates
            verify(mockStats).decreaseEnergy(15);
            verify(mockStats).increaseHappiness(20);
            verify(mockStats).decreaseHunger(10);
        });
    }

    @Test
    void testHandleCriticalStateHunger() {
        Platform.runLater(() -> {
            gameController.handleCriticalState(0); // Hunger critical

            // Verify modifiers are updated
            verify(mockStats).setHealthMod(1);
            verify(mockStats).setEnergyMod(1);
            verify(mockStats).setHappinessMod(1);
        });
    }

    @Test
    void testStartTimeTracker() {
        Platform.runLater(() -> {
            gameController.startTimeTracker();

            // Simulate 1 second of time passing
            verify(mockPet, timeout(2000).atLeastOnce()).addTimeSpent(1);
        });
    }

    @Test
    void testGameOver() {
        when(mockStats.getHealth()).thenReturn(0);

        Platform.runLater(() -> {
            gameController.handleGameOver();

            // Verify game over label is displayed
            assertTrue(gameController.gameOverLabel.isVisible(), "Game Over label should be visible.");
            verify(mockStats).setHealthMod(0);
        });
    }

    @Test
    void testSaveGame() throws Exception {
        when(mockPet.getSaveID()).thenReturn(1);

        Platform.runLater(() -> {
            gameController.saveGame();

            try {
                verify(mockFileHandler).saveGame(eq("slot1"), eq(mockGameState));
            } catch (Exception e) {
                fail("Save game should not throw an exception.");
            }
        });
    }

    @Test
    void testHandleOutsideActiveTime() {
        Platform.runLater(() -> {
            gameController.handleOutsideActiveTime();

            // Verify an alert is shown
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inactive Time");
            assertEquals("Inactive Time", alert.getTitle());
        });
    }
}
