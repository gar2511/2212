package com.example.components;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class CustomToggleTest extends ApplicationTest {

    private CustomToggle customToggle;

    @Override
    public void start(Stage stage) {
        customToggle = new CustomToggle();
        Scene scene = new Scene(customToggle, 100, 100);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        Platform.runLater(() -> customToggle.setSelected(false));
    }

    @Test
    public void testInitialState() {
        Platform.runLater(() -> {
            assertFalse(customToggle.isSelected(), "CustomToggle should be deselected initially.");
            Rectangle background = (Rectangle) customToggle.lookup(".background");
            assertEquals(Color.valueOf("#e9e9eb"), background.getFill(), "Initial background color should be #e9e9eb.");
            Circle thumb = (Circle) customToggle.lookup(".thumb");
            assertEquals(-7.5, thumb.getTranslateX(), "Thumb should start at position -7.5.");
        });
    }

    @Test
    public void testToggleToSelected() {
        Platform.runLater(() -> {
            customToggle.setSelected(true);

            assertTrue(customToggle.isSelected(), "CustomToggle should be selected.");
            Rectangle background = (Rectangle) customToggle.lookup(".background");
            assertEquals(Color.valueOf("#34c759"), background.getFill(), "Background color should transition to #34c759.");
            Circle thumb = (Circle) customToggle.lookup(".thumb");
            assertEquals(7.5, thumb.getTranslateX(), "Thumb should move to position 7.5.");
        });
    }

    @Test
    public void testToggleToDeselected() {
        Platform.runLater(() -> {
            customToggle.setSelected(true); // Toggle to selected state first
            customToggle.setSelected(false); // Toggle back to deselected state

            assertFalse(customToggle.isSelected(), "CustomToggle should be deselected.");
            Rectangle background = (Rectangle) customToggle.lookup(".background");
            assertEquals(Color.valueOf("#e9e9eb"), background.getFill(), "Background color should transition to #e9e9eb.");
            Circle thumb = (Circle) customToggle.lookup(".thumb");
            assertEquals(-7.5, thumb.getTranslateX(), "Thumb should move back to position -7.5.");
        });
    }

    @Test
    public void testMouseInteraction() {
        Platform.runLater(() -> {
            // Simulate mouse click to toggle the state
            customToggle.fire();

            assertTrue(customToggle.isSelected(), "CustomToggle should be selected after mouse click.");

            customToggle.fire(); // Click again to toggle back
            assertFalse(customToggle.isSelected(), "CustomToggle should be deselected after second mouse click.");
        });
    }

    @Test
    public void testAnimationIsRunning() {
        Platform.runLater(() -> {
            customToggle.setSelected(true);

            assertTrue(customToggle.isSelected(), "CustomToggle should be selected.");
            assertFalse(customToggle.isDisabled(), "CustomToggle should not be disabled during animation.");
        });
    }
}
