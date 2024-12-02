package com.example.model;

/**
 * Model class representing a virtual pet in the game.
 * Encapsulates the pet's attributes, including name, species, stats, inventory, and gameplay-related data.
 * Provides methods for accessing and modifying these attributes.
 */
public class Pet {

    // The name of the pet
    private String name;

    // The species of the pet
    private String species;

    // The vital statistics of the pet
    private VitalStats stats;

    // The inventory associated with the pet
    private Inventory inventory;

    // The save ID for the pet's game state
    private int saveID;

    // States associated with the pet
    private int[] states;

    // Default item slot for actions involving items 1 or 2
    private int defaultItem12 = 1;

    // Default item slot for actions involving items 3 or 4
    private int defaultItem34 = 3;

    // The pet's current score
    private int score = 0;

    // Total time spent in seconds
    private long totalTimeSpent = 0;

    // Time limit in seconds (0 indicates no limit)
    private long timeLimit = 0;

    // Current playtime in milliseconds for the session
    private long currentPlayTime = 0;

    /**
     * Default no-arguments constructor.
     * Required for JSON serialization/deserialization.
     */
    public Pet() {
        this.stats = new VitalStats();
        this.inventory = new Inventory();
    }

    /**
     * Constructs a new {@code Pet} with the specified name, species, and save ID.
     *
     * @param name    The name to assign to the pet.
     * @param species The species of the pet.
     * @param saveID  The save ID associated with the pet's game state.
     */
    public Pet(String name, String species, int saveID) {
        this.name = name;
        this.species = species;
        this.stats = new VitalStats();
        this.inventory = new Inventory();
        this.saveID = saveID;
    }

    /**
     * Retrieves the name of the pet.
     *
     * @return The current name of the pet.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the pet.
     *
     * @param name The new name to assign to the pet.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the species of the pet.
     *
     * @return The species of the pet.
     */
    public String getSpecies() {
        return species;
    }

    /**
     * Retrieves the vital statistics of the pet.
     *
     * @return A {@link VitalStats} object representing the pet's stats.
     */
    public VitalStats getStats() {
        return stats;
    }

    /**
     * Retrieves the save ID associated with the pet.
     *
     * @return The save ID of the pet.
     */
    public int getSaveID() {
        return saveID;
    }

    /**
     * Retrieves the inventory associated with the pet.
     *
     * @return The {@link Inventory} object representing the pet's inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Called when a stat threshold is reached.
     * Placeholder for additional logic, such as changing pet mood or triggering events.
     */
    private void handleThreshold() {
        System.out.println("Threshold reached for " + name + ". Consider taking action!");
    }

    /**
     * Executes an action on the pet, modifying its stats.
     *
     * @param action The {@link PetAction} to perform on the pet.
     */
    public void performAction(PetAction action) {
        action.execute(stats);
    }

    /**
     * Retrieves the default item slot for actions involving items 1 or 2.
     *
     * @return The default item slot (1 or 2).
     */
    public int getDefaultItem12() {
        return defaultItem12;
    }

    /**
     * Sets the default item slot for actions involving items 1 or 2.
     *
     * @param defaultItem12 The item slot to set (1 or 2).
     */
    public void setDefaultItem12(int defaultItem12) {
        this.defaultItem12 = defaultItem12;
    }

    /**
     * Retrieves the default item slot for actions involving items 3 or 4.
     *
     * @return The default item slot (3 or 4).
     */
    public int getDefaultItem34() {
        return defaultItem34;
    }

    /**
     * Sets the default item slot for actions involving items 3 or 4.
     *
     * @param defaultItem34 The item slot to set (3 or 4).
     */
    public void setDefaultItem34(int defaultItem34) {
        this.defaultItem34 = defaultItem34;
    }

    /**
     * Retrieves the current score of the pet.
     *
     * @return The pet's score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the pet.
     *
     * @param value The new score value.
     */
    public void setScore(int value) {
        this.score = value;
    }

    /**
     * Retrieves the total time spent with the pet.
     *
     * @return The total time spent in seconds.
     */
    public long getTotalTimeSpent() {
        return totalTimeSpent;
    }

    /**
     * Adds time spent to the pet's total playtime.
     *
     * @param seconds The additional time to add, in seconds.
     */
    public void addTimeSpent(long seconds) {
        this.totalTimeSpent += seconds;
    }

    /**
     * Resets the total time spent with the pet to zero.
     */
    public void resetTotalTimeSpent() {
        this.totalTimeSpent = 0;
    }

    /**
     * Retrieves the current time limit for the pet.
     *
     * @return The time limit in seconds (0 if no limit is set).
     */
    public long getTimeLimit() {
        return timeLimit;
    }

    /**
     * Sets the time limit for the pet.
     *
     * @param timeLimit The time limit to set, in seconds.
     */
    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * Retrieves the current playtime for the session.
     *
     * @return The current playtime in milliseconds.
     */
    public long getCurrentPlayTime() {
        return currentPlayTime;
    }

    /**
     * Sets the current playtime for the session.
     *
     * @param playTime The current playtime to set, in milliseconds.
     */
    public void setCurrentPlayTime(long playTime) {
        this.currentPlayTime = playTime;
    }
}
