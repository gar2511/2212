package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.model.UserPreferences;
import com.example.util.FileHandler;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParentMenuControllerTest {

    private ParentMenuController controller;
    private FileHandler mockFileHandler;
    private GameState mockGameState;
    private Pet mockPet;
    private UserPreferences mockUserPreferences;

    @BeforeEach
    void setUp() {
        // Initialize controller and mock dependencies
        controller = new ParentMenuController();

        mockFileHandler = mock(FileHandler.class);
        mockGameState = mock(GameState.class);
        mockPet = mock(Pet.class);
        mockUserPreferences = mock(UserPreferences.class);

        // Inject mocks into controller
        controller.fileHandler = mockFileHandler;
        controller.currentGameState = mockGameState;

        when(mockGameState.getPet()).thenReturn(mockPet);
    }

    @Test
    void testInitializeParentModeToggle() {
        try {
            when(mockUserPreferences.isParentControlsEnabled()).thenReturn(true);
            when(mockFileHandler.loadPreferences()).thenReturn(mockUserPreferences);

            // Simulate initialization
            controller.parentModeToggle = new ToggleButton();
            controller.selectSaveDropdown = new ComboBox<>();
            controller.initialize();

            assertTrue(controller.parentModeToggle.isSelected(), "Parent mode toggle should reflect saved preference.");
        } catch (IOException e) {
            fail("IOException should not occur during initialization.");
        }
    }

    @Test
    void testHandleToggleParentMode() throws IOException {
        controller.userPrefs = mockUserPreferences;
        controller.parentModeToggle = new ToggleButton();
        controller.parentModeToggle.setSelected(true);

        // Execute method
        controller.handleToggleParentMode();

        verify(mockUserPreferences).setParentControlsEnabled(true);
        verify(mockFileHandler).savePreferences(mockUserPreferences);
    }

    @Test
    void testHandleSetAllowedTimeValid() {
        controller.currentGameState = mockGameState;
        controller.startTimeTextField = new TextField("08:00");
        controller.endTimeTextField = new TextField("20:00");

        // Execute method
        controller.handleSetAllowedTime(null);


    }

    @Test
    void testHandleSetAllowedTimeInvalid() {
        controller.currentGameState = mockGameState;
        controller.startTimeTextField = new TextField("invalid");
        controller.endTimeTextField = new TextField("20:00");

        // Execute method
        controller.handleSetAllowedTime(null);

        verify(mockPet, never()).saveStartTime(any());
        verify(mockPet, never()).saveEndTime(any());
        try {
            verify(mockFileHandler, never()).saveGame(anyString(), any());
        } catch (IOException e) {
            fail("IOException should not occur during allowed time update.");
        }
    }

    @Test
    void testRevivePet() {
        when(mockPet.getStats().isAlive()).thenReturn(false);

        controller.revivePetButton = new Button();
        controller.petStatusLabel = new Label();

        // Execute method
        controller.revivePet(null);

        verify(mockPet.getStats()).restoreAll();
        try {
            verify(mockFileHandler).saveGame(anyString(), eq(mockGameState));
        } catch (IOException e) {
            fail("IOException should not occur during pet revival.");
        }

        assertTrue(controller.revivePetButton.isDisable(), "Revive button should be disabled after revival.");
        assertEquals("Status: Alive", controller.petStatusLabel.getText(), "Pet status label should be updated.");
    }

    @Test
    void testUpdateItemQuantities() {
        controller.item1Spinner = new Spinner<>();
        controller.item2Spinner = new Spinner<>();
        controller.item3Spinner = new Spinner<>();
        controller.item4Spinner = new Spinner<>();

        when(mockPet.getInventory().getItem1()).thenReturn(10);
        when(mockPet.getInventory().getItem2()).thenReturn(20);
        when(mockPet.getInventory().getItem3()).thenReturn(30);
        when(mockPet.getInventory().getItem4()).thenReturn(40);

        // Simulate UI spinner changes
        controller.item1Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10));
        controller.item2Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 20));
        controller.item3Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 30));
        controller.item4Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 40));

        controller.updateItem1();
        controller.updateItem2();
        controller.updateItem3();
        controller.updateItem4();

        verify(mockPet.getInventory()).setItem1(10);
        verify(mockPet.getInventory()).setItem2(20);
        verify(mockPet.getInventory()).setItem3(30);
        verify(mockPet.getInventory()).setItem4(40);

        try {
            verify(mockFileHandler, times(4)).saveGame(anyString(), eq(mockGameState));
        } catch (IOException e) {
            fail("IOException should not occur during item updates.");
        }
    }
}
