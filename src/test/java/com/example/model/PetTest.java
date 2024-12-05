package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class PetTest {

    private Pet pet;

    @BeforeEach
    void setUp() {
        pet = new Pet("Buddy", "Dog", 1);
    }

    @Test
    void testConstructor() {
        assertEquals("Buddy", pet.getName(), "Pet name should be 'Buddy'.");
        assertEquals("Dog", pet.getSpecies(), "Pet species should be 'Dog'.");
        assertNotNull(pet.getStats(), "VitalStats should not be null.");
        assertNotNull(pet.getInventory(), "Inventory should not be null.");
        assertEquals(1, pet.getSaveID(), "Save ID should be 1.");
    }

    @Test
    void testSetName() {
        pet.setName("Charlie");
        assertEquals("Charlie", pet.getName(), "Pet name should be updated to 'Charlie'.");
    }

    @Test
    void testDefaultItems() {
        assertEquals(1, pet.getDefaultItem12(), "Default item for 1/2 should be 1.");
        assertEquals(3, pet.getDefaultItem34(), "Default item for 3/4 should be 3.");

        pet.setDefaultItem12(2);
        pet.setDefaultItem34(4);

        assertEquals(2, pet.getDefaultItem12(), "Default item for 1/2 should be updated to 2.");
        assertEquals(4, pet.getDefaultItem34(), "Default item for 3/4 should be updated to 4.");
    }

    @Test
    void testScoreManagement() {
        assertEquals(0, pet.getScore(), "Initial score should be 0.");

        pet.setScore(100);
        assertEquals(100, pet.getScore(), "Score should be updated to 100.");
    }

    @Test
    void testTimeSpentManagement() {
        assertEquals(0, pet.getTotalTimeSpent(), "Total time spent should be 0 initially.");

        pet.addTimeSpent(120);
        assertEquals(120, pet.getTotalTimeSpent(), "Total time spent should be updated to 120 seconds.");

        pet.resetTotalTimeSpent();
        assertEquals(0, pet.getTotalTimeSpent(), "Total time spent should be reset to 0.");
    }

    @Test
    void testCurrentPlayTime() {
        assertEquals(0, pet.getCurrentPlayTime(), "Current play time should be 0 initially.");

        pet.setCurrentPlayTime(300);
        assertEquals(300, pet.getCurrentPlayTime(), "Current play time should be updated to 300 seconds.");

        pet.resetCurrentPlayTime();
        assertEquals(0, pet.getCurrentPlayTime(), "Current play time should be reset to 0.");
    }

    @Test
    void testTimeLimits() {
        assertEquals(0, pet.getTimeLimit(), "Time limit should be 0 (no limit) initially.");

        pet.setTimeLimit(3600);
        assertEquals(3600, pet.getTimeLimit(), "Time limit should be updated to 3600 seconds.");
    }

    @Test
    void testStartAndEndTimes() {
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        pet.saveStartTime(startTime);
        pet.saveEndTime(endTime);

        assertEquals(startTime, pet.getStartTime(), "Start time should be set to 9:00.");
        assertEquals(endTime, pet.getEndTime(), "End time should be set to 17:00.");
    }

    @Test
    void testPerformAction() {
        VitalStats stats = pet.getStats();
        PetAction action = (s) -> s.increaseHappiness(10);

        assertEquals(70, stats.getHappiness(), "Initial happiness should be 70.");

        pet.performAction(action);
        assertEquals(80, stats.getHappiness(), "Happiness should increase by 10 after the action.");
    }

    @Test
    void testInventoryManagement() {
        Inventory inventory = pet.getInventory();
        assertNotNull(inventory, "Inventory should not be null.");

    }
}
