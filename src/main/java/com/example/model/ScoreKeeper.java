package com.example.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

/**
 * A utility class for managing a score that increases over time.
 * The score is updated periodically based on a specified rate of points per second.
 */
public class ScoreKeeper {

    // Property to track the current score
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    // Timeline for periodically updating the score
    private final Timeline timeline;

    // Points gained per second
    private final int pointsPerSecond;

    /**
     * Constructs a ScoreKeeper with the specified points gained per second.
     *
     * @param pointsPerSecond Number of points gained per second.
     */
    public ScoreKeeper(int pointsPerSecond) {
        this.pointsPerSecond = pointsPerSecond;

        // Create a timeline that updates the score every second
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> increaseScore(pointsPerSecond))
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Runs indefinitely
    }

    /**
     * Starts the scorekeeper's timer, causing the score to increase over time.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Stops the scorekeeper's timer, halting the periodic score updates.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Resets the score to zero and stops the scorekeeper's timer.
     */
    public void reset() {
        stop();
        setScore(0);
    }

    /**
     * Increases the score by a specified amount.
     *
     * @param points The amount of points to add to the current score.
     */
    private void increaseScore(int points) {
        setScore(getScore() + points);
    }

    /**
     * Retrieves the current score.
     *
     * @return The current score as an integer.
     */
    public int getScore() {
        return score.get();
    }

    /**
     * Sets the score to a specified value.
     *
     * @param value The new score value.
     */
    public void setScore(int value) {
        score.set(value);
    }

    /**
     * Retrieves the score property, allowing for binding and property listeners.
     *
     * @return An {@link IntegerProperty} representing the current score.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Returns a string representation of the ScoreKeeper, including the current score.
     *
     * @return A string describing the current state of the ScoreKeeper.
     */
    @Override
    public String toString() {
        return "ScoreKeeper{score=" + getScore() + "}";
    }
}
