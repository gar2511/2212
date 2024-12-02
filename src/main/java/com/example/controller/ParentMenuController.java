package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.util.FileHandler;
import com.example.components.CustomToggle;
import com.example.components.CustomButton;
import com.example.model.UserPreferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static com.example.App.PlayButtonSound;

/**
 * Controller for the Parent Menu interface.
 * Manages parental controls, save game options, and pet revival functionality.
 */
public class ParentMenuController {

    @FXML
    private Label petScoreLabel;

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

    @FXML
    private Spinner<Integer> timeLimitSpinner;

    @FXML
    private Separator divider;

    @FXML
    private Separator secondDivider;

    @FXML
    private Separator inventoryDivider;

    @FXML
    private Label petStatusLabel;

    @FXML
    private CustomButton removeProfileButton;

    private FileHandler fileHandler;
    private GameState currentGameState;
    private boolean isParentModeEnabled = false;
    private UserPreferences userPrefs;

    /**
     * Initializes the Parent Menu interface.
     * Loads user preferences, sets up dropdown options, and configures visibility based on parent mode.
     */
    @FXML
    public void initialize() {
        fileHandler = new FileHandler();

        try {
            userPrefs = fileHandler.loadPreferences();
            isParentModeEnabled = userPrefs.isParentControlsEnabled();
            parentModeToggle.setSelected(isParentModeEnabled);

            selectSaveDropdown.setVisible(isParentModeEnabled);
            selectSaveDropdown.setManaged(isParentModeEnabled);
        } catch (IOException e) {
            System.err.println("Failed to load preferences: " + e.getMessage());
            userPrefs = new UserPreferences();
        }

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
        timeLimitSection.setVisible(false);
        timeLimitSection.setManaged(false);
        viewStatsSection.setVisible(false);
        viewStatsSection.setManaged(false);
        revivePetButton.setVisible(false);
        revivePetButton.setManaged(false);
        inventorySection.setVisible(false);
        inventorySection.setManaged(false);

        selectSaveDropdown.setOnAction(event -> {
            String selected = selectSaveDropdown.getValue();
            if (selected != null && !selected.equals("Select a save file...")) {
                loadSelectedSave();
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
                secondDivider.setVisible(false);
                secondDivider.setManaged(false);
                inventoryDivider.setVisible(false);
                inventoryDivider.setManaged(false);
            }
        });

        item1Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        item2Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        item3Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        item4Spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
    }

