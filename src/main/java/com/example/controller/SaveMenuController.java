package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
            if (index >= 0) {
                selectedSlotIndex = index;
                showNewSaveDialog();
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
}