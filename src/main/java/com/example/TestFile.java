package com.example;

import com.example.model.Pet;
import com.example.model.VitalStats;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test file for the Pet class.
 * Covers functionality such as name and species management, inventory handling,
 * score tracking, and time management.
 */
class PetTest {

    private Pet pet;

    /**
     * Set up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        pet = new Pet("TestPet", "mole", 1); // Initialize with default values
    }

    /**
     * Clean up resources after each test.
     */
    @AfterEach
    void tearDown() {
        pet = null;
    }

    /**
     * Test name and species assignment.
     */
    @Test
    void testNameAndSpecies() {
        assertEquals("TestPet", pet.getName(), "Pet name should be 'TestPet'.");
        assertEquals("mole", pet.getSpecies(), "Pet species should be 'mole'.");

        pet.setName("NewName");
        assertEquals("NewName", pet.getName(), "Pet name should be updated to 'NewName'.");
    }

    /**
     * Test inventory initialization.
     */
    @Test
    void testInventoryInitialization() {
        assertNotNull(pet.getInventory(), "Inventory should be initialized.");
    }

    /**
     * Test score management.
     */
    @Test
    void testScoreManagement() {
        assertEquals(0, pet.getScore(), "Initial score should be 0.");
        pet.setScore(100);
        assertEquals(100, pet.getScore(), "Score should be updated to 100.");
    }

    /**
     * Test default item settings.
     */
    @Test
    void testDefaultItemSettings() {
        assertEquals(1, pet.getDefaultItem12(), "Default item 1/2 should be 1.");
        assertEquals(3, pet.getDefaultItem34(), "Default item 3/4 should be 3.");

        pet.setDefaultItem12(2);
        pet.setDefaultItem34(4);
        assertEquals(2, pet.getDefaultItem12(), "Default item 1/2 should be updated to 2.");
        assertEquals(4, pet.getDefaultItem34(), "Default item 3/4 should be updated to 4.");
    }

    /**
     * Test save ID assignment.
     */
    @Test
    void testSaveID() {
        assertEquals(1, pet.getSaveID(), "Save ID should be 1.");
    }

    /**
     * Test time management.
     */
    @Test
    void testTimeManagement() {
        assertEquals(0, pet.getTotalTimeSpent(), "Total time spent should initially be 0.");
        pet.addTimeSpent(120); // Add 2 minutes
        assertEquals(120, pet.getTotalTimeSpent(), "Total time spent should be updated to 120 seconds.");

        pet.resetTotalTimeSpent();
        assertEquals(0, pet.getTotalTimeSpent(), "Total time spent should be reset to 0.");
    }

    /**
     * Test time limit functionality.
     */
    @Test
    void testTimeLimit() {
        assertEquals(0, pet.getTimeLimit(), "Initial time limit should be 0 (no limit).");
        pet.setTimeLimit(3600); // 1 hour
        assertEquals(3600, pet.getTimeLimit(), "Time limit should be updated to 3600 seconds.");
    }

    /**
     * Test current playtime tracking.
     */
    @Test
    void testCurrentPlayTimeTracking() {
        assertEquals(0, pet.getCurrentPlayTime(), "Current playtime should initially be 0.");
        pet.setCurrentPlayTime(1500); // 25 minutes
        assertEquals(1500, pet.getCurrentPlayTime(), "Current playtime should be updated to 1500 seconds.");
    }

    /**
     * Test actions performed on VitalStats.
     */
    @Test
    void testPerformAction() {
        VitalStats stats = pet.getStats();
        assertNotNull(stats, "VitalStats should be initialized.");

        // Example action that increases hunger by 10
        stats.increaseHappiness(10);


        assertEquals(60, stats.getHunger(), "Hunger should be increased by 10.");
    }
}
