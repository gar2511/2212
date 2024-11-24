package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.model.VitalStats;
import com.example.util.FileHandler;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Random;

/**
 * Controller class responsible for managing the game scene/view.
 * This class handles the mole animation, sprite rendering, and navigation
 * to other scenes within the application.
 */
public class GameController {


    public ProgressBar energyBar;
    public ProgressBar healthBar;
    public ProgressBar hungerBar;
    public ProgressBar happinessBar;
    @FXML
    private ImageView moleSprite;


    private Timeline animation;
    private Timeline statsDecayTimeline;
    private Random random = new Random();
    @FXML
    private Button feedButton, playButton, giftButton, exerciseButton, vetButton, inventoryButton;

    @FXML
    private Label gameOverLabel; // For the "Game Over" message


    /**
     * Constructor for GameController.
     * Initializes the controller when the game scene is created.
     */
    public GameController() {
        System.out.println("GameController initialized");

    }

    /**
     * Initializes the game scene.
     * This method is automatically called after the FXML file is loaded.
     * It sets up the mole sprite, handles fallback rendering in case of errors,
     * and starts the mole animation.
     */
    @FXML
    public void initialize() {
        // Get the current GameState instance
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet(); // Retrieve the Pet object
        if (pet != null) {
            VitalStats stats = pet.getStats();

            // Bind progress bars to stats
            energyBar.progressProperty().bind(Bindings.divide(stats.energyProperty(), 100.0));
            healthBar.progressProperty().bind(Bindings.divide(stats.healthProperty(), 100.0));
            hungerBar.progressProperty().bind(Bindings.divide(stats.hungerProperty(), 100.0));
            happinessBar.progressProperty().bind(Bindings.divide(stats.happinessProperty(), 100.0));


            System.out.println("Loaded Pet: " + pet.getName() + ", Type: " + pet.getSpecies());
            // Update UI or initialize game logic with the Pet's data
            //setupPetData(pet);
        } else {
            System.out.println("No pet found. Please create or load a save.");
        }
        try {

            // If pet is a certain species, load that image
            if (pet.getSpecies().equals("Mole")) {
                // Load image from resources
                Image moleImage = new Image(getClass().getResourceAsStream("/images/mole.png"));
                if (moleImage.isError()) {
                    System.err.println("Error loading mole image: " + moleImage.getException());
                } else {
                    moleSprite.setImage(moleImage);
                }
            }
            else if (pet.getSpecies().equals("Bear")) {
                // Load image from resources
                Image bearImage = new Image(getClass().getResourceAsStream("/images/bear.png"));
                if (bearImage.isError()) {
                    System.err.println("Error loading mole image: " + bearImage.getException());
                } else {
                    moleSprite.setImage(bearImage);
                }
            }
            else if (pet.getSpecies().equals("Cat")) {
                // Load image from resources
                Image catImage = new Image(getClass().getResourceAsStream("/images/cat.png"));
                if (catImage.isError()) {
                    System.err.println("Error loading mole image: " + catImage.getException());
                } else {
                    moleSprite.setImage(catImage);
                }
            }
            else { // if no image then mole image
                System.out.println("Default Image, " + pet.getSpecies() + " image not found.");
                Image moleImage = new Image(getClass().getResourceAsStream("/images/mole.png"));
                if (moleImage.isError()) {
                    System.err.println("Error loading mole image: " + moleImage.getException());
                } else {
                    moleSprite.setImage(moleImage);
                }
            }

        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialize hotkeys
        setupHotkeys();

        startStatsDecay();


    }
    /**
     * Starts the timeline for the constant decay of stats.
     */
    private void startStatsDecay() {
        statsDecayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            GameState gameState = GameState.getCurrentState();
            Pet pet = gameState.getPet();

            if (pet != null) {
                VitalStats stats = pet.getStats();
                // Initialize species-specific modifiers
                int speciesHungerMod = 0;
                int speciesHappinessMod = 0;
                int speciesEnergyMod = 0;
                int speciesHealthMod = 0;

                // Apply species-specific modifiers
                switch (pet.getSpecies()) {
                    case "cat":
                        speciesHungerMod = -1; // Eats less food
                        speciesHappinessMod = 0; // Happiness decays normally
                        speciesEnergyMod = 1; // Loses energy faster
                        speciesHealthMod = 0;   // Health decays normally
                        break;
                        //TODO: why is B capitalized
                    case "Bear":
                        speciesHungerMod = 2; // Hunger decreases rapidly
                        speciesHappinessMod = -1; // Happiness decays slower
                        speciesEnergyMod = 1;  // Energy decays normally
                        speciesHealthMod = 0; // Health decays slower
                        break;
                    case "mole":
                        // Mole is the default (no modifiers)
                        speciesHungerMod = 0;
                        speciesHappinessMod = 0;
                        speciesEnergyMod = 0;
                        speciesHealthMod = 0;
                        break;
                    default:
                        System.out.println("Unknown species: " + pet.getSpecies());
                        break;
                }



                // Decay the stats
                stats.decreaseEnergy(2+ speciesEnergyMod+ pet.getStats().getEnergyMod());   // Decrease energy by 1 every second
                stats.decreaseHealth(2+ speciesHealthMod+ pet.getStats().getHealthMod());  // Decrease health by 1 every second
                stats.decreaseHunger(2+ speciesHungerMod+ pet.getStats().getHungerMod());   // Decrease hunger by 1 every second
                stats.decreaseHappiness(2+ speciesHappinessMod+pet.getStats().getHappinessMod()); // Decrease happiness by 1 every second

                // Log the changes (for debugging)
                System.out.println("Stats Decayed: Energy=" + stats.getEnergy() +
                        ", Health=" + stats.getHealth() +
                        ", Hunger=" + stats.getHunger() +
                        ", Happiness=" + stats.getHappiness());

                // Check the petState array for critical states
                int[] petState = stats.getState();
                if (petState[3] == 1) {
                    handleCriticalState(3);
                    return; // Ignore other states if Hunger is critical
                }
                for (int i = 0; i < petState.length-1; i++) {
                    if (petState[i] == 1) {
                        handleCriticalState(i); // Handle each critical state
                    } else {maintainState(i);}
                }
            }
        }));

        statsDecayTimeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely
        statsDecayTimeline.play(); // Start the timeline
    }

    /**
     * Stops the timeline for stats decay.
     */
    private void stopStatsDecay() {
        if (statsDecayTimeline != null) {
            statsDecayTimeline.stop();
        }
    }

    /**
     * Event handler for the back button.
     * Stops the mole animation and switches the scene back to the main menu.
     */
    @FXML
    private void goBack() {
        stopStatsDecay();
        if (animation != null) {
            animation.stop();
        }
        SceneController.getInstance().switchToMainMenu();
    }
    @FXML
    private void feedPet() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet != null) {
            if (pet.getDefaultItem12()==1) {
                VitalStats stats = pet.getStats();
                stats.increaseHunger(20); // Increase hunger by 20
                stats.increaseHappiness(10); // Increase happiness by 10
                System.out.println(pet.getName() + " has been fed Item 1! Hunger and happiness increased.");
            }
            if (pet.getDefaultItem12()==2) {
                VitalStats stats = pet.getStats();
                stats.increaseHunger(30); // Increase hunger by 20
                stats.increaseHappiness(5); // Increase happiness by 10
                System.out.println(pet.getName() + " has been fed Item 2! Hunger and happiness increased.");
            }
        } else {
            System.out.println("No pet to feed!");
        }

    }
    @FXML
    private void playPet(){
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet != null) {
            VitalStats stats = pet.getStats();
            stats.decreaseEnergy(15); // Decrease energy by 15
            stats.increaseHappiness(20); // Increase happiness by 20
            stats.decreaseHunger(10); // Decrease hunger by 10
            System.out.println(pet.getName() + " is playing! Energy and hunger decreased, happiness increased.");
        } else {
            System.out.println("No pet to play with!");
        }
    }
    @FXML
    private void giveGift(){
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet.getDefaultItem34()==3) {
            VitalStats stats = pet.getStats();
            stats.increaseEnergy(20); // Increase Energy by 20
            stats.increaseHappiness(10); // Increase happiness by 10
            stats.decreaseHealth(40);
            System.out.println(pet.getName() + " has been fed Item 3! Hunger and happiness increased.");
        }
        if (pet.getDefaultItem34()==4) {
            VitalStats stats = pet.getStats();
            stats.increaseHunger(60); // Increase hunger by 60
            stats.increaseHappiness(15); // Decrease happiness by 15
            System.out.println(pet.getName() + " has been fed Item 4! Hunger and happiness increased.");
        }
    }
    @FXML
    private void exercisePet(){
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet != null) {
            VitalStats stats = pet.getStats();
            stats.decreaseHunger(20); // Decrease hunger
            stats.decreaseHappiness(5); // Decrease happiness slightly
            stats.increaseEnergy(10); // Increase energy
            System.out.println(pet.getName() + " has exercised! Energy increased, hunger decreased.");
        } else {
            System.out.println("No pet to exercise!");
        }
    }
    @FXML
    private void takeVet(){
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet != null) {
            VitalStats stats = pet.getStats();
            stats.increaseHealth(50); // Increase health
            stats.increaseEnergy(30); // Increase energy
            stats.decreaseHappiness(10); // Decrease happiness
            System.out.println(pet.getName() + " went to the vet! Health and hygiene increased, but happiness decreased.");
        } else {
            System.out.println("No pet to take to the vet!");
        }
    }
    @FXML
    private void openInventory(){
        System.out.println("Inventory has not been made yet");
        stopStatsDecay();
        if (animation != null) {
            animation.stop();
        }
        SceneController.getInstance().switchToInventory();
    }
    @FXML
    private void saveGame() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        try {
            FileHandler fileHandler = new FileHandler();
            fileHandler.saveGame("slot" + pet.getSaveID(), gameState); // Save with a filename
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleCriticalState(int index) {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet == null) {
            System.err.println("No pet found in handleCriticalState.");
            return;
        }

        VitalStats stats = pet.getStats();

        if (index == 3) { // Health is critically low
            System.out.println("Checking health critical state...");
            if (stats.getHealth() <= 0) {
                System.out.println("Health has reached 0. Game over.");
                stopStatsDecay(); // Stop stat decay

                // Disable all buttons except for "New Game" and "Load Game"
                feedButton.setDisable(true);
                playButton.setDisable(true);
                giftButton.setDisable(true);
                exerciseButton.setDisable(true);
                vetButton.setDisable(true);
                inventoryButton.setDisable(true);

                // Show "Game Over" message
                if (gameOverLabel == null) {
                    System.err.println("gameOverLabel is null. Check FXML binding.");
                } else {
                    gameOverLabel.setVisible(true);
                    System.out.println("Game Over Label is now visible.");
                }
                return; // Exit after handling Game Over
            }
        } else {
            switch (index) {
                case 0: // Hunger
                    System.out.println("Hunger is critically low! Consider feeding the pet.");
                    stats.setHappinessMod(5);
                    stats.setHealthMod(5);
                    break;
                case 1: // Happiness
                    System.out.println("Happiness is critically low! Consider playing with the pet.");
                    exerciseButton.setDisable(true);
                    vetButton.setDisable(true);
                    break;
                case 2: // Energy
                    System.out.println("Energy is critically low! Pet needs rest.");
                    feedButton.setDisable(true);
                    playButton.setDisable(true);
                    giftButton.setDisable(true);
                    exerciseButton.setDisable(true);
                    vetButton.setDisable(true);
                    stats.setEnergyMod(-7);
                    break;
                default:
                    System.out.println("Unknown critical state detected.");
                    break;
            }
        }
    }
    private void maintainState(int index) {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        if (pet == null) {
            System.err.println("No pet found in maintainState.");
            return;
        }
        VitalStats stats = pet.getStats();
        switch (index) {
            case 0:
                stats.setHappinessMod(0);
                stats.setHealthMod(0);
                break;
            case 1:
                exerciseButton.setDisable(false);
                vetButton.setDisable(false);
                break;
            case 2:
                feedButton.setDisable(false);
                playButton.setDisable(false);
                giftButton.setDisable(false);
                exerciseButton.setDisable(false);
                vetButton.setDisable(false);
                stats.setEnergyMod(0);
                break;
            default:
                break;
        }
    }
    private void setupHotkeys() {
        moleSprite.setFocusTraversable(true); // Ensure moleSprite can receive key events
        moleSprite.requestFocus(); // Request focus on the moleSprite node

        moleSprite.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F: // Feed the pet
                    feedPet();
                    break;
                case P: // Play with the pet
                    playPet();
                    break;
                case G: // Give a gift
                    giveGift();
                    break;
                case E: // Exercise the pet
                    exercisePet();
                    break;
                case V: // Take the pet to the vet
                    takeVet();
                    break;
                case I: // Open inventory
                    openInventory();
                    break;
                case S: // Save the game
                    saveGame();
                    break;
                case Q: // Go back to the main menu
                    goBack();
                    break;
                default:
                    System.out.println("Unhandled key: " + event.getCode());
            }
        });


    }
}


