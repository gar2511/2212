package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;

/**
 * Model class representing the vital statistics of a virtual pet.
 * Tracks and manages hunger, happiness, energy, and health levels with associated modifiers and states.
 */
public class VitalStats {

    // Modifiers affecting each stat
    private int hungerMod = 0;
    private int happinessMod = 0;
    private int healthMod = 0;
    private int energyMod = 0;

    // Properties representing current levels of each stat
    private final IntegerProperty hunger = new SimpleIntegerProperty(80);
    private final IntegerProperty happiness = new SimpleIntegerProperty(70);
    private final IntegerProperty energy = new SimpleIntegerProperty(100);
    private final IntegerProperty health = new SimpleIntegerProperty(100);

    // Array representing the state of each stat: 0 = OK, 1 = Critical
    private final int[] petState = {0, 0, 0, 0};

    // Thresholds for stats to be considered in critical state
    private static final int[] CRITICAL_THRESHOLD = {20, 25, 0, 0};

    private boolean suppressListeners = false;
    private boolean alive = true;

    /**
     * Constructor initializes vital stats with default values and attaches listeners
     * to automatically clamp values and update states when properties change.
     */
    public VitalStats() {
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

    // Getters for property objects
    /**
     * Gets the property representing the hunger level of the pet.
     * This property can be bound to UI components for real-time updates.
     * @return The hunger property.
     */
    public IntegerProperty hungerProperty() {
        return hunger;
    }
    /**
     * Gets the property representing the happiness level of the pet.
     *
     * @return The happiness property.
     */
    public IntegerProperty happinessProperty() {
        return happiness;
    }
    /**
     * Gets the property representing the energy level of the pet.
     *
     * @return The energy property.
     */
    public IntegerProperty energyProperty() {
        return energy;
    }
    /**
     * Gets the property representing the health level of the pet.
     *
     * @return The health property.
     */
    public IntegerProperty healthProperty() {
        return health;
    }

    // Getters and setters for individual stat values
    /**
     * Gets the current hunger level of the pet.
     *
     * @return The current hunger value (0-100).
     */
    public int getHunger() {
        return hunger.get();
    }
    /**
     * Sets the hunger level of the pet.
     * Automatically clamps the value between 0 and 100.
     *
     * @param value The new hunger value.
     */
    public void setHunger(int value) {
        hunger.set(clampValue(value));
    }
    /**
     * Gets the current happiness level of the pet.
     *
     * @return The current happiness value (0-100).
     */
    public int getHappiness() {
        return happiness.get();
    }
    /**
     * Sets the happiness level of the pet.
     *
     * @param value The new happiness value.
     */
    public void setHappiness(int value) {
        happiness.set(clampValue(value));
    }
    /**
     * Gets the current energy level of the pet.
     *
     * @return The current energy value (0-100).
     */
    public int getEnergy() {
        return energy.get();
    }
    /**
     * Sets the energy level of the pet.
     * Automatically clamps the value between 0 and 100.
     *
     * @param value The new energy value.
     */
    public void setEnergy(int value) {
        energy.set(clampValue(value));
    }
    /**
     * Gets the current health level of the pet.
     *
     * @return The current health value (0-100).
     */
    public int getHealth() {
        return health.get();
    }
    /**
     * Sets the health level of the pet.
     * Automatically clamps the value between 0 and 100.
     *
     * @param value The new health value.
     */
    public void setHealth(int value) {
        health.set(clampValue(value));
    }

    // Adjust methods for modifying stats incrementally
    /**
     * Increases the pet's health by the specified value.
     * The health level is capped at a maximum of 100.
     *
     * @param value The amount to increase health.
     */
    public void increaseHealth(int value) {
        health.set(Math.min(100, health.get() + value));
    }
    /**
     * Decreases the pet's health by the specified value.
     * The health level cannot drop below 0.
     *
     * @param value The amount to decrease health.
     */
    public void decreaseHealth(int value) {
        health.set(Math.max(0, health.get() - value));
    }
    /**
     * Increases the pet's hunger by the specified value.
     * The hunger level is capped at a maximum of 100.
     *
     * @param value The amount to increase hunger.
     */
    public void increaseHunger(int value) {
        hunger.set(Math.min(100, hunger.get() + value));
    }
    /**
     * Decreases the pet's hunger by the specified value.
     * The hunger level cannot drop below 0.
     *
     * @param value The amount to decrease hunger.
     */
    public void decreaseHunger(int value) {
        hunger.set(Math.max(0, hunger.get() - value));
    }
    /**
     * Increases the pet's happiness by the specified value.
     * The happiness level is capped at a maximum of 100.
     *
     * @param value The amount to increase happiness.
     */
    public void increaseHappiness(int value) {
        happiness.set(Math.min(100, happiness.get() + value));
    }
    /**
     * Decreases the pet's happiness by the specified value.
     * The happiness level cannot drop below 0.
     *
     * @param value The amount to decrease happiness.
     */
    public void decreaseHappiness(int value) {
        happiness.set(Math.max(0, happiness.get() - value));
    }

    /**
     * Increases the pet's energy by the specified value.
     * The energy level is capped at a maximum of 100.
     *
     * @param value The amount to increase energy.
     */
    public void increaseEnergy(int value) {
        energy.set(Math.min(100, energy.get() + value));
    }
    /**
     * Decreases the pet's energy by the specified value.
     * The energy level cannot drop below 0.
     *
     * @param value The amount to decrease energy.
     */
    public void decreaseEnergy(int value) {
        energy.set(Math.max(0, energy.get() - value));
    }

    // Modifiers for stats
    /**
     * Gets the modifier affecting the pet's hunger stat.
     *
     * @return The hunger modifier.
     */
    public int getHungerMod() {
        return hungerMod;
    }
    /**
     * Sets the modifier affecting the pet's hunger stat.
     *
     * @param hungerMod The new hunger modifier value.
     */
    public void setHungerMod(int hungerMod) {
        this.hungerMod = hungerMod;
    }

    /**
     * Gets the modifier affecting the pet's health stat.
     *
     * @return The health modifier.
     */
    public int getHealthMod() {
        return healthMod;
    }

    /**
     * Sets the modifier affecting the pet's health stat.
     *
     * @param healthMod The new health modifier value.
     */
    public void setHealthMod(int healthMod) {
        this.healthMod = healthMod;
    }

    /**
     * Gets the modifier affecting the pet's happiness stat.
     *
     * @return The happiness modifier.
     */
    public int getHappinessMod() {
        return happinessMod;
    }

    /**
     * Sets the modifier affecting the pet's happiness stat.
     *
     * @param happinessMod The new happiness modifier value.
     */
    public void setHappinessMod(int happinessMod) {
        this.happinessMod = happinessMod;
    }

    /**
     * Gets the modifier affecting the pet's energy stat.
     *
     * @return The energy modifier.
     */
    public int getEnergyMod() {
        return energyMod;
    }

    /**
     * Sets the modifier affecting the pet's energy stat.
     *
     * @param energyMod The new energy modifier value.
     */
    public void setEnergyMod(int energyMod) {
        this.energyMod = energyMod;
    }

    // Methods to get and set pet states
    /**
     * Gets the current state of the pet's stats.
     * Each index in the array represents a stat:
     * 0 = Hunger, 1 = Happiness, 2 = Energy, 3 = Health.
     * A value of 0 indicates OK, and 1 indicates Critical.
     *
     * @return A copy of the pet state array.
     */
    public int[] getState() {
        return petState.clone();
    }

    /**
     * Sets the current state of the pet's stats.
     * Updates the pet state array with the provided values.
     *
     * @param state An array representing the new state of the pet's stats.
     */
    @JsonProperty("state")
    public void setState(int[] state) {
        System.arraycopy(state, 0, petState, 0, Math.min(state.length, petState.length));
    }

    /**
     * Gets the state of a specific stat by index.
     * Index mapping: 0 = Hunger, 1 = Happiness, 2 = Energy, 3 = Health.
     *
     * @param index The index of the stat to retrieve.
     * @return The state of the stat (0 = OK, 1 = Critical).
     */
    public int getVitalState(int index) {
        return petState[index];
    }


    // Updates pet state and applies modifiers based on current stat values
    private void updatePetState(int index, int newValue) {
        if (newValue <= CRITICAL_THRESHOLD[index]) {
            petState[index] = 1;
            System.out.println(getStatName(index) + " is critically low! Current value: " + newValue);
        } else {
            petState[index] = 0;
        }

        applyModifiers();
    }

    // Applies calculated modifiers to the stats
    private void applyModifiers() {
        setHealthMod(calculateHealthModifier());
        setEnergyMod(calculateEnergyModifier());
        setHungerMod(calculateHungerModifier());
        setHappinessMod(calculateHappinessModifier());
    }

    // Helpers to calculate modifiers
    private int calculateHealthModifier() {
        return hunger.get() <= 20 ? 1 : 0;
    }

    private int calculateEnergyModifier() {
        return happiness.get() <= 50 ? 2 : 0;
    }

    private int calculateHungerModifier() {
        return energy.get() <= 50 ? 2 : 0;
    }

    private int calculateHappinessModifier() {
        return health.get() <= 50 ? 2 : 0;
    }

    // Helper to get stat names
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

    // Clamps a value to the range [0, 100]
    private int clampValue(int value) {
        return Math.max(0, Math.min(100, value));
    }

    /**
     * Restores all stats to their maximum values and resets pet states.
     */
    public void restoreAll() {
        suppressListeners = true;
        setHealth(100);
        setEnergy(100);
        setHunger(100);
        setHappiness(100);
        suppressListeners = false;
        Arrays.fill(petState, 0);
    }

    /**
     * Checks whether the pet is alive based on its health.
     *
     * @return {@code true} if the pet's health is above 0, {@code false} otherwise.
     */
    @JsonProperty("alive")
    public boolean isAlive() {
        return health.get() > 0;
    }

    /**
     * Sets the alive status of the pet and adjusts its health accordingly.
     *
     * @param alive {@code true} to set the pet as alive, {@code false} otherwise.
     */
    @JsonProperty("alive")
    public void setAlive(boolean alive) {
        this.alive = alive;
        if (alive && health.get() <= 0) {
            setHealth(1);
        }
    }
}
