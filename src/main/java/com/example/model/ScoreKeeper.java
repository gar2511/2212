package com.example.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

/**
 * A scorekeeper that increases the score over time.
 */
public class ScoreKeeper {
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final Timeline timeline;

    /**
     * Constructs a ScoreKeeper with the specified points gained per second.
     *
     * @param pointsPerSecond Number of points gained per second.
     */
    public ScoreKeeper(int pointsPerSecond) {
        // Points gained per second

        // Create a timeline that updates the score every second
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), _ -> increaseScore(pointsPerSecond))
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Runs indefinitely
    }

    /**
     * Starts the scorekeeper's timer.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the scorekeeper's timer.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Increases the score by a specified amount.
     *
     * @param points The amount of points to add.
     */
    private void increaseScore(int points) {
        setScore(getScore() + points);
    }

    // Getter and setter for the score
    public int getScore() {
        return score.get();
    }

    public void setScore(int value) {
        score.set(value);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    @Override
    public String toString() {
        return "ScoreKeeper{score=" + getScore() + "}";
    }
}
