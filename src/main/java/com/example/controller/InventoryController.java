package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InventoryController {

    @FXML
    private ImageView item1Image, item2Image, item3Image, item4Image;

    @FXML
    private Label item1Quantity, item2Quantity, item3Quantity, item4Quantity;


    @FXML
    private RadioButton radioItem1, radioItem2, radioItem3, radioItem4;
    @FXML
    private ToggleGroup item12ToggleGroup;

    @FXML
    private ToggleGroup item34ToggleGroup;


    @FXML
    public void initialize() {
        item12ToggleGroup = new ToggleGroup();
        radioItem1.setToggleGroup(item12ToggleGroup);
        radioItem2.setToggleGroup(item12ToggleGroup);

        item34ToggleGroup = new ToggleGroup();
        radioItem3.setToggleGroup(item34ToggleGroup);
        radioItem4.setToggleGroup(item34ToggleGroup);
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

        // Initialize radio button selection based on defaults
        if (pet.getDefaultItem12() == 1) {
            radioItem1.setSelected(true);
        } else {
            radioItem2.setSelected(true);
        }

        if (pet.getDefaultItem34() == 3) {
            radioItem3.setSelected(true);
        } else {
            radioItem4.setSelected(true);
        }

        // Add listeners to update defaults
        item12ToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == radioItem1) {
                pet.setDefaultItem12(1);
            } else if (newValue == radioItem2) {
                pet.setDefaultItem12(2);
            }
        });

        item34ToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == radioItem3) {
                pet.setDefaultItem34(3);
            } else if (newValue == radioItem4) {
                pet.setDefaultItem34(4);
            }
        });
    }

    @FXML
    private void goBack() {
        // Navigate back to the game screen
        SceneController.getInstance().switchToGame();
    }
}
