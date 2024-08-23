package com.mycompany.sokovangame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import model.Square;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
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
    private int playerPosX = -1;
    private int playerPosY = -1;
    private int initialGoalX;
    private int initialGoalY;
    private String message;
    private Map<Integer, Character> reverseTypeMap;
    private Stack<int[]> boxesOnGoals = new Stack<>();
    boolean band = false;
    private int initialPlayerPosX;
    private int initialPlayerPosY;
    private Map<Character, Integer> typeMap;
    private Queue<Integer> repetition = new ArrayDeque<>();
    private int movesCount = 1;
    private List<List<Square>> backupMatrix = new ArrayList<>();
    private String messageText = "";
    private String messageButton = "";

    @FXML
    private GridPane BoardGame;
    private List<List<Square>> gameMatrix = new ArrayList<>();
    @FXML
    private Label txtGameName;
    @FXML
    private Label txtPlayerName;
    @FXML
    private Label txtLevel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTypeMap();

        setupControls();

        Platform.runLater(() -> {
            BoardGame.requestFocus();
        });
    }

    public void setItems(int characterNumber, String GameName, String PlayerName, int level) throws URISyntaxException {
        this.characterNumber = characterNumber;
        this.GameName = GameName;
        this.PlayerName = PlayerName;
        this.level = level;
        try {
            loadBoard("levels/level" + level + ".txt", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Map<Integer, Integer> levelGoals = Map.of(
            1, 4,
            2, 5,
            3, 7,
            4, 7,
            5, 7
    );

    public void setItemsSavedGame(int characterNumber, String GameName, String PlayerName, int level) throws URISyntaxException {
        this.characterNumber = characterNumber;
        this.GameName = GameName;
        this.PlayerName = PlayerName;
        this.level = level;
        
        try {
            loadBoard("levelsSaved/gameSavedLevel" + level + ".txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updatePlayerPosition();

    }

    private void initializeTypeMap() {
        typeMap = new HashMap<>();
        typeMap.put(' ', 0);
        typeMap.put('#', 3);
        typeMap.put('.', 2);
        typeMap.put('$', 1);
        typeMap.put('!', 4);
        typeMap.put('@', 0);
        BoardGame.requestFocus();

        reverseTypeMap = new HashMap<>();
        reverseTypeMap.put(0, ' ');
        reverseTypeMap.put(3, '#');
        reverseTypeMap.put(2, '.');
        reverseTypeMap.put(1, '$');
        reverseTypeMap.put(4, '!');
    }

    private void loadBoard(String resourcePath, Boolean isSaved) throws IOException, URISyntaxException {
        BoardGame.getChildren().clear();
        gameMatrix.clear();

        Path resourceRoot = Paths.get(getClass().getClassLoader().getResource("").toURI()).getParent().getParent();
        Path filePath = resourceRoot.resolve("src/main/resources/" + resourcePath);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
                        initialPlayerPosX = row;
                        initialPlayerPosY = col;
                    }
                    Square square = new Square(type);
                    rowList.add(square);
                    Square backupSquare = new Square(type);
                    backupRowList.add(backupSquare);
                    BoardGame.add(square.getButtonSquare(), col, row);
                }
                gameMatrix.add(rowList);
                backupMatrix.add(backupRowList);
                row++;
            }

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Boxes on Goals:")) {
                    int boxesOnGoalsCount = Integer.parseInt(line.split(": ")[1].trim());
                    for (int i = 0; i < boxesOnGoalsCount; i++) {
                        boxesOnGoals.push(new int[]{0, 0});
                    }
                } else if (line.startsWith("Repetition: ")) {
                    repetition.clear();
                    String[] moves = line.substring(12).trim().split(" ");
                    for (String move : moves) {
                        repetition.add(Integer.parseInt(move));
                    }
                }
                if (line.startsWith("Initial position: ") && isSaved) {
                    String[] positions = line.split(": ")[1].trim().split(",");
                    int savedPlayerPosX = Integer.parseInt(positions[0].trim());
                    int savedPlayerPosY = Integer.parseInt(positions[1].trim());
                    initialPlayerPosX = savedPlayerPosX;
                    initialPlayerPosY = savedPlayerPosY;
                    System.out.println(initialPlayerPosX + "asdasd" + initialPlayerPosY);
                }
                if (line.startsWith("Moves Count: ") && isSaved) {
                movesCount = Integer.parseInt(line.split(": ")[1].trim());
                }
            }
            updatePlayerPosition();
            checkLevelCompletion(messageText, messageButton);
            BoardGame.requestFocus();
        }
    }

    public Square getListSquare(int i, int j) {
        if (i < 0 || i >= gameMatrix.size() || j < 0 || j >= gameMatrix.get(i).size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: i=" + i + ", j=" + j);
        }
        return gameMatrix.get(i).get(j);
    }

    public void setCharacterNumber(int characterNumber) {
        this.characterNumber = characterNumber;
        updatePlayerPosition();
    }

    public void setupControls() {

        BoardGame.setFocusTraversable(true);
        BoardGame.setOnKeyPressed(this::keyControls);
        BoardGame.requestFocus();

    }

    public void updatePlayerPosition() {
        txtGameName.setText(GameName);
        txtPlayerName.setText(PlayerName);
        txtLevel.setText(String.valueOf(level));
        if (playerPosX >= 0 && playerPosY >= 0) {
            getListSquare(playerPosX, playerPosY).setType(characterNumber);

        }

    }

    public void playRepetition(int movesCount, Queue<Integer> repetition, int initialPlayerPosX, int initialPlayerPosY) throws IOException {
        this.movesCount = movesCount;
        this.repetition = repetition;
        this.initialPlayerPosX = initialPlayerPosX;
        this.initialPlayerPosY = initialPlayerPosY;
        if (repetition.isEmpty()) {
            if (playerPosX > 0 && playerPosY > 0) {

                playerPosX = initialPlayerPosX;
                playerPosY = initialPlayerPosY;
            }
        }

        getListSquare(playerPosX, playerPosY).setType(0);
        playerPosX = initialPlayerPosX;
        playerPosY = initialPlayerPosY;
        updatePlayerPosition();

        restoreBoard();

        PauseTransition pause = new PauseTransition(Duration.millis(400));
        pause.setOnFinished(event -> {
            this.movesCount--;
            if (this.movesCount == 0) {

                pause.stop();
                this.movesCount += 1;
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
        int directionX = x - playerPosX;
        int directionY = y - playerPosY;
        int newCordX = playerPosX + directionX;
        int newCordY = playerPosY + directionY;
        int newItemCordX = playerPosX + directionX;
        int newItemCordY = playerPosY + directionY;
        int BoxCordX = playerPosX + directionX * 2;
        int BoxCordY = playerPosY + directionY * 2;
        registerPlayerMove(directionX, directionY);

        if (isValidPosition(x, y) && getListSquare(newCordX, newCordY).getType() != 3 && getListSquare(newCordX, newCordY).getType() != 4 && getListSquare(newCordX, newCordY).getType() != 2 && isMoveBox(newCordX, newCordY, directionX, directionY)) {
            getListSquare(playerPosX, playerPosY).setType(0);
            playerPosX = x;
            playerPosY = y;
            updatePlayerPosition();

            restoreItem(initialGoalX, initialGoalY, directionX, directionY);

        } else if (isValidPosition(x, y) && getListSquare(newCordX, newCordY).getType() != 3 && getListSquare(newCordX, newCordY).getType() != 4 && isValidPosition(x, y) && getListSquare(newCordX, newCordY).getType() != 1 && isMoveBox(newCordX, newCordY, directionX, directionY)) {
            getListSquare(playerPosX, playerPosY).setType(0);
            playerPosX = x;
            playerPosY = y;
            initialGoalX = newItemCordX;
            initialGoalY = newItemCordY;
            band = true;
            updatePlayerPosition();

        } else if (isValidPosition(x, y) && getListSquare(newCordX, newCordY).getType() != 3 && getListSquare(newCordX, newCordY).getType() != 2 && isValidPosition(x, y) && getListSquare(newCordX, newCordY).getType() != 1 && isMoveBox(newCordX, newCordY, directionX, directionY) && isMovePlayer(newCordX, newCordY, directionX, directionY)) {
            int newBoxCordX = playerPosX + directionX;
            int newBoxCordY = playerPosY + directionY;

            getListSquare(playerPosX, playerPosY).setType(0);
            playerPosX = x;
            playerPosY = y;
            initialGoalX = newItemCordX;
            initialGoalY = newItemCordY;
            takeOutBox(newCordX, newCordY, directionX, directionY);
            band = true;
            updatePlayerPosition();
        } else {

            System.out.println("Cannot move there!");
        }
        checkLevelCompletion(messageText, messageButton);
    }

    private boolean isMoveBox(int newCordX, int newCordY, int directionX, int directionY) {
        int newBoxCordX = playerPosX + directionX * 2;
        int newBoxCordY = playerPosY + directionY * 2;

        if (getListSquare(newCordX, newCordY).getType() == 1) {
            int targetType = getListSquare(newBoxCordX, newBoxCordY).getType();
            if (targetType != 0 && targetType != 2) {
                return false;

            } else if (targetType == 2) {
                getListSquare(newBoxCordX, newBoxCordY).setType(4);
                initialGoalX = newBoxCordX;
                initialGoalY = newBoxCordY;
                boxesOnGoals.push(new int[]{newBoxCordX, newBoxCordY});

            } else {
                getListSquare(newBoxCordX, newBoxCordY).setType(1);
            }
        }

        return true;
    }

    private boolean isMovePlayer(int newCordX, int newCordY, int directionX, int directionY) {
        int newBoxCordX = playerPosX + directionX * 2;
        int newBoxCordY = playerPosY + directionY * 2;
        int targetType = getListSquare(newBoxCordX, newBoxCordY).getType();

        if (targetType != 0 && targetType != 2) {
            initialGoalX = newBoxCordX;
            initialGoalY = newBoxCordY;
            return false;
        } else {
            getListSquare(newCordX, newCordY).setType(targetType);
            band = true;
        }

        return true;
    }

    private void takeOutBox(int newCordX, int newCordY, int directionX, int directionY) {
        int newBoxCordX = playerPosX + directionX;
        int newBoxCordY = playerPosY + directionY;

        int targetType = getListSquare(newBoxCordX, newBoxCordY).getType();

        if (targetType == 0 || targetType == 2) {
            getListSquare(newBoxCordX, newBoxCordY).setType(1);
            updatePlayerPosition();
        } else {
            System.out.println("Cannot move there!");
        }

        if (!boxesOnGoals.isEmpty()) {
            boxesOnGoals.pop();
        }
    }

    private void restoreItem(int initialGoalX, int initialGoalY, int directionX, int directionY) {
        int newBoxCordX = playerPosX + directionX;
        int newBoxCordY = playerPosY + directionY;
        if (band == true) {
            getListSquare(initialGoalX, initialGoalY).setType(2);
            band = false;
        }
    }
    
    private boolean isLevelCompleted(int currentLevel) {
        int totalGoals = levelGoals.getOrDefault(currentLevel, 0);

        return boxesOnGoals.size() == totalGoals;
    }

    private void checkLevelCompletion(String messageText, String messageButton) {
        if (isLevelCompleted(level)) {
            try {
                Stage currentStage = (Stage) BoardGame.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("NextLevelView.fxml"));
                Parent nextLevel = loader.load();

                Scene scene = new Scene(nextLevel, 800, 600);

                NextLevelViewController controller = loader.getController();
                if (level < 5) {
                    controller.setMessage("You passed level", "NEXT LEVEL");
                } else {
                    controller.setMessage("You passed all levels", "GO TO MENU");
                }
                controller.setItems(characterNumber, GameName, PlayerName, level, movesCount, repetition, initialPlayerPosX, initialPlayerPosY, level);
                Stage nextLevelStage = new Stage();
                nextLevelStage.setTitle("Next Level");
                nextLevelStage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
                nextLevelStage.setResizable(false);
                nextLevelStage.setScene(scene);

                nextLevelStage.show();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    @FXML
    private void saveGameButton(ActionEvent event) {
        try {
            Path resourceRoot = Paths.get(getClass().getClassLoader().getResource("").toURI()).getParent().getParent();
            Path levelsSavedPath = resourceRoot.resolve("src/main/resources/levelsSaved");
            File directory = levelsSavedPath.toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Path saveFilePath = levelsSavedPath.resolve("gameSavedLevel" + level + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFilePath.toFile()))) {
                for (int i = 0; i < gameMatrix.size(); i++) {
                    StringBuilder line = new StringBuilder();
                    for (int j = 0; j < gameMatrix.get(i).size(); j++) {
                        Square square = getListSquare(i, j);
                        int type = square.getType();
                        char symbol = reverseTypeMap.getOrDefault(type, ' ');

                        if (i == playerPosX && j == playerPosY) {
                            symbol = '@';
                        }
                        line.append(symbol);
                    }
                    writer.write(line.toString());
                    writer.newLine();
                }
                writer.newLine();
                writer.write("Player Name: " + PlayerName);
                writer.newLine();
                writer.write("Game Name: " + GameName);
                writer.newLine();
                writer.write("Level: " + level);
                writer.newLine();
                writer.write("Character Number: " + characterNumber);
                writer.newLine();
                writer.write("Boxes on Goals: " + boxesOnGoals.size());

                // Guardar la secuencia de movimientos
                writer.newLine();
                writer.write("Repetition: ");
                for (Integer move : repetition) {
                    writer.write(move + " ");
                }
                writer.newLine();
                writer.write("Initial position: " + initialPlayerPosX + "," + initialPlayerPosY);
                writer.newLine();
                writer.write("Moves Count: " + movesCount);
                writer.flush();
                showAlert("Your game has been saved successfully!");
                BoardGame.requestFocus();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Failed to Save Game");
            alert.setContentText("There was an error saving your game.");
            alert.showAndWait();
        }
    }

    @FXML
    private void resetButton(ActionEvent event) throws URISyntaxException {
        repetition.clear();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Reset Level Confirmation");
        alert.setHeaderText("Are you sure you want to reset the level?");
        alert.setContentText("You will lose your current progress in this level.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                loadBoard("levels/level" + level + ".txt", false);
                while (!boxesOnGoals.isEmpty()) {
                    boxesOnGoals.pop();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BoardGame.requestFocus();
    }

    @FXML
    private void mainMenuButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Return to Main Menu Confirmation");
        alert.setHeaderText("Are you sure you want to return to the main menu?");
        alert.setContentText("You will lose your current progress if you haven't saved your game.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuView.fxml"));
            Parent startMenuView = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(startMenuView);
        }
        BoardGame.requestFocus();
    }

    @FXML
    private void closeButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to exit, or Cancel to stay.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
        BoardGame.requestFocus();
    }

    @FXML
    private void helpButton(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpView.fxml"));
            Parent helpView = loader.load();

            Scene scene = new Scene(helpView);

            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            stage.setResizable(false);
            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            BoardGame.requestFocus();
        });
    }

    private void goToMenu(Stage currentStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuView.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Menu");
            newStage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            newStage.setResizable(true);
            newStage.initModality(Modality.NONE);
            newStage.show();

            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
        private void showAlert(String message) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("AlertDialog.fxml"));
            Parent root = loader.load();

            AlertDialogController controller = loader.getController();
            controller.setMessage(message);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("MESSAGE");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
