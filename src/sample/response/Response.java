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
    public String winner;
    public String loser;
    public String roomName;

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
                ", winner=" + winner +
                ", loser=" + loser +
                ", roomName=" + roomName +
                '}';
    }

    public enum ResponseType {
        USER_AUTHENTICATED,
        USER_JOINED_ROOM,
        USER_LEFT_ROOM,
        ROOM_CREATED,
        GAME_STARTED,
        BLOCK_ROOM,
        SOMEBODY_GUESSED_WRONG,
        LETTER_RECEIVED,
        GAME_FINISHED,
        YOU_LOST;
    }
}
