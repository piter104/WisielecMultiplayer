package controller;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Drawing;

public class Game {
    @FXML
    private Text lettersOut;
    @FXML
    private TextField lettersIn;

    private int number = 0;
    private String letters = "";
    private final String word = "Konczak";
    Circle circle;

    Group group;
    Stage stage;
    Drawing drawing = new Drawing();
    PathTransition pathTransition = new PathTransition();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);


    public void initData(Parent gameRoot, Stage gameStage) {
        stage = gameStage;
        group = new Group(gameRoot);

        alert.setTitle("Koniec Gry");
        alert.setHeaderText(null);
        alert.setContentText("Przegrałeś kolego");

        pathTransition.setPath(drawing.getPath());
        group.getChildren().add(drawing.getPath());
        group.getChildren().add(drawing.getCircle());

        stage.setScene(new Scene(group));
        stage.show();
    }

    public void enterLetter(ActionEvent event) {
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
                    alert.showAndWait();
                }
            }
        }
        lettersIn.clear();
        lettersOut.setText(letters);
    }
}
