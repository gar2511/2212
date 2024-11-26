package com.example.model;

/**
 * Model class representing a virtual pet in the game.
 * Encapsulates the pet's name and provides constructors for creation
 * and methods for accessing and modifying the pet's name.
 */
public class Pet {
    // The name of the pet
    private String name;
    private String species;
    private VitalStats stats;

    private Inventory inventory;
    private int saveID;
    private int[] states;
    private int defaultItem12 = 1; // Default to item 1
    private int defaultItem34 = 3; // Default to item 3


    /**
     * Default no-arguments constructor.
     * Required for Jackson JSON serialization/deserialization.
     */
    public Pet() {
        this.stats = new VitalStats();
        this.inventory = new Inventory();
    }


    /**
     * Constructs a new {@code Pet} with the specified name.
     *
     * @param name The name to assign to the pet.
     */
    public Pet(String name, String species,int saveID) {
        this.name = name;
        this.species = species;
        this.stats = new VitalStats();
        this.inventory = new Inventory();
        this.saveID = saveID;
        this.defaultItem12 = 1;
        this.defaultItem34 = 3;
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
    public String getSpecies() {
        return species;
    }
    public VitalStats getStats() {
        return stats;
    }

    public int getSaveID() {
        return saveID;
    }
    /**
     * Retrieves the inventory of the pet.
     *
     * @return The {@link Inventory} object representing the pet's inventory.
     */
    public Inventory getInventory() {return inventory;}

    // Called when a stat threshold is reached
    private void handleThreshold() {
        System.out.println("Threshold reached for " + name + ". Consider taking action!");
        // Add additional logic, e.g., changing pet mood or triggering events
    }
    public void performAction(PetAction action) {
        action.execute(stats);
    }
    public int getDefaultItem12() {
        return defaultItem12;
    }

    public void setDefaultItem12(int defaultItem12) {
        this.defaultItem12 = defaultItem12;
    }

    public int getDefaultItem34() {
        return defaultItem34;
    }

    public void setDefaultItem34(int defaultItem34) {
        this.defaultItem34 = defaultItem34;
    }

}

