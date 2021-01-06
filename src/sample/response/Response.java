package sample.response;

import java.util.List;

public class Response {
    public String type;
    public String result;
    public List<Room> rooms;
    public String hostNick;
    public List<Integer> letterPositions;
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
}
