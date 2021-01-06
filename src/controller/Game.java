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
import sample.Connection;
import sample.Drawing;
import sample.response.Response;

import java.util.List;

public class Game {
    @FXML
    private Text lettersOut;
    @FXML
    private TextField lettersIn;

    private int number = 0;
    private String letters = "";
    private final String word = "";
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

        // trzeba dopisać jeszcze kreski hasła

        stage.setScene(new Scene(group));
        stage.show();
    }

    public void enterLetter(ActionEvent event) {
        if (lettersIn.getText().length() == 1) {
            Response response = Connection.getInstance().guessLetter(lettersIn.getText());
            letters = letters.isBlank() ? lettersIn.getText() : letters + ", " + lettersIn.getText();
            List<Integer> letterPositions = response.letterPositions;
            if (letterPositions.isEmpty()) {
                if (number < 10) {
                    drawing.draw(number);
                    number++;
                } else {
                    alert.showAndWait();
                }
            } else {
                //TODO dodać literkę na odpowiednich miejsach (wskasanych w letterPositions) na kreskach
            }

            if (response.gameFinished) {
                //TODO ALERT WYGRAłes
                alert.showAndWait();
            }
        }
        lettersIn.clear();
        lettersOut.setText(letters);
    }
}
