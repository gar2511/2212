package com.example.model;

/**
 * Functional interface representing an action that can be performed on a pet.
 * The action modifies the pet's vital statistics.
 */
public interface PetAction {

    /**
     * Executes the action on the provided {@link VitalStats}.
     *
     * @param stats The {@link VitalStats} object representing the pet's vital statistics.
     */
    void execute(VitalStats stats);
}
