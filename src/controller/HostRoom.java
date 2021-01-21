package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    public void leaveRoom(ActionEvent event) {
        Connection.getInstance().leaveRoom(roomInfo.getText());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/Lobby.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            Stage tempStage = (Stage) roomInfo.getScene().getWindow();
            tempStage.close();

            Lobby lobbyController = fxmlLoader.getController();
            lobbyController.initData(root, stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
