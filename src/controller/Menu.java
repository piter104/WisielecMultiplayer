package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Connection;

public class Menu {
    @FXML
    private Text emptyNick;
    @FXML
    private TextField nick;

    public void pressButton(ActionEvent event) {
        if (nick.getText().length() == 0) {
            emptyNick.setText("Nie podałeś nicku");
        } else {
            Connection.getInstance().setNick(nick.getText(), this);
        }
    }

    public void nameError() {
        Stage stage = (Stage) emptyNick.getScene().getWindow();
        stage.setOnCloseRequest((WindowEvent we) -> {
            Connection.getInstance().leaveLobby();
            Connection.getInstance().getThread().set(false);
            Connection.getInstance().closeSocket();
        });
            emptyNick.setText("Nazwa użytkownika już istnieje!");
    }

    public void enterLobby() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/Lobby.fxml"));
            Parent root = fxmlLoader.load();

            Stage tempStage = (Stage) emptyNick.getScene().getWindow();

            Lobby lobbyController = fxmlLoader.getController();
            lobbyController.initData(root, tempStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
