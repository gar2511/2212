package com.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import java.io.IOException;
import java.io.File;
import java.time.LocalTime;
import java.util.Arrays;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.util.FileHandler;
import com.example.components.CustomButton;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import static com.example.App.PlayButtonSound;

/**
 * Controller for the save game menu interface.
 * Manages save slots, including creating new saves, editing existing saves, and deleting them.
 * Also handles UI updates and interactions with the save menu.
 */
public class SaveMenuController {
    @FXML
    ListView<String> saveSlotList;

    @FXML
    VBox newSaveDialogue;

    @FXML
    TextField petNameField;
    @FXML
    ComboBox<String> petTypeComboBox; // ComboBox for pet type

    int selectedSlotIndex = -1;

    /**
     * Initializes the save menu interface.
     * This method is automatically called after the FXML file is loaded.
     * - Populates the save slot list with default values or saved game data.
     * - Sets up event handlers for interactive components.
     */
    @FXML
    public void initialize() {
        FileHandler fileHandler = new FileHandler();

        // Initialize save slots with default placeholders
        ObservableList<String> slots = FXCollections.observableArrayList(
                "CLICK TO CREATE NEW SAVE",
                "CLICK TO CREATE NEW SAVE",
                "CLICK TO CREATE NEW SAVE",
                "CLICK TO CREATE NEW SAVE"
        );

        // Load existing save files and update slots with saved pet names
        File[] saveFiles = fileHandler.getSaveFiles();
        System.out.println(Arrays.toString(saveFiles));
        if (saveFiles != null) {
            for (File file : saveFiles) {
                String fileName = file.getName();
                if (fileName.matches("slot\\d+\\.json")) {
                    int slotIndex = Integer.parseInt(fileName.replaceAll("[^0-9]", ""));
                    try {
                        GameState state = fileHandler.loadGame("slot" + slotIndex);
                        if (state != null && state.getPet() != null) {
                            Pet pet = state.getPet();
                            // Update slot with both pet name and species
                            slots.set(slotIndex, pet.getName() + " " + pet.getSpecies());
                        }
                    } catch (IOException ignored) {
                        // Ignore corrupted files and leave default text
                    }
                }
            }
        }

        saveSlotList.setItems(slots);
        saveSlotList.setFocusTraversable(false);

        // Handle user interaction with save slots
        saveSlotList.setOnMouseClicked(event -> {
            PlayButtonSound();
            int index = saveSlotList.getSelectionModel().getSelectedIndex();
            if (index >= 0 && "CLICK TO CREATE NEW SAVE".equals(saveSlotList.getItems().get(index))) {
                selectedSlotIndex = index;
                showNewSaveDialogue();
            }
        });

        // Set up custom cell rendering for save slots
        setupCustomListCells();
    }

