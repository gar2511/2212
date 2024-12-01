package com.example.components;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;

public class CustomSlider extends Region {
    private final Rectangle track;
    private final Rectangle progressTrack;
    private final Circle thumb;
    private final DoubleProperty value = new SimpleDoubleProperty(0);
    private final DoubleProperty min = new SimpleDoubleProperty(0);
    private final DoubleProperty max = new SimpleDoubleProperty(100);
    private boolean isDragging = false;
    private final Timeline valueTransition;
    private boolean isAnimating = false;

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
        valueProperty().addListener((_, _, newVal) -> {
            int intValue = (int) Math.round(newVal.doubleValue());
            setValue(intValue);
        });

        // setup initial value
        setValue(50);

        // add style class
        getStyleClass().add("custom-slider");

        // initialize value transition timeline
        valueTransition = new Timeline();
        valueTransition.setOnFinished(_ -> requestLayout());
    }

    private void setupEventHandlers() {
        // mouse pressed on track
        setOnMousePressed(this::handleMouseEvent);
        // mouse dragged on track
        setOnMouseDragged(this::handleMouseEvent);
        // mouse released
        setOnMouseReleased(_ -> isDragging = false);
    }

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
        valueTransition.setOnFinished(_ -> {
            isAnimating = false;
            requestLayout();
        });
        valueTransition.play();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        double trackHeight = 16;
        double trackY = (getHeight() - trackHeight) / 2;
        double thumbRadius = thumb.getRadius();

        // layout track
        track.setWidth(getWidth() - (getPadding().getLeft() + getPadding().getRight()));
        track.setHeight(trackHeight);
        track.setX(getPadding().getLeft());
        track.setY(trackY);
        track.setArcWidth(trackHeight);
        track.setArcHeight(trackHeight);

        // calculate thumb position first
        double rawProgressWidth = (getValue() - getMin()) / (getMax() - getMin()) * track.getWidth();
        double minThumbX = progressTrack.getX() + thumbRadius;
        double maxThumbX = progressTrack.getX() + track.getWidth() - thumbRadius;
        double thumbX = Math.max(minThumbX, Math.min(maxThumbX, progressTrack.getX() + rawProgressWidth));
        double thumbY = getHeight() / 2;

        // layout progress track to match thumb position
        double progressWidth = thumbX - progressTrack.getX();
        progressTrack.setWidth(progressWidth + 8);
        progressTrack.setHeight(trackHeight);
        progressTrack.setX(getPadding().getLeft());
        progressTrack.setY(trackY);
        progressTrack.setArcWidth(trackHeight);
        progressTrack.setArcHeight(trackHeight);

        // set thumb position
        thumb.setCenterX(thumbX);
        thumb.setCenterY(thumbY);
    }

    public double getValue() {
        return value.get();
    }

    public void setValue(double newValue) {
        value.set((int) Math.round(newValue)); // force integer values
        requestLayout();
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public double getMin() {
        return min.get();
    }

    public void setMin(double value) {
        min.set(value);
        requestLayout();
    }

    public DoubleProperty minProperty() {
        return min;
    }

    public double getMax() {
        return max.get();
    }

    public void setMax(double value) {
        max.set(value);
        requestLayout();
    }

    public DoubleProperty maxProperty() {
        return max;
    }

    public void setAnimating(boolean animating) {
        isAnimating = animating;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public boolean isAnimating() {
        return isAnimating;
    }
}