package com.example.components;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class StatBarTest extends ApplicationTest {

    private StatBar statBar;

    @Override
    public void start(Stage stage) {
        statBar = new StatBar();
        Scene scene = new Scene(statBar, 300, 100);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        Platform.runLater(() -> {
            statBar.setLabelText("Health");
            statBar.setProgress(1.0); // Full progress initially
        });
    }

    @Test
    public void testInitialLabelText() {
        Platform.runLater(() -> assertEquals("Health", statBar.getLabelText(), "Initial label text should be 'Health'."));
    }

    @Test
    public void testUpdateLabelText() {
        Platform.runLater(() -> {
            statBar.setLabelText("Mana");
            assertEquals("Mana", statBar.getLabelText(), "Label text should update to 'Mana'.");
        });
    }

    @Test
    public void testInitialProgress() {
        Platform.runLater(() -> assertEquals(1.0, statBar.getProgress(), "Initial progress should be 1.0."));
    }

    @Test
    public void testUpdateProgressWithinRange() {
        Platform.runLater(() -> {
            statBar.setProgress(0.5);
            assertEquals(0.5, statBar.getProgress(), "Progress should update to 0.5.");
        });
    }

    @Test
    public void testUpdateProgressOutOfRange() {
        Platform.runLater(() -> {
            statBar.setProgress(1.5); // Exceeds the maximum range of 1.0
            assertEquals(1.0, statBar.getProgress(), "Progress should be capped at 1.0.");

            statBar.setProgress(-0.1); // Below the minimum range of 0.0
            assertEquals(0.0, statBar.getProgress(), "Progress should be capped at 0.0.");
        });
    }

    @Test
    public void testColorTransitionOnProgressChange() {
        Platform.runLater(() -> {
            // Set progress to mid-range and check color
            statBar.setProgress(0.5);
            ProgressBar progressBar = statBar.getProgressBar();

            // Validate that the progress bar color changes accordingly
            String expectedStyle = "-fx-accent: rgb(128, 128, 0);"; // Greenish-yellow for 0.5 progress
            assertTrue(progressBar.getStyle().contains(expectedStyle),
                    "Progress bar color should transition to greenish-yellow.");
        });
    }

    @Test
    public void testSmoothAnimationBetweenProgressValues() {
        Platform.runLater(() -> {
            statBar.setProgress(0.75);

            // Simulate animation midway
            double midProgress = 0.6;
            statBar.setProgress(midProgress);
            assertNotEquals(midProgress, statBar.getProgressBar().getProgress(),
                    "Progress should animate smoothly rather than jumping directly.");
        });
    }

    @Test
    public void testLabelAndProgressSynchronization() {
        Platform.runLater(() -> {
            statBar.setLabelText("Energy");
            statBar.setProgress(0.3);

            assertEquals("Energy", statBar.getLabelText(), "Label text should match 'Energy'.");
            assertEquals(0.3, statBar.getProgress(), "Progress should update to 0.3.");
        });
    }
}
