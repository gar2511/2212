package com.example.components;

import javafx.animation.TranslateTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Interpolator;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * CustomToggle is a stylized toggle button with animated transitions for state changes.
 * <br><br>
 * This component provides a smooth toggle effect with color transitions for the background
 * and position transitions for the thumb.
 *
 * <b>Example Use:</b>
 * <pre>{@code
 * CustomToggle toggle = new CustomToggle();
 * toggle.setSelected(true);
 * toggle.setOnAction(event -> System.out.println("Toggled: " + toggle.isSelected()));
 * }</pre>
 *
 * <b>Example Output:</b>
 * A toggle button with a smooth animated transition between states.
 *
 * @version 1.0
 * @author Gary, Kevin, Michael, Rajvir, Zein
 */
public class CustomToggle extends ToggleButton {

    /** The background track of the toggle switch. */
    private final Rectangle background;

    /** The thumb of the toggle switch. */
    private final Circle thumb;

    /** Timeline for animating the background color. */
    private Timeline colorTimeline;

    /** Timeline for animating the thumb's position. */
    private Timeline translateTimeline;

    /** Indicates whether an animation is currently running. */
    private boolean isAnimating = false;

    /**
     * Constructs a new CustomToggle with default settings.
     * <p>
     * Initializes the toggle button's layout, animations, and event listeners.
     * </p>
     */
    public CustomToggle() {
        // create background track
        background = new Rectangle(39, 24);
        background.setArcWidth(24);
        background.setArcHeight(24);
        background.setFill(Color.valueOf("#e9e9eb"));

        // create thumb
        thumb = new Circle(10.5);
        thumb.setFill(Color.WHITE);
        thumb.setTranslateX(-7.5);
        thumb.setEffect(new javafx.scene.effect.DropShadow(3, 0, 1.5, Color.rgb(0, 0, 0, 0.2)));

        // stack components
        StackPane layout = new StackPane(background, thumb);
        layout.setMaxSize(39, 24);
        setGraphic(layout);

        // prevent default handling
        setOnMouseClicked(event -> {
            if (!isAnimating) {
                setSelected(!isSelected());
                event.consume();
            }
        });

        // style the toggle button
        setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        // add listener for state changes
        selectedProperty().addListener((obs, oldVal, newVal) -> {
            animateToggle(newVal);
        });

        // set initial state
        background.setFill(Color.valueOf("#e9e9eb"));
        thumb.setTranslateX(-7.5);
    }

    /**
     * Animates the toggle button's transition between selected and deselected states.
     *
     * @param selected {@code true} if the toggle button is selected, {@code false} otherwise
     */
    private void animateToggle(boolean selected) {
        // stop any running animations
        if (colorTimeline != null) colorTimeline.stop();
        if (translateTimeline != null) translateTimeline.stop();

        isAnimating = true;

        // color transition
        Color startColor = (Color) background.getFill();
        Color endColor = selected ? Color.valueOf("#34c759") : Color.valueOf("#e9e9eb");

        colorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        background.fillProperty(),
                        startColor,
                        Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
                )),
                new KeyFrame(Duration.millis(300), new KeyValue(
                        background.fillProperty(),
                        endColor,
                        Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
                ))
        );

        // thumb translation
        double endX = selected ? 7.5 : -7.5;

        translateTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        thumb.translateXProperty(),
                        thumb.getTranslateX(),
                        Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
                )),
                new KeyFrame(Duration.millis(300), new KeyValue(
                        thumb.translateXProperty(),
                        endX,
                        Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
                ))
        );

        // play animations
        colorTimeline.play();
        translateTimeline.play();
        translateTimeline.setOnFinished(e -> isAnimating = false);
    }
}
