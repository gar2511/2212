package com.example.controller;

import com.example.model.GameState;
import com.example.model.Pet;
import com.example.model.VitalStats;
import com.example.util.FileHandler;
import com.example.components.StatBar;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.scene.image.Image;
import java.io.IOException;
import java.util.Objects;
//import java.util.Random;
import com.example.model.ScoreKeeper;

/**
 * Controller class responsible for managing the game scene/view.
 * This class handles the mole animation, sprite rendering, and navigation
 * to other scenes within the application.
 */
public class GameController {


    @FXML
    private StatBar energyBar;
    @FXML
    private StatBar healthBar;
    @FXML
    private StatBar hungerBar;
    @FXML
    private StatBar happinessBar;
    @FXML
    private ImageView moleSprite;

    private Timeline animation;
    private Timeline statsDecayTimeline;
    //private final Random random = new Random();
    @FXML
    private Button feedButton, playButton, giftButton, exerciseButton, vetButton, inventoryButton;

    @FXML
    private Label gameOverLabel; // For the "Game Over" message
    private ScoreKeeper scoreKeeper; // Scorekeeper instance
    @FXML
    private Label scoreLabel; // Label to display the score

    private Timeline timeTracker; // Timeline to track playtime
    private long currentPlayTime = 0; // Current session playtime in seconds

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
        String species = pet.getSpecies();
        VitalStats stats = pet.getStats();

        // Bind progress bars to stats
        energyBar.progressProperty().bind(Bindings.divide(stats.energyProperty(), 100.0));
        healthBar.progressProperty().bind(Bindings.divide(stats.healthProperty(), 100.0));
        hungerBar.progressProperty().bind(Bindings.divide(stats.hungerProperty(), 100.0));
        happinessBar.progressProperty().bind(Bindings.divide(stats.happinessProperty(), 100.0));


