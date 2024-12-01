package com.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import javafx.scene.control.Label;


public class TutorialGameController {
    @FXML
    private StackPane tutorialDialog;

    @FXML
    private Label tutorialMessage;
    public void showTutorialDialog(String message) {
        tutorialMessage.setText(message);
        tutorialDialog.setVisible(true);
    }
    @FXML
    private void closeTutorialDialog() {
        tutorialDialog.setVisible(false);
    }
    @FXML
    private void showFeedTutorial() {
        showTutorialDialog("The Feed button replenishes your pet's hunger. Use it when your pet is starving!");
    }

    @FXML
    private void showPlayTutorial() {
        showTutorialDialog("The Play button boosts your pet's happiness. Keep your pet entertained!");
    }

    @FXML
    private void showGiftTutorial() {
        showTutorialDialog("The Give Gift button increases your pet's affection. Use it to build a stronger bond!");
    }

    @FXML
    private void showExerciseTutorial() {
        showTutorialDialog("The Exercise button keeps your pet healthy. Don't forget regular exercise!");
    }

    @FXML
    private void showVetTutorial() {
        showTutorialDialog("The Take to Vet button restores health. Use it when your pet is unwell!");
    }

    @FXML
    private void showInventoryTutorial() {
        SceneController.getInstance().switchToTutorialInventory();
    }

    public void togglePlayPauseTutorial(ActionEvent actionEvent) {
        showTutorialDialog("The Pause button, pauses the game.");
    }

    public void saveGameTutorial(ActionEvent actionEvent) {
        showTutorialDialog("This is what saves your game and keeps your progress updated! Make sure to use it often!");
    }

    public void goBackTutorial(ActionEvent actionEvent) {
        showTutorialDialog("Go Back Button, exits the game");
    }

    public void showResourceInfo(ActionEvent actionEvent) {showTutorialDialog( "These are your resources meters, as time progresses your resources will decrease over time, certain states will alter the speed of decay");
    }

    public void showPetInfo(ActionEvent actionEvent) {showTutorialDialog( "This is your pet depending on what pet you've chosen there your resource meter will decay differently, Mole is the standard with equal decay, Cats require more food but also drain energy slower, Bears are the highest difficulty demanding high food, play and energy as they all decay quick but health decays slower");
    }
    public void exitTutorial(ActionEvent actionEvent) {SceneController.getInstance().switchToMainMenu();
    }

    public void showPetStates(ActionEvent actionEvent) {showTutorialDialog("If your pet drops to low levels of any of the resources concequences will occur, with energy reaching 0 the pet will fall asleep and not wake up until energy is full, when hunger is empty happiness and hunger will decrease faster, when happiness is angry only actions that increase happiness are allowed, when health reaches 0 the game ends and you will have to try again");
    }
}
