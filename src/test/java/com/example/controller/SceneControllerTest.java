package com.example.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SceneControllerTest {

    private SceneController sceneController;

    @BeforeEach
    void setUp() {
        // Ensure a fresh instance for every test
        SceneController.setInstance(null);
        sceneController = SceneController.getInstance();
    }

    @Test
    void testSingletonInstance() {
        // Verify that the singleton instance is consistent
        SceneController firstInstance = SceneController.getInstance();
        SceneController secondInstance = SceneController.getInstance();

        assertSame(firstInstance, secondInstance, "SceneController should follow the singleton pattern.");
    }

    @Test
    void testSwitchToGame() {
        // Mock dependencies
        SceneController mockController = Mockito.spy(sceneController);

        // Inject mock scene controller
        SceneController.setInstance(mockController);

        // Call method
        mockController.switchToGame();

        // Verify that the correct FXML file is loaded
        verify(mockController).loadFXML("game.fxml");
    }

    @Test
    void testSwitchToMainMenu() {
        // Mock dependencies
        SceneController mockController = Mockito.spy(sceneController);

        // Inject mock scene controller
        SceneController.setInstance(mockController);

        // Call method
        mockController.switchToMainMenu();

        // Verify that the correct FXML file is loaded
        verify(mockController).loadFXML("main_menu.fxml");
    }

    @Test
    void testGetCurrentSceneWhenNull() {
        // Mock the Stage and Window behavior
        Stage mockStage = mock(Stage.class);
        Scene mockScene = mock(Scene.class);
        when(mockStage.getScene()).thenReturn(mockScene);
        when(mockStage.isShowing()).thenReturn(true);

        // Set up mock stage as the only showing window
        Mockito.mockStatic(Stage.class).when(Stage::getWindows).thenReturn(java.util.List.of(mockStage));

        // Verify the current scene retrieval
        assertSame(mockScene, sceneController.getCurrentScene(), "Current scene should be retrieved from the first showing window.");
    }

    @Test
    void testLoadFXMLValidFile() {
        // Mock the FXMLLoader behavior
        Parent mockRoot = mock(Parent.class);
        FXMLLoader mockLoader = mock(FXMLLoader.class);

    }

    @Test
    void testLoadFXMLInvalidFile() {
        // Mock the FXMLLoader behavior to throw an IOException
        FXMLLoader mockLoader = mock(FXMLLoader.class);


    }
}
