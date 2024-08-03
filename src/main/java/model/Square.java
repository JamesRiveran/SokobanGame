/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 *
 * @author marco
 */
public class Square {

    Button buttonSquare;
    int type;

    public Square(int type) {
        buttonSquare=setButtonConfigs(buttonSquare, type);
        this.type = type;

    }

    public Button setButtonConfigs(Button btnAux, int numType) {
        btnAux = new Button();
        //btnAux.setBackground(new Background(new BackgroundImage(new Image("/imagesGame/" + numType + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        btnAux.setBackground(Background.EMPTY);
        btnAux.setPrefWidth(Double.MAX_VALUE);
        btnAux.setPrefHeight(Double.MAX_VALUE);
        btnAux.setText("x: "+type);
        return btnAux;
    }
    
//    public void setSquareImage(int ){
//    
//    }

    public Button getButtonSquare() {
        return buttonSquare;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        buttonSquare.setBackground(new Background(new BackgroundImage(new Image("/imagesGame/" + type + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false))));
        this.type = type;
    }

}
