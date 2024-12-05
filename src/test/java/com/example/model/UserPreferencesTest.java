package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPreferencesTest {

    private UserPreferences userPreferences;

    @BeforeEach
    void setUp() {
        userPreferences = new UserPreferences();
    }

    @Test
    void testDefaultValues() {
        // Verify the default values of the UserPreferences class
        assertFalse(userPreferences.isParentControlsEnabled(), "Parental controls should be disabled by default.");
        assertEquals(50.0, userPreferences.getVolume(), "Default volume should be 50.0.");
        assertEquals("", userPreferences.getParentUsername(), "Default parent username should be an empty string.");
        assertEquals("", userPreferences.getParentPassword(), "Default parent password should be an empty string.");
    }

    @Test
    void testSetParentControlsEnabled() {
        // Enable parental controls and verify
        userPreferences.setParentControlsEnabled(true);
        assertTrue(userPreferences.isParentControlsEnabled(), "Parental controls should be enabled.");

        // Disable parental controls and verify
        userPreferences.setParentControlsEnabled(false);
        assertFalse(userPreferences.isParentControlsEnabled(), "Parental controls should be disabled.");
    }

    @Test
    void testSetVolume() {
        // Set a valid volume and verify
        userPreferences.setVolume(75.0);
        assertEquals(75.0, userPreferences.getVolume(), "Volume should be set to 75.0.");

        // Set the volume to 0 and verify
        userPreferences.setVolume(0.0);
        assertEquals(0.0, userPreferences.getVolume(), "Volume should be set to 0.0.");

        // Set the volume to 100 and verify
        userPreferences.setVolume(100.0);
        assertEquals(100.0, userPreferences.getVolume(), "Volume should be set to 100.0.");
    }

    @Test
    void testSetParentUsername() {
        // Set a username and verify
        userPreferences.setParentUsername("parentUser");
        assertEquals("parentUser", userPreferences.getParentUsername(), "Parent username should be set to 'parentUser'.");
    }

    @Test
    void testSetParentPassword() {
        // Set a password and verify
        userPreferences.setParentPassword("securePassword");
        assertEquals("securePassword", userPreferences.getParentPassword(), "Parent password should be set to 'securePassword'.");
    }

    @Test
    void testUpdateMultipleFields() {
        // Update multiple fields and verify
        userPreferences.setParentControlsEnabled(true);
        userPreferences.setVolume(80.0);
        userPreferences.setParentUsername("admin");
        userPreferences.setParentPassword("1234");

        assertTrue(userPreferences.isParentControlsEnabled(), "Parental controls should be enabled.");
        assertEquals(80.0, userPreferences.getVolume(), "Volume should be set to 80.0.");
        assertEquals("admin", userPreferences.getParentUsername(), "Parent username should be set to 'admin'.");
        assertEquals("1234", userPreferences.getParentPassword(), "Parent password should be set to '1234'.");
    }
}
