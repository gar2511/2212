package com.example.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Model class representing the inventory of a pet.
 * Contains four items with quantities ranging from 0 to 100.
 */
public class Inventory {
    private final IntegerProperty item1 = new SimpleIntegerProperty(99);
    private final IntegerProperty item2 = new SimpleIntegerProperty(99);
    private final IntegerProperty item3 = new SimpleIntegerProperty(99);
    private final IntegerProperty item4 = new SimpleIntegerProperty(299);

    // Getters and setters for Item 1
    public int getItem1() {
        return item1.get();
    }

    public void setItem1(int value) {
        item1.set(clampValue(value));
    }

    public IntegerProperty item1Property() {
        return item1;
    }

    // Getters and setters for Item 2
    public int getItem2() {
        return item2.get();
    }

    public void setItem2(int value) {
        item2.set(clampValue(value));
    }

    public IntegerProperty item2Property() {
        return item2;
    }

    // Getters and setters for Item 3
    public int getItem3() {
        return item3.get();
    }

    public void setItem3(int value) {
        item3.set(clampValue(value));
    }

    public IntegerProperty item3Property() {
        return item3;
    }

    // Getters and setters for Item 4
    public int getItem4() {
        return item4.get();
    }

    public void setItem4(int value) {
        item4.set(clampValue(value));
    }

    public IntegerProperty item4Property() {
        return item4;
    }

    // Method to decrease Item 1 by 1
    public void decreaseItem1() {
        setItem1(getItem1() - 1);
    }

    // Method to decrease Item 2 by 1
    public void decreaseItem2() {
        setItem2(getItem2() - 1);
    }

    // Method to decrease Item 3 by 1
    public void decreaseItem3() {
        setItem3(getItem3() - 1);
    }

    // Method to decrease Item 4 by 1
    public void decreaseItem4() {
        setItem4(getItem4() - 1);
    }

    // Helper method to ensure item quantities stay within a valid range
    private int clampValue(int value) {
        return Math.max(0, Math.min(99, value));
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "item1=" + getItem1() +
                ", item2=" + getItem2() +
                ", item3=" + getItem3() +
                ", item4=" + getItem4() +
                '}';
    }
}
