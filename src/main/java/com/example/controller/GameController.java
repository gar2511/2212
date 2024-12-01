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
import java.util.Random;
import com.example.model.ScoreKeeper;

import static com.example.App.PlayButtonSound;

/**
 * Controller class responsible for managing the game scene/view.
 * This class handles the mole animation, sprite rendering, and navigation
 * to other scenes within the application.
 */
public class GameController {


    public Label playTimeLabel;
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
    private Random random = new Random();
    @FXML
    private Button feedButton, playButton, giftButton, exerciseButton, vetButton, inventoryButton;

    @FXML
    private Label gameOverLabel; // For the "Game Over" message
    private ScoreKeeper scoreKeeper; // Scorekeeper instance
    @FXML
    private Label scoreLabel; // Label to display the score

    private Timeline timeTracker; // Timeline to track playtime

    @FXML
    private Button playPauseButton;
    private boolean isPaused = false;

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

            // Load image from resourcesSS
            Image petImage = new Image(getClass().getResourceAsStream("/images/" + species.toLowerCase() + ".png"));
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
    /**
     * Starts the timeline to track the playtime of the game session.
     * Updates the playtime label and handles logic for time spent and time limits.
     * If the time limit is reached, the game is saved and the user is sent back to the main menu.
     */
    /**
     * Starts the timeline to track the playtime of the game session.
     * Updates the playtime label and handles logic for time spent and time limits.
     * If the time limit is reached, the game is saved and the user is sent back to the main menu.
     */
    private void startTimeTracker() {
        // Check if the time tracker timeline is already running
        // Check if the time tracker timeline is already running
        if (timeTracker != null) {
            System.out.println("Time tracker is already running.");
            return; // If it is running, exit the method
        }

        // Create a new timeline with a keyframe that triggers every 50 milliseconds
        // Create a new timeline with a keyframe that triggers every 50 milliseconds
        timeTracker = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            // Get the current game state and pet instance
            // Get the current game state and pet instance
            GameState gameState = GameState.getCurrentState();
            Pet pet = gameState.getPet();

            if (pet != null) {
                // Increment playtime by 50 milliseconds
                pet.setCurrentPlayTime(pet.getCurrentPlayTime()+50);

                // Convert the playtime to seconds for display purposes
                long secondsElapsed = pet.getCurrentPlayTime() / 1000;

                // Update the playtime label on the UI thread
                Platform.runLater(() -> {
                    playTimeLabel.setText("Current Session's Play Time: " + formatPlayTime(secondsElapsed));
                });

                // Add 1 second to the pet's total time spent every 1000 milliseconds
                if (pet.getCurrentPlayTime() % 1000 == 0) {
                    pet.addTimeSpent(1); // Increment the total playtime by 1 second

                    // Log the current playtime to the console

                    // Log the current playtime to the console
                    Platform.runLater(() -> {
                        System.out.println("Current Playtime: " + formatPlayTime(secondsElapsed));
                    });
                }

                // Check if the pet's time limit is set and if the playtime exceeds it
                // Check if the pet's time limit is set and if the playtime exceeds it
                if (pet.getTimeLimit() > 0 && secondsElapsed >= pet.getTimeLimit()) {
                    // Log the time limit reached and save the game
                    // Log the time limit reached and save the game
                    System.out.println("Time limit reached! Saving and exiting.");
                    stopTimeTracker(); // Stop the time tracker timeline
                    saveGame(); // Save the game state
                    stopTimeTracker(); // Stop the time tracker timeline
                    saveGame(); // Save the game state
                    goBack(); // Exit to the main menu
                }
            }
        }));

        // Set the timeline to run indefinitely
        // Set the timeline to run indefinitely
        timeTracker.setCycleCount(Timeline.INDEFINITE);
        // Start the timeline
        // Start the timeline
        timeTracker.play();
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
        statsDecayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            GameState gameState = GameState.getCurrentState();
            Pet pet = gameState.getPet();

            if (pet != null) {
                VitalStats stats = pet.getStats();
                Platform.runLater(() -> {
                    // Initialize species-specific modifiers
                    int speciesHungerMod = 0;
                    int speciesHappinessMod = 0;
                    int speciesEnergyMod = 0;
                    int speciesHealthMod = 0;

                    // Apply species-specific modifiers based on pet type
                    switch (pet.getSpecies()) {
                        case "cat":
                            speciesHungerMod = 0;
                            speciesHappinessMod = 0;
                            speciesEnergyMod = 0;
                            speciesHealthMod = 0;
                            break;
                        case "Bear":
                            speciesHungerMod = 0;
                            speciesHappinessMod = 0;
                            speciesEnergyMod = 0;
                            speciesHealthMod = 0;
                            break;
                        case "mole":
                            // Default values (all 0)
                            break;
                    }

                    // Decay each stat by 1 (plus any modifiers)
                    stats.decreaseEnergy(1 + speciesEnergyMod + pet.getStats().getEnergyMod());
                    stats.decreaseHealth(1 + speciesHealthMod + pet.getStats().getHealthMod());
                    stats.decreaseHunger(1 + speciesHungerMod + pet.getStats().getHungerMod());
                    stats.decreaseHappiness(1 + speciesHappinessMod + pet.getStats().getHappinessMod());

                    // Check critical states
                    int[] petState = stats.getState();
                    if (petState[3] == 1) {
                        handleCriticalState(3);
                    } else {
                        for (int i = 0; i < petState.length-1; i++) {
                            if (petState[i] == 1) {
                                handleCriticalState(i);
                            } else {
                                maintainState(i);
                            }
                        }
                    }
                });
            }
        }));

        statsDecayTimeline.setCycleCount(Timeline.INDEFINITE);
        statsDecayTimeline.play();
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
     * Stops the time tracker.
     */
    private void stopTimeTracker() {
        if (timeTracker != null) {
            timeTracker.stop();
            timeTracker = null;
            System.out.println("Time tracker stopped.");
        }
    }

    /**
     * Event handler for the back button.
     * Stops the mole animation and switches the scene back to the main menu.
     */
    @FXML
    private void goBack() {
        PlayButtonSound();
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        stopStatsDecay();
        stopTimeTracker();
        pet.setCurrentPlayTime(0);
        if (animation != null) {
            animation.stop();
        }
        if (scoreKeeper != null) {
            scoreKeeper.stop();
        }
        System.out.println("Game paused. Returning to main menu.");
        SceneController.getInstance().switchToMainMenu();
    }
    @FXML
    private void feedPet() {
        PlayButtonSound();
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
    private void playPet() {
        PlayButtonSound();
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
    private void giveGift() {
        PlayButtonSound();
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
    private void exercisePet() {
        PlayButtonSound();
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
    private void takeVet() {
        PlayButtonSound();
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
    private void openInventory() {
        PlayButtonSound();
        System.out.println("Inventory has not been made yet");
        stopStatsDecay();
        stopTimeTracker();
        if (animation != null) {
            animation.stop();
        }
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        if (pet != null) {
            pet.setScore(scoreKeeper.getScore());
            pet.addTimeSpent(pet.getCurrentPlayTime() / 1000);
            System.out.println("Paused and Saved Score: " + pet.getScore());
            System.out.println("Paused and Saved Time: " + pet.getCurrentPlayTime() / 1000 + " seconds");
        }
        SceneController.getInstance().switchToInventory();
    }
    @FXML
    private void saveGame() {
        PlayButtonSound();
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
            if (stats.getHealth() <= 0) {
                handleGameOver();
                return;
            }
            // When health is critical, it affects energy and happiness
            stats.setEnergyMod(2);     // Tired more quickly when unhealthy
            stats.setHappinessMod(1);   // Slightly unhappy when sick
        } else {
            switch (index) {
                case 0: // Hunger critical
                    System.out.println("Hunger is critically low!");
                    stats.setHealthMod(1);     // Being hungry affects health
                    stats.setEnergyMod(1);     // Being hungry makes you tired
                    stats.setHappinessMod(1);  // Being hungry makes you unhappy
                    setPetStateImage("hungry");
                    break;
                    
                case 1: // Happiness critical
                    System.out.println("Happiness is critically low!");
                    stats.setHealthMod(1);     // Being unhappy affects health
                    stats.setEnergyMod(1);     // Being unhappy makes you tired
                    setPetStateImage("angry");
                    Platform.runLater(() -> {
                        exerciseButton.setDisable(true);
                        vetButton.setDisable(true);
                    });
                    break;
                    
                case 2: // Energy critical
                    System.out.println("Energy is critically low!");
                    stats.setHealthMod(1);      // Being exhausted affects health
                    stats.setHungerMod(1);      // Being tired makes you hungry
                    stats.setHappinessMod(1);   // Being tired makes you unhappy
                    setPetStateImage("sleepy");
                    disableAllButtons();
                    break;
            }
        }
    }

    private void maintainState(int index) {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        if (pet == null) return;
        
        VitalStats stats = pet.getStats();
        switch (index) {
            case 0: // Hunger normal
                stats.setHealthMod(0);
                stats.setEnergyMod(0);
                stats.setHappinessMod(0);
                break;
                
            case 1: // Happiness normal
                stats.setHealthMod(0);
                stats.setEnergyMod(0);
                enableAllButtons();
                break;
                
            case 2: // Energy normal
                stats.setHealthMod(0);
                stats.setHungerMod(0);
                stats.setHappinessMod(0);
                enableAllButtons();
                break;
                
            case 3: // Health normal
                stats.setEnergyMod(0);
                stats.setHappinessMod(0);
                break;
        }
    }

    private void disableAllButtons() {
        feedButton.setDisable(true);
        playButton.setDisable(true);
        giftButton.setDisable(true);
        exerciseButton.setDisable(true);
        vetButton.setDisable(true);
    }

    private void enableAllButtons() {
        feedButton.setDisable(false);
        playButton.setDisable(false);
        giftButton.setDisable(false);
        exerciseButton.setDisable(false);
        vetButton.setDisable(false);
    }

    private void handleGameOver() {
        System.out.println("Health has reached 0. Game over.");
        stopStatsDecay();
        disableAllButtons();
        setPetStateImage("dead");
        if (gameOverLabel != null) {
            gameOverLabel.setVisible(true);
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
                case SPACE: // Pause/Resume game
                    togglePlayPause();
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

        if (pet == null) {
            System.err.println("No pet found in setPetImage.");
        }
        else {
            Image petImage = new Image(getClass().getResourceAsStream("/images/" + species.toLowerCase() + ".png"));
            moleSprite.setImage(petImage);
        }
    }

    private void setPetStateImage(String petState) {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        String species = pet.getSpecies();

        if (pet == null) {
            System.err.println("No pet found in setPetImage.");
        }
        else if ((pet != null && petState == null) || (pet != null && (petState.toLowerCase().equals("happy") || petState.toLowerCase().equals("normal")))) {
            Image petImage = new Image(getClass().getResourceAsStream("/images/" + species.toLowerCase() + ".png"));
            moleSprite.setImage(petImage);
        }
        else {
            Image petImage = new Image(getClass().getResourceAsStream("/images/" + species.toLowerCase() + "_" + petState.toLowerCase() + ".png"));
            moleSprite.setImage(petImage);
        }
    }

    @FXML
    private void togglePlayPause() {
        isPaused = !isPaused;
        
        if (isPaused) {
            // pause all timelines
            if (statsDecayTimeline != null) {
                statsDecayTimeline.pause();
            }
            if (timeTracker != null) {
                timeTracker.pause();
            }
            if (scoreKeeper != null) {
                scoreKeeper.stop();
            }
            playPauseButton.setText("Resume");
            
            // disable all action buttons
            feedButton.setDisable(true);
            playButton.setDisable(true);
            giftButton.setDisable(true);
            exerciseButton.setDisable(true);
            vetButton.setDisable(true);
            inventoryButton.setDisable(true);
            
        } else {
            // resume all timelines
            if (statsDecayTimeline != null) {
                statsDecayTimeline.play();
            }
            if (timeTracker != null) {
                timeTracker.play();
            }
            if (scoreKeeper != null) {
                scoreKeeper.start();
            }
            playPauseButton.setText("Pause");
            
            // re-enable all action buttons
            feedButton.setDisable(false);
            playButton.setDisable(false);
            giftButton.setDisable(false);
            exerciseButton.setDisable(false);
            vetButton.setDisable(false);
            inventoryButton.setDisable(false);
        }
    }
}
