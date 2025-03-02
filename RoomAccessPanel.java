import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class RoomAccessPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;

    public RoomAccessPanel() {
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        // เพิ่มคอลัมน์ Floor ด้วย
        tableModel = new DefaultTableModel(new Object[]{
                "Username", "Card Type", "Room", "Floor", "Issue Time", "Expiration Time", "Status"
        }, 0);

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> updateTableData());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);

        updateTableData();
    }

    private void updateTableData() {
        tableModel.setRowCount(0);
        Map<User, List<AccessCard>> accessMap = AccessControl.getInstance().getAllAccess();
        for (User user : accessMap.keySet()) {
            List<AccessCard> cards = accessMap.get(user);
            for (AccessCard card : cards) {
                String status = card.isExpired() ? "Expired" : "Active";
                String roomName = (card.getRoom() == null) ? "-" : card.getRoom().getName();
                String floorName = (card.getFloor() == null) ? "-" : card.getFloor().getFloorName();
                tableModel.addRow(new Object[]{
                        user.getUsername(),
                        card.getCardType(),
                        roomName,
                        floorName,
                        card.getIssueTime().toString(),
                        card.getExpirationTime().toString(),
                        status
                });
            }
        }
    }
}
