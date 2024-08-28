/*
 * Click nbfs:
 * Click nbfs:
 */
package com.mycompany.sokovangame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class AlertDialogController implements Initializable {

    @FXML
    private Label lblAbvertens;
    @FXML
    private Label txtTexts;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setMessage(String message) {
        txtTexts.setText(message);
    }

    private void closeButton(ActionEvent event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void AcceptButton(ActionEvent event) {

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
