package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Priority;
import javafx.scene.control.Label;

public class SaveMenuController {
    @FXML
    private ListView<String> saveSlotList;
    
    @FXML
    private VBox newSaveDialog;
    
    @FXML
    private TextField petNameField;
    
    private int selectedSlotIndex = -1;
    
    @FXML
    public void initialize() {
        ObservableList<String> slots = FXCollections.observableArrayList(
            "CLICK TO CREATE NEW SAVE",
            "CLICK TO CREATE NEW SAVE",
            "CLICK TO CREATE NEW SAVE",
            "CLICK TO CREATE NEW SAVE"
        );
        saveSlotList.setItems(slots);
        
        saveSlotList.setFocusTraversable(false);
        
        saveSlotList.setOnMouseClicked(event -> {
            int index = saveSlotList.getSelectionModel().getSelectedIndex();
            if (index >= 0 && "CLICK TO CREATE NEW SAVE".equals(saveSlotList.getItems().get(index))) {
                selectedSlotIndex = index;
                showNewSaveDialog();
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
                
                playButton.setOnAction(e -> handlePlay(getItem()));
                editButton.setOnAction(e -> handleEdit(getItem()));
                deleteButton.setOnAction(e -> handleDelete(getItem()));
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
                    content.getChildren().setAll(text);
                    
                    if (!"CLICK TO CREATE NEW SAVE".equals(item)) {
                        buttons.setVisible(true);
                        content.getChildren().add(buttons);
                    } else {
                        buttons.setVisible(false);
                    }
                    
                    setGraphic(content);
                }
            }
        });
    }
    
    private void showNewSaveDialog() {
        saveSlotList.setDisable(true);
        newSaveDialog.setVisible(true);
        petNameField.setText("PET " + (selectedSlotIndex + 1));
        petNameField.selectAll();
        petNameField.requestFocus();
    }
    
    @FXML
    private void confirmNewSave() {
        String petName = petNameField.getText().trim();
        if (!petName.isEmpty()) {
            ObservableList<String> slots = saveSlotList.getItems();
            slots.set(selectedSlotIndex, petName);
            hideNewSaveDialog();
            // TODO: Create actual save file
        }
    }
    
    @FXML
    private void cancelNewSave() {
        hideNewSaveDialog();
    }
    
    private void hideNewSaveDialog() {
        newSaveDialog.setVisible(false);
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
        showNewSaveDialog();
    }
    
    private void handleDelete(String saveName) {
        // TODO: Add confirmation dialog
        int index = saveSlotList.getItems().indexOf(saveName);
        saveSlotList.getItems().set(index, "CLICK TO CREATE NEW SAVE");
    }
}