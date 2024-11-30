package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;

public class VitalStats {
    private int hungerMod = 0;
    private int happinessMod = 0;
    private int healthMod = 0;
    private int energyMod = 0;

    private final IntegerProperty hunger = new SimpleIntegerProperty(80);      // Default: well-fed
    private final IntegerProperty happiness = new SimpleIntegerProperty(70);  // Default: fairly happy
    private final IntegerProperty energy = new SimpleIntegerProperty(100);    // Default: fully energized
    private final IntegerProperty health = new SimpleIntegerProperty(100);    // Default: healthy

    // petState array to represent the state of each stat: 0 = OK, 1 = Critical
    private final int[] petState = {0, 0, 0, 0}; // Index 0 = Hunger, 1 = Happiness, 2 = Energy, 3 = Health

    // Define thresholds for stats
    private static final int[] CRITICAL_THRESHOLD = {20, 25, 0, 0}; // Critical threshold for warnings

    private boolean suppressListeners = false;

    private boolean alive = true;  // Add this field

    public VitalStats() {
        // Add listeners to enforce clamping and update state
        hunger.addListener((observable, oldValue, newValue) -> {
            if (!suppressListeners) {
                hunger.set(clampValue(newValue.intValue()));
                updatePetState(0, hunger.get());
            }
        });

        happiness.addListener((observable, oldValue, newValue) -> {
            if (!suppressListeners) {
                happiness.set(clampValue(newValue.intValue()));
                updatePetState(1, happiness.get());
            }
        });

        energy.addListener((observable, oldValue, newValue) -> {
            if (!suppressListeners) {
                energy.set(clampValue(newValue.intValue()));
                updatePetState(2, energy.get());
            }
        });

        health.addListener((observable, oldValue, newValue) -> {
            if (!suppressListeners) {
                health.set(clampValue(newValue.intValue()));
                updatePetState(3, health.get());
            }
        });
    }

    // Getters for properties
    public IntegerProperty hungerProperty() {
        return hunger;
    }

    public IntegerProperty happinessProperty() {
        return happiness;
    }

    public IntegerProperty energyProperty() {
        return energy;
    }

    public IntegerProperty healthProperty() {
        return health;
    }

    // Getters and setters for values
    public int getHunger() {
        return hunger.get();
    }

    public void setHunger(int value) {
        hunger.set(clampValue(value));
    }

    public int getHappiness() {
        return happiness.get();
    }

    public void setHappiness(int value) {
        happiness.set(clampValue(value));
    }

    public int getEnergy() {
        return energy.get();
    }

    public void setEnergy(int value) {
        energy.set(clampValue(value));
    }

    public int getHealth() {
        return health.get();
    }

    public void setHealth(int value) {
        health.set(clampValue(value));
    }

    // Adjust methods
    public void increaseHealth(int value) {
        health.set(Math.min(100, health.get() + value));
    }

    public void decreaseHealth(int value) {
        health.set(Math.max(0, health.get() - value));
    }

    public void increaseHunger(int value) {
        hunger.set(Math.min(100, hunger.get() + value));
    }

    public void decreaseHunger(int value) {
        hunger.set(Math.max(0, hunger.get() - value));
    }

    public void increaseHappiness(int value) {
        happiness.set(Math.min(100, happiness.get() + value));
    }

    public void decreaseHappiness(int value) {
        happiness.set(Math.max(0, happiness.get() - value));
    }

    public void increaseEnergy(int value) {
        energy.set(Math.min(100, energy.get() + value));
    }

    public void decreaseEnergy(int value) {
        energy.set(Math.max(0, energy.get() - value));
    }

    // Modifier getters and setters
    public int getHungerMod() {
        return hungerMod;
    }

    public int getHealthMod() {
        return healthMod;
    }

    public int getHappinessMod() {
        return happinessMod;
    }

    public int getEnergyMod() {
        return energyMod;
    }

    public void setHungerMod(int hungerMod) {
        this.hungerMod = hungerMod;
    }

    public void setHealthMod(int healthMod) {
        this.healthMod = healthMod;
    }

    public void setEnergyMod(int energyMod) {
        this.energyMod = energyMod;
    }

    public void setHappinessMod(int happinessMod) {
        this.happinessMod = happinessMod;
    }

    public int[] getState() {
        return petState.clone();
    }

    @JsonProperty("state")
    public void setState(int[] state) {
        System.arraycopy(state, 0, petState, 0, Math.min(state.length, petState.length));
    }

    public int getVitalState(int index) {
        return petState[index];
    }

    // Update petState array based on the stat value
    private void updatePetState(int index, int newValue) {

        if (index == 2) { // Handle energy-specific logic
            System.out.println(getStatName(index) + " is currently: " + newValue);
            if (newValue == 0 && petState[index] == 0) {
                petState[index] = 1; // Set to critical state when energy first drops to 0
                System.out.println(getStatName(index) + " has dropped to 0! Entering critical state.");
            } else if (newValue == 100 && petState[index] == 1) {
                petState[index] = 0; // Exit critical state when energy fully restores to 100
                System.out.println(getStatName(index) + " is fully restored to 100! Exiting critical state.");
            }
            return; // Exit after handling energy
        }

        // Handle other stats
        if (newValue <= CRITICAL_THRESHOLD[index]) {
            petState[index] = 1; // Critical state
            System.out.println(getStatName(index) + " is critically low! Current value: " + newValue);
        } else {
            petState[index] = 0; // Normal state
            System.out.println(getStatName(index) + " is no longer critically low! Current value: " + newValue);
        }
    }



    // Helper to get the stat name from the index
    private String getStatName(int index) {
        switch (index) {
            case 0:
                return "Hunger";
            case 1:
                return "Happiness";
            case 2:
                return "Energy";
            case 3:
                return "Health";
            default:
                return "Unknown";
        }
    }

    // Clamp method to ensure values stay within range
    private int clampValue(int value) {
        return Math.max(0, Math.min(100, value));
    }

    // Restore all stats to their maximum values
    public void restoreAll() {
        suppressListeners = true; // Suppress listeners to avoid redundant updates
        setHealth(100);
        setEnergy(100);
        setHunger(100);
        setHappiness(100);
        suppressListeners = false; // Re-enable listeners
        Arrays.fill(petState, 0); // Reset all states to normal
    }

    @JsonProperty("alive")
    public boolean isAlive() {
        return health.get() > 0;
    }

    @JsonProperty("alive")
    public void setAlive(boolean alive) {
        this.alive = alive;
        // If setting to true, ensure health is above 0
        if (alive && health.get() <= 0) {
            setHealth(1);
        }
    }
}
