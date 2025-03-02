import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String ROOM_FILE = "rooms.txt";
    // ถ้าต้องการเก็บ floors แยกไฟล์
    private static final String FLOOR_FILE = "floors.txt";

    public static void saveRooms(List<String> rooms) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROOM_FILE))) {
            for (String room : rooms) {
                writer.write(room);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadRooms() {
        List<String> rooms = new ArrayList<>();
        File file = new File(ROOM_FILE);
        if (!file.exists()) return rooms;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rooms.add(line.trim());
            }
        } catch (IOException e) {
            // ไฟล์อาจยังไม่มีในรอบแรก
        }
        return rooms;
    }

    // หากต้องการบันทึก floor
    public static void saveFloors(List<String> floors) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FLOOR_FILE))) {
            for (String f : floors) {
                writer.write(f);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadFloors() {
        List<String> floors = new ArrayList<>();
        File file = new File(FLOOR_FILE);
        if (!file.exists()) return floors;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                floors.add(line.trim());
            }
        } catch (IOException e) {
            // ไฟล์อาจยังไม่มี
        }
        return floors;
    }
}
