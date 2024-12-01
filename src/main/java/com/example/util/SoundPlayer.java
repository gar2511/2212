package com.example.util;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundPlayer {

    private Clip clip;
    private URL[] soundURL = new URL[5];

    public SoundPlayer() {
        soundURL[0] = getClass().getResource("/sounds/overture.wav");   // background music
        soundURL[1] = getClass().getResource("/sounds/water.wav");      // initialize with other .wav files for use in other settings
        soundURL[1] = getClass().getResource("/sounds/water.wav");      // initialize with other .wav files for use in other settings
        soundURL[2] = getClass().getResource("/sounds/overture.wav");   // initialize with other .wav files for use in other settings
        soundURL[3] = getClass().getResource("/sounds/overture.wav");   // initialize with other .wav files for use in other settings
        soundURL[4] = getClass().getResource("/sounds/overture.wav");   // initialize with other .wav files for use in other settings
    }

    public void setFile(int i) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void close() {
        clip.close();
    }

    public void flush() {
        clip.flush();
    }

    public void setZeroPosition() {
        clip.setMicrosecondPosition(0);
    }

    // returns current volume
    // values range from 0.0 to 1.0
    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    // sets volume
    // values inputted should range from 0.0 to 1.0
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

}