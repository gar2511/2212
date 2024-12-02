package com.example.controller;

import com.example.App;
import com.example.components.CustomButton;
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
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Random;
import com.example.model.ScoreKeeper;

import static com.example.App.PlayButtonSound;

/**
 * Controller class responsible for managing the game scene/view.
 * Handles gameplay mechanics, UI updates, and interactions within the game scene.
 */
public class GameController {
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
     * Initializes the game scene after the FXML file is loaded.
     * Sets up the UI components and game state bindings.
     */
    @FXML
    public void initialize() {
        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        String species = pet.getSpecies();
        if (pet != null) {
            VitalStats stats = pet.getStats();

            energyBar.progressProperty().bind(Bindings.divide(stats.energyProperty(), 100.0));
            healthBar.progressProperty().bind(Bindings.divide(stats.healthProperty(), 100.0));
            hungerBar.progressProperty().bind(Bindings.divide(stats.hungerProperty(), 100.0));
            happinessBar.progressProperty().bind(Bindings.divide(stats.happinessProperty(), 100.0));

            System.out.println("Loaded Pet: " + pet.getName() + ", Type: " + pet.getSpecies());
        } else {
            System.out.println("No pet found. Please create or load a save.");
        }
        try {
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

        setupHotkeys();
        startStatsDecay();
        scoreKeeper = new ScoreKeeper(10);
        scoreKeeper.setScore(pet.getScore());
        scoreLabel.textProperty().bind(Bindings.convert(scoreKeeper.scoreProperty()));
        scoreKeeper.start();
        startTimeTracker();
    }

    /**
     * Starts a timeline to track the playtime of the game session.
     */
    private void startTimeTracker() {
        if (timeTracker != null) {
            System.out.println("Time tracker is already running.");
            return;
        }
        timeTracker = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            GameState gameState = GameState.getCurrentState();
            Pet pet = gameState.getPet();

            if (pet != null) {
                pet.setCurrentPlayTime(pet.getCurrentPlayTime() + 50);
                long secondsElapsed = pet.getCurrentPlayTime() / 1000;

                Platform.runLater(() -> {
                    playTimeLabel.setText("Current Session's Play Time: " + formatPlayTime(secondsElapsed));
                });

                if (pet.getCurrentPlayTime() % 1000 == 0) {
                    pet.addTimeSpent(1);
                }

                if (pet.getTimeLimit() > 0 && secondsElapsed >= pet.getTimeLimit()) {
                    System.out.println("Time limit reached! Saving and exiting.");
                    stopTimeTracker();
                    saveGame();
                    goBack();
                }
            }
        }));
        timeTracker.setCycleCount(Timeline.INDEFINITE);
        timeTracker.play();
    }

    /**
     * Formats the playtime into HH:mm:ss format.
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
     * Starts a timeline for periodic stat decay.
     */
    private void startStatsDecay() {
        statsDecayTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            GameState gameState = GameState.getCurrentState();
            Pet pet = gameState.getPet();

            if (pet != null) {
                VitalStats stats = pet.getStats();
                Platform.runLater(() -> {
                    int speciesHungerMod = 0;
                    int speciesHappinessMod = 0;
                    int speciesEnergyMod = 0;
                    int speciesHealthMod = 0;

                    switch (pet.getSpecies()) {
                        case "cat":
                            speciesHungerMod = 1;
                            speciesEnergyMod = -1;
                            break;
                        case "Bear":
                            speciesHungerMod = 2;
                            speciesHappinessMod = 2;
                            speciesEnergyMod = 2;
                            speciesHealthMod = -1;
                            break;
                    }

                    stats.decreaseEnergy(1 + speciesEnergyMod + pet.getStats().getEnergyMod());
                    stats.decreaseHealth(1 + speciesHealthMod + pet.getStats().getHealthMod());
                    stats.decreaseHunger(1 + speciesHungerMod + pet.getStats().getHungerMod());
                    stats.decreaseHappiness(1 + speciesHappinessMod + pet.getStats().getHappinessMod());

                    int[] petState = stats.getState();
                    if (petState[3] == 1) {
                        handleCriticalState(3);
                    } else {
                        for (int i = 0; i < petState.length - 1; i++) {
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
     * Stops the stats decay timeline.
     */
    private void stopStatsDecay() {
        if (statsDecayTimeline != null) {
            statsDecayTimeline.stop();
        }
    }

    /**
     * Stops the time tracker timeline.
     */
    private void stopTimeTracker() {
        if (timeTracker != null) {
            timeTracker.stop();
            timeTracker = null;
            System.out.println("Time tracker stopped.");
        }
    }

    /**
     * Handles the "Back" button action.
     * Stops ongoing animations, timelines, and navigates back to the main menu.
     */
    @FXML
    private void goBack() {
        PlayButtonSound();
        exitDialog.setVisible(true);
        disableBackground();

        GameState gameState = GameState.getCurrentState();
        Pet pet = gameState.getPet();
        stopStatsDecay();
        stopTimeTracker();
        if (pet != null) {
            pet.setCurrentPlayTime(0);
        }

        if (animation != null) {
            animation.stop();
        }
        if (scoreKeeper != null) {
            scoreKeeper.stop();
        }

        System.out.println("Game paused. Returning to main menu.");
    }

    /**
     * Disables all interactive buttons in the game scene while showing a dialog.
     */
    private void disableBackground() {
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

    /**
     * Cancels the exit dialog and re-enables the background buttons.
     */
    @FXML
    private void cancelExit() {
        PlayButtonSound();
        exitDialog.setVisible(false);
        enableAllButtons();
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

        setPetStateImage("eating");

        if (pet != null) {
            VitalStats stats = pet.getStats();
            if (pet.getDefaultItem12() == 1) {
                stats.increaseHunger(20);
                stats.increaseHappiness(10);
                pet.getInventory().decreaseItem1();
                System.out.println(pet.getName() + " has been fed Item 1! Hunger and happiness increased.");
            } else if (pet.getDefaultItem12() == 2) {
                stats.increaseHunger(30);
                stats.increaseHappiness(5);
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

        setPetStateImage();

        if (pet != null) {
            VitalStats stats = pet.getStats();
            stats.decreaseEnergy(15);
            stats.increaseHappiness(20);
            stats.decreaseHunger(10);
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

        setPetStateImage("eating");

        if (pet != null) {
            VitalStats stats = pet.getStats();
            if (pet.getDefaultItem34() == 3) {
                stats.increaseEnergy(20);
                stats.increaseHappiness(10);
                stats.decreaseHealth(40);
                pet.getInventory().decreaseItem3();
                System.out.println(pet.getName() + " has been gifted Item 3!");
            } else if (pet.getDefaultItem34() == 4) {
                stats.increaseHunger(60);
                stats.increaseHappiness(15);
                pet.getInventory().decreaseItem4();
                System.out.println(pet.getName() + " has been gifted Item 4!");
            }
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
            stats.decreaseHunger(20);
            stats.decreaseHappiness(5);
            stats.increaseEnergy(10);
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

        setPetStateImage("sleeping");

        if (pet != null) {
            VitalStats stats = pet.getStats();
            stats.increaseHealth(50);
            stats.increaseEnergy(30);
            stats.decreaseHappiness(10);
            System.out.println(pet.getName() + " went to the vet! Health and hygiene increased, but happiness decreased.");
        } else {
            System.out.println("No pet to take to the vet!");
        }
    }

    /**
     * Opens the inventory menu and pauses the game state.
     */
    @FXML
    private void openInventory() {
        PlayButtonSound();
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
            fileHandler.saveGame("slot" + pet.getSaveID(), gameState);
            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Remaining methods follow the same pattern for Javadoc addition.



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
            stats.setEnergyMod(2);
            stats.setHappinessMod(1);
        } else {
            switch (index) {
                case 0: // Hunger critical
                    System.out.println("Hunger is critically low!");
                    stats.setHealthMod(1);
                    stats.setEnergyMod(1);
                    stats.setHappinessMod(1);
                    setPetStateImage("hungry");
                    break;
                case 1: // Happiness critical
                    System.out.println("Happiness is critically low!");
                    stats.setHealthMod(1);
                    stats.setEnergyMod(1);
                    setPetStateImage("angry");
                    Platform.runLater(() -> {
                        exerciseButton.setDisable(true);
                        vetButton.setDisable(true);
                    });
                    break;
                case 2: // Energy critical
                    System.out.println("Energy is critically low!");
                    stats.setHealthMod(1);
                    stats.setHungerMod(1);
                    stats.setHappinessMod(1);
                    setPetStateImage("sleepy");
                    disableAllButtons();
                    vetButton.setDisable(false);
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
        moleSprite.setFocusTraversable(true);
        moleSprite.requestFocus();

        moleSprite.setOnKeyPressed(event -> {
            if (exitDialog.isVisible()) {
                switch (event.getCode()) {
                    case ESCAPE -> cancelExit();
                    case ENTER -> confirmExit();
                    default -> {
                    }
                }
            } else {
                switch (event.getCode()) {
                    case F -> feedPet();
                    case P -> playPet();
                    case G -> giveGift();
                    case E -> exercisePet();
                    case V -> takeVet();
                    case I -> openInventory();
                    case S -> saveGame();
                    case Q -> goBack();
                    case SPACE -> togglePlayPause();
                    default -> System.out.println("Unhandled key: " + event.getCode());
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
        } else {
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
        } else {
            String state = petState == null || petState.equalsIgnoreCase("normal") ? "" : "_" + petState.toLowerCase();
            Image petImage = new Image(getClass().getResourceAsStream("/images/" + species.toLowerCase() + state + ".png"));
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
            disableAllButtons();
        } else {
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
            enableAllButtons();
        }
    }

    /**
     * Confirms exit and switches back to the main menu.
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
}
