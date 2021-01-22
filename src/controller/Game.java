package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Connection;


public class Game {
    @FXML
    private Text lettersOut;
    @FXML
    private TextField lettersIn;
    @FXML
    private Text password;

    private String roomName;

    private String letters = "";

    Integer number = 0;

    Group group;
    Stage stage;


    public void initData(Group group, Stage gameStage, String roomName) {
        stage = gameStage;
        this.group = group;
        this.roomName = roomName;

        stage.setScene(new Scene(group));
        stage.show();
    }

    public Text getPassword() {
        return password;
    }

    public void enterLetter(ActionEvent event) {
        if (number < 10) {
            String guessedLetter = lettersIn.getText();
            if (guessedLetter.length() == 1) {
                letters = letters.isBlank() ? guessedLetter : letters + ", " + guessedLetter;
                Connection.getInstance().guessLetter(guessedLetter, roomName);
            }
        }
        lettersIn.clear();
        lettersOut.setText(letters);
    }

    public void endGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/Lobby.fxml"));
            Parent root = fxmlLoader.load();

            Stage tempStage = (Stage) password.getScene().getWindow();

            Lobby lobbyController = fxmlLoader.getController();
            lobbyController.initData(root, tempStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
