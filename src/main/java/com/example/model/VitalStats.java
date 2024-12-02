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

    // petState array to represent the state of each stat: 0 = OK, 1 = Critical
    private final int[] petState = {0, 0, 0, 0}; // Index 0 = Hunger, 1 = Happiness, 2 = Energy, 3 = Health

    // Define thresholds for stats
    private static final int[] CRITICAL_THRESHOLD = {20, 25, 0, 0}; // Critical threshold for warnings

    private boolean suppressListeners = false;

    private boolean alive = true;  // Add this field
    /**
     * Constructor initializes vital stats with default values and attaches listeners
     * to automatically clamp values and update states when properties change.
     */
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
     * Sets the modifier affecting the pet's health stat.
     *
     * @return healthMod The new health modifier value.
     */
    public int getHealthMod() {
        return healthMod;
    }
    /**
     * Sets the modifier affecting the pet's happiness stat.
     *
     * @return happinessMod The new happiness modifier value.
     */
    public int getHappinessMod() {
        return happinessMod;
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
     * Sets the modifier affecting the pet's hunger stat.
     *
     * @param hungerMod The new hunger modifier value.
     */
    public void setHungerMod(int hungerMod) {
        this.hungerMod = hungerMod;
    }

    public void setHealthMod(int healthMod) {
        this.healthMod = healthMod;
    }
    /**
     * Sets the modifier affecting the pet's energy stat.
     *
     * @param energyMod The new energy modifier value.
     */
    public void setEnergyMod(int energyMod) {
        this.energyMod = energyMod;
    }

    public void setHappinessMod(int happinessMod) {
        this.happinessMod = happinessMod;
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

    // Update petState array based on the stat value
    private void updatePetState(int index, int newValue) {
        // Handle critical state
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


    // Reset all modifiers first
        int totalHealthMod = 0;
        int totalEnergyMod = 0;
        int totalHungerMod = 0;
        int totalHappinessMod = 0;

        // Get current values
        int currentHunger = hunger.get();
        int currentHappiness = happiness.get();
        int currentEnergy = energy.get();
        int currentHealth = health.get();

        // Hunger effects
        if (currentHunger <= 50) {
            // Hunger affects energy more when there's a big difference
            if (currentHunger + 20 < currentEnergy) {
                totalEnergyMod += 3;    // Significant energy drain when hungry
            }
            // Hunger affects happiness when there's a difference
            if (currentHunger + 15 < currentHappiness) {
                totalHappinessMod += 2; // Being hungry makes you unhappy
            }
            // Only affect health when critically low
            if (currentHunger <= 20) {
                totalHealthMod += 1;    // Malnutrition starts affecting health
            }
        }
        
        // Happiness effects
        if (currentHappiness <= 50) {
            // Happiness primarily affects energy when there's a big gap
            if (currentHappiness + 25 < currentEnergy) {
                totalEnergyMod += 2;    // Depression drains energy
            }
        }
        
        // Energy effects
        if (currentEnergy <= 50) {
            // Energy primarily affects happiness when there's a significant difference
            if (currentEnergy + 20 < currentHappiness) {
                totalHappinessMod += 2; // Being tired makes you grumpy
            }
        }
        
        // Health effects - affects everything when low
        if (currentHealth <= 50) {
            // Health affects all stats more severely when there's a big difference
            if (currentHealth + 30 < currentEnergy) {
                totalEnergyMod += 3;    // Poor health severely affects energy
            }
            if (currentHealth + 25 < currentHappiness) {
                totalHappinessMod += 2; // Being sick makes you unhappy
            }
            if (currentHealth + 20 < currentHunger) {
                totalHungerMod += 2;    // Sickness affects appetite
            }
        }

        // Apply accumulated modifiers
        setHealthMod(totalHealthMod);
        setEnergyMod(totalEnergyMod);
        setHungerMod(totalHungerMod);
        setHappinessMod(totalHappinessMod);
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

    /**
     * Restores all stats to their maximum values and resets pet states.
     */
    public void restoreAll() {
        suppressListeners = true; // Suppress listeners to avoid redundant updates
        setHealth(100);
        setEnergy(100);
        setHunger(100);
        setHappiness(100);
        suppressListeners = false; // Re-enable listeners
        Arrays.fill(petState, 0); // Reset all states to normal
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
        // If setting to true, ensure health is above 0
        if (alive && health.get() <= 0) {
            setHealth(1);
        }
    }
}
