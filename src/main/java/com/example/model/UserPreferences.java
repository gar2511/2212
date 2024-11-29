package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPreferences {
    @JsonProperty("volume")
    private double volume;
    
    @JsonProperty("parentUsername")
    private String parentUsername;
    
    @JsonProperty("parentPassword")
    private String parentPassword;
    
    @JsonProperty("parentControlsEnabled")
    private boolean parentControlsEnabled;

    // default constructor for Jackson
    public UserPreferences() {
        // set default values
        this.volume = 50.0;
        this.parentUsername = "CS2212";
        this.parentPassword = "Rubberducky!";
        this.parentControlsEnabled = false;
    }

    // getters and setters
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getParentUsername() {
        return parentUsername;
    }

    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    public String getParentPassword() {
        return parentPassword;
    }

    public void setParentPassword(String parentPassword) {
        this.parentPassword = parentPassword;
    }

    public boolean isParentControlsEnabled() {
        return parentControlsEnabled;
    }

    public void setParentControlsEnabled(boolean parentControlsEnabled) {
        this.parentControlsEnabled = parentControlsEnabled;
    }
}