    /**
     * Sets up custom rendering for save slot list items.
     * Each slot includes buttons for playing, editing, or deleting a save.
     * Adds mouse event handling for button visibility on hover.
     */
    private void setupCustomListCells() {
        saveSlotList.setCellFactory(lv -> new ListCell<String>() {
            private final CustomButton playButton = new CustomButton("PLAY");
            private final CustomButton editButton = new CustomButton("EDIT");
            private final CustomButton deleteButton = new CustomButton("DELETE");
            private final HBox buttons = new HBox(10, playButton, editButton, deleteButton);
            private final Label text = new Label();
            private final StackPane content = new StackPane();
            private final FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), text);

            {
                playButton.getStyleClass().add("save-slot-button");
                editButton.getStyleClass().add("save-slot-button");
                deleteButton.getStyleClass().add("save-slot-button");
                
                buttons.setVisible(false);
                buttons.getStyleClass().add("save-slot-buttons");
                text.getStyleClass().add("save-slot-text");
                
                // Center the buttons in the StackPane
                StackPane.setAlignment(buttons, javafx.geometry.Pos.CENTER);
                buttons.setAlignment(javafx.geometry.Pos.CENTER);
                
                playButton.setOnAction(e -> {
                    PlayButtonSound();
                    e.consume();
                    handlePlay(getItem());
                });

                editButton.setOnAction(e -> {
                    PlayButtonSound();
                    e.consume();
                    handleEdit(getItem());
                });

                deleteButton.setOnAction(e -> {
                    PlayButtonSound();
                    e.consume();
                    handleDelete(getItem());
                });

                setOnMouseEntered(e -> {
                    if (getItem() != null && !"CLICK TO CREATE NEW SAVE".equals(getItem())) {
                        buttons.setVisible(true);
                        fadeTransition.setFromValue(1.0);
                        fadeTransition.setToValue(0.3);
                        fadeTransition.play();
                    }
                });

                setOnMouseExited(e -> {
                    buttons.setVisible(false);
                    fadeTransition.setFromValue(0.3);
                    fadeTransition.setToValue(1.0);
                    fadeTransition.play();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    text.setText(item);
                    content.getChildren().setAll(text, buttons);
                    StackPane.setAlignment(text, javafx.geometry.Pos.CENTER);
                    setGraphic(content);
                    
                    buttons.setVisible(false);
                    buttons.setManaged(!"CLICK TO CREATE NEW SAVE".equals(item));
                    text.setOpacity(1.0);
                }
            }
        });
    }

    /**
     * Displays the dialogue for creating or editing a save.
     * Allows the user to input a pet name and confirm the action.
     */
    void showNewSaveDialogue() {
        PlayButtonSound();
        String petName = "PET " + (selectedSlotIndex + 1);
        saveSlotList.setDisable(true);
        newSaveDialogue.setVisible(true);
        petNameField.setText(petName);
        petNameField.selectAll();
        petNameField.requestFocus();
    }

    /**
     * Displays the dialogue for creating or editing a save.
     * Allows the user to input a pet name and confirm the action.
     * If editing an existing save with a name, display that.
     * @param petName the existing pet name
     */
    private void showNewSaveDialogue(String petName) {
        PlayButtonSound();
        saveSlotList.setDisable(true);
        newSaveDialogue.setVisible(true);
        petNameField.setText(petName);
        petNameField.selectAll();
        petNameField.requestFocus();
    }

    /**
     * Confirms the creation or editing of a save slot.
     * Saves the new game state to a file and updates the save slot list.
     */
    @FXML
    private void confirmNewSave() {
        PlayButtonSound();
        String petName = petNameField.getText().trim();
        String petType = petTypeComboBox.getSelectionModel().getSelectedItem();
        if (!petName.isEmpty() && petType != null) {
            try {
                Pet pet = new Pet(petName, petType, selectedSlotIndex);
                GameState gameState = GameState.getCurrentState();
                gameState.setPet(pet);

                FileHandler fileHandler = new FileHandler();
                fileHandler.saveGame("slot" + selectedSlotIndex, gameState);

                saveSlotList.getItems().set(selectedSlotIndex, petName + " " + petType);
                hideNewSaveDialogue();
            } catch (IOException e) {
                handleSaveError("save game", e);
            }
        }
    }

    /**
     * Cancels the current save creation/edit operation.
     * Hides the dialogue and restores the save slot list.
     */
    @FXML
    void cancelNewSave() {
        PlayButtonSound();
        hideNewSaveDialogue();
    }

    /**
     * Hides the new save dialogue and re-enables the save slot list.
     */
    private void hideNewSaveDialogue() {
        newSaveDialogue.setVisible(false);
        saveSlotList.setDisable(false);
        selectedSlotIndex = -1;
    }

    /**
     * Returns the user to the main menu.
     */
    @FXML
    private void goBack() {
        PlayButtonSound();
        SceneController.getInstance().switchToMainMenu();
    }

    /**
     * Handles the play button action for a save slot.
     * Checks if the current time is within allowed playtime or both fields are null,
     * Then switches to the game scene.
     *
     * @param saveName The name of the save to load and play.
     */
    private void handlePlay(String saveName) {
        int index = saveSlotList.getItems().indexOf(saveName);
        try {
            System.out.println("Playing: " + saveName);
            FileHandler fileHandler = new FileHandler();
            GameState loadedState = fileHandler.loadGame("slot" + index);
            Pet pet = loadedState.getPet();

            // Retrieve start and end times from the Pet object
            LocalTime startTime = pet.getStartTime(); // Assumes getter for LocalTime
            LocalTime endTime = pet.getEndTime();     // Assumes getter for LocalTime

            // Validate the current time
            if (isAllowedToPlay(startTime, endTime)) {
                GameState.loadState(loadedState); // Set the loaded state as the current state
                SceneController.getInstance().switchToGame();
            } else {
                showErrorDialog("Playtime Restricted",
                        "You can only play between " +
                                (startTime != null ? startTime.toString() : "any time") +
                                " and " + (endTime != null ? endTime.toString() : "any time") + ".");
            }
        } catch (IOException e) {
            e.printStackTrace();
            handleSaveError("load game", e);
        }
    }
    /**
     * Checks whether the current time is within the allowed playtime range.
     *
     * @param startTimeStr The start time as a string (null means no restriction).
     * @param endTimeStr   The end time as a string (null means no restriction).
     * @return true if the current time is within the range or no restrictions are set.
     */
    /**
     * Checks whether the current time is within the allowed playtime range.
     *
     * @param startTime The start time as a LocalTime (null means no restriction).
     * @param endTime   The end time as a LocalTime (null means no restriction).
     * @return true if the current time is within the range or no restrictions are set.
     */
    private boolean isAllowedToPlay(LocalTime startTime, LocalTime endTime) {
        LocalTime now = LocalTime.now();

        if (startTime == null && endTime == null) {
            return true; // No restrictions
        }
        if (startTime != null && endTime != null) {
            if (startTime.isBefore(endTime)) {
                return !now.isBefore(startTime) && !now.isAfter(endTime);
            } else {
                // Handles overnight ranges (e.g., 22:00 to 06:00)
                return !now.isBefore(startTime) || !now.isAfter(endTime);
            }
        }
        if (startTime != null) {
            return !now.isBefore(startTime);
        }
        if (endTime != null) {
            return !now.isAfter(endTime);
        }
        return false;
    }

    /**
     * Displays an error dialog with the provided title and message.
     *
     * @param title   The title of the dialog.
     * @param message The message of the dialog.
     */
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the edit button action for a save slot.
     * Opens the dialogue to rename the save.
     *
     * @param saveName The name of the save to edit.
     */
    private void handleEdit(String saveName) {
        int index = saveSlotList.getItems().indexOf(saveName);
        try {
            FileHandler fileHandler = new FileHandler();
            GameState thisSave = fileHandler.loadGame("slot" + index);
            Pet thisPet = thisSave.getPet();
            String petName = thisPet.getName();
            selectedSlotIndex = saveSlotList.getItems().indexOf(saveName);
            showNewSaveDialogue(petName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the delete button action for a save slot.
     * Deletes the save file and updates the save slot list.
     *
     * @param saveName The name of the save to delete.
     */
    private void handleDelete(String saveName) {
        int index = saveSlotList.getItems().indexOf(saveName);
        try {
            FileHandler fileHandler = new FileHandler();
            fileHandler.deleteSave("slot" + index);
            Platform.runLater(() -> {
                saveSlotList.getItems().set(index, "CLICK TO CREATE NEW SAVE");
                saveSlotList.getSelectionModel().clearSelection();
            });
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: Show error dialog to user
        }
    }

    /**
     * Handles errors encountered during save operations.
     * Displays an alert dialog to notify the user.
     * @param operation The type of operation that failed.
     * @param e The exception that occurred.
     */
    private void handleSaveError(String operation, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save Error");
        alert.setHeaderText("Failed to " + operation);
        alert.setContentText("An error occurred: " + e.getMessage());
        alert.showAndWait();
    }
}
