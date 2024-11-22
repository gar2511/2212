package com.example.model;

public class Mole extends Pet{

    public Mole(String name) {
        super(name);
    }

    @Override
    public void performAction() {
        System.out.println(getName() + " is moleing!");
    }
}
