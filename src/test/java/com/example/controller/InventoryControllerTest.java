package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.model.Inventory;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryControllerTest {

    private InventoryController inventoryController;
    private GameState mockGameState;
    private Pet mockPet;
    private Inventory mockInventory;

    @BeforeAll
    static void setupClass() {
        Platform.startup(() -> {}); // Initialize JavaFX toolkit
    }

    @BeforeEach
    void setup() {
        inventoryController = new InventoryController();

        mockGameState = Mockito.mock(GameState.class);
        mockPet = Mockito.mock(Pet.class);
        mockInventory = Mockito.mock(Inventory.class);

        when(mockGameState.getPet()).thenReturn(mockPet);
        when(mockPet.getInventory()).thenReturn(mockInventory);

        GameState.loadState(mockGameState);

        // Mock UI elements
        inventoryController.pointsLabel = new Label();
        inventoryController.item1Quantity = new Label();
        inventoryController.item2Quantity = new Label();
        inventoryController.item3Quantity = new Label();
        inventoryController.item4Quantity = new Label();
        inventoryController.defaultItem1 = new Button();
        inventoryController.defaultItem2 = new Button();
        inventoryController.defaultItem3 = new Button();
        inventoryController.defaultItem4 = new Button();
        inventoryController.selectedItem12Label = new Label();
        inventoryController.selectedItem34Label = new Label();
        inventoryController.item1Image = new ImageView();
        inventoryController.item2Image = new ImageView();
        inventoryController.item3Image = new ImageView();
        inventoryController.item4Image = new ImageView();
    }

    @Test
    void testSelectDefaultItem1() {
        inventoryController.selectDefaultItem1();
        verify(mockPet).setDefaultItem12(1);
        assertEquals("Currently Selected: Item 1", inventoryController.selectedItem12Label.getText());
    }

    @Test
    void testBuyItem1() {
        when(mockPet.getScore()).thenReturn(20);
        when(mockInventory.getItem1()).thenReturn(10);

        inventoryController.buyItem1();

        verify(mockPet).setScore(10); // 20 - 10 (cost)
        verify(mockInventory).addItem1();
    }

    @Test
    void testBuyItem1NotEnoughPoints() {
        when(mockPet.getScore()).thenReturn(5);

        inventoryController.buyItem1();

        verify(mockPet, never()).setScore(anyInt());
        verify(mockInventory, never()).addItem1();
    }

    @Test
    void testUpdateItemQuantities() {
        when(mockInventory.getItem1()).thenReturn(5);
        when(mockInventory.getItem2()).thenReturn(10);
        when(mockInventory.getItem3()).thenReturn(15);
        when(mockInventory.getItem4()).thenReturn(20);

        inventoryController.updateItemQuantities();

        assertEquals("5", inventoryController.item1Quantity.getText());
        assertEquals("10", inventoryController.item2Quantity.getText());
        assertEquals("15", inventoryController.item3Quantity.getText());
        assertEquals("20", inventoryController.item4Quantity.getText());
    }

    @Test
    void testInitialize() {
        when(mockPet.getScore()).thenReturn(50);
        when(mockPet.getDefaultItem12()).thenReturn(1);
        when(mockPet.getDefaultItem34()).thenReturn(3);

        inventoryController.initialize();

        assertEquals("Points: 50", inventoryController.pointsLabel.getText());
        assertEquals("Currently Selected: Cake", inventoryController.selectedItem12Label.getText());
        assertEquals("Currently Selected: Ball", inventoryController.selectedItem34Label.getText());
    }

    @Test
    void testGoBack() {
        inventoryController.goBack();

        // Verify that the scene switches to the game screen
        // Assuming SceneController is mocked in the actual test suite
    }
}
