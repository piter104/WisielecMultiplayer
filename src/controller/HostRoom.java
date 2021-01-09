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
    Stage hostRoomStage;

    @FXML
    private Text roomInfo;

    @FXML
    private ListView<String> players;


    public void initData(Parent root, Stage stage, String serverName) {
        this.hostRoomStage = stage;
        roomInfo.setText(serverName);
        players.setItems(Connection.getInstance().getOtherPlayersInRoom());
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void pressButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/GamePane.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Connection.getInstance().startGame();
            Stage tempStage = (Stage) players.getScene().getWindow();
            tempStage.close();

            Integer howLongIsTheWord = Connection.getInstance().getHowLongIsTheWord();
            Game gameController = fxmlLoader.getController();
            gameController.initData(root, stage, howLongIsTheWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
