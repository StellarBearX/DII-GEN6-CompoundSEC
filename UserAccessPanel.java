import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.util.List;

public class UserAccessPanel extends JPanel {
    private JComboBox<String> cbUsers;
    private JComboBox<String> cbRooms;
    private JComboBox<String> cbFloors;
    private JButton btnGrantRoom;
    private JButton btnGrantFloor;
    private JButton btnRevoke;
    private JButton btnExtend;
    private JButton btnRefresh;
    private JTextArea taAccess;
    private LogPanel logPanel;

    public UserAccessPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        cbUsers = new JComboBox<>();
        populateUserComboBox();

        cbRooms = new JComboBox<>();
        List<String> rooms = FileManager.loadRooms();
        if (rooms.isEmpty()) {
            rooms.add("Office");
            rooms.add("Meeting Room");
            rooms.add("Reception");
        }
        for (String r : rooms) {
            cbRooms.addItem(r);
        }

        cbFloors = new JComboBox<>();
        List<String> floors = FileManager.loadFloors();
        if (floors.isEmpty()) {
            floors.add("Low Floor");
            floors.add("Medium Floor");
            floors.add("High Floor");
        }
        for (String f : floors) {
            cbFloors.addItem(f);
        }

        btnGrantRoom = new JButton("Grant Room");
        btnGrantFloor = new JButton("Grant Floor");
        btnRevoke = new JButton("Revoke");
        btnExtend = new JButton("Extend");
        btnRefresh = new JButton("Refresh");

        topPanel.add(new JLabel("User:"));
        topPanel.add(cbUsers);
        topPanel.add(new JLabel("Room:"));
        topPanel.add(cbRooms);
        topPanel.add(btnGrantRoom);
        topPanel.add(new JLabel("Floor:"));
        topPanel.add(cbFloors);
        topPanel.add(btnGrantFloor);
        topPanel.add(btnRevoke);
        topPanel.add(btnExtend);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        taAccess = new JTextArea("User Access Assignments:\n");
        taAccess.setEditable(false);
        add(new JScrollPane(taAccess), BorderLayout.CENTER);

        // Grant Access (Room)
        btnGrantRoom.addActionListener(e -> {
            String username = (String) cbUsers.getSelectedItem();
            String roomName = (String) cbRooms.getSelectedItem();
            if (username == null || roomName == null) return;

            User user = new User(username, "");
            Room room = new Room(roomName);
            AccessControl.getInstance().grantRoomAccess(user, room, "RoomCard");
            logPanel.addLog("Granted ROOM access to user " + username + " for room " + roomName);
            updateAccessDisplay();
        });

        // Grant Access (Floor)
        btnGrantFloor.addActionListener(e -> {
            String username = (String) cbUsers.getSelectedItem();
            String floorName = (String) cbFloors.getSelectedItem();
            if (username == null || floorName == null) return;

            User user = new User(username, "");
            Floor floor = new Floor(floorName);
            AccessControl.getInstance().grantFloorAccess(user, floor, "FloorCard");
            logPanel.addLog("Granted FLOOR access to user " + username + " for floor " + floorName);
            updateAccessDisplay();
        });

        // Revoke (ตัวอย่าง revoke สิทธิ์ห้อง)
        btnRevoke.addActionListener(e -> {
            String username = (String) cbUsers.getSelectedItem();
            String roomName = (String) cbRooms.getSelectedItem();
            if (username == null || roomName == null) return;

            User user = new User(username, "");
            AccessControl.getInstance().revokeRoomAccess(user, new Room(roomName));
            logPanel.addLog("Revoked ROOM access from user " + username + " for room " + roomName);
            updateAccessDisplay();
        });

        // Extend (ขยายเวลาบัตร)
        btnExtend.addActionListener(e -> {
            String username = (String) cbUsers.getSelectedItem();
            String roomName = (String) cbRooms.getSelectedItem();
            if (username == null || roomName == null) return;

            // ตัวอย่าง extend บัตรอีก 30 นาที
            AccessControl.getInstance().extendCard(new User(username, ""), "RoomCard", roomName, Duration.ofMinutes(30), false);
            logPanel.addLog("Extended ROOM card for user " + username + " (room " + roomName + ") by 30 min");
            updateAccessDisplay();
        });

        // Refresh
        btnRefresh.addActionListener(e -> {
            populateUserComboBox();
            updateAccessDisplay();
        });
    }

    private void populateUserComboBox() {
        cbUsers.removeAllItems();
        List<String> userList = CredentialManager.getUserList();
        for (String user : userList) {
            cbUsers.addItem(user);
        }
    }

    private void updateAccessDisplay() {
        StringBuilder sb = new StringBuilder("User Access Assignments:\n");
        for (User user : AccessControl.getInstance().getAllAccess().keySet()) {
            List<Room> accessible = AccessControl.getInstance().getAccessibleRooms(user);
            sb.append(user.getUsername()).append(" : ");
            for (Room r : accessible) {
                sb.append(r.getName()).append(", ");
            }
            if (!accessible.isEmpty()) {
                sb.setLength(sb.length() - 2);
            }
            sb.append("\n");
        }
        taAccess.setText(sb.toString());
    }
}
