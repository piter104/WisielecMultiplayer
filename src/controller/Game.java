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

import java.util.ArrayList;
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

    private Integer number = 0;
    //przesuniecie wisielcow
    private Integer xMove = 150;
    private Integer yMove = 150;
    private String letters = "";
    private final String word = "";
    char[] tempPassword;
    // liczba graczy
    int playerNumber = 4;

    Group group;
    Stage stage;
    List<Drawing> hangManList = new ArrayList<>();
    List<Text> textList = new ArrayList<>();
    List<Circle> headList = new ArrayList<>();
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

        for (int i = 0; i < playerNumber; i++) {
            textList.add(new Text());
            hangManList.add(new Drawing(xMove * i, yMove));
            textList.get(i).setText("nick gracza: " + i);
            textList.get(i).setX(hangManList.get(i).getX() + xMove * i);
            textList.get(i).setY(hangManList.get(i).getY() + yMove + 40);
        }
        String repeater = IntStream
                .range(0, howLongIsTheWord).mapToObj(i -> "_")
                .collect(Collectors.joining(" "));

        for (int i = 0; i < playerNumber; i++) {
            pathTransition.setPath(hangManList.get(i).getPath());
            headList.add(hangManList.get(i).getCircle());
            group.getChildren().add(hangManList.get(i).getPath());
            group.getChildren().add(hangManList.get(i).getCircle());
            group.getChildren().add(textList.get(i));
        }

        password.setText(repeater);

        stage.setScene(new Scene(group));
        stage.show();
    }

    public void enterLetter(ActionEvent event) {
        //TODO liczenie błędów na serwerze
        if (number < 10) {
            String guessedLetter = lettersIn.getText();
            if (guessedLetter.length() == 1) {
                Connection.getInstance().guessLetter(guessedLetter);
                letters = letters.isBlank() ? guessedLetter : letters + ", " + guessedLetter;
                //TODO::: NAPRAWAWAWAW
                List<Integer> letterPositions = List.of();
                if (letterPositions.isEmpty()) {
                    for (int i = 0; i < playerNumber; i++) {
                        hangManList.get(i).draw(number);
                        if (headList.get(i) != hangManList.get(i).getCircle())
                            group.getChildren().add(hangManList.get(i).getCircle());
                    }
                    number++;
                } else {
                    tempPassword = password.getText().toCharArray();
                    for (int i : letterPositions)
                        tempPassword[2 * i] = guessedLetter.toCharArray()[0];
                    password.setText(String.valueOf(tempPassword));
                }
// TODO:
//                if (response.gameFinished) {
//                    if (!password.getText().contains("_"))
//                        alertWin.showAndWait();
//                }
            }
            lettersIn.clear();
            lettersOut.setText(letters);
        } else
            alertLose.showAndWait();
    }
}
