package com.mycompany.sokovangame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class StartMenuViewController implements Initializable {

    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtPlayerName;
    @FXML
    private Label lblCharacter;
    private int characterNumber;
    private String GameName;
    private String PlayerName;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar la imagen de fondo
        loadDefaultBackgroundImage();
        // Cargar la imagen de personaje predeterminada
        loadDefaultCharacterImage();
    }

    @FXML
    private void ChooseCharacterButton(ActionEvent event) {
        try {
            // Guarda los datos actuales
            String playerName = txtPlayerName.getText();
            String itemName = txtItemName.getText();

            // Cargar la vista de selección de personaje
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChooseCharacterView.fxml"));
            Parent chooseCharacterView = loader.load();

            // Obtener el controlador de la nueva vista y pasar los datos
            ChooseCharacterViewController controller = loader.getController();
            controller.initData(playerName, itemName, this);

            // Agregar la vista de selección de personajes al stackPane
            stackPane.getChildren().add(chooseCharacterView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStartMenu() {
        // Eliminar todas las vistas secundarias excepto la imagen de fondo
        if (stackPane.getChildren().size() > 1) {
            stackPane.getChildren().remove(1, stackPane.getChildren().size());
        }
    }

    private void loadDefaultBackgroundImage() {
        String imagePath = "/images/background.png"; // Ruta de la imagen de fondo
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            Image backgroundImage = new Image(imageUrl.toString());

            // Crear ImageView para la imagen de fondo
            ImageView backgroundImageView = new ImageView(backgroundImage);
            backgroundImageView.setFitWidth(mainBorderPane.getWidth()); // Ajusta según el tamaño del BorderPane
            backgroundImageView.setFitHeight(mainBorderPane.getHeight());
            backgroundImageView.setPreserveRatio(true);

            // Asegúrate de que la imagen de fondo esté en el fondo del stackPane
            stackPane.getChildren().add(0, backgroundImageView);
        } else {
            System.out.println("Resource not found: " + imagePath);
        }
    }

    private void loadDefaultCharacterImage() {
        String defaultImagePath = "/images/c.png"; // Imagen predeterminada
        URL imageUrl = getClass().getResource(defaultImagePath);
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toString());

            // Crear ImageView y ajustar su tamaño
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(60); // Ajusta el ancho según sea necesario
            imageView.setFitHeight(60); // Ajusta la altura según sea necesario
            imageView.setPreserveRatio(true); // Mantiene la proporción de la imagen

            lblCharacter.setGraphic(imageView);
        } else {
            System.out.println("Resource not found: " + defaultImagePath);
        }
    }

    public void setCharacter(String characterName, int characterNumber) {
        this.characterNumber = characterNumber;
        System.out.println(characterNumber);

        String imagePath = "/imagesGame/" + characterName.toLowerCase() + ".png";
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toString());

            // Crear ImageView y ajustar su tamaño
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(50); // Ajusta el ancho según sea necesario
            imageView.setFitHeight(50); // Ajusta la altura según sea necesario
            imageView.setPreserveRatio(true); // Mantiene la proporción de la imagen

            lblCharacter.setGraphic(imageView);
        } else {
            System.out.println("Resource not found: " + imagePath);
        }
    }

    public int getCharacterNumber() {
        return characterNumber;
    }

    @FXML
    private void StartGameButton(ActionEvent event) throws IOException {
        String playerName = txtPlayerName.getText();
        String itemName = txtItemName.getText();

        // Verificar si los campos de texto están llenos y si un personaje ha sido seleccionado
        if (playerName.isEmpty() || itemName.isEmpty() || characterNumber == 0) {
            // Mostrar advertencia al usuario
            showAlert("Make sure you fill in the required spaces.");
        } else {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Game.fxml"));
            Parent gameView = loader.load();

            // Obtener el controlador de la vista del juego
            GameController controller = loader.getController();
            controller.setItems(characterNumber,itemName,playerName); // Pasar el número del personaje
    
            // Crear un nuevo Stage para la vista del juego
            Stage gameStage = new Stage();
            gameStage.setTitle("Game");
            gameStage.setScene(new Scene(gameView, 800, 600)); // Tamaño preferido (ancho, alto)
            gameStage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            gameStage.setResizable(true);
            gameStage.initModality(Modality.NONE); // Permite que la ventana principal esté activa mientras se muestra la ventana del juego
            gameStage.show();

            // Cerrar la ventana actual (menú)
            Stage currentStage = (Stage) txtPlayerName.getScene().getWindow();
            currentStage.close();
        }
    }

    private void showAlert(String message) {
        try {
            // Cargar el archivo FXML para la ventana de advertencia
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AlertDialog.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana de advertencia
            AlertDialogController controller = loader.getController();
            controller.setMessage(message); // Pasar el mensaje de advertencia

            // Crear una nueva ventana (Stage) para la advertencia
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("MESSAGE");
            stage.getIcons().add(new Image(App.class.getResourceAsStream("/imagesGame/steve.png")));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal
            stage.showAndWait(); // Espera a que la ventana de advertencia se cierre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void AboutButton(ActionEvent event) {
        try {
            showStartMenu();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AboutUs.fxml"));
            Parent aboutUsView = loader.load();
            stackPane.getChildren().add(aboutUsView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerName(String playerName) {
        this.txtPlayerName.setText(playerName);
    }

    public void setItemName(String itemName) {
        this.txtItemName.setText(itemName);
    }

}
