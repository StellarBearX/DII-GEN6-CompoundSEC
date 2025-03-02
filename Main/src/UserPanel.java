import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private JFrame parentFrame;
    private String username;
    private JTextArea taInfo;

    // เพิ่ม list แสดงห้องทั้งหมด
    private JList<String> listAllRooms;
    private DefaultListModel<String> listModelRooms;
    private JButton btnSimulateAccess;

    public UserPanel(JFrame frame, String username) {
        this.parentFrame = frame;
        this.username = username;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        // ส่วนบน: แสดงปุ่ม + ชื่อผู้ใช้งาน
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("User: " + username));
        add(topPanel, BorderLayout.NORTH);

        // ส่วนกลางซ้าย: แสดงทุกห้องที่มีในระบบ
        // โหลดทุกห้องจากไฟล์
        listModelRooms = new DefaultListModel<>();
        List<String> allRooms = FileManager.loadRooms();
        // ถ้าไฟล์ rooms.txt ยังว่าง ลองใส่ตัวอย่างเข้าไป
        if (allRooms.isEmpty()) {
            allRooms.add("Office");
            allRooms.add("Meeting Room");
            allRooms.add("Reception");
        }
        for (String r : allRooms) {
            listModelRooms.addElement(r);
        }
        listAllRooms = new JList<>(listModelRooms);
        listAllRooms.setBorder(BorderFactory.createTitledBorder("All Rooms in System"));

        // ส่วนกลางขวา: แสดงผลสรุป (accessible rooms / ผลการ simulate)
        taInfo = new JTextArea(15, 30);
        taInfo.setBorder(BorderFactory.createTitledBorder("Simulation Result"));
        taInfo.setEditable(false);

        // รวมสองส่วนกลางใน panel เดียวแบบ GridLayout 2 ช่อง
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.add(new JScrollPane(listAllRooms));
        centerPanel.add(new JScrollPane(taInfo));
        add(centerPanel, BorderLayout.CENTER);

        // สร้างปุ่มสำหรับลอง Simulation
        btnSimulateAccess = new JButton("Simulate Access");
        btnSimulateAccess.addActionListener(e -> simulateAccessForAllRooms());

        // สร้างปุ่ม Logout
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            parentFrame.setContentPane(new LoginPanel(parentFrame));
            parentFrame.revalidate();
        });

        // แถบล่าง ใส่ปุ่ม Simulation และปุ่ม Logout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnSimulateAccess);
        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);

        // เริ่มต้น แสดง Accessible Rooms ให้ผู้ใช้เห็น (เหมือนของเดิม)
        refreshAccessibleRooms();
    }

    private void refreshAccessibleRooms() {
        List<Room> accessibleRooms = AccessControl.getInstance().getAccessibleRooms(new User(username, ""));
        StringBuilder sb = new StringBuilder("Accessible Rooms for " + username + ":\n");
        if (accessibleRooms.isEmpty()) {
            sb.append("No access granted yet.\n");
        } else {
            for (Room room : accessibleRooms) {
                sb.append(room.getName()).append("\n");
            }
        }
        // แสดงสรุปว่าเข้าได้ห้องอะไรบ้าง (ของเดิม)
        sb.append("\n--- End of Accessible Rooms ---\n");
        taInfo.setText(sb.toString());
    }

    // เมธอดสำหรับปุ่ม Simulate Access
    private void simulateAccessForAllRooms() {
        StringBuilder sb = new StringBuilder();
        sb.append("Simulation: Checking access for user ").append(username).append("\n\n");

        List<Room> accessibleRooms = AccessControl.getInstance().getAccessibleRooms(new User(username, ""));
        // จะได้รายชื่อห้องที่ user เข้าถึงได้
        // หรือจะเช็คแบบห้องต่อห้องก็ได้

        // หยิบชื่อห้องจาก listModelRooms มาลองเทียบ
        for (int i = 0; i < listModelRooms.size(); i++) {
            String roomName = listModelRooms.getElementAt(i);
            // เช็คว่า roomName อยู่ใน accessibleRooms หรือไม่
            boolean canAccess = accessibleRooms.stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase(roomName));
            if (canAccess) {
                sb.append(roomName).append(" : Access Granted\n");
            } else {
                sb.append(roomName).append(" : Access Denied\n");
            }
        }

        taInfo.setText(sb.toString());
    }
}
