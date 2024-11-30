package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.util.FileHandler;
import com.example.components.CustomToggle;
import com.example.model.UserPreferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ParentMenuController {

    public Label petScoreLabel;
    @FXML
    private ToggleButton parentModeToggle;
    @FXML
    private ComboBox<String> selectSaveDropdown;
    @FXML
    private VBox timeLimitSection;
    @FXML
    private TextField timeLimitInput;
    @FXML
    private CustomToggle timeLimitToggle;
    @FXML
    private HBox viewStatsSection;
    @FXML
    private Button revivePetButton;
    @FXML
    private HBox inventorySection;
    @FXML
    private Button goBackButton;
    @FXML
    private Button updateTimeLimitButton;

    @FXML
    private Label saveScoreLabel;
    private FileHandler fileHandler;
    private GameState currentGameState;
    private boolean isParentModeEnabled = false;
    private UserPreferences userPrefs;

    @FXML
    private Spinner<Integer> timeLimitSpinner;

    @FXML
    private Separator divider;

    @FXML
    private Separator secondDivider;

    @FXML
    private Label petStatusLabel;

    @FXML
    public void initialize() {
        fileHandler = new FileHandler();
        
        // load preferences first
        try {
            userPrefs = fileHandler.loadPreferences();
            isParentModeEnabled = userPrefs.isParentControlsEnabled();
            parentModeToggle.setSelected(isParentModeEnabled);
            
            // Set initial visibility based on parent mode
            selectSaveDropdown.setVisible(isParentModeEnabled);
            selectSaveDropdown.setManaged(isParentModeEnabled);
        } catch (IOException e) {
            System.err.println("failed to load preferences: " + e.getMessage());
            userPrefs = new UserPreferences();
        }

        // populate the dropdown with save files
        File[] saveFiles = fileHandler.getSaveFiles();
        if (saveFiles != null) {
            selectSaveDropdown.getItems().add("Select a save file...");
            for (File file : saveFiles) {
                String fileName = file.getName();
                if (fileName.matches("slot\\d+\\.json")) {
                    try {
                        int slotIndex = Integer.parseInt(fileName.replaceAll("[^0-9]", ""));
                        GameState state = fileHandler.loadGame("slot" + slotIndex);
                        if (state != null && state.getPet() != null) {
                            String petName = state.getPet().getName() + " " + state.getPet().getSpecies();
                            selectSaveDropdown.getItems().add(petName);
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to load save file: " + e.getMessage());
                    }
                }
            }
        }
        selectSaveDropdown.setValue("Select a save file...");
        
        // Make sure sections are hidden when "Select a save file..." is initially selected
        timeLimitSection.setVisible(false);
        timeLimitSection.setManaged(false);
        viewStatsSection.setVisible(false);
        viewStatsSection.setManaged(false);
        revivePetButton.setVisible(false);
        revivePetButton.setManaged(false);
        inventorySection.setVisible(false);
        inventorySection.setManaged(false);

        // Add listener to dropdown for save selection
        selectSaveDropdown.setOnAction(event -> {
            String selected = selectSaveDropdown.getValue();
            if (selected != null && !selected.equals("Select a save file...")) {
                loadSelectedSave();
                // Show sections only when a valid save is selected
                divider.setVisible(true);
                divider.setManaged(true);
                timeLimitSection.setVisible(true);
                timeLimitSection.setManaged(true);
                viewStatsSection.setVisible(true);
                viewStatsSection.setManaged(true);
                revivePetButton.setVisible(true);
                revivePetButton.setManaged(true);
                inventorySection.setVisible(true);
                inventorySection.setManaged(true);
            } else {
                // Hide sections when no save is selected
                divider.setVisible(false);
                divider.setManaged(false);
                timeLimitSection.setVisible(false);
                timeLimitSection.setManaged(false);
                viewStatsSection.setVisible(false);
                viewStatsSection.setManaged(false);
                revivePetButton.setVisible(false);
                revivePetButton.setManaged(false);
                inventorySection.setVisible(false);
                inventorySection.setManaged(false);
            }
        });

        // Initialize spinners with default values
        item1Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        item2Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        item3Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        item4Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
    }
    private void loadSelectedSave() {
        String selectedPetName = selectSaveDropdown.getValue();
        if (selectedPetName != null && !selectedPetName.equals("Select a save file...")) {
            try {
                // Find the corresponding save file by iterating through saves
                File[] saveFiles = fileHandler.getSaveFiles();
                for (File file : saveFiles) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".json")) {
                        GameState state = fileHandler.loadGame(fileName.replace(".json", ""));
                        if (state != null && state.getPet() != null) {
                            String petFullName = state.getPet().getName() + " " + state.getPet().getSpecies();
                            if (petFullName.equals(selectedPetName)) {
                                currentGameState = state;
                                Pet pet = currentGameState.getPet();
                                
                                // Update UI components with the pet's current state
                                petScoreLabel.setText("Score: " + pet.getScore());
                                petScoreLabel.setVisible(true);
                                item1Spinner.getValueFactory().setValue(pet.getInventory().getItem1());
                                item2Spinner.getValueFactory().setValue(pet.getInventory().getItem2());
                                item3Spinner.getValueFactory().setValue(pet.getInventory().getItem3());
                                item4Spinner.getValueFactory().setValue(pet.getInventory().getItem4());
                                secondDivider.setVisible(true);
                                secondDivider.setManaged(true);
                                boolean isAlive = pet.getStats().isAlive();
                                petStatusLabel.setText("Status: " + (isAlive ? "Alive" : "Dead"));
                                petStatusLabel.setVisible(true);
                                revivePetButton.setDisable(isAlive);
                                break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to load save file: " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleToggleParentMode() {
        isParentModeEnabled = !isParentModeEnabled;
        
        // update preferences
        userPrefs.setParentControlsEnabled(isParentModeEnabled);
        try {
            fileHandler.savePreferences(userPrefs);
        } catch (IOException e) {
            System.err.println("failed to save preferences: " + e.getMessage());
        }
        
        if (isParentModeEnabled) {
            // Only show the dropdown initially
            selectSaveDropdown.setVisible(true);
            selectSaveDropdown.setManaged(true);
            selectSaveDropdown.setValue("Select a save file...");
            
            // Keep other sections hidden until a save is selected
            divider.setVisible(false);
            divider.setManaged(false);
            timeLimitSection.setVisible(false);
            timeLimitSection.setManaged(false);
            viewStatsSection.setVisible(false);
            viewStatsSection.setManaged(false);
            revivePetButton.setVisible(false);
            revivePetButton.setManaged(false);
            inventorySection.setVisible(false);
            inventorySection.setManaged(false);
        } else {
            // Hide all related controls
            selectSaveDropdown.setVisible(false);
            selectSaveDropdown.setManaged(false);
            divider.setVisible(false);
            divider.setManaged(false);
            timeLimitSection.setVisible(false);
            timeLimitSection.setManaged(false);
            timeLimitInput.setVisible(false);
            timeLimitInput.setManaged(false);
            viewStatsSection.setVisible(false);
            viewStatsSection.setManaged(false);
            revivePetButton.setVisible(false);
            revivePetButton.setManaged(false);
            inventorySection.setVisible(false);
            inventorySection.setManaged(false);
            secondDivider.setVisible(false);
            secondDivider.setManaged(false);
        }
    }
    @FXML
    private void handleTimeLimitToggle() {
        boolean isTimeLimitEnabled = timeLimitToggle.isSelected();
        timeLimitSpinner.setDisable(!isTimeLimitEnabled);
        timeLimitSpinner.setOpacity(isTimeLimitEnabled ? 1.0 : 0.5);
        updateTimeLimitButton.setDisable(!isTimeLimitEnabled);
        updateTimeLimitButton.setOpacity(isTimeLimitEnabled ? 1.0 : 0.5);
        
        if (isTimeLimitEnabled && currentGameState != null) {
            timeLimitSpinner.getValueFactory().setValue((int) currentGameState.getPet().getTimeLimit());
        }
    }

    @FXML
    private void handleUpdateTimeLimit() {
        if (currentGameState != null) {
            int timeLimit = timeLimitSpinner.getValue();
            currentGameState.getPet().setTimeLimit(timeLimit);
            try {
                // get the slot number from the current game state
                String slotNumber = "slot" + currentGameState.getPet().getSaveID();
                fileHandler.saveGame(slotNumber, currentGameState);
                System.out.println("Time limit updated to " + timeLimit + " minutes.");
            } catch (IOException e) {
                System.err.println("Failed to save time limit: " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleGoBack() {
        if (isParentModeEnabled) {
            // Show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText("Parent Mode is still active");
            alert.setContentText("Are you sure you want to leave without disabling Parent Mode?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Go back to the previous scene
                    Stage stage = (Stage) goBackButton.getScene().getWindow();
                    SceneController.getInstance().switchToSettings();
                }
            });
        } else {
            // Go back immediately
            Stage stage = (Stage) goBackButton.getScene().getWindow();
            SceneController.getInstance().switchToSettings();
        }
    }

    @FXML
    public void revivePet(ActionEvent actionEvent) {
        if (currentGameState != null && !currentGameState.getPet().getStats().isAlive()) {
            currentGameState.getPet().getStats().restoreAll();
            petStatusLabel.setText("Status: Alive");
            revivePetButton.setDisable(true);
            saveCurrentGameState();
        }
    }
    @FXML
    private Spinner<Integer> item1Spinner, item2Spinner, item3Spinner, item4Spinner;

    @FXML
    private void updateItem1() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem1(item1Spinner.getValue());
            saveCurrentGameState();
        }
    }

    @FXML
    private void updateItem2() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem2(item2Spinner.getValue());
            saveCurrentGameState();
        }
    }

    @FXML
    private void updateItem3() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem3(item3Spinner.getValue());
            saveCurrentGameState();
        }
    }

    @FXML
    private void updateItem4() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem4(item4Spinner.getValue());
            saveCurrentGameState();
        }
    }

    private void saveCurrentGameState() {
        try {
            if (currentGameState != null) {
                // get the slot number from the current game state
                String slotNumber = "slot" + currentGameState.getPet().getSaveID();
                fileHandler.saveGame(slotNumber, currentGameState);
                System.out.println("Save file " + slotNumber + " updated.");
            }
        } catch (IOException e) {
            System.out.println("Failed to save game state: " + e.getMessage());
        }
    }
}