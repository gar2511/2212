package com.example.components;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class StatBar extends HBox {
    private final Label label;
    private final ProgressBar progressBar;
    private final StringProperty labelText = new SimpleStringProperty();
    private final DoubleProperty progress = new SimpleDoubleProperty();
    private final ObjectProperty<Color> barColor = new SimpleObjectProperty<>(Color.GREEN);
    private Timeline timeline;
    private static final Duration DECAY_INTERVAL = Duration.seconds(1);
    private double targetValue = 1.0;
    private double lastValue = 1.0;

    public StatBar() {
        label = new Label();
        progressBar = new ProgressBar();
        
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        label.setPrefWidth(100);
        label.setAlignment(Pos.CENTER_RIGHT);
        
        progressBar.setPrefWidth(150);
        progressBar.getStyleClass().add("stat-progress-bar");
        progressBar.setProgress(1.0);
        
        label.textProperty().bind(labelText);
        
        barColor.addListener((obs, oldColor, newColor) -> {
            String colorStyle = String.format("-fx-accent: rgb(%d, %d, %d);",
                (int)(newColor.getRed() * 255),
                (int)(newColor.getGreen() * 255),
                (int)(newColor.getBlue() * 255));
            progressBar.setStyle(colorStyle);
        });
        
        progress.addListener((obs, oldVal, newVal) -> {
            double currentValue = progressBar.getProgress();
            targetValue = newVal.doubleValue();
            
            // Only start new animation if significant change or direction change
            if (Math.abs(currentValue - targetValue) > 0.001 || 
                (currentValue - lastValue) * (targetValue - currentValue) < 0) {
                
                Duration duration = oldVal.doubleValue() > newVal.doubleValue() 
                    ? DECAY_INTERVAL 
                    : Duration.millis(250);
                
                animateProgressAndColor(currentValue, targetValue, duration);
            }
            lastValue = currentValue;
        });
        
        getChildren().addAll(label, progressBar);
    }
    
    private void animateProgressAndColor(double oldValue, double newValue, Duration duration) {
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
            oldValue = progressBar.getProgress();
        }
        
        Color startColor = Color.rgb(
            (int)(255 * (1 - oldValue)),
            (int)(255 * oldValue),
            0
        );
        
        Color endColor = Color.rgb(
            (int)(255 * (1 - newValue)),
            (int)(255 * newValue),
            0
        );
        
        KeyValue progressStart = new KeyValue(progressBar.progressProperty(), oldValue, Interpolator.EASE_BOTH);
        KeyValue progressEnd = new KeyValue(progressBar.progressProperty(), newValue, Interpolator.EASE_BOTH);
        KeyValue colorStart = new KeyValue(barColor, startColor, Interpolator.EASE_BOTH);
        KeyValue colorEnd = new KeyValue(barColor, endColor, Interpolator.EASE_BOTH);
        
        timeline = new Timeline(
            new KeyFrame(Duration.ZERO, progressStart, colorStart),
            new KeyFrame(duration, progressEnd, colorEnd)
        );
        
        timeline.play();
    }
    
    public String getLabelText() {
        return labelText.get();
    }
    
    public void setLabelText(String text) {
        labelText.set(text);
    }
    
    public StringProperty labelTextProperty() {
        return labelText;
    }
    
    public double getProgress() {
        return progress.get();
    }
    
    public void setProgress(double value) {
        progress.set(value);
    }
    
    public DoubleProperty progressProperty() {
        return progress;
    }
    
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
