package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.Connection;

import java.util.List;

public class Lobby {

    Stage lobbyStage;

    private ObservableList<String> items;

    @FXML
    private ListView<String> serverList;

    public void initData(Parent root, Stage stage) {
        this.lobbyStage = stage;
        items = FXCollections.observableArrayList( Connection.getInstance().getRooms());
        serverList.setItems(items);
        lobbyStage.setScene(new Scene(root));
        lobbyStage.show();
    }

    public void pressButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/HostRoom.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            Stage tempStage = (Stage) serverList.getScene().getWindow();
            tempStage.close();
            Connection.getInstance().joinRoom("TODO:ZMIENIC");
            String serverName = "utwórz tworzenie hosta z nazwa serwera";
            HostRoom hostRoomController = fxmlLoader.getController();
            hostRoomController.initData(root, stage, serverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseServerButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/HostRoom.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();

            String serverName = serverList.getSelectionModel().getSelectedItem();

            Stage tempStage = (Stage) serverList.getScene().getWindow();
            tempStage.close();

            HostRoom hostRoomController = fxmlLoader.getController();
            hostRoomController.initData(root, stage, serverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