    /**
     * Loads the selected save file and updates the UI components accordingly.
     */
    private void loadSelectedSave() {
        String selectedPetName = selectSaveDropdown.getValue();
        if (selectedPetName != null && !selectedPetName.equals("Select a save file...")) {
            try {
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
                                petScoreLabel.setText("Score: " + pet.getScore());
                                petScoreLabel.setVisible(true);
                                item1Spinner.getValueFactory().setValue(pet.getInventory().getItem1());
                                item2Spinner.getValueFactory().setValue(pet.getInventory().getItem2());
                                item3Spinner.getValueFactory().setValue(pet.getInventory().getItem3());
                                item4Spinner.getValueFactory().setValue(pet.getInventory().getItem4());
                                secondDivider.setVisible(true);
                                secondDivider.setManaged(true);
                                inventoryDivider.setVisible(true);
                                inventoryDivider.setManaged(true);
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




    /**
     * Toggles the parental controls mode.
     * Saves the updated state and adjusts the UI accordingly.
     */

    @FXML
    private void handleToggleParentMode() {
        PlayButtonSound();
        isParentModeEnabled = parentModeToggle.isSelected();

        // only update the enabled state, don't clear password
        userPrefs.setParentControlsEnabled(isParentModeEnabled);
        try {
            fileHandler.savePreferences(userPrefs);
        } catch (IOException e) {
            System.err.println("failed to save preferences: " + e.getMessage());
        }

        // update UI visibility
        updateVisibility(isParentModeEnabled);
    }
    /**
     * Updates the visibility of UI sections based on the parent mode status.
     *
     * @param enabled True if parent mode is enabled, false otherwise.
     */
    private void updateVisibility(boolean enabled) {
        selectSaveDropdown.setVisible(enabled);
        selectSaveDropdown.setManaged(enabled);

        if (enabled) {
            selectSaveDropdown.setValue("Select a save file...");
        }

        // Hide all other sections
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
        secondDivider.setVisible(false);
        secondDivider.setManaged(false);
        inventoryDivider.setVisible(false);
        inventoryDivider.setManaged(false);
    }
    /**
     * Handles the toggling of the time limit feature.
     * Enables or disables the spinner and update button based on the toggle state.
     */
    @FXML
    private void handleTimeLimitToggle() {
        PlayButtonSound();
        boolean isTimeLimitEnabled = timeLimitToggle.isSelected();
        timeLimitSpinner.setDisable(!isTimeLimitEnabled);
        timeLimitSpinner.setOpacity(isTimeLimitEnabled ? 1.0 : 0.5);
        updateTimeLimitButton.setDisable(!isTimeLimitEnabled);
        updateTimeLimitButton.setOpacity(isTimeLimitEnabled ? 1.0 : 0.5);

        if (isTimeLimitEnabled && currentGameState != null) {
            timeLimitSpinner.getValueFactory().setValue((int) currentGameState.getPet().getTimeLimit());
        }
    }
    /**
     * Updates the time limit for the selected save slot.
     * Saves the updated time limit to the save file.
     */
    @FXML
    private void handleUpdateTimeLimit() {
        PlayButtonSound();
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

    /**
     * Navigates back to the settings screen.
     */
    @FXML
    private void handleGoBack() {
        PlayButtonSound();
        SceneController.getInstance().switchToSettings();
    }
    /**
     * Revives the pet associated with the current game state.
     * Restores all pet stats and updates the save file.
     *
     * @param actionEvent The action event triggered by the revive button.
     */
    @FXML
    public void revivePet(ActionEvent actionEvent) {
        PlayButtonSound();
        if (currentGameState != null && !currentGameState.getPet().getStats().isAlive()) {
            currentGameState.getPet().getStats().restoreAll();
            petStatusLabel.setText("Status: Alive");
            revivePetButton.setDisable(true);
            saveCurrentGameState();
        }
    }
    @FXML
    private Spinner<Integer> item1Spinner, item2Spinner, item3Spinner, item4Spinner;

    /**
     * Updates the quantity of Item 1 in the inventory.
     */
    @FXML
    private void updateItem1() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem1(item1Spinner.getValue());
            saveCurrentGameState();
        }
    }
    /**
     * Updates the quantity of Item 2 in the inventory.
     */
    @FXML
    private void updateItem2() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem2(item2Spinner.getValue());
            saveCurrentGameState();
        }
    }
    /**
     * Updates the quantity of Item 3 in the inventory.
     */
    @FXML
    private void updateItem3() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem3(item3Spinner.getValue());
            saveCurrentGameState();
        }
    }
    /**
     * Updates the quantity of Item 4 in the inventory.
     */
    @FXML
    private void updateItem4() {
        if (currentGameState != null) {
            currentGameState.getPet().getInventory().setItem4(item4Spinner.getValue());
            saveCurrentGameState();
        }
    }
    /**
     * Saves the current game state to the corresponding save file.
     */
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
    /**
     * Handles the removal of the parent profile.
     * Shows a confirmation dialog and deletes the profile if confirmed.
     */
    @FXML
    private void handleRemoveProfile() {
        // create custom dialog
        StackPane dialogOverlay = new StackPane();
        dialogOverlay.getStyleClass().add("dialog-overlay");

        VBox dialogContent = new VBox();
        dialogContent.getStyleClass().add("dialog-content");
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setSpacing(20);

        Label titleLabel = new Label("Remove Parent Profile?");
        titleLabel.getStyleClass().add("dialog-title");

        Label messageLabel = new Label("This will remove the parent PIN and disable parental controls.");
        messageLabel.getStyleClass().add("dialog-message");

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);

        CustomButton confirmButton = new CustomButton("REMOVE");
        confirmButton.getStyleClass().add("dialog-button-confirm");

        CustomButton cancelButton = new CustomButton("BACK");
        cancelButton.getStyleClass().add("dialog-button-cancel");

        buttonBox.getChildren().addAll(confirmButton, cancelButton);
        dialogContent.getChildren().addAll(titleLabel, messageLabel, buttonBox);
        dialogOverlay.getChildren().add(dialogContent);

        // add dialog to the current scene
        StackPane root = (StackPane) parentModeToggle.getScene().getRoot();
        root.getChildren().add(dialogOverlay);

        // handle button actions
        confirmButton.setOnAction(e -> {
            root.getChildren().remove(dialogOverlay);
            // clear password and disable controls
            userPrefs.setParentPassword("");
            userPrefs.setParentControlsEnabled(false);

            try {
                fileHandler.savePreferences(userPrefs);
                // update UI
                parentModeToggle.setSelected(false);
                handleToggleParentMode();
                // go back to settings
                SceneController.getInstance().switchToSettings();
            } catch (IOException ex) {
                System.err.println("failed to save preferences: " + ex.getMessage());
            }
        });

        cancelButton.setOnAction(e -> {
            root.getChildren().remove(dialogOverlay);
        });
    }
}