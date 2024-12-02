package com.example.components;

import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.application.Platform;

/**
 * CustomSlider is a custom JavaFX component that provides a stylized slider
 * with animation and value control.
 * <br><br>
 * <b>Example Use:</b>
 * <pre>{@code
 * CustomSlider slider = new CustomSlider();
 * slider.setMin(0);
 * slider.setMax(100);
 * slider.setValue(50);
 * }</pre>
 *
 * <b>Example Output:</b>
 * A slider with smooth animations and precise value handling.
 *
 * @version 1.0
 * @author Gary, Kevin, Michael, Rajvir, Zein
 */
public class CustomSlider extends Region {

    /** The background track of the slider. */
    private final Rectangle track;

    /** The progress indicator of the slider. */
    private final Rectangle progressTrack;

    /** The draggable thumb of the slider. */
    private final Circle thumb;

    /** The current value of the slider. */
    private final DoubleProperty value = new SimpleDoubleProperty(0);

    /** The minimum value of the slider. */
    private final DoubleProperty min = new SimpleDoubleProperty(0);

    /** The maximum value of the slider. */
    private final DoubleProperty max = new SimpleDoubleProperty(100);

    /** Indicates whether the thumb is being dragged. */
    private boolean isDragging = false;

    /** The animation for moving the thumb. */
    private TranslateTransition thumbAnimation;

    /** The animation for transitioning the slider's value. */
    private Timeline valueTransition;

    /** Indicates whether an animation is currently running. */
    private boolean isAnimating = false;

