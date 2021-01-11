package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Connection;


public class HostRoom {
    public static Stage hostRoomStage;

    @FXML
    private Text roomInfo;

    @FXML
    public ListView<String> players;


    public void initData(Parent root, Stage stage, String serverName) {
        hostRoomStage = stage;
        roomInfo.setText(serverName);
        players.setItems(Connection.getInstance().getOtherPlayersInRoom());
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void pressButton(ActionEvent event) {
        Connection.getInstance().startGame(roomInfo.getText());
    }
}
