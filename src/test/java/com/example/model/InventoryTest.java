package com.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void testInitialQuantities() {
        assertEquals(99, inventory.getItem1(), "Item 1 should initially have a quantity of 99.");

    }

    @Test
    void testDecreaseItemQuantity() {
        inventory.decreaseItem1();

        assertEquals(98, inventory.getItem1(), "Item 1 quantity should decrease by 1.");

    }

    @Test
    void testIncreaseItemQuantity() {
        inventory.addItem1();


        assertEquals(100, inventory.getItem1(), "Item 1 quantity should increase by 1.");

    }

    @Test
    void testClampValueAtZero() {
        inventory.setItem1(-10);
        inventory.setItem2(-1);
        inventory.setItem3(0);
        inventory.setItem4(-99);

        assertEquals(0, inventory.getItem1(), "Item 1 quantity should not go below 0.");
        assertEquals(0, inventory.getItem2(), "Item 2 quantity should not go below 0.");
        assertEquals(0, inventory.getItem3(), "Item 3 quantity should not go below 0.");
        assertEquals(0, inventory.getItem4(), "Item 4 quantity should not go below 0.");
    }

    @Test
    void testClampValueAtMax() {
        inventory.setItem1(150);
        inventory.setItem2(200);
        inventory.setItem3(101);
        inventory.setItem4(1000);

        assertEquals(99, inventory.getItem1(), "Item 1 quantity should not exceed 99.");
        assertEquals(99, inventory.getItem2(), "Item 2 quantity should not exceed 99.");
        assertEquals(99, inventory.getItem3(), "Item 3 quantity should not exceed 99.");
        assertEquals(99, inventory.getItem4(), "Item 4 quantity should not exceed 99.");
    }

    @Test
    void testToStringMethod() {
        String expected = "Inventory{item1=99, item2=99, item3=99, item4=99}";
        assertEquals(expected, inventory.toString(), "The toString method should correctly represent the inventory.");
    }
}
