package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.util.FileHandler;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveMenuControllerTest {

    private SaveMenuController controller;
    private FileHandler fileHandlerMock;

    @BeforeEach
    void setUp() {
        // Initialize the controller and mock dependencies
        controller = new SaveMenuController();
        fileHandlerMock = mock(FileHandler.class);

        // Mock FXML fields
        controller.saveSlotList = new ListView<>();
        controller.petNameField = new TextField();
        controller.petTypeComboBox = new ComboBox<>(FXCollections.observableArrayList("Dog", "Cat"));


    }

    @Test
    void testInitializeDefaultSlots() {
        // Simulate controller initialization
        controller.initialize();

        // Verify default slot values
        assertEquals(4, controller.saveSlotList.getItems().size());
        assertEquals("CLICK TO CREATE NEW SAVE", controller.saveSlotList.getItems().get(0));
    }

    @Test
    void testShowNewSaveDialogue() {
        // Simulate selecting a slot and showing the new save dialogue
        controller.selectedSlotIndex = 1;
        controller.showNewSaveDialogue();

        // Verify pet name field is populated correctly
        assertEquals("PET 2", controller.petNameField.getText());
        assertTrue(controller.newSaveDialogue.isVisible());
    }


    @Test
    void testCancelNewSave() {
        // Simulate canceling new save creation
        controller.newSaveDialogue.setVisible(true);
        controller.cancelNewSave();

        // Verify dialogue is hidden
        assertFalse(controller.newSaveDialogue.isVisible());
    }
}