    /**
     * Constructs a new CustomSlider with default settings.
     * <p>
     * Initializes the slider's layout, animations, and event handlers.
     * </p>
     */
    public CustomSlider() {
        // setup base styling with reduced padding
        setPadding(new Insets(5, 20, 5, 20));
        setPrefHeight(30);
        setMinHeight(30);
        setMaxHeight(30);

        // set integer range
        setMin(0);
        setMax(100);

        double trackHeight = 12;

        // create background track
        track = new Rectangle();
        track.getStyleClass().add("slider-track");
        track.setHeight(trackHeight);
        track.setArcWidth(trackHeight);
        track.setArcHeight(trackHeight);

        // create coloured progress track
        progressTrack = new Rectangle();
        progressTrack.getStyleClass().add("slider-progress-track");
        progressTrack.setHeight(trackHeight);
        progressTrack.setArcWidth(trackHeight);
        progressTrack.setArcHeight(trackHeight);

        // create thumb with smaller radius
        thumb = new Circle();
        thumb.getStyleClass().add("slider-thumb");
        thumb.setRadius(8);

        // add elements (without the percentage label)
        getChildren().addAll(track, progressTrack, thumb);

        // setup event handlers
        setupEventHandlers();

        // modify the value listener to use integer values
        valueProperty().addListener((obs, oldVal, newVal) -> {
            int intValue = (int) Math.round(newVal.doubleValue());
            setValue(intValue);
        });

        // setup initial value
        setValue(50);

        // add style class
        getStyleClass().add("custom-slider");

        // initialize value transition timeline
        valueTransition = new Timeline();
        valueTransition.setOnFinished(e -> requestLayout());

        // Add a layout listener to ensure proper initial layout
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(this::requestLayout);
            }
        });
    }

    /**
     * Sets up event handlers for mouse interactions on the slider.
     */
    private void setupEventHandlers() {
        // mouse pressed on track
        setOnMousePressed(this::handleMouseEvent);
        // mouse dragged on track
        setOnMouseDragged(this::handleMouseEvent);
        // mouse released
        setOnMouseReleased(e -> isDragging = false);
    }

    /**
     * Handles mouse interactions for adjusting the slider's value.
     *
     * @param event the MouseEvent triggering this handler
     */
    private void handleMouseEvent(MouseEvent event) {
        double trackWidth = getWidth() - (getPadding().getLeft() + getPadding().getRight()) - 16;
        double mouseX = event.getX() - getPadding().getLeft() - 8;
        double proportion = Math.min(1, Math.max(0, mouseX / trackWidth));

        // calculate value directly from proportion
        double newValue = getMin() + (proportion * (getMax() - getMin()));
        double targetValue = Math.round(Math.max(getMin(), Math.min(getMax(), newValue)));

        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            // Always start a new animation on click, even if one is running
            valueTransition.stop();
            animateToValue(targetValue);
        } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            isDragging = true;
            // During drag, immediately update value
            valueTransition.stop();
            setValue(targetValue);
        }
    }

    /**
     * Animates the slider's value to a target value.
     *
     * @param targetValue the target value for the slider
     */
    private void animateToValue(double targetValue) {
        valueTransition.stop();
        isAnimating = true;

        KeyValue keyValue = new KeyValue(
                valueProperty(),
                targetValue,
                Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
        );

        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), keyValue);

        valueTransition.getKeyFrames().clear();
        valueTransition.getKeyFrames().add(keyFrame);
        valueTransition.setOnFinished(e -> {
            isAnimating = false;
            requestLayout();
        });
        valueTransition.play();
    }

    @Override
    public void layoutChildren() {
        if (!isVisible()) {
            return;
        }

        super.layoutChildren();

        double trackHeight = 16;
        double trackY = (getHeight() - trackHeight) / 2;
        double thumbRadius = thumb.getRadius();

        // layout track first
        track.setWidth(getWidth() - (getPadding().getLeft() + getPadding().getRight()));
        track.setHeight(trackHeight);
        track.setX(getPadding().getLeft());
        track.setY(trackY);
        track.setArcWidth(trackHeight);
        track.setArcHeight(trackHeight);

        // calculate thumb position based on current value
        double proportion = (getValue() - getMin()) / (getMax() - getMin());
        double availableWidth = track.getWidth() - (thumbRadius * 2);
        double thumbX = track.getX() + thumbRadius + (availableWidth * proportion);
        double thumbY = getHeight() / 2;

        // layout progress track
        progressTrack.setHeight(trackHeight);
        progressTrack.setX(getPadding().getLeft());
        progressTrack.setY(trackY);
        progressTrack.setArcWidth(trackHeight);
        progressTrack.setArcHeight(trackHeight);
        progressTrack.setWidth((thumbX - progressTrack.getX()) + 8); // Keep the +8 adjustment

        // position thumb
        thumb.setCenterX(thumbX);
        thumb.setCenterY(thumbY);
    }

    /**
     * Gets the current value of the slider.
     *
     * @return the slider's current value
     */
    public double getValue() {
        return value.get();
    }

    /**
     * Sets the value of the slider.
     *
     * @param newValue the new value to set
     */
    public void setValue(double newValue) {
        value.set((int) Math.round(newValue)); // force integer values
        requestLayout();
    }

    /**
     * The value property of the slider.
     *
     * @return the value property
     */
    public DoubleProperty valueProperty() {
        return value;
    }

    /**
     * Gets the minimum value of the slider.
     *
     * @return the slider's minimum value
     */
    public double getMin() {
        return min.get();
    }

    /**
     * Sets the minimum value of the slider.
     *
     * @param value the minimum value to set
     */
    public void setMin(double value) {
        min.set(value);
        requestLayout();
    }

    /**
     * The minimum value property of the slider.
     *
     * @return the minimum value property
     */
    public DoubleProperty minProperty() {
        return min;
    }

    /**
     * Gets the maximum value of the slider.
     *
     * @return the slider's maximum value
     */
    public double getMax() {
        return max.get();
    }

    /**
     * Sets the maximum value of the slider.
     *
     * @param value the maximum value to set
     */
    public void setMax(double value) {
        max.set(value);
        requestLayout();
    }

    /**
     * The maximum value property of the slider.
     *
     * @return the maximum value property
     */
    public DoubleProperty maxProperty() {
        return max;
    }
}
