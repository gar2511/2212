package com.example.controller;

import com.example.model.UserPreferences;
import com.example.util.FileHandler;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerTest {

    private LoginController loginController;
    private FileHandler mockFileHandler;
    private UserPreferences mockUserPreferences;

    @BeforeAll
    static void setupClass() {
        javafx.application.Platform.startup(() -> {
        }); // Initialize JavaFX toolkit
    }

    @BeforeEach
    void setup() {
        loginController = new LoginController();

        // Mock dependencies
        mockFileHandler = Mockito.mock(FileHandler.class);
        mockUserPreferences = Mockito.mock(UserPreferences.class);

        // Inject mocks into the controller
        loginController.fileHandler = mockFileHandler;
        loginController.userPrefs = mockUserPreferences;

        // Mock UI elements
        loginController.pinField = new PasswordField();
        loginController.confirmPinField = new PasswordField();
        loginController.pinErrorIcon = new StackPane();
        loginController.confirmPinErrorIcon = new StackPane();
        loginController.titleLabel = new Label();
        loginController.pinDotsContainer = new HBox();
        loginController.dots = new Circle[6];
        for (int i = 0; i < loginController.dots.length; i++) {
            loginController.dots[i] = new Circle();
        }
    }

    @Test
    void testInitializeCreationMode() throws Exception {
        when(mockFileHandler.loadPreferences()).thenReturn(mockUserPreferences);
        when(mockUserPreferences.getParentPassword()).thenReturn("");

        loginController.initialize();

        assertEquals("Create Parent PIN", loginController.titleLabel.getText());
        assertTrue(loginController.isCreationMode);
    }

    @Test
    void testInitializeLoginMode() throws Exception {
        when(mockFileHandler.loadPreferences()).thenReturn(mockUserPreferences);
        when(mockUserPreferences.getParentPassword()).thenReturn("123456");

        loginController.initialize();

        assertEquals("Parent Login", loginController.titleLabel.getText());
        assertFalse(loginController.isCreationMode);
    }

    @Test
    void testHandleCreateSuccessful() throws Exception {
        when(mockFileHandler.loadPreferences()).thenReturn(mockUserPreferences);
        loginController.initialize();

        loginController.currentPin.append("123456");
        loginController.handleCreate();

        assertEquals("Confirm PIN", loginController.titleLabel.getText());
        assertEquals("123456", loginController.firstPin);

        loginController.currentPin.append("123456");
        loginController.handleCreate();

        verify(mockUserPreferences).setParentPassword("123456");
        verify(mockUserPreferences).setParentControlsEnabled(true);
        verify(mockFileHandler).savePreferences(mockUserPreferences);
    }

    @Test
    void testHandleAuthenticationSuccess() {
        when(mockUserPreferences.getParentPassword()).thenReturn("123456");
        loginController.currentPin.append("123456");

        loginController.handleAuthentication();

        // Mock a switch to parent menu, e.g., by verifying a method call
    }

    @Test
    void testHandleAuthenticationFailure() {
        when(mockUserPreferences.getParentPassword()).thenReturn("123456");
        loginController.currentPin.append("654321");

        loginController.handleAuthentication();

        // Verify shaking animation or error state
        assertTrue(loginController.isShaking);
    }

    @Test
    void testUpdatePinDots() {
        loginController.currentPin.append("123");
        loginController.updatePinDots();

        for (int i = 0; i < 3; i++) {
            assertEquals(javafx.scene.paint.Color.WHITE, loginController.dots[i].getFill());
        }
        for (int i = 3; i < 6; i++) {
            assertEquals(javafx.scene.paint.Color.valueOf("rgba(255, 255, 255, 0.3)"), loginController.dots[i].getFill());
        }
    }

    @Test
    void testHandleKeyPressValidInput() {
        loginController.handleKeyPress(new javafx.scene.input.KeyEvent(
                javafx.scene.input.KeyEvent.KEY_PRESSED, "1", "1", KeyCode.DIGIT1, false, false, false, false));
        assertEquals("1", loginController.currentPin.toString());
    }

    @Test
    void testHandleKeyPressBackspace() {
        loginController.currentPin.append("123");
        loginController.handleKeyPress(new javafx.scene.input.KeyEvent(
                javafx.scene.input.KeyEvent.KEY_PRESSED, "", "", KeyCode.BACK_SPACE, false, false, false, false));
        assertEquals("12", loginController.currentPin.toString());
    }
}
