import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class RoomManagementPanel extends JPanel {
    private DefaultListModel<String> roomListModel;
    private JList<String> roomList;
    private JTextField txtRoom;
    private JButton btnAddRoom;
    private JButton btnRemoveRoom;
    private LogPanel logPanel;

    public RoomManagementPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        roomListModel = new DefaultListModel<>();

        List<String> rooms = FileManager.loadRooms();
        if (rooms.isEmpty()) {
            roomListModel.addElement("Office");
            roomListModel.addElement("Meeting Room");
            roomListModel.addElement("Reception");
        } else {
            for(String room : rooms) {
                roomListModel.addElement(room);
            }
        }

        roomList = new JList<>(roomListModel);
        add(new JScrollPane(roomList), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        txtRoom = new JTextField(15);
        btnAddRoom = new JButton("Add Room");
        btnRemoveRoom = new JButton("Remove Room");

        panelSouth.add(txtRoom);
        panelSouth.add(btnAddRoom);
        panelSouth.add(btnRemoveRoom);
        add(panelSouth, BorderLayout.SOUTH);

        btnAddRoom.addActionListener(e -> {
            String newRoom = txtRoom.getText().trim();
            if (!newRoom.isEmpty()) {
                roomListModel.addElement(newRoom);
                txtRoom.setText("");
                saveRoomsToFile();
                logPanel.addLog("Added room: " + newRoom);
            }
        });

        btnRemoveRoom.addActionListener(e -> {
            int selectedIndex = roomList.getSelectedIndex();
            if (selectedIndex != -1) {
                String removedRoom = roomListModel.getElementAt(selectedIndex);
                roomListModel.remove(selectedIndex);
                saveRoomsToFile();
                logPanel.addLog("Removed room: " + removedRoom);
            }
        });
    }

    private void saveRoomsToFile() {
        java.util.List<String> rooms = new java.util.ArrayList<>();
        for (int i = 0; i < roomListModel.size(); i++) {
            rooms.add(roomListModel.get(i));
        }
        FileManager.saveRooms(rooms);
    }
}
