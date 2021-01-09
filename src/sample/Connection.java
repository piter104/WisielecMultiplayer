package sample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

        try {
            clientSocket.connect(socketAddress, timeOut);
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            clientSocket.close();
        } catch (Exception e) {

        }

        System.out.println("Connection established");

        Thread responseReader = new Thread(() -> {
            while(true) {
                Response response = readMessage();
                dispatchResponse(response);
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

    private Response readMessage() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            length = inputStream.read(buffer);
            result.write(buffer, 0, length);
            Response response = mapper.readValue(result.toByteArray(), Response.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    public Integer startGame() {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.START_GAME;
        sendRequest(request); //TODO:REMOVE
        Response response = readMessage();
        return response.howLongIsTheWord;
    }

    public Response guessLetter(String letter) {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.SEND_LETTER;
        request.letter = letter;
        sendRequest(request);

        Response response = readMessage(); //TODO:REMOVE
        return response;
    }


    private void dispatchResponse(Response response) {
        switch (response.type) {
            case USER_AUTHENTICATED:
                if ( response.rooms != null ) {
                    List<String> updateRooms = response.rooms.stream().map(Room::getRoomName).collect(Collectors.toList());
                    rooms.addAll(updateRooms);
                    System.out.printf("Serverd responded with: %s \n", response.toString());
                }

                break;
            case USER_JOINED_ROOM:
                System.out.println(response.toString());
                otherPlayersInRoom.setAll(response.otherPlayersInRoom);
                break;
            case GAME_STARTED:
                this.howLongIsTheWord = response.howLongIsTheWord;
                break;
            case LETTER_RECEIVED:
                break;
        }
    }

    public ObservableList<String> getOtherPlayersInRoom() {
        return otherPlayersInRoom;
    }
}
