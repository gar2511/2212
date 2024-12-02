package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import static com.example.App.PlayButtonSound;

/**
 * Controller class for managing the tutorial dialogs in the game.
 * Provides explanations for various game features and user actions.
 */
public class TutorialGameController {

    @FXML
    private StackPane tutorialDialog;

    @FXML
    private Label tutorialMessage;

    /**
     * Displays the tutorial dialog with the specified message.
     *
     * @param message The message to display in the tutorial dialog.
     */
    public void showTutorialDialog(String message) {
        tutorialMessage.setText(message);
        tutorialDialog.setVisible(true);
    }

    /**
     * Closes the currently visible tutorial dialog.
     */
    @FXML
    private void closeTutorialDialog() {
        PlayButtonSound();
        tutorialDialog.setVisible(false);
    }

    /**
     * Shows the tutorial dialog explaining the "Feed" button.
     */
    @FXML
    private void showFeedTutorial() {
        PlayButtonSound();
        showTutorialDialog("The Feed button replenishes your pet's hunger. Use it when your pet is starving!");
    }

    /**
     * Shows the tutorial dialog explaining the "Play" button.
     */
    @FXML
    private void showPlayTutorial() {
        PlayButtonSound();
        showTutorialDialog("The Play button boosts your pet's happiness. Keep your pet entertained!");
    }

    /**
     * Shows the tutorial dialog explaining the "Give Gift" button.
     */
    @FXML
    private void showGiftTutorial() {
        PlayButtonSound();
        showTutorialDialog("The Give Gift button increases your pet's affection. Use it to build a stronger bond!");
    }

    /**
     * Shows the tutorial dialog explaining the "Exercise" button.
     */
    @FXML
    private void showExerciseTutorial() {
        PlayButtonSound();
        showTutorialDialog("The Exercise button keeps your pet healthy. Don't forget regular exercise!");
    }

    /**
     * Shows the tutorial dialog explaining the "Take to Vet" button.
     */
    @FXML
    private void showVetTutorial() {
        PlayButtonSound();
        showTutorialDialog("The Take to Vet button restores health. Use it when your pet is unwell!");
    }

    /**
     * Navigates to the inventory tutorial screen.
     */
    @FXML
    private void showInventoryTutorial() {
        PlayButtonSound();
        SceneController.getInstance().switchToTutorialInventory();
    }

    /**
     * Shows the tutorial dialog explaining the "Pause" button.
     *
     * @param actionEvent The event triggered by the pause button action.
     */
    public void togglePlayPauseTutorial(ActionEvent actionEvent) {
        PlayButtonSound();
        showTutorialDialog("The Pause button pauses the game.");
    }

    /**
     * Shows the tutorial dialog explaining the "Save Game" button.
     *
     * @param actionEvent The event triggered by the save game button action.
     */
    public void saveGameTutorial(ActionEvent actionEvent) {
        PlayButtonSound();
        showTutorialDialog("This saves your game and keeps your progress updated! Make sure to use it often!");
    }

    /**
     * Shows the tutorial dialog explaining the "Go Back" button.
     *
     * @param actionEvent The event triggered by the go back button action.
     */
    public void goBackTutorial(ActionEvent actionEvent) {
        PlayButtonSound();
        showTutorialDialog("The Go Back button exits to the main menu.");
    }

    /**
     * Shows the tutorial dialog explaining the resource meters.
     *
     * @param actionEvent The event triggered by clicking on resource info.
     */
    public void showResourceInfo(ActionEvent actionEvent) {
        PlayButtonSound();
        showTutorialDialog("These are your resource meters. Over time, resources will decrease. Certain states will alter the speed of decay.");
    }

    /**
     * Shows the tutorial dialog explaining pet-specific behaviors and resource decay rates.
     *
     * @param actionEvent The event triggered by clicking on pet info.
     */
    public void showPetInfo(ActionEvent actionEvent) {
        PlayButtonSound();
        showTutorialDialog("This is your pet. Depending on the pet you've chosen, resource decay rates differ. Moles have balanced decay. Cats require more food but drain energy slower. Bears are the hardest, requiring high food, play, and energy, but their health decays slower.");
    }

    /**
     * Exits the tutorial and navigates to the main menu.
     *
     * @param actionEvent The event triggered by the exit tutorial button action.
     */
    public void exitTutorial(ActionEvent actionEvent) {
        PlayButtonSound();
        SceneController.getInstance().switchToMainMenu();
    }

    /**
     * Shows the tutorial dialog explaining the consequences of low resource levels.
     *
     * @param actionEvent The event triggered by clicking on pet states info.
     */
    public void showPetStates(ActionEvent actionEvent) {
        PlayButtonSound();
        showTutorialDialog("If your pet's resources drop too low, consequences occur. Zero energy puts the pet to sleep until energy is full. Empty hunger accelerates happiness and hunger decay. Low happiness restricts actions to increasing happiness only. Zero health ends the game.");
    }
}
