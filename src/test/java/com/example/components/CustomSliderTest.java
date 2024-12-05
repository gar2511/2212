package com.example.components;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class CustomSliderTest extends ApplicationTest {

    private CustomSlider customSlider;

    @Override
    public void start(Stage stage) {
        customSlider = new CustomSlider();
        Scene scene = new Scene(customSlider, 300, 50);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Reset the slider before each test
        Platform.runLater(() -> {
            customSlider.setMin(0);
            customSlider.setMax(100);
            customSlider.setValue(50);
        });
    }

    @Test
    public void testInitialValue() {
        Platform.runLater(() -> assertEquals(50, customSlider.getValue(), "Initial value should be 50."));
    }

    @Test
    public void testSetValueWithinRange() {
        Platform.runLater(() -> {
            customSlider.setValue(75);
            assertEquals(75, customSlider.getValue(), "Value should be updated to 75.");
        });
    }

    @Test
    public void testSetValueOutOfRange() {
        Platform.runLater(() -> {
            customSlider.setValue(150);
            assertEquals(100, customSlider.getValue(), "Value should not exceed the maximum of 100.");

            customSlider.setValue(-10);
            assertEquals(0, customSlider.getValue(), "Value should not go below the minimum of 0.");
        });
    }

    @Test
    public void testSetMinAndMax() {
        Platform.runLater(() -> {
            customSlider.setMin(10);
            customSlider.setMax(90);

            assertEquals(10, customSlider.getMin(), "Minimum value should be updated to 10.");
            assertEquals(90, customSlider.getMax(), "Maximum value should be updated to 90.");

            customSlider.setValue(100);
            assertEquals(90, customSlider.getValue(), "Value should be capped at the new maximum of 90.");
        });
    }

    @Test
    public void testMouseInteractionSetsValue() {
        Platform.runLater(() -> {
            double mouseX = customSlider.getWidth() * 0.75; // Simulate a click at 75% of the track
            double mouseY = customSlider.getHeight() / 2;

            MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_PRESSED, mouseX, mouseY, mouseX, mouseY, null, 1,
                    false, false, false, false, true, false, false, false, false, false, null);

            customSlider.fireEvent(mouseEvent);

            int expectedValue = 75;
            assertEquals(expectedValue, (int) customSlider.getValue(),
                    "Slider value should update to match the position of the mouse click.");
        });
    }

    @Test
    public void testThumbPositionUpdatesWithValue() {
        Platform.runLater(() -> {
            customSlider.setValue(75);

            // Calculate expected thumb position
            double trackWidth = customSlider.getWidth() - customSlider.getPadding().getLeft() - customSlider.getPadding().getRight() - (customSlider.getPrefHeight());
            double expectedThumbX = customSlider.getPadding().getLeft() + (trackWidth * 0.75);

            assertEquals(expectedThumbX, customSlider.lookup(".slider-thumb").getLayoutX(), 1.0,
                    "Thumb position should align with the slider value.");
        });
    }
}

