package com.example.model;

public class Cat extends Pet {
    public Cat(String name) {
        super(name);
    }

    @Override
    public void performAction() {
        System.out.println(getName() + " is meowing!");
    }

}
