package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.Drawing;

public class Game {
    Drawing drawing = new Drawing();
    @FXML
    private Text lettersOut;
    @FXML
    private TextField lettersIn;
    private int number = 0;
    private String letters = "";
    private String word = "Konczak";

    public void enterLetter(ActionEvent e) {
        if (lettersIn.getText().length() == 1) {
            if (letters.length() != 0) {
                letters = letters + ", " + lettersIn.getText();
            } else {
                letters = lettersIn.getText();
            }
            if (word.indexOf(lettersIn.getText()) == -1) {
                if (number < 10) {
                    drawing.draw(number);
                    number++;
                } else {
                    //koniec gry
                }
            }
        }
        lettersIn.clear();
        lettersOut.setText(letters);
    }
}
