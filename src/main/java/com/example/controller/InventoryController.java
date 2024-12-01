package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InventoryController {

    public Label pointsLabel;
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

    @FXML
    private void buyItem1() {
        buyItem(10, 1); // Item 1 costs 10 points
    }

    @FXML
    private void buyItem2() {
        buyItem(15, 2); // Item 2 costs 15 points
    }

    @FXML
    private void buyItem3() {
        buyItem(20, 3); // Item 3 costs 20 points
    }

    @FXML
    private void buyItem4() {
        buyItem(25, 4); // Item 4 costs 25 points
    }

    private void buyItem(int cost,int itemNumber) {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        // Check if the user has enough points
        if (pet.getScore() >= cost) {
            switch (itemNumber) {
                case 1:
                    if (pet.getInventory().getItem1() < 99) {
                        pet.setScore(pet.getScore() - cost); // Deduct points
                        pet.getInventory().addItem1(); // Add 1 to item 1
                        System.out.println("Added Item 1");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 1.");
                    }
                    break;
                case 2:
                    if (pet.getInventory().getItem2() < 99) {
                        pet.setScore(pet.getScore() - cost); // Deduct points
                        pet.getInventory().addItem2(); // Add 1 to item 2
                        System.out.println("Added Item 2");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 2.");
                    }
                    break;
                case 3:
                    if (pet.getInventory().getItem3() < 99) {
                        pet.setScore(pet.getScore() - cost); // Deduct points
                        pet.getInventory().addItem3(); // Add 1 to item 3
                        System.out.println("Added Item 3");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 3.");
                    }
                    break;
                case 4:
                    if (pet.getInventory().getItem4() < 99) {
                        pet.setScore(pet.getScore() - cost); // Deduct points
                        pet.getInventory().addItem4(); // Add 1 to item 4
                        System.out.println("Added Item 4");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 4.");
                    }
                    break;
                default:
                    System.out.println("Invalid item number.");
                    break;
            }
            updateItemQuantities();
            updatePointsDisplay();

        } else {
            System.out.println("Not enough points to buy this item!");
        }
    }
    private void updateItemQuantities() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        item1Quantity.setText(Integer.toString(pet.getInventory().getItem1()));
        item2Quantity.setText(Integer.toString(pet.getInventory().getItem2()));
        item3Quantity.setText(Integer.toString(pet.getInventory().getItem3()));
        item4Quantity.setText(Integer.toString(pet.getInventory().getItem4()));
    }

    private void updatePointsDisplay() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        System.out.println("HEre is your poiint" + pet.getScore());
        pointsLabel.setText("Points: " + Integer.toString(pet.getScore()));
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
        // Set Points
        pointsLabel.setText("Points: " + Integer.toString(pet.getScore()));

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
