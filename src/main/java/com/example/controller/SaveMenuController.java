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
import com.example.model.GameState;
import com.example.model.Pet;
import com.example.util.FileHandler;

public class SaveMenuController {
    @FXML
    private ListView<String> saveSlotList;

    @FXML
    private VBox newSaveDialogue;

    @FXML
    private TextField petNameField;

    private int selectedSlotIndex = -1;

    @FXML
    public void initialize() {
        FileHandler fileHandler = new FileHandler();
        ObservableList<String> slots = FXCollections.observableArrayList(
                "CLICK TO CREATE NEW SAVE",
                "CLICK TO CREATE NEW SAVE",
                "CLICK TO CREATE NEW SAVE",
                "CLICK TO CREATE NEW SAVE"
        );

        // Load existing saves
        File[] saveFiles = fileHandler.getSaveFiles();
        if (saveFiles != null) {
            for (File file : saveFiles) {
                String fileName = file.getName();
                if (fileName.matches("slot\\d+\\.json")) {
                    int slotIndex = Integer.parseInt(fileName.replaceAll("[^0-9]", ""));
                    try {
                        GameState state = fileHandler.loadGame("slot" + slotIndex);
                        if (state != null && state.getPet() != null) {
                            slots.set(slotIndex, state.getPet().getName());
                        }
                    } catch (IOException ignored) {
                        // If file is corrupted, keep default text
                    }
                }
            }
        }

        saveSlotList.setItems(slots);
        saveSlotList.setFocusTraversable(false);

        saveSlotList.setOnMouseClicked(event -> {
            int index = saveSlotList.getSelectionModel().getSelectedIndex();
            if (index >= 0 && "CLICK TO CREATE NEW SAVE".equals(saveSlotList.getItems().get(index))) {
                selectedSlotIndex = index;
                showNewSaveDialogue();
            }
        });

        setupCustomListCells();
    }

    private void setupCustomListCells() {
        saveSlotList.setCellFactory(lv -> new ListCell<String>() {
            private final Button playButton = new Button("PLAY");
            private final Button editButton = new Button("EDIT");
            private final Button deleteButton = new Button("DELETE");
            private final HBox buttons = new HBox(10, playButton, editButton, deleteButton);
            private final HBox content = new HBox(20);

            {
                buttons.setVisible(false);
                buttons.getStyleClass().add("save-slot-buttons");
                playButton.getStyleClass().add("save-slot-button");
                editButton.getStyleClass().add("save-slot-button");
                deleteButton.getStyleClass().add("save-slot-button");

                content.setAlignment(javafx.geometry.Pos.CENTER);
                buttons.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

                playButton.setOnAction(e -> {
                    e.consume();
                    handlePlay(getItem());
                });
                editButton.setOnAction(e -> {
                    e.consume();
                    handleEdit(getItem());
                });
                deleteButton.setOnAction(e -> {
                    e.consume();
                    handleDelete(getItem());
                });

                setOnMouseEntered(e -> {
                    if (getItem() != null && !"CLICK TO CREATE NEW SAVE".equals(getItem())) {
                        buttons.setVisible(true);
                    }
                });

                setOnMouseExited(e -> {
                    buttons.setVisible(false);
                });

                buttons.setOnMouseClicked(event -> event.consume());
                content.setOnMouseClicked(event -> {
                    if (getItem() != null && !"CLICK TO CREATE NEW SAVE".equals(getItem())) {
                        event.consume();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label text = new Label(item);
                    text.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(text, Priority.ALWAYS);
                    content.getChildren().setAll(text, buttons);
                    setGraphic(content);
                }
            }
        });

        saveSlotList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            saveSlotList.refresh();
        });
    }

    private void showNewSaveDialogue() {
        saveSlotList.setDisable(true);
        newSaveDialogue.setVisible(true);
        petNameField.setText("PET " + (selectedSlotIndex + 1));
        petNameField.selectAll();
        petNameField.requestFocus();
    }

    @FXML
    private void confirmNewSave() {
        String petName = petNameField.getText().trim();
        if (!petName.isEmpty()) {
            try {
                // Create new GameState with pet
                GameState gameState = new GameState();
                Pet pet = new Pet(petName);
                gameState.setPet(pet);
                
                // Save to file
                FileHandler fileHandler = new FileHandler();
                fileHandler.saveGame("slot" + selectedSlotIndex, gameState);
                
                // Update UI
                ObservableList<String> slots = saveSlotList.getItems();
                slots.set(selectedSlotIndex, petName);
                hideNewSaveDialogue();
            } catch (IOException e) {
                // TODO: Show error dialog to user
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelNewSave() {
        hideNewSaveDialogue();
    }

    private void hideNewSaveDialogue() {
        newSaveDialogue.setVisible(false);
        saveSlotList.setDisable(false);
        selectedSlotIndex = -1;
    }

    @FXML
    private void goBack() {
        SceneController.getInstance().switchToMainMenu();
    }

    private void handlePlay(String saveName) {
        // TODO: Implement play functionality
        System.out.println("Playing: " + saveName);
    }

    private void handleEdit(String saveName) {
        selectedSlotIndex = saveSlotList.getItems().indexOf(saveName);
        showNewSaveDialogue();
    }

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
}