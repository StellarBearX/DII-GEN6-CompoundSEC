import java.io.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

public class AccessControl {
    private Map<User, List<AccessCard>> userAccessMap;
    private static AccessControl instance = new AccessControl();
    private static final String ACCESS_FILE = "accesscontrol.txt";

    // ระยะเวลาหมดอายุของบัตร (30 นาที)
    private static final Duration CARD_DURATION = Duration.ofMinutes(30);

    private AccessControl() {
        userAccessMap = new HashMap<>();
        loadAccessAssignments();
    }

    public static AccessControl getInstance() {
        return instance;
    }

    // ให้สิทธิ์ในระดับห้อง
    public void grantRoomAccess(User user, Room room, String cardType) {
        List<AccessCard> cards = userAccessMap.computeIfAbsent(user, k -> new ArrayList<>());
        // ตรวจสอบว่ามีบัตรที่ยังไม่หมดอายุสำหรับห้องนี้หรือยัง
        boolean exists = false;
        for (AccessCard c : cards) {
            if (!c.isExpired() && c.getRoom() != null &&
                    c.getRoom().getName().equalsIgnoreCase(room.getName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = now.plus(CARD_DURATION);
            AccessCard newCard = new AccessCard(cardType, room, null, now, expiration);
            cards.add(newCard);
        }
        saveAccessAssignments();
    }

    // ให้สิทธิ์ในระดับ Floor (เข้าถึงทุกห้องใน Floor นั้น)
    public void grantFloorAccess(User user, Floor floor, String cardType) {
        List<AccessCard> cards = userAccessMap.computeIfAbsent(user, k -> new ArrayList<>());
        boolean exists = false;
        for (AccessCard c : cards) {
            if (!c.isExpired() && c.getFloor() != null &&
                    c.getFloor().getFloorName().equalsIgnoreCase(floor.getFloorName())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration = now.plus(CARD_DURATION);
            AccessCard newCard = new AccessCard(cardType, null, floor, now, expiration);
            cards.add(newCard);
        }
        saveAccessAssignments();
    }

    // เพิกถอนสิทธิ์ในระดับห้อง
    public void revokeRoomAccess(User user, Room room) {
        List<AccessCard> cards = userAccessMap.get(user);
        if (cards != null) {
            cards.removeIf(c -> c.getRoom() != null &&
                    c.getRoom().getName().equalsIgnoreCase(room.getName()));
            saveAccessAssignments();
        }
    }

    // เพิกถอนสิทธิ์ในระดับ Floor
    public void revokeFloorAccess(User user, Floor floor) {
        List<AccessCard> cards = userAccessMap.get(user);
        if (cards != null) {
            cards.removeIf(c -> c.getFloor() != null &&
                    c.getFloor().getFloorName().equalsIgnoreCase(floor.getFloorName()));
            saveAccessAssignments();
        }
    }

    // ขยายเวลาบัตร (ตัวอย่างการ modify)
    public void extendCard(User user, String cardType, String name, Duration additionalTime, boolean isFloor) {
        List<AccessCard> cards = userAccessMap.get(user);
        if (cards != null) {
            for (AccessCard c : cards) {
                if (!c.isExpired() && c.getCardType().equalsIgnoreCase(cardType)) {
                    if (isFloor && c.getFloor() != null &&
                            c.getFloor().getFloorName().equalsIgnoreCase(name)) {
                        // ขยายเวลาหมดอายุ
                        LocalDateTime newExp = c.getExpirationTime().plus(additionalTime);
                        cards.remove(c);
                        AccessCard extended = new AccessCard(c.getCardType(), null, c.getFloor(),
                                c.getIssueTime(), newExp);
                        cards.add(extended);
                        break;
                    } else if (!isFloor && c.getRoom() != null &&
                            c.getRoom().getName().equalsIgnoreCase(name)) {
                        LocalDateTime newExp = c.getExpirationTime().plus(additionalTime);
                        cards.remove(c);
                        AccessCard extended = new AccessCard(c.getCardType(), c.getRoom(), null,
                                c.getIssueTime(), newExp);
                        cards.add(extended);
                        break;
                    }
                }
            }
            saveAccessAssignments();
        }
    }

    // ดึงรายชื่อห้องที่ User ยังมีสิทธิ์ (ยังไม่หมดอายุ)
    public List<Room> getAccessibleRooms(User user) {
        List<Room> accessible = new ArrayList<>();
        List<AccessCard> cards = userAccessMap.getOrDefault(user, new ArrayList<>());
        for (AccessCard card : cards) {
            if (!card.isExpired()) {
                // ถ้าเป็นบัตรระดับ Room
                if (card.getRoom() != null) {
                    accessible.add(card.getRoom());
                }
                // ถ้าเป็นบัตรระดับ Floor ให้ add ห้องทั้งหมดใน floor
                if (card.getFloor() != null) {
                    accessible.addAll(card.getFloor().getRooms());
                }
            }
        }
        return accessible;
    }

    public Map<User, List<AccessCard>> getAllAccess() {
        return userAccessMap;
    }

    private void saveAccessAssignments() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCESS_FILE))) {
            for (Map.Entry<User, List<AccessCard>> entry : userAccessMap.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey().getUsername()).append(",");
                for (AccessCard card : entry.getValue()) {
                    sb.append(card.toString()).append(";");
                }
                if (sb.charAt(sb.length() - 1) == ';') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAccessAssignments() {
        File file = new File(ACCESS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            userAccessMap.clear();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String cardData = parts[1].trim();
                    String[] cardParts = cardData.split(";");
                    List<AccessCard> cards = new ArrayList<>();
                    for (String cp : cardParts) {
                        AccessCard c = AccessCard.fromString(cp);
                        if (c != null) cards.add(c);
                    }
                    userAccessMap.put(new User(username, ""), cards);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
