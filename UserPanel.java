import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class UserPanel extends JPanel {
    private JFrame parentFrame;
    private String username;
    private JTextArea taInfo;

    public UserPanel(JFrame frame, String username) {
        this.parentFrame = frame;
        this.username = username;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        taInfo = new JTextArea();
        taInfo.setEditable(false);
        refreshAccessibleRooms();

        add(new JScrollPane(taInfo), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAttempt = new JButton("Attempt Access");
        JButton btnLogout = new JButton("Logout");
        bottomPanel.add(btnAttempt);
        bottomPanel.add(btnLogout);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAttempt.addActionListener(e -> {
            String roomName = JOptionPane.showInputDialog(this, "Enter Room to Access:");
            if (roomName == null || roomName.trim().isEmpty()) return;

            List<Room> accessibleRooms = AccessControl.getInstance().getAccessibleRooms(new User(username, ""));
            boolean canAccess = accessibleRooms.stream()
                    .anyMatch(r -> r.getName().equalsIgnoreCase(roomName.trim()));

            // Log
            LogPanel globalLog = AdminPanelGlobalLog.getLogPanel();
            // (ดูหมายเหตุด้านล่าง)

            if (canAccess) {
                JOptionPane.showMessageDialog(this, "Access Granted to room: " + roomName);
                if (globalLog != null) {
                    globalLog.addLog("User " + username + " accessed room " + roomName + " successfully.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied to room: " + roomName);
                if (globalLog != null) {
                    globalLog.addLog("User " + username + " tried to access room " + roomName + " but was denied.");
                }
            }
        });

        btnLogout.addActionListener(e -> {
            parentFrame.setContentPane(new LoginPanel(parentFrame));
            parentFrame.revalidate();
        });
    }

    private void refreshAccessibleRooms() {
        List<Room> accessibleRooms = AccessControl.getInstance().getAccessibleRooms(new User(username, ""));
        StringBuilder sb = new StringBuilder("Accessible Rooms for " + username + ":\n");
        if(accessibleRooms.isEmpty()){
            sb.append("No access granted yet.\n");
        } else {
            for (Room room : accessibleRooms) {
                sb.append(room.getName()).append("\n");
            }
        }
        taInfo.setText(sb.toString());
    }
}
