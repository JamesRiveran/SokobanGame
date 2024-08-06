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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 * 
 * Author marco
 */

public class GameController implements Initializable {
    private int characterNumber = 5;
    private int playerPosX;
    private int playerPosY;
    private Map<Character, Integer> typeMap;

    @FXML
    private GridPane BoardGame;

    Square[][] gameMatrix = new Square[10][10];

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTypeMap();
        try {
            loadBoard("levels/level1.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupControls();
    }

    private void initializeTypeMap() {
        typeMap = new HashMap<>();
        typeMap.put(' ', 0); // Space - empty path
        typeMap.put('#', 3); // Wall
        typeMap.put('.', 4); // Place for a box
        typeMap.put('$', 2); // Box
        typeMap.put('!', 1); // Box in correct place
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
        gameMatrix[playerPosX][playerPosY].setType(characterNumber);
    }

    public void setPlayerPosition(int x, int y) {
        if (isValidPosition(x, y)) {
            gameMatrix[playerPosX][playerPosY].setType(0); // Clear previous position
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
