/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sokovangame;

import model.Square;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private String GameName;
    private String PlayerName;
    private long iniciarTiempo;
    private boolean corriendo = false;
    private AnimationTimer timer;
    private long lastUpdate = 0;
    private long tiempoAcumulado = 0;
    public int cont = 0;
    @FXML
    private GridPane BoardGame;
    private int playerPosX = 7;
    private int playerPosY = 0;
    Square[][] gameMatrix = new Square[10][10];
    @FXML
    private Label txtGameName;
    @FXML
    private Label txtPlayerName;
    @FXML
    private Label txtCronometer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                gameMatrix[i][j] = new Square(0);
                BoardGame.add(gameMatrix[i][j].getButtonSquare(), j, i);
            }
        }

        setupControls();
        iniciar();
    }

    public void setItems(int characterNumber, String GameName, String PlayerName) {
        this.characterNumber = characterNumber;
        this.GameName = GameName;
        this.PlayerName = PlayerName;
        updatePlayerPosition();
    }

    public void setupControls() {
        BoardGame.setOnKeyPressed(event -> {
            keyControls(event);
        });
    }

    public void updatePlayerPosition() {
        txtGameName.setText(GameName);
        txtPlayerName.setText(PlayerName);
        gameMatrix[playerPosX][playerPosY].setType(characterNumber);
    }

    public void setPlayerPosition(int x, int y) {
        if (isValidPosition(x, y)) {
            gameMatrix[playerPosX][playerPosY].setType(0);
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
        System.out.println(event.getCode().toString());
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

    @FXML
    private void resetButton(ActionEvent event) {
    }

    @FXML
    private void undoButton(ActionEvent event) {
    }

    @FXML
    private void optionsButton(ActionEvent event) {
    }

    @FXML
    private void helpButton(ActionEvent event) {
    }

    private void reiniciar() {
        detener();
        iniciarTiempo = 0;
        tiempoAcumulado = 0;
        txtCronometer.setText("00:00:00");
    }

    private void iniciar() {
        updatePlayerPosition();
        if (!corriendo && iniciarTiempo == 0) {
            corriendo = true;
            iniciarTiempo = System.currentTimeMillis();
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    updateCrono(tiempoAcumulado);
                }
            };
            timer.start();
        }
    }

    private void reanudar() {
        if (!corriendo && iniciarTiempo != 0) {
            corriendo = true;
            iniciarTiempo = System.currentTimeMillis() - tiempoAcumulado;
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    updateCrono(tiempoAcumulado);
                }
            };
            timer.start();
        }
    }
    int contador = 0;

    private void detener() {
        if (cont == 0) {
            if (corriendo) {
                corriendo = false;
                long currentTime = System.currentTimeMillis();
                tiempoAcumulado = (currentTime - iniciarTiempo);
                if (timer != null) {
                    timer.stop();
                    timer = null;
                }
            }
            cont++;
        } else {
            reanudar();
            cont = 0;
        }
    }

    private void updateCrono(Long segundosAContar) {
        if (corriendo) {
            long ahora = System.currentTimeMillis();
            long transcurrido = ahora - iniciarTiempo + (segundosAContar * 1000);
            long segundos = transcurrido / 1000;
            long minutos = (segundos / 60) % 60;
            long horas = segundos / 3600;

            txtCronometer.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos % 60));

            long now = System.nanoTime();
            if (now - lastUpdate >= 1_000_000_000) {
                lastUpdate = now;
            }
        }
    }
}
