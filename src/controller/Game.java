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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    @FXML
    private Text lettersOut;
    @FXML
    private TextField lettersIn;
    @FXML
    private Text password;

    private int number = 0;
    private String letters = "";
    private final String word = "";
    char[] tempPassword;

    Group group;
    Stage stage;
    Drawing drawing = new Drawing();
    Circle circle = drawing.getCircle();
    PathTransition pathTransition = new PathTransition();
    Alert alertLose = new Alert(Alert.AlertType.INFORMATION);
    Alert alertWin = new Alert(Alert.AlertType.INFORMATION);


    public void initData(Parent gameRoot, Stage gameStage, Integer howLongIsTheWord) {
        stage = gameStage;
        group = new Group(gameRoot);

        alertLose.setTitle("Koniec Gry");
        alertLose.setHeaderText(null);
        alertLose.setContentText("Przegrałeś kolego");

        alertWin.setTitle("Gratulacje");
        alertWin.setHeaderText(null);
        alertWin.setContentText("Wygrałeś kolego");

        pathTransition.setPath(drawing.getPath());
        group.getChildren().add(drawing.getPath());

        String repeater = IntStream
                .range(0, howLongIsTheWord).mapToObj(i -> "_")
                .collect(Collectors.joining(" "));

        password.setText(repeater);

        stage.setScene(new Scene(group));
        stage.show();
    }

    public void enterLetter(ActionEvent event) {
        //TODO liczenie błędów na serwerze
        if (number < 10) {
            String guessedLetter = lettersIn.getText();
            if (guessedLetter.length() == 1) {
                Response response = Connection.getInstance().guessLetter(guessedLetter);
                letters = letters.isBlank() ? guessedLetter : letters + ", " + guessedLetter;
                List<Integer> letterPositions = response.letterPositions;
                if (letterPositions.isEmpty()) {
                    drawing.draw(number);
                    if (circle != drawing.getCircle())
                        group.getChildren().add(drawing.getCircle());
                    number++;
                } else {
                    tempPassword = password.getText().toCharArray();
                    for (int i : letterPositions)
                        tempPassword[2 * i] = guessedLetter.toCharArray()[0];
                    password.setText(String.valueOf(tempPassword));
                }

                if (response.gameFinished) {
                    if (!password.getText().contains("_"))
                        alertWin.showAndWait();
                }
            }
            lettersIn.clear();
            lettersOut.setText(letters);
        } else
            alertLose.showAndWait();
    }
}
