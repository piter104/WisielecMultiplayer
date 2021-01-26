package sample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Game;
import controller.HostRoom;
import controller.Lobby;
import controller.Menu;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
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
    private Boolean thread = true;
    private String nick;
    private Integer howLongIsTheWord;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectMapper mapper;
    private ObservableList<String> rooms = FXCollections.observableArrayList();
    private ObservableList<String> otherPlayersInRoom = FXCollections.observableArrayList();
    private Menu menu;
    private Lobby lobby;
    private HostRoom hostRoom;
    private List<Integer> letterPositions;
    private String hostNick;
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
            while (thread) {
                readMessage().ifPresent(this::dispatchResponse);
            }
        });
        responseReader.start();
    }

    public void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void setThread(Boolean thread) {
        this.thread = thread;
    }

    public String getHostNick() {
        return hostNick;
    }

    public void setHostNick(String hostNick) {
        this.hostNick = hostNick;
    }

    public ObservableList<String> getRooms() {
        return rooms;
    }

    public Integer getHowLongIsTheWord() {
        return howLongIsTheWord;
    }

    public void setNick(String text, Menu menu) {
        this.nick = text;
        this.menu = menu;
        Request request = new Request();
        request.nick = text;
        request.type = Request.RequestType.LOGIN;
        sendRequest(request);
    }

    public void setLobby(Lobby lobbyController) {
        lobby = lobbyController;
    }

    public void setHostRoom(HostRoom hostRoom) {
        this.hostRoom = hostRoom;
    }

    public String getNick() {
        return nick;
    }

    public void joinRoom(String roomName) {
        Request request = new Request();
        request.roomName = roomName;
        request.nick = nick;
        request.type = Request.RequestType.JOIN_ROOM;
        sendRequest(request);
    }

    public void createRoom(String roomName) {
        setHostNick(nick);
        Request request = new Request();
        request.roomName = roomName;
        request.nick = nick;
        request.type = Request.RequestType.CREATE_ROOM;
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

    public void leaveRoom(String roomName) {
        otherPlayersInRoom.clear();
        setHostNick("");
        Request request = new Request();
        request.roomName = roomName;
        request.nick = nick;
        request.type = Request.RequestType.LEAVE_ROOM;
        sendRequest(request);
    }

    public void removeUser(String roomName, String userName) {
        Request request = new Request();
        request.roomName = roomName;
        request.nick = userName;
        request.type = Request.RequestType.LEAVE_ROOM;
        sendRequest(request);
    }

    public void leaveGame(String roomName) {
        Request request = new Request();
        request.roomName = roomName;
        request.nick = nick;
        request.type = Request.RequestType.LEAVE_GAME;
        sendRequest(request);
    }

    public void leaveLobby() {
        Request request = new Request();
        request.nick = nick;
        request.type = Request.RequestType.LEAVE_LOBBY;
        sendRequest(request);
    }

    public void leaveHostRoom(String roomName) {
        Request request = new Request();
        request.roomName = roomName;
        request.nick = nick;
        request.type = Request.RequestType.LEAVE_HOSTROOM;
        sendRequest(request);
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
        System.out.printf("Server responded with: %s \n", response.toString());

        switch (response.type) {
            case USER_AUTHENTICATED:
                if (response.rooms != null) {
                    List<String> updateRooms = response.rooms.stream().filter(room -> !room.getMaxPlayers()).map(Room::getRoomName).collect(Collectors.toList());
                    rooms.setAll(updateRooms);
                }
                if (response.nameOk)
                    Platform.runLater(() -> menu.enterLobby());
                else {
                    Platform.runLater(() -> menu.nameError());
                    this.nick = "";
                }
                break;
            case USER_JOINED_CREATED_ROOM:
                Platform.runLater(() -> {
                    otherPlayersInRoom.setAll(response.otherPlayersInRoom);
                    lobby.enterHostRoom();
                });
                response.otherPlayersInRoom.stream().findFirst().ifPresent(this::setHostNick);
                Platform.runLater(() -> hostRoom.hostMessage());
                break;
            case USER_JOINED_ROOM:
                Platform.runLater(() -> {
                    otherPlayersInRoom.setAll(response.otherPlayersInRoom);
                });
                response.otherPlayersInRoom.stream().findFirst().ifPresent(this::setHostNick);
                if (hostNick == nick)
                    Platform.runLater(() -> hostRoom.hostMessage());
                break;
            case USER_LEFT_ROOM:
                Platform.runLater(() -> {
                    response.otherPlayersInRoom.stream().findFirst().ifPresent(this::setHostNick);
                    otherPlayersInRoom.setAll(response.otherPlayersInRoom);
                    if (response.rooms != null) {
                        List<String> updateRooms = response.rooms.stream().filter(room -> !room.getMaxPlayers()).map(Room::getRoomName).collect(Collectors.toList());
                        rooms.setAll(updateRooms);
                    }
                    hostRoom.leaveRoom();
                });
                break;
            case USER_LEFT_GAME:
                break;
            case ROOM_CREATED:
                List<String> updateRooms = response.rooms.stream().filter(room -> !room.getMaxPlayers()).map(Room::getRoomName).collect(Collectors.toList());
                if (response.nameOk) {
                    Platform.runLater(() -> {
                        rooms.setAll(updateRooms);
                        lobby.updateServerList();
                    });
                } else {
                    Platform.runLater(() -> {
                        lobby.nameError();
                    });
                }
                break;
            case SERVERS_INFO:
                updateRooms = response.rooms.stream().filter(room -> !room.getMaxPlayers()).map(Room::getRoomName).collect(Collectors.toList());
                Platform.runLater(() -> {
                    rooms.setAll(updateRooms);
                    lobby.updateServerList();
                });
                break;
            case GAME_STARTED:
                this.howLongIsTheWord = response.howLongIsTheWord;
                Platform.runLater(() -> openGame(response.roomName));
                break;
            case BLOCK_ROOM:
                updateRooms = response.rooms.stream().filter(room -> !room.getMaxPlayers()).map(Room::getRoomName).collect(Collectors.toList());
                Platform.runLater(() -> {
                    rooms.setAll(updateRooms);
                    lobby.updateServerList();
                });
                break;
            case LETTER_RECEIVED:
                this.letterPositions = response.letterPositions;
                Platform.runLater(() -> handleLetter(response));
                break;
            case SOMEBODY_GUESSED_WRONG:
                Platform.runLater(() -> updateOthersHangmen(response));
                break;
            case GAME_FINISHED:
                Platform.runLater(() -> {
                    printEndWinAlert(response);
                    gameController.endGame();
                    clearAfterGame();
                });
                break;
            case GAME_LOST:
                Platform.runLater(() -> {
                    if(response.timeRunOut) {
                        printEndTimeRunOutAlert(response);
                    }
                    else {
                        printEndLostAlert(response);
                    }
                    gameController.endGame();
                    clearAfterGame();
                });
                break;
            case YOU_LOST:
                Platform.runLater(() -> printLoseAlert(response));
                break;
        }
    }

    private void clearAfterGame() {
        try {
            mistakesCounter.clear();
        } catch (Exception e) {
        }
        hangManList.clear();
        headList.clear();
        group.getChildren().clear();
        otherPlayersInRoom.clear();
    }

    private void updateOthersHangmen(Response response) {
        mistakesCounter = response.userWrongCounterMap;
        for (int i = 0; i < otherPlayersInRoom.size(); i++) {
            hangManList.get(i).draw(response.userWrongCounterMap.get(otherPlayersInRoom.get(i)));
            if (headList.get(i) != hangManList.get(i).getCircle())
                group.getChildren().add(hangManList.get(i).getCircle());
        }
    }

    public ObservableList<String> getOtherPlayersInRoom() {
        return otherPlayersInRoom;
    }

    Game gameController;

    //przesuniecie wisielcow
    private Integer xMove = 150;
    private Integer yMove = 150;

    Group group;

    private void openGame(String roomName) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxmlFiles/GamePane.fxml"));
            Parent root = fxmlLoader.load();

            Stage tempStage = (Stage) HostRoom.hostRoomStage.getScene().getWindow();

            Integer howLongIsTheWord = Connection.getInstance().getHowLongIsTheWord();
            gameController = fxmlLoader.getController();

            group = new Group(root);

            Integer playerNumber = otherPlayersInRoom.size();

            for (int i = 0; i < playerNumber; i++) {
                textList.add(new Text());
                hangManList.add(new Drawing(4 * xMove / playerNumber * i, yMove));
                textList.get(i).setText(otherPlayersInRoom.get(i));
                textList.get(i).setX(hangManList.get(i).getX() + 4 * xMove / playerNumber * i);
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

            gameController.initData(group, tempStage, roomName);

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

    private void printEndWinAlert(Response response) {
        alertWin.setTitle("Koniec Gry");
        alertWin.setHeaderText(null);
        alertWin.setContentText("Wygrał użytkownik: " + response.winner + "\nHasło: " + response.word);
        alertWin.showAndWait();
    }

    private void printEndLostAlert(Response response) {
        alertWin.setTitle("Koniec Gry");
        alertWin.setHeaderText(null);
        alertWin.setContentText("Nikt nie wygrał! \n Hasło:  " + response.word);
        alertWin.showAndWait();
    }

    private void printLoseAlert(Response response) {
        alertLose.setTitle("Koniec Gry");
        alertLose.setHeaderText(null);
        alertLose.setContentText("Przegrałeś: " + response.loser);
        alertLose.showAndWait();
    }

    private void  printEndTimeRunOutAlert(Response response) {
        alertWin.setTitle("Koniec Gry");
        alertWin.setHeaderText(null);
        alertWin.setContentText("Skończył się czas! \n Hasło:  " + response.word);
        alertWin.showAndWait();
    }

}
