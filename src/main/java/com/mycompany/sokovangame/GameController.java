package com.mycompany.sokovangame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import model.Square;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 * 
 * Author marco
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
    private int playerPosX;
    private int playerPosY;
    private Map<Character, Integer> typeMap;

    @FXML
    private GridPane BoardGame;

    Square[][] gameMatrix = new Square[10][10];
    @FXML
    private Label txtGameName;
    @FXML
    private Label txtPlayerName;
    @FXML
    private Label txtCronometer;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTypeMap();
        try {
            loadBoard("levels/level1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupControls();
        iniciar();
    }


    public void setItems(int characterNumber, String GameName, String PlayerName) {
        this.characterNumber = characterNumber;
        this.GameName = GameName;
        this.PlayerName = PlayerName;

    private void initializeTypeMap() {
        typeMap = new HashMap<>();
        typeMap.put(' ', 0); // Space - empty path
        typeMap.put('#', 3); // Wall
        typeMap.put('.', 2); // Place for a box
        typeMap.put('$', 1); // Box
        typeMap.put('!', 4); // Box in correct place
        typeMap.put('@', 0); // Player's starting position, initially empty, replaced later
    }

    private void loadBoard(String resourcePath) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null && row < gameMatrix.length) {
            for (int col = 0; col < line.length() && col < gameMatrix[row].length; col++) {
                char typeChar = line.charAt(col);
                int type = typeMap.getOrDefault(typeChar, 0);

                if (typeChar == '@') {
                    playerPosX = row;
                    playerPosY = col;
                    type = characterNumber;
                }

                gameMatrix[row][col] = new Square(type);
                BoardGame.add(gameMatrix[row][col].getButtonSquare(), col, row);
            }
            row++;
        }
        reader.close();
        updatePlayerPosition();
    }

    public void setCharacterNumber(int characterNumber) {
        this.characterNumber = characterNumber;
        updatePlayerPosition();
    }

    public void setupControls() {
        BoardGame.setFocusTraversable(true);
        BoardGame.setOnKeyPressed(this::keyControls);
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
            System.out.println("Cannot move out of bounds!");
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
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
