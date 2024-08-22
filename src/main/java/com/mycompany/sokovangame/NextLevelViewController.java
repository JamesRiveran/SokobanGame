package com.mycompany.sokovangame;

import java.io.IOException;
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
    @FXML
    private Label txt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización
    }

    public void setMessage(String messageText,String messageButton) {
        System.out.println(messageText);
        System.out.println(messageButton);
        txtMessage.setText(messageText);
        btnGoNextLevel.setText(messageButton);
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
        this.message = message;
    }

    @FXML
    private void reviewButton(ActionEvent event) {
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

            // Cerrar la ventana actual
            Stage currentStage = (Stage) txtMessage.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

@FXML
private void goNextLevelButton(ActionEvent event) {
    try {
        FXMLLoader loader;
        Parent view;

        if (level < 5) {
            loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            view = loader.load();

            GameController controller = loader.getController();
            controller.setItems(characterNumber, itemName, playerName, level + 1);

        } else {
            loader = new FXMLLoader(getClass().getResource("StartMenuView.fxml"));
            view = loader.load();
        }

        Stage stage = new Stage();
        stage.setTitle("Game");
        stage.setScene(new Scene(view, 800, 600));
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
        stage.setResizable(true);
        stage.initModality(Modality.NONE);
        stage.show();

        // Cerrar la ventana actual
        Stage currentStage = (Stage) txtMessage.getScene().getWindow();
        currentStage.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}


}
