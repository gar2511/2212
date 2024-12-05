package com.example.components;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for CustomButton.
 */
public class CustomButtonTest extends ApplicationTest {

    private CustomButton customButton;

    @Override
    public void start(Stage stage) {
        // Initialize the JavaFX environment
    }

    @BeforeEach
    public void setUp() {
        customButton = new CustomButton("Test Button");
    }

    @Test
    public void testButtonText() {
        assertEquals("Test Button", customButton.getText(), "The button text should match the constructor input.");
    }

    @Test
    public void testHoverAnimation() {
        // Simulate hover
        interact(() -> customButton.getOnMouseEntered().handle(null));

        // Validate that the effect is applied
        ColorAdjust effect = (ColorAdjust) customButton.getEffect();
        assertNotNull(effect, "ColorAdjust effect should not be null after hover.");
        assertEquals(-0.1, effect.getBrightness(), 0.01, "Brightness should be reduced when hovered.");
    }

    @Test
    public void testExitAnimation() {
        // Simulate hover and then exit
        interact(() -> {
            customButton.getOnMouseEntered().handle(null);
            customButton.getOnMouseExited().handle(null);
        });

        // Validate that the effect is reset
        ColorAdjust effect = (ColorAdjust) customButton.getEffect();
        assertNotNull(effect, "ColorAdjust effect should not be null after hover exit.");
        assertEquals(0, effect.getBrightness(), 0.01, "Brightness should return to normal after hover exit.");
    }

    @Test
    public void testPressAndReleaseAnimations() {
        // Simulate press
        interact(() -> customButton.getOnMousePressed().handle(null));

        // Validate scale after press
        assertEquals(0.9, customButton.getScaleX(), 0.01, "ScaleX should be reduced when pressed.");
        assertEquals(0.9, customButton.getScaleY(), 0.01, "ScaleY should be reduced when pressed.");

        // Simulate release
        interact(() -> customButton.getOnMouseReleased().handle(null));

        // Validate scale after release
        assertEquals(1.05, customButton.getScaleX(), 0.01, "ScaleX should increase slightly after release.");
        assertEquals(1.05, customButton.getScaleY(), 0.01, "ScaleY should increase slightly after release.");
    }

    @Test
    public void testCustomButtonStyleClass() {
        assertTrue(customButton.getStyleClass().contains("custom-button"),
                "The button should have the 'custom-button' style class.");
    }
}
