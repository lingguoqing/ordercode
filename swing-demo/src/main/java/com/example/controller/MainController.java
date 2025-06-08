package com.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.application.Platform;

public class MainController {
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextArea outputArea;
    
    @FXML
    private void handleGreetButton() {
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            outputArea.appendText("Hello, " + name + "!\n");
            nameField.clear();
        } else {
            outputArea.appendText("Please enter your name.\n");
        }
    }
    
    @FXML
    private void handleClearButton() {
        outputArea.clear();
    }
    
    @FXML
    private void handleExitButton() {
        Platform.exit();
    }
} 