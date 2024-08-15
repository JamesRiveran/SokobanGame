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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class NextLevelViewController implements Initializable {

    
    private int characterNumber;
    private String itemName;
    private String playerName;
    private int level;
    @FXML
    private Label txt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void reviewButton(ActionEvent event) {
    }

    public void setItems(int characterNumber, String GameName, String PlayerName, int level) {
        this.characterNumber = characterNumber;
        this.itemName = GameName;
        this.playerName = PlayerName;
        this.level = level;
        
    }

    @FXML
    private void goNextLevelButton(ActionEvent event) throws IOException {
        //String playerName = txtPlayerName.getText();
        //String itemName = txtItemName.getText();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
        Parent gameView = loader.load();

        // Obtener el controlador de la vista del juego
        GameController controller = loader.getController();
        controller.setItems(characterNumber, itemName, playerName, level + 1); // Pasar el número del personaje

        // Crear un nuevo Stage para la vista del juego
        Stage gameStage = new Stage();
        gameStage.setTitle("Game");
        gameStage.setScene(new Scene(gameView, 800, 600)); // Tamaño preferido (ancho, alto)
        gameStage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
        gameStage.setResizable(true);
        gameStage.initModality(Modality.NONE); // Permite que la ventana principal esté activa mientras se muestra la ventana del juego
        gameStage.show();

        // Cerrar la ventana actual (menú)
        Stage currentStage = (Stage) txt.getScene().getWindow();
        currentStage.close();
    }

}
