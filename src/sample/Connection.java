package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import sample.request.Request;
import sample.response.Response;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public final class Connection {

    private static Connection instance;
    private Socket clientSocket;
    private String nick;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private ObjectMapper mapper;

    private Connection() throws IOException {
        mapper = new ObjectMapper();

        String host = "127.0.0.1";
        int port = 1111;
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        clientSocket = new Socket();
        int timeOut = 10000;

        try {
            clientSocket.connect(socketAddress, timeOut);
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (SocketTimeoutException ex) {
            clientSocket.close();
        } catch (IOException e) {
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
        return new ArrayList<String>(List.of("Sztucznie", "Wpisany", "Serwer", "To jest"));
    }

    public void setNick(String text) {
        nick = text;
        Request request = new Request();
        request.setNick("witamWitam");
        request.setType("LOGIN");
        try {
            byte[] bytes = mapper.writeValueAsBytes(request);
            outputStream.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }


        Response r = readMessage();
        System.out.printf("Read from server: %s, %s", r.getType(), r.getResult());
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
}
