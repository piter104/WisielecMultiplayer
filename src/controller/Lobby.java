package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Connection;

public class Lobby {

    Stage lobbyStage;


    @FXML
    private ListView<String> serverList;

    @FXML
    private TextField newRoom;

    @FXML
    private Text emptyWarning;

    @FXML
    private Text noRoomChosen;

    public void initData(Parent root, Stage stage) {
        this.lobbyStage = stage;
        lobbyStage.setResizable(false);
        lobbyStage.setOnCloseRequest((WindowEvent we) -> {
            Connection.getInstance().leaveLobby();
            Connection.getInstance().setThread(false);
            Connection.getInstance().closeSocket();
        });
        Connection.getInstance().setLobby(this);
        serverList.setItems(Connection.getInstance().getRooms());
        lobbyStage.setScene(new Scene(root));
        lobbyStage.show();
    }

    public void updateServerList() {
        serverList.setItems(Connection.getInstance().getRooms());
    }

    public void pressButton(ActionEvent event) {
        if (newRoom.getText().isEmpty()) {
            emptyWarning.setText("Nie podałeś nazwy pokoju!");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/HostRoom.fxml"));
                Parent root = fxmlLoader.load();

                Connection.getInstance().createRoom(newRoom.getText());

                Stage tempStage = (Stage) serverList.getScene().getWindow();

                HostRoom hostRoomController = fxmlLoader.getController();
                hostRoomController.initData(root, tempStage, newRoom.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void chooseServerButton(ActionEvent event) {
        if (serverList.getSelectionModel().getSelectedItem() == null) {
            noRoomChosen.setText("Nie wybrałeś pokoju!");
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/HostRoom.fxml"));
                Parent root = fxmlLoader.load();
                System.out.println(serverList.getSelectionModel().getSelectedItem());
                String serverName = serverList.getSelectionModel().getSelectedItem();

                Stage tempStage = (Stage) serverList.getScene().getWindow();

                Connection.getInstance().joinRoom(serverName);

                HostRoom hostRoomController = fxmlLoader.getController();
                hostRoomController.initData(root, tempStage, serverName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
