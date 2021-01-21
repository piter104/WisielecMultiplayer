package sample.request;

public class Request {
    public RequestType type;
    public String nick;
    public String roomName;
    public String letter;

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", nick='" + nick + '\'' +
                ", roomName='" + roomName + '\'' +
                ", letter='" + letter + '\'' +
                '}';
    }


    public enum RequestType {
        JOIN_ROOM,
        LEAVE_ROOM,
        CREATE_ROOM,
        START_GAME,
        SEND_LETTER,
        LOGIN
    }
}
