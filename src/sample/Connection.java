package sample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Game;
import controller.HostRoom;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.request.Request;
import sample.response.Response;
import sample.response.Room;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Connection {

    private static Connection instance;
    private Socket clientSocket;
    private String nick;
    private Integer howLongIsTheWord;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectMapper mapper;
    private ObservableList<String> rooms = FXCollections.observableArrayList();
    private Optional<Room> chosenRoom;
    private ObservableList<String> otherPlayersInRoom = FXCollections.observableArrayList();

    private Connection() throws IOException {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        String host = "127.0.0.1";
        int port = 1111;
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        clientSocket = new Socket();
        int timeOut = 10000;
        clientSocket.connect(socketAddress, timeOut);
        try {

            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            clientSocket.close();
        } catch (Exception e) {

        }

        System.out.println("Connection established");

        Thread responseReader = new Thread(() -> {
            while (true) {
                readMessage().ifPresent(this::dispatchResponse);
            }
        });
        responseReader.start();
    }

    public static Connection getInstance() {
        if (instance == null) {
            try {
                instance = new Connection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public ObservableList<String> getRooms() {
        return rooms;
    }

    public Integer getHowLongIsTheWord() {
        return howLongIsTheWord;
    }

    public void setNick(String text) {
        nick = text;
        Request request = new Request();
        request.nick = text;
        request.type = Request.RequestType.LOGIN;
        sendRequest(request);
    }

    public void joinRoom(String roomName) {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.JOIN_ROOM;
        sendRequest(request);
    }

    private void sendRequest(Request request) {
        System.out.printf("Sending request: %s \n", request.toString());
        try {
            byte[] bytes = mapper.writeValueAsBytes(request);
            outputStream.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<Response> readMessage() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            length = inputStream.read(buffer);
            if (length == -1) {
                return Optional.empty();
            }
            result.write(buffer, 0, length);
            Response response = mapper.readValue(result.toByteArray(), Response.class);
            return Optional.of(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    public void startGame() {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.START_GAME;
        sendRequest(request); //TODO:REMOVE
    }

    public void guessLetter(String letter) {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.SEND_LETTER;
        request.letter = letter;
        sendRequest(request);
    }


    private void dispatchResponse(Response response) {
        switch (response.type) {
            case USER_AUTHENTICATED:
                if (response.rooms != null) {
                    List<String> updateRooms = response.rooms.stream().map(Room::getRoomName).collect(Collectors.toList());
                    rooms.addAll(updateRooms);
                    System.out.printf("Serverd responded with: %s \n", response.toString());
                }

                break;
            case USER_JOINED_ROOM:
                System.out.println(response.toString());
                Platform.runLater(() -> otherPlayersInRoom.setAll(response.otherPlayersInRoom));
                break;
            case GAME_STARTED:
                this.howLongIsTheWord = response.howLongIsTheWord;
                Platform.runLater(this::openGame);
                break;
            case LETTER_RECEIVED:
                break;
        }
    }

    public ObservableList<String> getOtherPlayersInRoom() {
        return otherPlayersInRoom;
    }
    @FXML
    public ListView<String> players;

    private void openGame() {

        try {


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/GamePane.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            Stage window = (Stage)HostRoom.hostRoomStage.getScene().getWindow();
            window.close();
            Integer howLongIsTheWord = Connection.getInstance().getHowLongIsTheWord();
            Game gameController = fxmlLoader.getController();
            gameController.initData(root, stage, howLongIsTheWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
