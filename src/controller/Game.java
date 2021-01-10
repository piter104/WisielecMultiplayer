package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Connection;

import java.util.ArrayList;
import java.util.List;

public class Game {
    @FXML
    private Text lettersOut;
    @FXML
    private TextField lettersIn;
    @FXML
    private Text password;


    private String letters = "";

    Integer number = 0;

    Group group;
    Stage stage;
    List<Integer> letterPositions = new ArrayList<>();
    List<Integer> mistakesNumber = new ArrayList<>();
    Alert alertLose = new Alert(Alert.AlertType.INFORMATION);
    Alert alertWin = new Alert(Alert.AlertType.INFORMATION);


    public void initData(Group group, Stage gameStage) {
        stage = gameStage;
        this.group = group;

        alertLose.setTitle("Koniec Gry");
        alertLose.setHeaderText(null);
        alertLose.setContentText("Przegrałeś kolego");

        alertWin.setTitle("Gratulacje");
        alertWin.setHeaderText(null);
        alertWin.setContentText("Wygrałeś kolego");

        stage.setScene(new Scene(group));
        stage.show();
    }

    public void updateMistakesNumber(Integer mistakesNumber) {
        this.number = mistakesNumber;
    }

    public Text getPassword() {
        return password;
    }

    public void enterLetter(ActionEvent event) {
        if (number < 10) {
            String guessedLetter = lettersIn.getText();
            letters = letters.isBlank() ? guessedLetter : letters + ", " + guessedLetter;
            if (guessedLetter.length() == 1) {
                Connection.getInstance().guessLetter(guessedLetter);
            }

        } else
            alertLose.showAndWait();
        lettersIn.clear();
        lettersOut.setText(letters);
    }
}
