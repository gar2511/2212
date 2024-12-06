package com.example.controller;

import com.example.App;
import com.example.components.CustomButton;
import com.example.controller.SceneController;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MainMenuControllerTest {

    private MainMenuController controller;
    private SceneController mockSceneController;

    @BeforeEach
    void setUp() {
        controller = new MainMenuController();

        // Mock the SceneController singleton
        mockSceneController = mock(SceneController.class);
        SceneController.setInstance(mockSceneController);

    }

    @Test
    void testStartGame() {
        controller.startGame();
        verify(mockSceneController).switchToSaveMenu();
    }

    @Test
    void testOpenSettings() {
        controller.openSettings();
        verify(mockSceneController).switchToSettings();
    }

    @Test
    void testOpenTutorial() {
        controller.openTutorial();
        verify(mockSceneController).switchToTutorial();
    }

    @Test
    void testExitGame() {
        StackPane mockExitDialog = mock(StackPane.class);
        controller.exitDialog = mockExitDialog;

        controller.exitGame();

        verify(mockExitDialog).setVisible(true);
    }

    @Test
    void testConfirmExit() {
        Platform.runLater(() -> {
            controller.confirmExit();

            // Verify the sound players are stopped and closed
            verify(App.getButtonSound()).stop();
            verify(App.getSoundPlayer()).stop();
            verify(App.getButtonSound()).close();
            verify(App.getSoundPlayer()).close();
        });
    }

    @Test
    void testCancelExit() {
        StackPane mockExitDialog = mock(StackPane.class);
        controller.exitDialog = mockExitDialog;

        controller.cancelExit();

        verify(mockExitDialog).setVisible(false);
    }
}
