package com.example.model;

public class Bear extends Pet{
    public Bear(String name) {
        super(name);
    }

    @Override
    public void performAction() {
        System.out.println(getName() + " is bearing!");
    }
}