        System.out.println("Loaded Pet: " + pet.getName() + ", Type: " + pet.getSpecies());
        // Update UI or initialize game logic with the Pet's data
        //setupPetData(pet);
        try {
            // Load image from resourcesSS
            Image petImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + species.toLowerCase() + ".png")));
            if (petImage.isError()) {
                System.err.println("Error loading mole image: " + petImage.getException());
            } else {
                moleSprite.setImage(petImage);
            }

        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialize hotkeys
        setupHotkeys();

        startStatsDecay();
        // Initialize the ScoreKeeper with 10 points per second
        scoreKeeper = new ScoreKeeper(10);
        scoreKeeper.setScore(pet.getScore());

        // Bind the scorekeeper's score to the label
        scoreLabel.textProperty().bind(Bindings.convert(scoreKeeper.scoreProperty()));

        // Start the scorekeeper
        scoreKeeper.start();
        // Start tracking playtime
        startTimeTracker();
    }
    private void startTimeTracker() {
        timeTracker = new Timeline(new KeyFrame(Duration.millis(50), _ -> {
            GameState gameState = GameState.getCurrentState();
            Pet pet = gameState.getPet();

            if (pet != null) {
                // increment by 50ms (0.05 seconds) since we're running every 50ms
                currentPlayTime += 0.05;
                // only add to timeSpent every full second
                if (currentPlayTime % 1 < 0.05) {
                    pet.addTimeSpent(1);
                }
                
                System.out.println("THIS IS YOUR TIME LIMIT BTW: " + pet.getTimeLimit());
                // Check if the time limit is reached (convert currentPlayTime to seconds)
                if (pet.getTimeLimit() > 0 && (int)currentPlayTime >= pet.getTimeLimit()) {
                    System.out.println("Time limit reached! Saving and exiting.");
                    stopTimeTracker();
                    saveGame();
                    goBack(); // Exit to the main menu
                }

                // Update UI or log playtime if necessary (only log every second)
                if (currentPlayTime % 1 < 0.05) {
                    Platform.runLater(() -> System.out.println("Current Playtime: " + formatPlayTime(currentPlayTime)));
                }
            }
        }));
        timeTracker.setCycleCount(Timeline.INDEFINITE);
        timeTracker.play();
    }

    /**
     * Stops the time tracker.
     */
    private void stopTimeTracker() {
        if (timeTracker != null) {
            timeTracker.stop();
        }
    }

    /**
     * Formats the playtime in HH:mm:ss format.
     *
     * @param seconds Total seconds of playtime.
     * @return Formatted time string.
     */
    private String formatPlayTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
    /**
     * Starts the timeline for the constant decay of stats.
     */
    private void startStatsDecay() {
        statsDecayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> {
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
                        // Happiness decays normally
                        speciesEnergyMod = 1; // Loses energy faster
                        // Health decays normally
                        break;
                    case "bear":
                        speciesHungerMod = 2; // Hunger decreases rapidly
                        speciesHappinessMod = -1; // Happiness decays slower
                        speciesEnergyMod = 1;  // Energy decays normally
                        // Health decays slower
                        break;
                    case "mole":
                        // Mole is the default (no modifiers)
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
                System.out.println("Here is my Decay of Happiness " + 2+ speciesHungerMod+ pet.getStats().getHungerMod());

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
        stopTimeTracker(); // Stop tracking playtime
        if (animation != null) {
            animation.stop();
        }
        if (scoreKeeper != null) {
            scoreKeeper.stop(); // Stop the scorekeeper
        }
        SceneController.getInstance().switchToMainMenu();
    }
    @FXML
    private void feedPet() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        // setting pet image to eating
        setPetStateImage("eating");

        if (pet != null) {
            if (pet.getDefaultItem12()==1) {
                VitalStats stats = pet.getStats();
                stats.increaseHunger(20); // Increase hunger by 20
                stats.increaseHappiness(10); // Increase happiness by 10
                pet.getInventory().decreaseItem1();
                System.out.println(pet.getName() + " has been fed Item 1! Hunger and happiness increased.");
            }
            if (pet.getDefaultItem12()==2) {
                VitalStats stats = pet.getStats();
                stats.increaseHunger(30); // Increase hunger by 20
                stats.increaseHappiness(5); // Increase happiness by 10
                pet.getInventory().decreaseItem2();
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

        //setting pet image to default happy image
        setPetStateImage();

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

        // setting pet image to eating
        setPetStateImage("eating");

        if (pet.getDefaultItem34()==3) {
            VitalStats stats = pet.getStats();
            stats.increaseEnergy(20); // Increase Energy by 20
            stats.increaseHappiness(10); // Increase happiness by 10
            stats.decreaseHealth(40);
            pet.getInventory().decreaseItem3();
            System.out.println(pet.getName() + " has been fed Item 3! Hunger and happiness increased.");
        }
        if (pet.getDefaultItem34()==4) {
            VitalStats stats = pet.getStats();
            stats.increaseHunger(60); // Increase hunger by 60
            stats.increaseHappiness(15); // Decrease happiness by 15
            pet.getInventory().decreaseItem4();
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

        // setting pet image to eating
        setPetStateImage("sleeping");

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
        if (scoreKeeper != null) {
            scoreKeeper.stop(); // Stop the scorekeeper
        }
        SceneController.getInstance().switchToInventory();
    }
    @FXML
    private void saveGame() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        pet.setScore(scoreKeeper.getScore());
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

                // Show dead pet image
                setPetStateImage("dead");

                // Show "Game Over" message
                if (gameOverLabel == null) {
                    System.err.println("gameOverLabel is null. Check FXML binding.");
                } else {
                    gameOverLabel.setVisible(true);
                    System.out.println("Game Over Label is now visible.");
                }
            }
        } else {
            switch (index) {
                case 0: // Hunger
                    System.out.println("Hunger is critically low! Consider feeding the pet.");
                    stats.setHappinessMod(5);
                    stats.setHealthMod(5);
                    setPetStateImage("hungry");
                    break;
                case 1: // Happiness
                    System.out.println("Happiness is critically low! Consider playing with the pet.");
                    setPetStateImage("angry");
                    Platform.runLater(() -> {
                        exerciseButton.setDisable(true);
                        vetButton.setDisable(true);
                    });
                    stats.setHappinessMod(4);
                    break;
                case 2: // Energy
                    System.out.println("Energy is critically low! Pet needs rest.");
                    setPetStateImage("sleepy");
                    feedButton.setDisable(true);
                    playButton.setDisable(true);
                    giftButton.setDisable(true);
                    exerciseButton.setDisable(true);
                    vetButton.setDisable(true);
                    stats.setEnergyMod(-7);
                    break;
                default:
                    System.out.println("Unknown critical state detected.");
                    setPetStateImage("sleepy");
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
                stats.setHappinessMod(2);
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

    private void setPetStateImage() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        String species = pet.getSpecies();

        Image petImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + species.toLowerCase() + ".png")));
        moleSprite.setImage(petImage);
    }

    private void setPetStateImage(String petState) {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        String species = pet.getSpecies();

        Image petImage;
        if (petState == null || petState.equalsIgnoreCase("happy") || petState.equalsIgnoreCase("normal")) {
            petImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + species.toLowerCase() + ".png")));
        }
        else {
            petImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/" + species.toLowerCase() + "_" + petState.toLowerCase() + ".png")));
        }
        moleSprite.setImage(petImage);
    }
}
