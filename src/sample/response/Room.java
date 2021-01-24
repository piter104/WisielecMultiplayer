package sample.response;

public class Room {
    private String roomName;
    private Boolean maxPlayers;

    public Boolean getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Boolean maxPlayers) {
        this.maxPlayers = maxPlayers;
    }


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                '}';
    }
}
