package com.mycompany.sokovangame;

import com.mycompany.sokovangame.StartMenuViewController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class ChooseCharacterViewController implements Initializable {
    private String playerName;
    private String itemName;
    private StartMenuViewController startMenuController;
    private GameController gameControler;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initData(String playerName, String itemName, StartMenuViewController startMenuController) {
        this.playerName = playerName;
        this.itemName = itemName;
        this.startMenuController = startMenuController;
    }

    @FXML
    private void SteveButton(ActionEvent event) {
        switchToStartMenu("Steve", 5);
    }

    @FXML
    private void AlexButton(ActionEvent event) {
        switchToStartMenu("Alex", 6);
    }

    @FXML
    private void CreeperButton(ActionEvent event) {
        switchToStartMenu("Creeper", 7);
    }

    @FXML
    private void EnderManButton(ActionEvent event) {
        switchToStartMenu("EnderMan", 8);
    }

    @FXML
    private void AldeanoButton(ActionEvent event) {
        switchToStartMenu("Esqueleto", 9);
    }

    @FXML
    private void ZombieButton(ActionEvent event) {
        switchToStartMenu("Zombie", 10);
    }

    private void switchToStartMenu(String characterName, int characterNumber) {
        // Establecer el personaje seleccionado y regresar al menú de inicio
        startMenuController.setCharacter(characterName, characterNumber);
        startMenuController.setPlayerName(playerName);
        startMenuController.setItemName(itemName);
        startMenuController.showStartMenu();
    }
}
