package com.example.util;

import javafx.animation.TranslateTransition;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

// Custom JavaFX toggle button component that provides a modern sliding switch appearance
public class Toggle extends ToggleButton {
    // UI components for the toggle switch
    private final Rectangle background;  // The background rectangle/track
    private final Circle thumb;         // The sliding circle/thumb
    private final TranslateTransition transition;  // Animation for thumb movement

    // Constructor creates and configures the toggle switch components
    public Toggle() {
        // Create and style the background track
        background = new Rectangle(52, 32);
        background.setArcWidth(32);     // Rounded corners
        background.setArcHeight(32);
        background.getStyleClass().add("toggle-background");

        // Create and position the sliding thumb
        thumb = new Circle(14);         // Radius of 14 pixels
        thumb.setTranslateX(16);        // Initial position (unselected)
        thumb.getStyleClass().add("toggle-thumb");

        // Stack the components (background behind thumb)
        StackPane layout = new StackPane(background, thumb);
        setGraphic(layout);

        // Set up the sliding animation
        transition = new TranslateTransition(Duration.millis(200), thumb);

        // Initialize to unselected state
        updateState(false);

        // Add listener to animate thumb position when selection changes
        selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateState(newVal);
        });

        // Add custom style class for CSS styling
        getStyleClass().add("toggle");
    }

    // Updates the toggle's visual state
    // @param selected true if toggle is selected/on, false if unselected/off
    private void updateState(boolean selected) {
        transition.stop();  // Stop any ongoing animation
        transition.setToX(selected ? 36 : 16);  // Move thumb right when selected, left when unselected
        transition.play();  // Start the animation
    }
}
