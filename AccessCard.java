import java.time.LocalDateTime;

public class AccessCard {
    private String cardType; // เช่น "RoomCard", "FloorCard", "VIP", ...
    private Room room;       // ถ้าเป็นการให้สิทธิ์ระดับ Room
    private Floor floor;     // ถ้าเป็นการให้สิทธิ์ระดับ Floor
    private LocalDateTime issueTime;
    private LocalDateTime expirationTime;

    public AccessCard(String cardType, Room room, Floor floor,
                      LocalDateTime issueTime, LocalDateTime expirationTime) {
        this.cardType = cardType;
        this.room = room;
        this.floor = floor;
        this.issueTime = issueTime;
        this.expirationTime = expirationTime;
    }

    public String getCardType() {
        return cardType;
    }

    public Room getRoom() {
        return room;
    }

    public Floor getFloor() {
        return floor;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

    @Override
    public String toString() {
        // roomName อาจเป็น "-" ถ้าเป็นการให้สิทธิ์ floor
        String roomName = (room == null) ? "-" : room.getName();
        String floorName = (floor == null) ? "-" : floor.getFloorName();
        return cardType + "|" + roomName + "|" + floorName + "|"
                + issueTime.toString() + "|" + expirationTime.toString();
    }

    // สร้าง AccessCard จากข้อมูลในไฟล์ (cardType|roomName|floorName|issueTime|expirationTime)
    public static AccessCard fromString(String data) {
        String[] parts = data.split("\\|");
        if (parts.length == 5) {
            String cardType = parts[0].trim();
            String rName = parts[1].trim();
            String fName = parts[2].trim();
            Room room = rName.equals("-") ? null : new Room(rName);
            Floor floor = fName.equals("-") ? null : new Floor(fName);
            LocalDateTime issue = LocalDateTime.parse(parts[3].trim());
            LocalDateTime expire = LocalDateTime.parse(parts[4].trim());
            return new AccessCard(cardType, room, floor, issue, expire);
        }
        return null;
    }
}
