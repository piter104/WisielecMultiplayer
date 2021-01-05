package sample;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectMapper mapper;
    private List<Room> rooms;
    private Optional<Room> chosenRoom;

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

    public List<String> getRooms() {
        return rooms.stream().map(Room::getRoomName).collect(Collectors.toList());
    }

    public void setNick(String text) {
        nick = text;
        Request request = new Request();
        request.nick = "witamWitam";
        request.type = Request.RequestType.LOGIN;
        sendRequest(request);

        Response r = readMessage();
        rooms = r.rooms;
        System.out.printf("Serverd responded with: %s \n", r.toString());
    }

    public void joinRoom(String roomName) {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.JOIN_ROOM;
        sendRequest(request);
        Response response = readMessage();

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

    public void startGame() {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.START_GAME;
        sendRequest(request);
        Response response = readMessage();
    }

    public Response guessLetter(String letter) {
        Request request = new Request();
        request.roomName = "room1";
        request.nick = nick;
        request.type = Request.RequestType.SEND_LETTER;
        request.letter = letter;
        sendRequest(request);

        Response response = readMessage();
        return response;
    }
}
