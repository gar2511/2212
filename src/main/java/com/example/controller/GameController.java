package com.example.controller;

import com.example.App;
import com.example.components.CustomButton;
import com.example.model.GameState;
import com.example.model.Pet;
import com.example.model.VitalStats;
import com.example.util.FileHandler;
import com.example.components.StatBar;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.scene.image.Image;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Random;
import com.example.model.ScoreKeeper;

import static com.example.App.PlayButtonSound;

/**
 * Controller class responsible for managing the game scene/view.
 * This class handles the mole animation, sprite rendering, and navigation
 * to other scenes within the application.
 */
public class GameController {
    private Timeline imageFlipTimeline;

    /**
     * Label to display the playtime of the current game session.
     */
    public Label playTimeLabel;
    /**
     * StackPane representing the exit dialog shown when the user attempts to exit the game.
     */
    public StackPane exitDialog;
    /**
     * Custom button used for navigating back to the main menu or previous scene.
     */
    public CustomButton backButton;
    /**
     * Custom button used for saving the current game state.
     */
    public CustomButton saveButton;
    public CustomButton sleepButton;
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
    private Timeline timeTracker; // Timeline to track playtime and active timeframe

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
            startActiveTimeTracker(pet); // Start the active time tracker
            startImageFlip(); // Start the image flip animation

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
     * Starts the image flipping animation. The image flips around every 15 seconds.
     */
    private void startImageFlip() {
        imageFlipTimeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> flipImage()));
        imageFlipTimeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        imageFlipTimeline.play();
    }

    /**
     * Flips the mole image horizontally by rotating it 180 degrees around the Y-axis.
     */
    private void flipImage() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), moleSprite);
        rotateTransition.setAxis(Rotate.Y_AXIS); // Rotate around the Y-axis
        rotateTransition.setFromAngle(0); // Start from the default angle
        rotateTransition.setToAngle(180); // Rotate to 180 degrees
        rotateTransition.setAutoReverse(true); // Flip back to the original state
        rotateTransition.play();
    }

    /**
     * Stops the image flipping animation.
     */
    private void stopImageFlip() {
        if (imageFlipTimeline != null) {
            imageFlipTimeline.stop();
        }
    }
    public void restartTimeTracker() {
        System.out.println("Restarting time tracker.");
        stopTimeTracker(); // Ensure the old tracker is stopped
        startTimeTracker(); // Start a new tracker
    }

    /**
     * Starts the timeline to track the playtime of the game session.
     * Updates the playtime label and handles logic for time spent and time limits.
     * If the time limit is reached, the game is saved and the user is sent back to the main menu.
     */
    private void startTimeTracker() {
        System.out.println("startTimeTracker called.");

        // Stop and nullify any existing time tracker
        if (timeTracker != null) {
            System.out.println("Stopping existing timeTracker.");
            stopTimeTracker();
        }

        // Initialize a new time tracker
        timeTracker = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            GameState gameState = GameState.getCurrentState();
            Pet pet = gameState.getPet();

            if (pet != null) {
                pet.addTimeSpent(1); // Increment total and current playtime by 1 second

                long sessionPlaytime = pet.getTotalTimeSpent(); // Fetch current session playtime

                // Update the playtime label
                Platform.runLater(() -> {
                    playTimeLabel.setText("Play Time: " + formatPlayTime(sessionPlaytime));
                });

                // Print seconds elapsed in the terminal
                System.out.println("Seconds elapsed: " + sessionPlaytime);
            } else {
                System.out.println("No pet found for time tracking.");
            }
        }));

        timeTracker.setCycleCount(Timeline.INDEFINITE);
        timeTracker.play();
        System.out.println("Time tracker started.");
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
                            speciesHungerMod = 1;
                            speciesHappinessMod = 0;
                            speciesEnergyMod = -1;
                            speciesHealthMod = 0;
                            break;
                        case "Bear":
                            speciesHungerMod = 2;
                            speciesHappinessMod = 2;
                            speciesEnergyMod = 2;
                            speciesHealthMod = -1;
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
        } else {
            System.out.println("stopTimeTracker called, but timeTracker was already null.");
        }
    }

    /**
     * Event handler for the back button.
     * Stops the mole animation and switches the scene back to the main menu.
     */
    @FXML
    private void goBack() {
        PlayButtonSound();
        stopActiveTimeTracker();
        stopImageFlip();
        exitDialog.setVisible(true);

        // Disable interaction with background UI
        disableBackground();

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
    }

    // Helper method to disable background interactions
    /**
     * Disables all interactive buttons in the game scene while showing a dialog.
     */
    private void disableBackground() {
        // Disable all UI elements behind the dialog
        feedButton.setDisable(true);
        playButton.setDisable(true);
        giftButton.setDisable(true);
        exerciseButton.setDisable(true);
        vetButton.setDisable(true);
        inventoryButton.setDisable(true);
        backButton.setDisable(true);
        saveButton.setDisable(true);
        playPauseButton.setDisable(true);
    }

    // Enable background after canceling the dialog
    /**
     * Cancels the exit dialog and re-enables the background buttons.
     */
    @FXML
    private void cancelExit() {
        PlayButtonSound();
        exitDialog.setVisible(false);

        // Re-enable all background UI elements
        feedButton.setDisable(false);
        playButton.setDisable(false);
        giftButton.setDisable(false);
        exerciseButton.setDisable(false);
        vetButton.setDisable(false);
        inventoryButton.setDisable(false);
        backButton.setDisable(false);
        saveButton.setDisable(false);
        playPauseButton.setDisable(false);
    }

    /**
     * Feeds the pet, increasing hunger and happiness stats.
     * Decreases the corresponding item count in the pet's inventory.
     */
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

    /**
     * Plays with the pet, increasing happiness but decreasing energy and hunger.
     */
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

    /**
     * Gives a gift to the pet, modifying stats based on the selected item.
     * Decreases the corresponding item count in the pet's inventory.
     */
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

    /**
     * Exercises the pet, increasing energy but decreasing hunger and happiness.
     */
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

    /**
     * Takes the pet to the vet, increasing health and energy but decreasing happiness.
     */
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

    /**
     * Opens the inventory menu and pauses the game state.
     */
    @FXML
    private void openInventory(){
        PlayButtonSound();
        stopStatsDecay();
        stopTimeTracker(); // Stop tracking playtime
        if (animation != null) {
            animation.stop();
        }
        // Save the current score to the Pet instance
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        if (pet != null) {
            pet.setScore(scoreKeeper.getScore()); // Save the score
            pet.addTimeSpent(pet.getCurrentPlayTime() / 1000); // Save the playtime in seconds
            System.out.println("Paused and Saved Score: " + pet.getScore());
            System.out.println("Paused and Saved Time: " + pet.getCurrentPlayTime() / 1000 + " seconds");
        }
        SceneController.getInstance().switchToInventory();
    }

    /**
     * Saves the current game state to a file.
     */
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



    /**
     * Handles critical state logic for a specific stat index.
     * Updates modifiers and displays warnings based on the pet's current stat state.
     *
     * @param index The index of the stat to handle (0 = Hunger, 1 = Happiness, 2 = Energy, 3 = Health).
     */
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
                    stats.setEnergyMod(-7);
                    setPetStateImage("sleepy");

                    // Disable all buttons except for the sleep action
                    disableAllButtons();


                    // Prevent exiting critical state until energy is restored to 100
                    if (stats.getEnergy() < 100) {

                        System.out.println("Pet remains in critical state until energy is restored to 100.");
                    } else {
                        System.out.println("Energy restored. Exiting critical state.");
                        maintainState(2); // Restore normal state
                    }
                    break;
            }
        }
    }

    /**
     * Restores a stat's normal state, resetting modifiers and enabling interactions.
     *
     * @param index The index of the stat to maintain (0 = Hunger, 1 = Happiness, 2 = Energy, 3 = Health).
     */
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

    /**
     * Disables all game interaction buttons.
     */
    private void disableAllButtons() {
        feedButton.setDisable(true);
        playButton.setDisable(true);
        giftButton.setDisable(true);
        exerciseButton.setDisable(true);
        vetButton.setDisable(true);
        sleepButton.setDisable(true);
    }

    /**
     * Enables all game interaction buttons.
     */
    private void enableAllButtons() {
        feedButton.setDisable(false);
        playButton.setDisable(false);
        giftButton.setDisable(false);
        exerciseButton.setDisable(false);
        vetButton.setDisable(false);
        sleepButton.setDisable(false);
    }

    /**
     * Handles the game over state, stopping the game and displaying the game over message.
     */
    private void handleGameOver() {
        System.out.println("Health has reached 0. Game over.");
        stopStatsDecay();
        disableAllButtons();
        setPetStateImage("dead");
        if (gameOverLabel != null) {
            gameOverLabel.setVisible(true);
        }
    }

    /**
     * Sets up keyboard hotkeys for game actions.
     */
    private void setupHotkeys() {
        moleSprite.setFocusTraversable(true); // Ensure moleSprite can receive key events
        moleSprite.requestFocus(); // Request focus on the moleSprite node

        moleSprite.setOnKeyPressed(event -> {
            if (exitDialog.isVisible()) {
                // Handle dialog-specific hotkeys
                switch (event.getCode()) {
                    case ESCAPE:
                        cancelExit(); // Close the dialog
                        break;
                    case ENTER:
                        confirmExit(); // Confirm exit
                        break;
                    default:
                        break;
                }
            } else {
                // Handle other hotkeys when the dialog is not visible
                switch (event.getCode()) {
                    case F:
                        feedPet();
                        break;
                    case P:
                        playPet();
                        break;
                    case G:
                        giveGift();
                        break;
                    case E:
                        exercisePet();
                        break;
                    case V:
                        takeVet();
                        break;
                    case I:
                        openInventory();
                        break;
                    case S:
                        saveGame();
                        break;
                    case Q:
                        goBack();
                        break;
                    case SPACE:
                        togglePlayPause();
                        break;
                    default:
                        System.out.println("Unhandled key: " + event.getCode());
                }
            }
        });
    }

    /**
     * Sets the pet's image to its default state based on its species.
     */
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

    /**
     * Sets the pet's image to a specific state based on its species and state.
     *
     * @param petState The state of the pet (e.g., "happy", "angry").
     */
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

    /**
     * Toggles between play and pause states, affecting game timelines and UI buttons.
     */
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
    /**
     * Handles the confirmation of exit.
     * Closes the application.
     */
    @FXML
    private void confirmExit() {
        PlayButtonSound();
        SceneController.getInstance().switchToMainMenu();
    }

    /**
     * Confirms returning to the main menu and stops game timelines.
     */
    @FXML
    private void confirmBackToMain() {
        PlayButtonSound();
        stopImageFlip();
        stopActiveTimeTracker();
        stopStatsDecay();
        stopTimeTracker();
        
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        if (pet != null) {
            pet.setCurrentPlayTime(0);
        }
        
        if (animation != null) {
            animation.stop();
        }
        if (scoreKeeper != null) {
            scoreKeeper.stop();
        }
        
        SceneController.getInstance().switchToMainMenu();
    }

    public void sleepPet(ActionEvent actionEvent) {
        PlayButtonSound();

        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();

        if (pet != null) {
            VitalStats stats = pet.getStats();

            // Set the pet image to "sleeping" state
            setPetStateImage("sleeping");

            // Disable all buttons except the sleep button
            disableAllButtons();
            sleepButton.setDisable(false);

            // Restore energy incrementally
            Timeline sleepTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                if (stats.getEnergy() < 100) {
                    stats.increaseEnergy(10); // Increment energy by 10 every second
                    System.out.println(pet.getName() + " is sleeping. Energy increased, hunger decreased.");
                } else {
                    System.out.println("Energy fully restored. Exiting sleep.");

                    // Stop the sleep timeline
                    ((Timeline) event.getSource()).stop();

                    // Restore normal state for energy
                    maintainState(2);

                    // Re-enable all buttons
                    enableAllButtons();
                }
            }));

            // Set the timeline to run indefinitely until energy reaches 100
            sleepTimeline.setCycleCount(Timeline.INDEFINITE);
            sleepTimeline.play();
        } else {
            System.out.println("No pet available to sleep!");
        }
    }
    /**
     * Starts a tracker to monitor whether the current time is within the allowed active timeframe.
     * If not, pauses the game or exits to the main menu.
     */
    private void startActiveTimeTracker(Pet pet) {
        timeTracker = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalTime currentTime = LocalTime.now();
            LocalTime startTime = pet.getStartTime();
            LocalTime endTime = pet.getEndTime();

            if (startTime != null && endTime != null) {
                if (!isWithinTimeframe(currentTime, startTime, endTime)) {
                    System.out.println("Current time " + currentTime + " is outside the allowed timeframe.");
                    handleOutsideActiveTime();
                }
            }
        }));

        timeTracker.setCycleCount(Timeline.INDEFINITE);
        timeTracker.play();
    }

    /**
     * Checks if the current time is within the allowed timeframe.
     *
     * @param currentTime The current time.
     * @param startTime   The start time of the allowed timeframe.
     * @param endTime     The end time of the allowed timeframe.
     * @return True if the current time is within the timeframe, false otherwise.
     */
    private boolean isWithinTimeframe(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
        if (endTime.isAfter(startTime)) {
            // Normal timeframe: Start < End (e.g., 08:00 to 18:00)
            return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        } else {
            // Overnight timeframe: End < Start (e.g., 22:00 to 06:00)
            return !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
        }
    }

    /**
     * Handles actions when the current time is outside the allowed active timeframe.
     */
    private void handleOutsideActiveTime() {
        Platform.runLater(() -> {
            // Pause all game activities
            togglePlayPause(); // This pauses the game

            // Display an alert or exit the game
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inactive Time");
            alert.setHeaderText("You are outside the allowed playtime!");
            alert.setContentText("Please return during your allowed playtime.");
            alert.showAndWait();

            // Optionally exit to main menu
            SceneController.getInstance().switchToMainMenu();
        });
    }

    /**
     * Stops the active time tracker.
     */
    private void stopActiveTimeTracker() {
        if (timeTracker != null) {
            timeTracker.stop();
        }
    }
}
