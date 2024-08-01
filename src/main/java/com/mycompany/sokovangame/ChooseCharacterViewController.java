/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sokovangame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class ChooseCharacterViewController implements Initializable {
    private String playerName;
    private String itemName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

     @FXML
    private void SteveButton(ActionEvent event) {
        switchToStartMenu("Steve", 1, event);
    }

    @FXML
    private void AlexButton(ActionEvent event) {
        switchToStartMenu("Alex", 2, event);
    }

    @FXML
    private void CreeperButton(ActionEvent event) {
        switchToStartMenu("Creeper", 3, event);
    }

    @FXML
    private void EnderManButton(ActionEvent event) {
        switchToStartMenu("EnderMan", 4, event);
    }

    @FXML
    private void AldeanoButton(ActionEvent event) {
        switchToStartMenu("Aldeano", 5, event);
    }

    @FXML
    private void ZombieButton(ActionEvent event) {
        switchToStartMenu("Zombie", 6, event);
    }

    public void initData(String playerName, String itemName) {
    this.playerName = playerName;
    this.itemName = itemName;
}

private void switchToStartMenu(String characterName, int characterNumber, ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuView.fxml"));
        Parent root = loader.load();

        StartMenuViewController controller = loader.getController();
        controller.setCharacter(characterName, characterNumber);
        controller.setPlayerName(playerName); // Pasa el nombre del jugador
        controller.setItemName(itemName); // Pasa el nombre del item

        Scene scene = new Scene(root, 840, 640);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    
}
