package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.Connection;
import sample.response.Response;

public class HostRoom {
    Stage hostRoomStage;
    private ObservableList<String> items;

    @FXML
    private Text roomInfo;

    @FXML
    private ListView<String> players;


    public void initData(Parent root, Stage stage, String serverName) {
        this.hostRoomStage = stage;
        roomInfo.setText(serverName);
        items = FXCollections.observableArrayList(
                "host", "ty");
        players.setItems(items);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void pressButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/GamePane.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            Response response = Connection.getInstance().startGame();
            Stage tempStage = (Stage) players.getScene().getWindow();
            tempStage.close();

            Game gameController = fxmlLoader.getController();
            gameController.initData(root, stage, response.howLongIsTheWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
