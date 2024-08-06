/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.sokovangame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class AboutUsController implements Initializable {

    /**
     * Initializes the controller class.
     */
     private StartMenuViewController startMenuController;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    private void switchToStartMenu(String characterName, int characterNumber) {
       
        startMenuController.showStartMenu();
    }
    
}
