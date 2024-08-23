package com.mycompany.sokovangame;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private String message;
    private int level;
    private int movesCount;
    private Queue<Integer> repetition = new ArrayDeque<>();
    int initialPlayerPosX;
    int initialPlayerPosY;
    @FXML
    private Label txtMessage;
    @FXML
    private Button btnGoNextLevel;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización
    }

    public void setMessage(String messageText, String messageButton) {
        txtMessage.setText(messageText);
        btnGoNextLevel.setText(messageButton);
    }

    @FXML
    private void reviewButton(ActionEvent event) throws URISyntaxException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            Parent gameView = loader.load();

            GameController controller = loader.getController();

            controller.setItems(characterNumber, itemName, playerName, level);
            controller.playRepetition(movesCount, repetition, initialPlayerPosX, initialPlayerPosY);

            Stage gameStage = new Stage();
            gameStage.setTitle("Game");
            gameStage.setScene(new Scene(gameView, 800, 600));
            gameStage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            gameStage.setResizable(true);
            gameStage.initModality(Modality.NONE);
            gameStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setItems(int characterNumber, String GameName, String PlayerName, int level, int movesCount, Queue<Integer> repetition, int initialPlayerPosX, int initialPlayerPosY, int nextLevel) {
        this.characterNumber = characterNumber;
        this.itemName = GameName;
        this.playerName = PlayerName;
        this.level = level;
        this.movesCount = movesCount;
        this.repetition = repetition;
        this.initialPlayerPosX = initialPlayerPosX;
        this.initialPlayerPosY = initialPlayerPosY;
    }

     @FXML
    private void goNextLevelButton(ActionEvent event) throws IOException, URISyntaxException {

        if (level < 5) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            Parent gameView = loader.load();

            GameController controller = loader.getController();
            controller.setItems(characterNumber, itemName, playerName, level + 1);

            Stage gameStage = new Stage();
            gameStage.setTitle("Game");
            gameStage.setScene(new Scene(gameView, 800, 600));
            gameStage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            gameStage.setResizable(true);
            gameStage.initModality(Modality.NONE);
            gameStage.show();

            Stage currentStage = (Stage) txtMessage.getScene().getWindow();
            currentStage.close();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartMenuView.fxml"));
            Parent gameView = loader.load();

            StartMenuViewController controller = loader.getController();


            Stage gameStage = new Stage();
            gameStage.setTitle("Menu");
            gameStage.setScene(new Scene(gameView, 800, 600));
            gameStage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            gameStage.setResizable(true);
            gameStage.initModality(Modality.NONE);
            gameStage.show();

            Stage currentStage = (Stage) txtMessage.getScene().getWindow();
            currentStage.close();  
        }
    }

}
