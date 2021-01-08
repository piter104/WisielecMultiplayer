package sample.response;

import java.util.List;

public class Response {
    public ResponseType type;
    public String result;
    public List<Room> rooms;
    public String hostNick;
    public List<Integer> letterPositions;
    public List<String> otherPlayersInRoom;
    public Integer howLongIsTheWord;
    public Boolean gameFinished;

    @Override
    public String toString() {
        return "Response{" +
                "type='" + type + '\'' +
                ", result='" + result + '\'' +
                ", rooms=" + rooms +
                ", hostNick='" + hostNick + '\'' +
                ", letterPositions=" + letterPositions +
                ", howLongIsTheWord=" + howLongIsTheWord +
                ", gameFinished=" + gameFinished +
                '}';
    }

    public enum ResponseType {
        USER_AUTHENTICATED,
        USER_JOINED_ROOM,
        GAME_STARTED,
        LETTER_RECEIVED

    }
}
