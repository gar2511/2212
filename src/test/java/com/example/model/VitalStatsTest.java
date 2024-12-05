package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VitalStatsTest {

    private VitalStats stats;

    @BeforeEach
    void setUp() {
        stats = new VitalStats();
    }

    @Test
    void testInitialValues() {
        assertEquals(80, stats.getHunger(), "Initial hunger should be 80.");
        assertEquals(70, stats.getHappiness(), "Initial happiness should be 70.");
        assertEquals(100, stats.getEnergy(), "Initial energy should be 100.");
        assertEquals(100, stats.getHealth(), "Initial health should be 100.");
        assertArrayEquals(new int[]{0, 0, 0, 0}, stats.getState(), "Initial pet state should be all OK.");
    }

    @Test
    void testStatClamping() {
        stats.setHunger(150);
        stats.setHappiness(-10);
        stats.setEnergy(200);
        stats.setHealth(-50);

        assertEquals(100, stats.getHunger(), "Hunger should clamp to 100.");
        assertEquals(0, stats.getHappiness(), "Happiness should clamp to 0.");
        assertEquals(100, stats.getEnergy(), "Energy should clamp to 100.");
        assertEquals(0, stats.getHealth(), "Health should clamp to 0.");
    }

    @Test
    void testIncreaseAndDecreaseStats() {
        stats.increaseHunger(10);
        stats.decreaseHunger(20);
        stats.increaseHappiness(15);
        stats.decreaseHappiness(30);
        stats.increaseEnergy(5);
        stats.decreaseEnergy(50);
        stats.increaseHealth(20);
        stats.decreaseHealth(70);

        assertEquals(70, stats.getHunger(), "Hunger should decrease by 10.");
        assertEquals(55, stats.getHappiness(), "Happiness should decrease by 15.");
        assertEquals(55, stats.getEnergy(), "Energy should decrease by 45.");
        assertEquals(50, stats.getHealth(), "Health should decrease by 50.");
    }

    @Test
    void testCriticalStateDetection() {
        stats.setHunger(15);
        stats.setHappiness(20);
        stats.setEnergy(0);
        stats.setHealth(0);

        int[] expectedState = {1, 1, 1, 1};
        assertArrayEquals(expectedState, stats.getState(), "All stats should be in a critical state.");
    }

    @Test
    void testStateRecovery() {
        stats.setHunger(15);
        stats.setHappiness(20);
        stats.setEnergy(0);
        stats.setHealth(0);

        stats.setHunger(50);
        stats.setHappiness(50);
        stats.setEnergy(100);
        stats.setHealth(100);

        int[] expectedState = {0, 0, 0, 0};
        assertArrayEquals(expectedState, stats.getState(), "All stats should recover to normal state.");
    }

    @Test
    void testModifiers() {
        stats.setHungerMod(2);
        stats.setHappinessMod(3);
        stats.setEnergyMod(-1);
        stats.setHealthMod(0);

        assertEquals(2, stats.getHungerMod(), "Hunger modifier should be set to 2.");
        assertEquals(3, stats.getHappinessMod(), "Happiness modifier should be set to 3.");
        assertEquals(-1, stats.getEnergyMod(), "Energy modifier should be set to -1.");
        assertEquals(0, stats.getHealthMod(), "Health modifier should remain 0.");
    }

    @Test
    void testRestoreAll() {
        stats.setHunger(20);
        stats.setHappiness(30);
        stats.setEnergy(40);
        stats.setHealth(10);
        stats.restoreAll();

        assertEquals(100, stats.getHunger(), "Hunger should restore to 100.");
        assertEquals(100, stats.getHappiness(), "Happiness should restore to 100.");
        assertEquals(100, stats.getEnergy(), "Energy should restore to 100.");
        assertEquals(100, stats.getHealth(), "Health should restore to 100.");
        assertArrayEquals(new int[]{0, 0, 0, 0}, stats.getState(), "All states should reset to OK.");
    }

    @Test
    void testAliveStatus() {
        stats.setHealth(50);
        assertTrue(stats.isAlive(), "Pet should be alive with health > 0.");

        stats.setHealth(0);
        assertFalse(stats.isAlive(), "Pet should not be alive with health = 0.");

        stats.setAlive(true);
        assertEquals(1, stats.getHealth(), "Setting alive to true should restore minimum health.");
    }

    @Test
    void testPetStateArrayManipulation() {
        int[] newState = {1, 0, 1, 0};
        stats.setState(newState);

        assertArrayEquals(newState, stats.getState(), "State array should be updated correctly.");
    }

    @Test
    void testDynamicModifiers() {
        stats.setHunger(40);  // Below 50, affects energy and happiness
        assertEquals(3, stats.getHungerMod(), "Hunger modifier should be calculated correctly.");
    }
}

