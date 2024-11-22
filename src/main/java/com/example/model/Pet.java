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

    /**
     * Default no-arguments constructor.
     * Required for Jackson JSON serialization/deserialization.
     */
    public Pet() {
    }

    /**
     * Constructs a new {@code Pet} with the specified name.
     *
     * @param name The name to assign to the pet.
     */
    public Pet(String name, String species) {
        this.name = name;
        this.species = species;
        this.stats = new VitalStats();
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

    public void performAction(PetAction action) {
        action.execute(stats);
    }

}

