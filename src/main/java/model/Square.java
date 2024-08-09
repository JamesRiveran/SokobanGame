package model;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class Square {
    private Button buttonSquare;
    private int type;

    public Square(int type) {
        this.type = type;
        this.buttonSquare = new Button();
        buttonSquare.setText(this.type+"");
        updateButtonVisuals();
    }

    private void updateButtonVisuals() {
        String imagePath = "/imagesGame/" + type + ".png";
        try {
            Image image = new Image(imagePath);
            buttonSquare.setBackground(new Background(new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, false, false, true, false)
            )));
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage()); // Depuración
        }
        buttonSquare.setPrefSize(100, 100);
        buttonSquare.setFocusTraversable(false); // Configura los botones para que no tomen el enfoque

    }

    public Button getButtonSquare() {
        return buttonSquare;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        this.buttonSquare.setText(type+"");
        updateButtonVisuals();
    }
}