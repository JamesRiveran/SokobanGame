/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.sokovangame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class ChooseLevelViewController implements Initializable {

    private StartMenuViewController startMenuController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void setStartMenuController(StartMenuViewController controller) {
        this.startMenuController = controller;
    }

    @FXML
    private void Level1Button(ActionEvent event) {
        switchToStartMenu(1);
    }

    @FXML
    private void Level2Button(ActionEvent event) {
        switchToStartMenu(2);
    }

    @FXML
    private void Level3Button(ActionEvent event) {
        switchToStartMenu(3);
    }

    @FXML
    private void Level4Button(ActionEvent event) {
        switchToStartMenu(4);
    }

    @FXML
    private void Level5Button(ActionEvent event) {
        switchToStartMenu(5);
    }

    private void switchToStartMenu(int level) {

        startMenuController.getLevel(level);
        startMenuController.showStartMenu();
    }
}
