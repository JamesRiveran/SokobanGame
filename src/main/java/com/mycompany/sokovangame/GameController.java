package com.mycompany.sokovangame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import model.Square;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * Author marco
 */
public class GameController implements Initializable {

    private int characterNumber;
    private String GameName;
    private String PlayerName;
    private int level;
    private long iniciarTiempo;//english
    private boolean corriendo = false;//english
    private AnimationTimer timer;
    private long lastUpdate = 0;
    private long tiempoAcumulado = 0;//english
    private int playerPosX = -1;
    private int playerPosY = -1;
    private int initialPlayerPosX;
    private int initialPlayerPosY;
    private Map<Character, Integer> typeMap;
    private Queue<Integer> repetition = new ArrayDeque<>();
    private int movesCount = 1;
    private List<List<Square>> backupMatrix = new ArrayList<>();

    @FXML
    private GridPane BoardGame;
    private List<List<Square>> gameMatrix = new ArrayList<>();
    @FXML
    private Label txtGameName;
    @FXML
    private Label txtPlayerName;
    @FXML
    private Label txtCronometer;
    @FXML
    private Label txtLevel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTypeMap();
        setupControls();

        Platform.runLater(() -> {
            BoardGame.requestFocus(); // Solicita el enfoque para el GridPane después de que la vista se cargue
        });

