package com.example.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Model class representing the inventory of a pet.
 * The inventory contains four items, each with a quantity ranging from 0 to 99.
 * Provides methods to get, set, and modify the quantities of these items.
 */
public class Inventory {

    private final IntegerProperty item1 = new SimpleIntegerProperty(99);
    private final IntegerProperty item2 = new SimpleIntegerProperty(99);
    private final IntegerProperty item3 = new SimpleIntegerProperty(99);
    private final IntegerProperty item4 = new SimpleIntegerProperty(299);

    /**
     * Gets the quantity of Item 1.
     *
     * @return The current quantity of Item 1.
     */
    public int getItem1() {
        return item1.get();
    }

    /**
     * Sets the quantity of Item 1.
     *
     * @param value The new quantity for Item 1 (clamped between 0 and 99).
     */
    public void setItem1(int value) {
        item1.set(clampValue(value));
    }

    /**
     * Gets the {@link IntegerProperty} for Item 1.
     *
     * @return The property for Item 1.
     */
    public IntegerProperty item1Property() {
        return item1;
    }

    /**
     * Gets the quantity of Item 2.
     *
     * @return The current quantity of Item 2.
     */
    public int getItem2() {
        return item2.get();
    }

    /**
     * Sets the quantity of Item 2.
     *
     * @param value The new quantity for Item 2 (clamped between 0 and 99).
     */
    public void setItem2(int value) {
        item2.set(clampValue(value));
    }

    /**
     * Gets the {@link IntegerProperty} for Item 2.
     *
     * @return The property for Item 2.
     */
    public IntegerProperty item2Property() {
        return item2;
    }

    /**
     * Gets the quantity of Item 3.
     *
     * @return The current quantity of Item 3.
     */
    public int getItem3() {
        return item3.get();
    }

    /**
     * Sets the quantity of Item 3.
     *
     * @param value The new quantity for Item 3 (clamped between 0 and 99).
     */
    public void setItem3(int value) {
        item3.set(clampValue(value));
    }

    /**
     * Gets the {@link IntegerProperty} for Item 3.
     *
     * @return The property for Item 3.
     */
    public IntegerProperty item3Property() {
        return item3;
    }

    /**
     * Gets the quantity of Item 4.
     *
     * @return The current quantity of Item 4.
     */
    public int getItem4() {
        return item4.get();
    }

    /**
     * Sets the quantity of Item 4.
     *
     * @param value The new quantity for Item 4 (clamped between 0 and 99).
     */
    public void setItem4(int value) {
        item4.set(clampValue(value));
    }

    /**
     * Gets the {@link IntegerProperty} for Item 4.
     *
     * @return The property for Item 4.
     */
    public IntegerProperty item4Property() {
        return item4;
    }

    /**
     * Decreases the quantity of Item 1 by 1.
     */
    public void decreaseItem1() {
        setItem1(getItem1() - 1);
    }

    /**
     * Decreases the quantity of Item 2 by 1.
     */
    public void decreaseItem2() {
        setItem2(getItem2() - 1);
    }

    /**
     * Decreases the quantity of Item 3 by 1.
     */
    public void decreaseItem3() {
        setItem3(getItem3() - 1);
    }

    /**
     * Decreases the quantity of Item 4 by 1.
     */
    public void decreaseItem4() {
        setItem4(getItem4() - 1);
    }

    /**
     * Increases the quantity of Item 1 by 1.
     */
    public void addItem1() {
        setItem1(getItem1() + 1);
    }

    /**
     * Increases the quantity of Item 2 by 1.
     */
    public void addItem2() {
        setItem2(getItem2() + 1);
    }

    /**
     * Increases the quantity of Item 3 by 1.
     */
    public void addItem3() {
        setItem3(getItem3() + 1);
    }

    /**
     * Increases the quantity of Item 4 by 1.
     */
    public void addItem4() {
        setItem4(getItem4() + 1);
    }

    /**
     * Ensures item quantities stay within a valid range (0 to 99).
     *
     * @param value The quantity to clamp.
     * @return The clamped quantity.
     */
    private int clampValue(int value) {
        return Math.max(0, Math.min(99, value));
    }

    /**
     * Returns a string representation of the inventory.
     *
     * @return A string containing the quantities of all items.
     */
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
