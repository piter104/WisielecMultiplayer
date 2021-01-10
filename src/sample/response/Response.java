package sample.response;

import java.util.List;
import java.util.Map;

public class Response {
    public ResponseType type;
    public String result;
    public List<Room> rooms;
    public String hostNick;
    public List<Integer> letterPositions;
    public List<String> otherPlayersInRoom;
    public Integer howLongIsTheWord;
    public Boolean gameFinished;
    public String letterGuessed;
    public Map<String, Integer> userWrongCounterMap;

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", result='" + result + '\'' +
                ", rooms=" + rooms +
                ", hostNick='" + hostNick + '\'' +
                ", letterPositions=" + letterPositions +
                ", otherPlayersInRoom=" + otherPlayersInRoom +
                ", howLongIsTheWord=" + howLongIsTheWord +
                ", gameFinished=" + gameFinished +
                ", letterGuessed='" + letterGuessed + '\'' +
                ", userWrongCounterMap=" + userWrongCounterMap +
                '}';
    }

    public enum ResponseType {
        USER_AUTHENTICATED,
        USER_JOINED_ROOM,
        GAME_STARTED,
        SOMEBODY_GUESSED_WRONG,
        LETTER_RECEIVED;

    }
}
