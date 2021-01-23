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
            try {
                Connection.getInstance().setNick(nick.getText());
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/Lobby.fxml"));
                Parent root = fxmlLoader.load();

                Stage tempStage = (Stage) emptyNick.getScene().getWindow();

                tempStage.setOnCloseRequest((WindowEvent we) -> {
                    Connection.getInstance().setThread(false);
                });

                Lobby lobbyController = fxmlLoader.getController();
                lobbyController.initData(root, tempStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
