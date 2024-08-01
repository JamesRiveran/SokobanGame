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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        loadDefaultCharacterImage();
    }

    @FXML
    private void ChooseCharacterButton(ActionEvent event) {
        try {
            // Guarda los datos actuales
            String playerName = txtPlayerName.getText();
            String itemName = txtItemName.getText();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChooseCharacterView.fxml"));
            Parent root = loader.load();

            // Obtén el controlador de la nueva vista y pasa los datos
            ChooseCharacterViewController controller = loader.getController();
            controller.initData(playerName, itemName);

            Scene scene = new Scene(root, 840, 640);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void LoadGameButton(ActionEvent event) {
    }

    @FXML
    private void StartGameButton(ActionEvent event) {
        String playerName = txtPlayerName.getText();
        String itemName = txtItemName.getText();

        // Verificar si los campos de texto están llenos y si un personaje ha sido seleccionado
        if (playerName.isEmpty() || itemName.isEmpty() || characterNumber == 0) {
            // Mostrar advertencia al usuario
            showAlert("Advertencia", "Debes ingresar el nombre del jugador, el nombre de la partida y seleccionar un personaje antes de iniciar el juego.");
        } else {
            // Lógica para iniciar el juego si todos los campos están llenos y un personaje está seleccionado
            // Por ejemplo, cambiar de escena o inicializar el juego
            System.out.println("Iniciando juego con: ");
            System.out.println("Nombre del jugador: " + playerName);
            System.out.println("Nombre de la partida: " + itemName);
            System.out.println("Número del personaje: " + characterNumber);
            // Aquí iría el código para iniciar el juego
        }
    }

// Método para mostrar la alerta
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void AboutButton(ActionEvent event) {
    }

    @FXML
    private void CreateLevelButton(ActionEvent event) {
    }

    public void setCharacter(String characterName, int characterNumber) {
        this.characterNumber = characterNumber;
        System.out.println(characterNumber);

        String imagePath = "/images/" + characterName.toLowerCase() + ".png";
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

    public void setPlayerName(String playerName) {
        this.txtPlayerName.setText(playerName);
    }

    public void setItemName(String itemName) {
        this.txtItemName.setText(itemName);
    }

}
