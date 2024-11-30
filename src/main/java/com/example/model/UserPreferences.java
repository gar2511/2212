package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserPreferences {
    @JsonProperty("volume")
    private double volume;
    
    @JsonProperty("parentControlsEnabled")
    private boolean parentControlsEnabled;
    
    @JsonProperty("parentUsername")
    private String parentUsername;
    
    @JsonProperty("parentPassword")
    private String parentPassword;
    
    private boolean parentalControlsEnabled = false;
    
    // Default constructor
    public UserPreferences() {
        this.volume = 50.0;
        this.parentControlsEnabled = false;
        this.parentUsername = "CS2212";
        this.parentPassword = "Rubberducky!";
    }
    
    // Getters and setters
    public double getVolume() {
        return volume;
    }
    
    public void setVolume(double volume) {
        this.volume = volume;
    }
    
    public boolean isParentControlsEnabled() {
        return parentControlsEnabled;
    }
    
    public void setParentControlsEnabled(boolean parentControlsEnabled) {
        this.parentControlsEnabled = parentControlsEnabled;
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
    
    public boolean isParentalControlsEnabled() {
        return parentalControlsEnabled;
    }
    
    public void setParentalControlsEnabled(boolean enabled) {
        this.parentalControlsEnabled = enabled;
    }
}