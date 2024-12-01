package com.example.components;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import javafx.animation.Interpolator;

public class CustomButton extends Button {
    private Timeline colorTimeline;
    private Timeline reverseColorTimeline;
    private ScaleTransition pressTransition;
    private ScaleTransition releaseTransition;

    public CustomButton() {
        this("");
    }

    public CustomButton(String text) {
        super(text);
        setupAnimation();
        getStyleClass().add("custom-button");
    }

    private void setupAnimation() {
        // hover animation
        SequentialTransition hoverAnimation = new SequentialTransition(this);
        ScaleTransition scaleUpBig = new ScaleTransition(Duration.millis(150));
        scaleUpBig.setToX(1.1);
        scaleUpBig.setToY(1.1);
        scaleUpBig.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scaleUpSmall = new ScaleTransition(Duration.millis(100));
        scaleUpSmall.setToX(1.05);
        scaleUpSmall.setToY(1.05);
        scaleUpSmall.setInterpolator(Interpolator.EASE_OUT);

        hoverAnimation.getChildren().addAll(scaleUpBig, scaleUpSmall);

        // exit animation
        SequentialTransition exitAnimation = new SequentialTransition(this);
        ScaleTransition scaleDownBig = new ScaleTransition(Duration.millis(150));
        scaleDownBig.setToX(0.95);
        scaleDownBig.setToY(0.95);
        scaleDownBig.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scaleDownSmall = new ScaleTransition(Duration.millis(100));
        scaleDownSmall.setToX(1.0);
        scaleDownSmall.setToY(1.0);
        scaleDownSmall.setInterpolator(Interpolator.EASE_OUT);

        exitAnimation.getChildren().addAll(scaleDownBig, scaleDownSmall);

        // press/release animations
        pressTransition = new ScaleTransition(Duration.millis(100), this);
        pressTransition.setToX(0.9);
        pressTransition.setToY(0.9);
        pressTransition.setInterpolator(Interpolator.EASE_OUT);

        releaseTransition = new ScaleTransition(Duration.millis(100), this);
        releaseTransition.setToX(1.05);
        releaseTransition.setToY(1.05);
        releaseTransition.setInterpolator(Interpolator.EASE_OUT);

        // color effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);
        setEffect(colorAdjust);

        // initialize the timelines
        colorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.brightnessProperty(), 0)),
                new KeyFrame(Duration.millis(200), new KeyValue(colorAdjust.brightnessProperty(), -0.1))
        );

        reverseColorTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(colorAdjust.brightnessProperty(), -0.1)),
                new KeyFrame(Duration.millis(200), new KeyValue(colorAdjust.brightnessProperty(), 0))
        );

        // event handlers
        setOnMouseEntered(_ -> {
            hoverAnimation.play();
            colorTimeline.play();
        });

        setOnMouseExited(_ -> {
            exitAnimation.play();
            reverseColorTimeline.play();
        });

        setOnMousePressed(_ -> pressTransition.play());
        setOnMouseReleased(_ -> releaseTransition.play());
    }
}