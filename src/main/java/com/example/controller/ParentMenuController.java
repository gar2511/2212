package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.util.FileHandler;
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
    private RadioButton timeLimitToggle;
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

    @FXML
    public void initialize() {
        fileHandler = new FileHandler();

        // Hide all sections initially
        timeLimitSection.setVisible(false);
        timeLimitSection.setManaged(false);
        viewStatsSection.setVisible(false);
        viewStatsSection.setManaged(false);
        revivePetButton.setVisible(false);
        revivePetButton.setManaged(false);
        inventorySection.setVisible(false);
        inventorySection.setManaged(false);

        // Populate the dropdown with save files
        File[] saveFiles = fileHandler.getSaveFiles();
        if (saveFiles != null) {
            selectSaveDropdown.getItems().add("Select a save file...");
            for (File file : saveFiles) {
                String fileName = file.getName();
                if (fileName.endsWith(".json")) {
                    selectSaveDropdown.getItems().add(fileName.replace(".json", ""));
                }
            }
        }
        selectSaveDropdown.setValue("Select a save file...");

        // Add listener to dropdown for save selection
        selectSaveDropdown.setOnAction(event -> {
            String selected = selectSaveDropdown.getValue();
            if (selected != null && !selected.equals("Select a save file...")) {
                loadSelectedSave();
                // Show sections only when a valid save is selected
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
        String selectedSave = selectSaveDropdown.getValue();
        if (selectedSave != null) {
            try {
                // Load the selected save file
                currentGameState = fileHandler.loadGame(selectedSave);
                Pet pet = currentGameState.getPet();

                if (pet != null) {
                    // Update UI components with the pet's current state
                    petScoreLabel.setText("Score: " + pet.getScore());
                    petScoreLabel.setVisible(true);
                    item1Spinner.getValueFactory().setValue(pet.getInventory().getItem1());
                    item2Spinner.getValueFactory().setValue(pet.getInventory().getItem2());
                    item3Spinner.getValueFactory().setValue(pet.getInventory().getItem3());
                    item4Spinner.getValueFactory().setValue(pet.getInventory().getItem4());

                    saveScoreLabel.setText("Score: " + pet.getScore());
                    saveScoreLabel.setVisible(true);

                    System.out.println("Save " + selectedSave + " loaded for pet: " + pet.getName());
                } else {
                    System.out.println("No pet found in save file: " + selectedSave);
                }
            } catch (IOException e) {
                System.out.println("Failed to load save file: " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleToggleParentMode() {
        isParentModeEnabled = !isParentModeEnabled;
        if (isParentModeEnabled) {
            // Enable blue text sections
            selectSaveDropdown.setVisible(true);
            timeLimitSection.setVisible(true);
            viewStatsSection.setVisible(true);
            revivePetButton.setVisible(true);
            inventorySection.setVisible(true);
        } else {
            // Hide all related controls
            selectSaveDropdown.setVisible(false);
            timeLimitSection.setVisible(false);
            timeLimitInput.setVisible(false);
            viewStatsSection.setVisible(false);
            revivePetButton.setVisible(false);
            inventorySection.setVisible(false);
        }
    }
    @FXML
    private void handleTimeLimitToggle() {
        boolean isTimeLimitEnabled = timeLimitToggle.isSelected();
        timeLimitInput.setVisible(isTimeLimitEnabled);
        updateTimeLimitButton.setVisible(isTimeLimitEnabled);
    }

    @FXML
    private void handleUpdateTimeLimit() {
        try {
            if (timeLimitInput.getText().isEmpty()) {
                System.out.println("Time limit input is empty.");
                return;
            }

            int timeLimit = Integer.parseInt(timeLimitInput.getText());
            if (timeLimit <= 0) {
                System.out.println("Time limit must be greater than zero.");
                return;
            }

            if (currentGameState != null) {
                currentGameState.getPet().setTimeLimit(timeLimit); // Update time limit in the game state
                saveCurrentGameState(); // Save the updated state
                System.out.println("Time limit updated to " + timeLimit + " minutes.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid time limit entered.");
            timeLimitInput.clear();
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

    public void revivePet(ActionEvent actionEvent) {
        if (currentGameState != null) {
            currentGameState.getPet().getStats().restoreAll();
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
            String selectedSave = selectSaveDropdown.getValue();
            if (selectedSave != null && currentGameState != null) {
                fileHandler.saveGame(selectedSave, currentGameState);
                System.out.println("Save file " + selectedSave + " updated.");
            }
        } catch (IOException e) {
            System.out.println("Failed to save game state: " + e.getMessage());
        }
    }
}