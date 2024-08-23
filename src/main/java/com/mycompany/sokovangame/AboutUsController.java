package com.mycompany.sokovangame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;

public class AboutUsController implements Initializable {

    private StartMenuViewController startMenuController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesario
    }

    public void setStartMenuController(StartMenuViewController startMenuController) {
        this.startMenuController = startMenuController;
    }

    @FXML
    private void returnToStartMenu(ActionEvent event) {
        if (startMenuController != null) {
            startMenuController.showStartMenu();
        }
    }
}
