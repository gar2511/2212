package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.App.PlayButtonSound;

/**
 * Controller class for managing the inventory screen.
 * Handles displaying item quantities, points, and selected items,
 * as well as purchasing items and setting default items.
 */
public class InventoryController {
    /**
     * Label to display the current points available to the player.
     * Updates dynamically as points are spent or earned during the game.
     */
    public Label pointsLabel;
    @FXML
    private ImageView item1Image, item2Image, item3Image, item4Image;

    @FXML
    private Label item1Quantity, item2Quantity, item3Quantity, item4Quantity;

    @FXML
    private Button defaultItem1, defaultItem2, defaultItem3, defaultItem4;

    @FXML
    private Label selectedItem12Label, selectedItem34Label;

    /**
     * Sets the default item 1 and updates the UI to reflect the selection.
     */
    @FXML
    private void selectDefaultItem1() {
        setSelected(defaultItem1, selectedItem12Label, "Item 1");
        GameState.getCurrentState().getPet().setDefaultItem12(1);
    }

    /**
     * Sets the default item 2 and updates the UI to reflect the selection.
     */
    @FXML
    private void selectDefaultItem2() {
        setSelected(defaultItem2, selectedItem12Label, "Item 2");
        GameState.getCurrentState().getPet().setDefaultItem12(2);
    }

    /**
     * Sets the default item 3 and updates the UI to reflect the selection.
     */
    @FXML
    private void selectDefaultItem3() {
        setSelected(defaultItem3, selectedItem34Label, "Item 3");
        GameState.getCurrentState().getPet().setDefaultItem34(3);
    }

    /**
     * Sets the default item 4 and updates the UI to reflect the selection.
     */
    @FXML
    private void selectDefaultItem4() {
        setSelected(defaultItem4, selectedItem34Label, "Item 4");
        GameState.getCurrentState().getPet().setDefaultItem34(4);
    }

    /**
     * Purchases item 1 if the user has enough points.
     */
    @FXML
    private void buyItem1() {
        buyItem(10, 1); // Item 1 costs 10 points
    }

    /**
     * Purchases item 2 if the user has enough points.
     */
    @FXML
    private void buyItem2() {
        buyItem(15, 2); // Item 2 costs 15 points
    }

    /**
     * Purchases item 3 if the user has enough points.
     */
    @FXML
    private void buyItem3() {
        buyItem(20, 3); // Item 3 costs 20 points
    }

    /**
     * Purchases item 4 if the user has enough points.
     */
    @FXML
    private void buyItem4() {
        buyItem(25, 4); // Item 4 costs 25 points
    }

    /**
     * Handles the logic for purchasing an item.
     *
     * @param cost       The cost of the item in points.
     * @param itemNumber The number representing the item.
     */
    private void buyItem(int cost, int itemNumber) {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet.getScore() >= cost) {
            switch (itemNumber) {
                case 1:
                    if (pet.getInventory().getItem1() < 99) {
                        pet.setScore(pet.getScore() - cost);
                        pet.getInventory().addItem1();
                        System.out.println("Added Item 1");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 1.");
                    }
                    break;
                case 2:
                    if (pet.getInventory().getItem2() < 99) {
                        pet.setScore(pet.getScore() - cost);
                        pet.getInventory().addItem2();
                        System.out.println("Added Item 2");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 2.");
                    }
                    break;
                case 3:
                    if (pet.getInventory().getItem3() < 99) {
                        pet.setScore(pet.getScore() - cost);
                        pet.getInventory().addItem3();
                        System.out.println("Added Item 3");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 3.");
                    }
                    break;
                case 4:
                    if (pet.getInventory().getItem4() < 99) {
                        pet.setScore(pet.getScore() - cost);
                        pet.getInventory().addItem4();
                        System.out.println("Added Item 4");
                    } else {
                        System.out.println("You already have the maximum quantity of Item 4.");
                    }
                    break;
                default:
                    System.out.println("Invalid item number.");
            }
            updateItemQuantities();
            updatePointsDisplay();
        } else {
            System.out.println("Not enough points to buy this item!");
        }
    }

    /**
     * Updates the displayed quantities of all items in the inventory.
     */
    private void updateItemQuantities() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        item1Quantity.setText(Integer.toString(pet.getInventory().getItem1()));
        item2Quantity.setText(Integer.toString(pet.getInventory().getItem2()));
        item3Quantity.setText(Integer.toString(pet.getInventory().getItem3()));
        item4Quantity.setText(Integer.toString(pet.getInventory().getItem4()));
    }

    /**
     * Updates the points display label with the current score.
     */
    private void updatePointsDisplay() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        pointsLabel.setText("Points: " + pet.getScore());
    }

    /**
     * Sets the selected default item and updates the UI to reflect the selection.
     *
     * @param selectedButton The button representing the selected item.
     * @param selectionLabel The label to update with the selection.
     * @param itemName       The name of the selected item.
     */
    private void setSelected(Button selectedButton, Label selectionLabel, String itemName) {
        defaultItem1.getStyleClass().remove("selected");
        defaultItem2.getStyleClass().remove("selected");
        defaultItem3.getStyleClass().remove("selected");
        defaultItem4.getStyleClass().remove("selected");

        selectedButton.getStyleClass().add("selected");
        selectionLabel.setText("Currently Selected: " + itemName);
    }

    /**
     * Initializes the inventory screen with item data and default selections.
     */
    @FXML
    public void initialize() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        item1Image.setImage(new Image("/images/item1.png"));
        item2Image.setImage(new Image("/images/item2.png"));
        item3Image.setImage(new Image("/images/item3.png"));
        item4Image.setImage(new Image("/images/item4.png"));

        pointsLabel.setText("Points: " + pet.getScore());
        updateItemQuantities();

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

    /**
     * Navigates back to the game screen.
     */
    @FXML
    private void goBack() {
        PlayButtonSound();
        SceneController.getInstance().switchToGame();
    }
}
