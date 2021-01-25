package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Connection;


public class HostRoom {
    public static Stage hostRoomStage;

    @FXML
    private Text roomInfo;

    @FXML
    public ListView<String> players;

    @FXML
    public Button remove;

    @FXML
    public Text error;


    public void initData(Parent root, Stage stage, String serverName) {
        hostRoomStage = stage;
        hostRoomStage.setResizable(false);
        hostRoomStage.setOnCloseRequest((WindowEvent we) -> {
            Connection.getInstance().leaveHostRoom(roomInfo.getText());
            Connection.getInstance().setThread(false);
            Connection.getInstance().closeSocket();
        });
        Connection.getInstance().setHostRoom(this);
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

            Stage tempStage = (Stage) roomInfo.getScene().getWindow();

            Lobby lobbyController = fxmlLoader.getController();
            lobbyController.initData(root, tempStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeGuest(ActionEvent event) {
        System.out.println(players.getSelectionModel().getSelectedItem());
        if (players.getSelectionModel().getSelectedItem() == null) {
            error.setText("Nie wybrałeś użytkownika!");
        } else {
            if (Connection.getInstance().getHostNick().equals(Connection.getInstance().getNick()))
                if (!Connection.getInstance().getNick().equals(players.getSelectionModel().getSelectedItem()))
                    try {
                        String clientName = players.getSelectionModel().getSelectedItem();
                        Connection.getInstance().removeUser(roomInfo.getText(), clientName);
                        error.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else
                    error.setText("Nie możesz wyrzucić hosta!");
            else
                error.setText("Nie jesteś hostem!");
        }
    }

    public void leaveRoom() {
        if (!Connection.getInstance().getOtherPlayersInRoom().contains(Connection.getInstance().getNick()))
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/Lobby.fxml"));
                Parent root = fxmlLoader.load();

                Stage tempStage = (Stage) players.getScene().getWindow();
                Connection.getInstance().getOtherPlayersInRoom().clear();
                Lobby lobbyController = fxmlLoader.getController();
                lobbyController.initData(root, tempStage);
            } catch (Exception e) {

            }
    }
}
