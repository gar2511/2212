package com.example.model;

public abstract class Pet {
    private String name;

    public Pet() {}

    public Pet(String name) {
        this.name = name;
    }

    // Common attribute: name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // Abstract method for pet-specific actions
    public abstract void performAction();
}
