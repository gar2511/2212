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

public class CustomSlider extends Region {
    private final Rectangle track;
    private final Rectangle progressTrack;
    private final Circle thumb;
    private final Label percentageLabel;
    private final DoubleProperty value = new SimpleDoubleProperty(0);
    private final DoubleProperty min = new SimpleDoubleProperty(0);
    private final DoubleProperty max = new SimpleDoubleProperty(100);
    private boolean isDragging = false;
    private TranslateTransition thumbAnimation;
    private Timeline valueTransition;

    public CustomSlider() {
        // setup base styling
        setPadding(new Insets(10));
        setPrefHeight(40);
        setMinHeight(40);
        setMaxHeight(40);
        
        // set integer range
        setMin(0);
        setMax(100);
        
        double trackHeight = 16;
        
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
        
        // create thumb
        thumb = new Circle();
        thumb.getStyleClass().add("slider-thumb");
        thumb.setRadius(8);
        
        // create percentage label
        percentageLabel = new Label("50%");
        percentageLabel.setStyle(
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px;" +
            "-fx-font-family: 'Courier New', monospace;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 0 0 0 10;"
        );
        
        // add all elements including the label
        getChildren().addAll(track, progressTrack, thumb, percentageLabel);
        
        // setup event handlers
        setupEventHandlers();
        
        // modify the value listener to update label and use integer
        valueProperty().addListener((obs, oldVal, newVal) -> {
            int intValue = (int) Math.round(newVal.doubleValue());
            setValue(intValue);
            percentageLabel.setText(intValue + "%");
        });
        
        // setup initial value
        setValue(50);
        
        // add style class
        getStyleClass().add("custom-slider");
        
        // initialize value transition timeline
        valueTransition = new Timeline();
        valueTransition.setOnFinished(e -> requestLayout());
    }

    private void setupEventHandlers() {
        // mouse pressed on track
        setOnMousePressed(this::handleMouseEvent);
        // mouse dragged on track
        setOnMouseDragged(this::handleMouseEvent);
        // mouse released
        setOnMouseReleased(e -> isDragging = false);
    }

    private void handleMouseEvent(MouseEvent event) {
        isDragging = true;
        double trackWidth = getWidth() - (getPadding().getLeft() + getPadding().getRight()) - 16;
        double mouseX = event.getX() - getPadding().getLeft() - 8;
        double proportion = Math.min(1, Math.max(0, mouseX / trackWidth));
        
        // calculate value directly from proportion
        double newValue = getMin() + (proportion * (getMax() - getMin()));
        double targetValue = Math.round(Math.max(getMin(), Math.min(getMax(), newValue)));
        
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            animateToValue(targetValue);
        } else {
            setValue(targetValue);
        }
    }

    private void animateToValue(double targetValue) {
        valueTransition.stop();
        
        // Option 1: Built-in interpolators with different acceleration curves
        KeyValue keyValue = new KeyValue(
            valueProperty(),
            targetValue,
            Interpolator.SPLINE(0.25, 0.1, 0.25, 1)
        );

        // Duration stays the same
        KeyFrame keyFrame = new KeyFrame(Duration.millis(600), keyValue);
        
        valueTransition.getKeyFrames().clear();
        valueTransition.getKeyFrames().add(keyFrame);
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
        
        // position the percentage label
        double labelX = track.getX() + track.getWidth();
        double labelY = (getHeight() - percentageLabel.prefHeight(-1)) / 2;
        percentageLabel.relocate(labelX, labelY);
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
}