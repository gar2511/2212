package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InventoryController {

    @FXML
    private ImageView item1Image, item2Image, item3Image, item4Image;

    @FXML
    private Label item1Quantity, item2Quantity, item3Quantity, item4Quantity;

    @FXML
    private Button defaultItem1, defaultItem2, defaultItem3, defaultItem4;

    @FXML
    private Label selectedItem12Label, selectedItem34Label;

    @FXML
    private void selectDefaultItem1() {
        setSelected(defaultItem1, selectedItem12Label, "Item 1");
        GameState.getCurrentState().getPet().setDefaultItem12(1);
    }

    @FXML
    private void selectDefaultItem2() {
        setSelected(defaultItem2, selectedItem12Label, "Item 2");
        GameState.getCurrentState().getPet().setDefaultItem12(2);
    }

    @FXML
    private void selectDefaultItem3() {
        setSelected(defaultItem3, selectedItem34Label, "Item 3");
        GameState.getCurrentState().getPet().setDefaultItem34(3);
    }

    @FXML
    private void selectDefaultItem4() {
        setSelected(defaultItem4, selectedItem34Label, "Item 4");
        GameState.getCurrentState().getPet().setDefaultItem34(4);
    }

    private void setSelected(Button selectedButton, Label selectionLabel, String itemName) {
        // Reset all buttons to default style
        defaultItem1.getStyleClass().remove("selected");
        defaultItem2.getStyleClass().remove("selected");
        defaultItem3.getStyleClass().remove("selected");
        defaultItem4.getStyleClass().remove("selected");

        // Add selected style to the clicked button
        selectedButton.getStyleClass().add("selected");

        // Update the label
        selectionLabel.setText("Currently Selected: " + itemName);
    }

    @FXML
    public void initialize() {
        // Load game state and pet
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        // Set item images
        item1Image.setImage(new Image("/images/item1.png"));
        item2Image.setImage(new Image("/images/item2.png"));
        item3Image.setImage(new Image("/images/item3.png"));
        item4Image.setImage(new Image("/images/item4.png"));

        // Set item quantities
        item1Quantity.setText(Integer.toString(pet.getInventory().getItem1()));
        item2Quantity.setText(Integer.toString(pet.getInventory().getItem2()));
        item3Quantity.setText(Integer.toString(pet.getInventory().getItem3()));
        item4Quantity.setText(Integer.toString(pet.getInventory().getItem4()));

        // Initialize button selection and labels based on defaults
        if (pet.getDefaultItem12() == 1) {
            setSelected(defaultItem1, selectedItem12Label, "Item 1");
        } else {
            setSelected(defaultItem2, selectedItem12Label, "Item 2");
        }

        if (pet.getDefaultItem34() == 3) {
            setSelected(defaultItem3, selectedItem34Label, "Item 3");
        } else {
            setSelected(defaultItem4, selectedItem34Label, "Item 4");
        }
    }

    @FXML
    private void goBack() {
        // Navigate back to the game screen
        SceneController.getInstance().switchToGame();
    }
}
