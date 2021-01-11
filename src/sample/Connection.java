package sample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Game;
import controller.HostRoom;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private List<Integer> letterPositions;
    Map<String, Integer> mistakesCounter;
    Alert alertLose = new Alert(Alert.AlertType.INFORMATION);
    Alert alertWin = new Alert(Alert.AlertType.INFORMATION);

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
        request.roomName = roomName;
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


    public void startGame(String roomName) {
        Request request = new Request();
        request.roomName = roomName;
        request.nick = nick;
        request.type = Request.RequestType.START_GAME;
        sendRequest(request);
    }

    public void guessLetter(String letter, String roomName) {
        Request request = new Request();
        request.roomName = roomName;
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
                    System.out.printf("Server responded with: %s \n", response.toString());
                }
                break;
            case USER_JOINED_ROOM:
                System.out.println(response.toString());

                Platform.runLater(() -> otherPlayersInRoom.setAll(response.otherPlayersInRoom));
                break;
            case GAME_STARTED:
                this.howLongIsTheWord = response.howLongIsTheWord;
                Platform.runLater(() -> openGame(response.roomName));
                break;
            case LETTER_RECEIVED:
                this.letterPositions = response.letterPositions;
                Platform.runLater(() -> handleLetter(response));
                break;
            case SOMEBODY_GUESSED_WRONG:
                Platform.runLater(() -> updateOthersHangmen(response));
                break;
            case GAME_FINISHED:
                Platform.runLater(() -> printWinAlert(response));
                break;
            case YOU_LOST:
                Platform.runLater(() -> printLoseAlert(response));
                break;
        }
    }

    private void updateOthersHangmen(Response response) {
        mistakesCounter = response.userWrongCounterMap;
        for (int i = 0; i < otherPlayersInRoom.size(); i++) {
            hangManList.get(i).draw(response.userWrongCounterMap.get(otherPlayersInRoom.get(i)));
            if (headList.get(i) != hangManList.get(i).getCircle())
                group.getChildren().add(hangManList.get(i).getCircle());
        }
        //gameController.updateMistakesNumber(number);
    }

    public ObservableList<String> getOtherPlayersInRoom() {
        return otherPlayersInRoom;
    }

    @FXML
    public ListView<String> players;


    Game gameController;

    //przesuniecie wisielcow
    private Integer xMove = 150;
    private Integer yMove = 150;

    Group group;

    private void openGame(String roomName) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/GamePane.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            Stage window = (Stage) HostRoom.hostRoomStage.getScene().getWindow();
            window.close();
            Integer howLongIsTheWord = Connection.getInstance().getHowLongIsTheWord();
            gameController = fxmlLoader.getController();

            group = new Group(root);

            Integer playerNumber = otherPlayersInRoom.size();

            for (int i = 0; i < playerNumber; i++) {
                textList.add(new Text());
                hangManList.add(new Drawing(xMove * i, yMove));
                textList.get(i).setText(otherPlayersInRoom.get(i));
                textList.get(i).setX(hangManList.get(i).getX() + xMove * i);
                textList.get(i).setY(hangManList.get(i).getY() + yMove + 40);
            }

            for (int i = 0; i < playerNumber; i++) {
                pathTransition.setPath(hangManList.get(i).getPath());
                headList.add(hangManList.get(i).getCircle());
                group.getChildren().add(hangManList.get(i).getPath());
                group.getChildren().add(hangManList.get(i).getCircle());
                group.getChildren().add(textList.get(i));
            }

            String repeater = IntStream
                    .range(0, howLongIsTheWord).mapToObj(i -> "_")
                    .collect(Collectors.joining(" "));

            gameController.getPassword().setText(repeater);

            gameController.initData(group, stage, roomName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<Drawing> hangManList = new ArrayList<>();
    List<Text> textList = new ArrayList<>();
    List<Circle> headList = new ArrayList<>();
    PathTransition pathTransition = new PathTransition();

    char[] tempPassword;

    private void handleLetter(Response response) {
        if (!letterPositions.isEmpty()) {
            tempPassword = gameController.getPassword().getText().toCharArray();
            for (int i : letterPositions)
                tempPassword[2 * i] = response.letterGuessed.toCharArray()[0];
            gameController.getPassword().setText(String.valueOf(tempPassword));
        }
    }

    private void printWinAlert(Response response) {
        alertWin.setTitle("Koniec Gry");
        alertWin.setHeaderText(null);
        alertWin.setContentText("Wygrał użytkownik: " + response.winner);
        alertWin.showAndWait();
    }

    private void printLoseAlert(Response response) {
        alertLose.setTitle("Koniec Gry");
        alertLose.setHeaderText(null);
        alertLose.setContentText("Przegrałeś: " + response.loser);
        alertLose.showAndWait();
    }
}
