package com.example.util;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Utility class for playing and managing audio clips in the application.
 * Provides functionality to play, loop, stop, and adjust the volume of audio.
 */
public class SoundPlayer {

    // Audio clip instance for playback
    private Clip clip;

    // Array of URLs pointing to sound files
    private URL[] soundURL = new URL[5];

    /**
     * Constructor initializes the sound URLs for use in the application.
     * Loads predefined sound files from the resources folder.
     */
    public SoundPlayer() {
        soundURL[0] = getClass().getResource("/sounds/overture.wav"); // Background music
        soundURL[1] = getClass().getResource("/sounds/water.wav");    // Additional sound effects
        soundURL[2] = getClass().getResource("/sounds/overture.wav");
        soundURL[3] = getClass().getResource("/sounds/overture.wav");
        soundURL[4] = getClass().getResource("/sounds/overture.wav");
    }

    /**
     * Loads the audio file corresponding to the specified index in the {@code soundURL} array.
     *
     * @param i The index of the sound file in the {@code soundURL} array.
     */
    public void setFile(int i) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts playing the loaded audio clip.
     */
    public void play() {
        clip.start();
    }

    /**
     * Starts playing the loaded audio clip in a continuous loop.
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops the playback of the current audio clip.
     */
    public void stop() {
        clip.stop();
    }

    /**
     * Closes the current audio clip and releases resources.
     */
    public void close() {
        clip.close();
    }

    /**
     * Flushes the audio clip's buffer, discarding all data currently in the buffer.
     */
    public void flush() {
        clip.flush();
    }

    /**
     * Resets the playback position of the current audio clip to the beginning.
     */
    public void setZeroPosition() {
        clip.setMicrosecondPosition(0);
    }

    /**
     * Retrieves the current volume level of the audio clip.
     *
     * @return The volume level as a float, ranging from 0.0 to 1.0.
     */
    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    /**
     * Sets the volume level of the audio clip.
     *
     * @param volume The desired volume level, ranging from 0.0 (mute) to 1.0 (maximum).
     * @throws IllegalArgumentException if the volume is not within the range [0.0, 1.0].
     */
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume not valid: " + volume);
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}
