package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class representing the user's preferences.
 * Includes settings for parental controls, volume, and credentials for the parent user.
 * Supports JSON serialization and deserialization using Jackson annotations.
 */
public class UserPreferences {

    // Indicates whether parental controls are enabled
    private boolean parentControlsEnabled;

    // Volume level as a percentage (0.0 to 100.0)
    private double volume;

    // Username for the parent user
    private String parentUsername;

    // Password for the parent user
    private String parentPassword;

    /**
     * Default constructor initializes user preferences with default values.
     * - Parental controls are disabled by default.
     * - Volume is set to 50%.
     * - Parent username and password are empty strings.
     */
    public UserPreferences() {
        this.parentControlsEnabled = false;
        this.volume = 50.0;
        this.parentUsername = "";
        this.parentPassword = "";
    }

    /**
     * Checks whether parental controls are enabled.
     *
     * @return {@code true} if parental controls are enabled, {@code false} otherwise.
     */
    @JsonProperty("parentControlsEnabled")
    public boolean isParentControlsEnabled() {
        return parentControlsEnabled;
    }

    /**
     * Sets whether parental controls are enabled.
     *
     * @param parentControlsEnabled {@code true} to enable parental controls, {@code false} to disable them.
     */
    @JsonProperty("parentControlsEnabled")
    public void setParentControlsEnabled(boolean parentControlsEnabled) {
        this.parentControlsEnabled = parentControlsEnabled;
    }

    /**
     * Retrieves the current volume level.
     *
     * @return The volume level as a double (0.0 to 100.0).
     */
    @JsonProperty("volume")
    public double getVolume() {
        return volume;
    }

    /**
     * Sets the volume level.
     *
     * @param volume The new volume level as a double (0.0 to 100.0).
     */
    @JsonProperty("volume")
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * Retrieves the parent user's username.
     *
     * @return The parent user's username as a string.
     */
    @JsonProperty("parentUsername")
    public String getParentUsername() {
        return parentUsername;
    }

    /**
     * Sets the parent user's username.
     *
     * @param parentUsername The new username for the parent user.
     */
    @JsonProperty("parentUsername")
    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    /**
     * Retrieves the parent user's password.
     *
     * @return The parent user's password as a string.
     */
    @JsonProperty("parentPassword")
    public String getParentPassword() {
        return parentPassword;
    }

    /**
     * Sets the parent user's password.
     *
     * @param parentPassword The new password for the parent user.
     */
    @JsonProperty("parentPassword")
    public void setParentPassword(String parentPassword) {
        this.parentPassword = parentPassword;
    }
}
