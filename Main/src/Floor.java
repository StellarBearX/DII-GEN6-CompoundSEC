import java.util.ArrayList;
import java.util.List;

public class Floor {
    private String floorName;
    private List<Room> rooms;

    public Floor(String floorName) {
        this.floorName = floorName;
        this.rooms = new ArrayList<>();
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    @Override
    public String toString() {
        return floorName;
    }
}
