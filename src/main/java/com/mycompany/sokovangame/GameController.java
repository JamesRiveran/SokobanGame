/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sokovangame;

import model.Square;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.S;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author marco
 */

// 1. caja
// 2. meta
// 3. muro
// 4. caja en meta
// 5. steve
// 6. alex
// 7. creeper
// 8. enderman
// 9. esqueleto
// 10. zombie
public class GameController implements Initializable {
    private int characterNumber;
    
    @FXML
    private GridPane BoardGame;
    private int playerPosX = 7;
    private int playerPosY = 0;
    Square[][] gameMatrix = new Square[10][10];
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                gameMatrix[i][j] = new Square(0);
                BoardGame.add(gameMatrix[i][j].getButtonSquare(), j, i);
            }
        }
        
        
        setupControls();
    }
    public void setCharacterNumber(int characterNumber) {
        this.characterNumber = characterNumber;
            updatePlayerPosition();
    }
    public void setupControls() {
        BoardGame.setOnKeyPressed(event -> {
            keyControls(event);
        });
    }

    public void updatePlayerPosition() {
        gameMatrix[playerPosX][playerPosY].setType(characterNumber);
    }

    public void setPlayerPosition(int x, int y) {
        if (isValidPosition(x, y)) {
            gameMatrix[playerPosX][playerPosY].setType(characterNumber);
            playerPosX = x;
            playerPosY = y;
            updatePlayerPosition();
        } else {
            //Añadir mensaje de no poder salirse del tablero
        }
    }

    private boolean isValidPosition(int x, int y) {
        boolean validMovement = x >= 0 && x < 10 && y >= 0 && y < 10;
        return validMovement;
    }

    public void keyControls(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                setPlayerPosition(playerPosX - 1, playerPosY);
                break;
            case A:
                setPlayerPosition(playerPosX, playerPosY - 1);
                break;
            case S:
                setPlayerPosition(playerPosX + 1, playerPosY);
                break;
            case D:
                setPlayerPosition(playerPosX, playerPosY + 1);
                break;
            default:
                break;
        }
    }

}
