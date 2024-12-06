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

/**
 * StatBar is a custom control combining a label and a progress bar with animated transitions.
 * <br><br>
 * This control dynamically updates its progress and color with smooth animations based on value changes.
 *
 * <b>Example Use:</b>
 * <pre>{@code
 * StatBar statBar = new StatBar();
 * statBar.setLabelText("Health");
 * statBar.setProgress(0.75);
 * }</pre>
 *
 * <b>Example Output:</b>
 * A horizontal bar displaying a label and a progress bar with animations reflecting changes.
 *
 * @version 1.0
 * @author Gary, Kevin, Michael, Rajvir, Zein
 */
public class StatBar extends HBox {

    /** The label displayed on the left side of the StatBar. */
    private final Label label;

    /** The progress bar displaying the current progress value. */
    private final ProgressBar progressBar;

    /** The text displayed in the label. */
    private final StringProperty labelText = new SimpleStringProperty();

    /** The current progress value of the bar. */
    private final DoubleProperty progress = new SimpleDoubleProperty();

    /** The color of the progress bar. */
    private final ObjectProperty<Color> barColor = new SimpleObjectProperty<>(Color.GREEN);

    /** The animation timeline for progress and color changes. */
    private Timeline timeline;

    /** The interval used for decay animations. */
    private static final Duration DECAY_INTERVAL = Duration.seconds(1);

    /** The target progress value for animations. */
    private double targetValue = 1.0;

    /** The last recorded progress value, used to determine animation direction. */
    private double lastValue = 1.0;

    /**
     * Constructs a new StatBar with default settings.
     * <p>
     * The StatBar initializes with a label, a progress bar, and properties to manage progress and color.
     * </p>
     */
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

    /**
     * Animates the progress and color of the StatBar.
     * <p>
     * Animations occur smoothly over the specified duration.
     * </p>
     *
     * @param oldValue the previous progress value
     * @param newValue the target progress value
     * @param duration the duration of the animation
     */
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

    /**
     * Gets the text displayed in the label.
     *
     * @return the label text
     */
    public String getLabelText() {
        return labelText.get();
    }

    /**
     * Sets the text displayed in the label.
     *
     * @param text the label text to set
     */
    public void setLabelText(String text) {
        labelText.set(text);
    }

    /**
     * The label text property.
     *
     * @return the label text property
     */
    public StringProperty labelTextProperty() {
        return labelText;
    }

    /**
     * Gets the current progress value.
     *
     * @return the progress value
     */
    public double getProgress() {
        return progress.get();
    }

    /**
     * Sets the current progress value.
     *
     * @param value the progress value to set
     */
    public void setProgress(double value) {
        progress.set(value);
    }

    /**
     * The progress value property.
     *
     * @return the progress value property
     */
    public DoubleProperty progressProperty() {
        return progress;
    }

    /**
     * Gets the ProgressBar component of the StatBar.
     *
     * @return the ProgressBar
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
