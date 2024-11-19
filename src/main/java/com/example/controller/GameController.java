package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;
import java.util.Random;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

// Controller class responsible for managing the game scene/view
public class GameController {

    @FXML
    private ImageView moleSprite;
    
    private Timeline animation;
    private Random random = new Random();

    // Constructor - called when the game scene is initialized
    public GameController() {
        System.out.println("GameController initialized");
    }

    @FXML
    public void initialize() {
        try {
            // Create a temporary colored rectangle as the mole sprite
            Rectangle moleRect = new Rectangle(100, 100);
            moleRect.setFill(Color.BROWN);
            moleRect.setArcWidth(20);
            moleRect.setArcHeight(20);
            
            // Convert rectangle to image
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            WritableImage image = moleRect.snapshot(params, null);
            
            moleSprite.setImage(image);
            
            // Start mole animation
            startMoleAnimation();
        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startMoleAnimation() {
        // Get parent bounds for constraining movement
        double maxX = ((StackPane) moleSprite.getParent()).getWidth() - moleSprite.getFitWidth();
        double maxY = ((StackPane) moleSprite.getParent()).getHeight() - moleSprite.getFitHeight();
        
        // Create timeline for continuous random movement
        animation = new Timeline(
            new KeyFrame(Duration.seconds(2), event -> {
                // Generate random positions within bounds
                double newX = random.nextDouble() * maxX;
                double newY = random.nextDouble() * maxY;
                
                // Create smooth transition to new position
                Timeline moveAnimation = new Timeline(
                    new KeyFrame(Duration.millis(500),
                        new KeyValue(moleSprite.translateXProperty(), newX),
                        new KeyValue(moleSprite.translateYProperty(), newY)
                    )
                );
                moveAnimation.play();
            })
        );
        
        // Set animation to repeat indefinitely
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    // Event handler for the back button
    // Returns the user to the main menu when clicked
    @FXML
    private void goBack() {
        if (animation != null) {
            animation.stop();
        }
        SceneController.getInstance().switchToMainMenu();
    }
}
