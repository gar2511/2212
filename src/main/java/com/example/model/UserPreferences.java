package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPreferences {
    private boolean parentControlsEnabled;
    private double volume;
    private String parentUsername;
    private String parentPassword;

    // default constructor
    public UserPreferences() {
        this.parentControlsEnabled = false;
        this.volume = 50.0;
        this.parentUsername = "";
        this.parentPassword = "";
    }

    // getters and setters
    @JsonProperty("parentControlsEnabled")
    public boolean isParentControlsEnabled() {
        return parentControlsEnabled;
    }

    @JsonProperty("parentControlsEnabled")
    public void setParentControlsEnabled(boolean parentControlsEnabled) {
        this.parentControlsEnabled = parentControlsEnabled;
    }

    @JsonProperty("volume")
    public double getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(double volume) {
        this.volume = volume;
    }

    @JsonProperty("parentUsername")
    public String getParentUsername() {
        return parentUsername;
    }

    @JsonProperty("parentUsername")
    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    @JsonProperty("parentPassword")
    public String getParentPassword() {
        return parentPassword;
    }

    @JsonProperty("parentPassword")
    public void setParentPassword(String parentPassword) {
        this.parentPassword = parentPassword;
    }
}