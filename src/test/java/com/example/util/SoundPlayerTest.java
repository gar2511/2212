package com.example.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Clip;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SoundPlayer} class.
 */
class SoundPlayerTest {

    private SoundPlayer soundPlayer;

    @BeforeEach
    void setUp() {
        soundPlayer = new SoundPlayer();
    }

    @Test
    void testSetFile() {
        assertDoesNotThrow(() -> soundPlayer.setFile(0), "setFile should not throw an exception for valid index.");
    }

    @Test
    void testPlay() {
        soundPlayer.setFile(0); // Load a valid sound file
        assertDoesNotThrow(() -> soundPlayer.play(), "play should not throw an exception for a valid audio clip.");
    }

    @Test
    void testLoop() {
        soundPlayer.setFile(0); // Load a valid sound file
        assertDoesNotThrow(() -> soundPlayer.loop(), "loop should not throw an exception for a valid audio clip.");
    }

    @Test
    void testStop() {
        soundPlayer.setFile(0); // Load a valid sound file
        soundPlayer.play();
        assertDoesNotThrow(() -> soundPlayer.stop(), "stop should not throw an exception.");
    }

    @Test
    void testSetVolumeValid() {
        soundPlayer.setFile(0); // Load a valid sound file
        assertDoesNotThrow(() -> soundPlayer.setVolume(0.5f), "setVolume should not throw an exception for valid volume.");
    }

    @Test
    void testSetVolumeInvalid() {
        soundPlayer.setFile(0); // Load a valid sound file
        assertThrows(IllegalArgumentException.class, () -> soundPlayer.setVolume(1.5f), "setVolume should throw an exception for invalid volume > 1.");
        assertThrows(IllegalArgumentException.class, () -> soundPlayer.setVolume(-0.5f), "setVolume should throw an exception for invalid volume < 0.");
    }

    @Test
    void testGetVolume() {
        soundPlayer.setFile(0); // Load a valid sound file
        soundPlayer.setVolume(0.7f);
        assertEquals(0.7f, soundPlayer.getVolume(), 0.1f, "getVolume should return the correct volume.");
    }

    @Test
    void testSetZeroPosition() {
        soundPlayer.setFile(0); // Load a valid sound file
        soundPlayer.play();
        assertDoesNotThrow(() -> soundPlayer.setZeroPosition(), "setZeroPosition should not throw an exception.");
    }

    @Test
    void testFlush() {
        soundPlayer.setFile(0); // Load a valid sound file
        soundPlayer.play();
        assertDoesNotThrow(() -> soundPlayer.flush(), "flush should not throw an exception.");
    }

    @Test
    void testClose() {
        soundPlayer.setFile(0); // Load a valid sound file
        assertDoesNotThrow(() -> soundPlayer.close(), "close should not throw an exception.");
    }

    @Test
    void testMultipleFiles() {
        assertDoesNotThrow(() -> soundPlayer.setFile(0), "setFile should work for index 0.");
        assertDoesNotThrow(() -> soundPlayer.setFile(1), "setFile should work for index 1.");
        assertDoesNotThrow(() -> soundPlayer.setFile(2), "setFile should work for index 2.");
    }


}
