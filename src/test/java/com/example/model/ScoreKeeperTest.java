package com.example.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreKeeperTest {

    private ScoreKeeper scoreKeeper;

    @BeforeEach
    void setUp() {
        scoreKeeper = new ScoreKeeper(10); // Initialize with 10 points per second
    }

    @Test
    void testInitialScore() {
        assertEquals(0, scoreKeeper.getScore(), "Initial score should be 0.");
    }

    @Test
    void testSetScore() {
        scoreKeeper.setScore(50);
        assertEquals(50, scoreKeeper.getScore(), "Score should be updated to 50.");
    }

    @Test
    void testIncreaseScore() {
        scoreKeeper.setScore(20);
        scoreKeeper.setScore(scoreKeeper.getScore() + 10); // Manually increase score
        assertEquals(30, scoreKeeper.getScore(), "Score should increase by 10 to 30.");
    }

    @Test
    void testResetScore() {
        scoreKeeper.setScore(100);
        scoreKeeper.reset();
        assertEquals(0, scoreKeeper.getScore(), "Score should reset to 0.");
    }


    @Test
    void testScorePropertyBinding() {
        // Bind the score property to another integer
        IntegerProperty testProperty = new SimpleIntegerProperty();
        testProperty.bind(scoreKeeper.scoreProperty());

        // Update the score and verify the bound property is updated
        scoreKeeper.setScore(42);
        assertEquals(42, testProperty.get(), "Bound property should reflect the updated score.");
    }

    @Test
    void testToString() {
        scoreKeeper.setScore(75);
        assertEquals("ScoreKeeper{score=75}", scoreKeeper.toString(), "toString should return the correct representation.");
    }
}