        setItems(5, "asd", "asdasd", 1);//debug
        iniciar();
    }

    public void setItems(int characterNumber, String GameName, String PlayerName, int level) {
        this.characterNumber = characterNumber;
        this.GameName = GameName;
        this.PlayerName = PlayerName;
        this.level = level;
        try {
            loadBoard("levels/level" + level + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(level);
        updatePlayerPosition();
        //BoardGame
    }

    private void initializeTypeMap() {
        typeMap = new HashMap<>();
        typeMap.put(' ', 0); // Espacio - camino vacío
        typeMap.put('#', 3); // Muro
        typeMap.put('.', 2); // Lugar para una caja
        typeMap.put('$', 1); // Caja
        typeMap.put('!', 4); // Caja en lugar correcto
        typeMap.put('@', 0); // Posición inicial del jugador, inicialmente vacía
        BoardGame.requestFocus();
    }

    private void loadBoard(String resourcePath) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        int row = 0;

        gameMatrix.clear();
        backupMatrix.clear();
        BoardGame.getChildren().clear();

        while ((line = reader.readLine()) != null && row < 10) {
            List<Square> rowList = new ArrayList<>();
            List<Square> backupRowList = new ArrayList<>();
            for (int col = 0; col < line.length() && col < 10; col++) {
                char typeChar = line.charAt(col);
                int type = typeMap.getOrDefault(typeChar, 0);

                if (typeChar == '@') {
                    playerPosX = row;
                    playerPosY = col;
                    type = characterNumber;
                }

                Square square = new Square(type);
                rowList.add(square);
                Square backupSquare = new Square(type);
                backupRowList.add(backupSquare);
            }
            gameMatrix.add(rowList);
            backupMatrix.add(backupRowList);
            row++;
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                BoardGame.add(getListSquare(i, j).getButtonSquare(), j, i);
            }
        }
    }

    public Square getListSquare(int i, int j) {
        return gameMatrix.get(i).get(j);
    }

    public void setCharacterNumber(int characterNumber) {
        this.characterNumber = characterNumber;
        updatePlayerPosition();
    }

    public void setupControls() {

        BoardGame.setFocusTraversable(true);
        BoardGame.setOnKeyPressed(this::keyControls);
        BoardGame.requestFocus(); // Solicitar el enfoque para el GridPane

    }

    public void updatePlayerPosition() {
        txtGameName.setText(GameName);
        txtPlayerName.setText(PlayerName);
        txtLevel.setText(String.valueOf(level));//*
        if (playerPosX >= 0 && playerPosY >= 0) {
            getListSquare(playerPosX, playerPosY).setType(characterNumber);
        }
    }

    @FXML
    public void playRepetition() throws IOException {
        initialPlayerPosX = 8;
        initialPlayerPosY = 4;
        getListSquare(playerPosX, playerPosY).setType(0);
        playerPosX = initialPlayerPosX;
        playerPosY = initialPlayerPosY;
        updatePlayerPosition();
        
        restoreBoard();
        PauseTransition pause = new PauseTransition(Duration.millis(750));
        pause.setOnFinished(event -> {
            movesCount--;
            if (movesCount == 0) {
                pause.stop();
                movesCount += 1;
                BoardGame.requestFocus();
                restoreBoard();

            } else {
                setPlayerPosition(playerPosX + repetition.poll(), playerPosY + repetition.poll());
                pause.playFromStart();
            }
        });
        pause.play();
    }

    private void restoreBoard() {
        gameMatrix.clear();
        BoardGame.getChildren().clear();
        for (int i = 0; i < backupMatrix.size(); i++) {
            List<Square> rowList = new ArrayList<>();
            for (int j = 0; j < backupMatrix.get(i).size(); j++) {
                Square backupSquare = backupMatrix.get(i).get(j);
                Square square = new Square(backupSquare.getType());
                rowList.add(square);
            }
            gameMatrix.add(rowList);
        }
        for (int i = 0; i < gameMatrix.size(); i++) {
            for (int j = 0; j < gameMatrix.get(i).size(); j++) {
                BoardGame.add(getListSquare(i, j).getButtonSquare(), j, i);
            }
        }
    }

    public void registerPlayerMove(int directionX, int directionY) {
        repetition.add(directionX);
        repetition.add(directionY);
    }

    public void setPlayerPosition(int x, int y) {
        int directionX = x - playerPosX, newCordX = directionX + playerPosX;
        int directionY = y - playerPosY, newCordY = directionY + playerPosY;
        registerPlayerMove(directionX, directionY);
        if (isValidPosition(x, y) && getListSquare(newCordX, newCordY).getType() != 3 && isMoveBox(newCordX, newCordY, directionX, directionY)) {
            getListSquare(playerPosX, playerPosY).setType(0);
            playerPosX = x;
            playerPosY = y;
            updatePlayerPosition();
        } else {
            System.out.println("Cannot move there!");
        }
    }

    private boolean isMoveBox(int newCordX, int newCordY, int directionX, int directionY) {
        int newBoxCordX = playerPosX + directionX * 2, newBoxCordY = playerPosY + directionY * 2;

        if (getListSquare(newCordX, newCordY).getType() == 1) {
            if (getListSquare(newBoxCordX, newBoxCordY).getType() != 0 && getListSquare(newBoxCordX, newBoxCordY).getType() != 2) {
                return false;
            } else {
                getListSquare(newBoxCordX, newBoxCordY).setType(1);
            }
        }
        return true;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public void keyControls(KeyEvent event) {
        movesCount++;
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

    private void iniciar() {//english
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

    private void reiniciar() {//english
        detener();
        iniciarTiempo = 0;
        tiempoAcumulado = 0;
        txtCronometer.setText("00:00:00");
    }

    private void detener() {//english
        if (corriendo) {
            corriendo = false;
            long currentTime = System.currentTimeMillis();
            tiempoAcumulado = currentTime - iniciarTiempo;
            if (timer != null) {
                timer.stop();
                timer = null;
            }
        }
    }

    private void reanudar() {//english
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

    @FXML
    private void resetButton(ActionEvent event) {
        reiniciar();
    }

    @FXML
    private void undoButton(ActionEvent event) {

    }

    @FXML
    private void helpButton(ActionEvent event) {
        try {
            // Cargar la vista de ayuda
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpView.fxml"));
            Parent helpView = loader.load();

            // Crear una nueva escena con la vista de ayuda
            Scene scene = new Scene(helpView);

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            stage.setResizable(false);
            stage.setScene(scene);

            // Mostrar la nueva ventana
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            BoardGame.requestFocus(); // Solicita el enfoque para el GridPane después de que la vista se cargue
        });
    }
}
