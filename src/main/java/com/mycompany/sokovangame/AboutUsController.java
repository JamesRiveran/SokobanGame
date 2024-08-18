/*
 * Click nbfs:
 * Click nbfs:
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
        
    }    
    private void switchToStartMenu(String characterName, int characterNumber) {
       
        startMenuController.showStartMenu();
    }
    
}
