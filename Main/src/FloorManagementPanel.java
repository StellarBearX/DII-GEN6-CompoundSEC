import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FloorManagementPanel extends JPanel {
    private DefaultListModel<String> floorListModel;
    private JList<String> floorList;
    private JTextField txtFloor;
    private JButton btnAddFloor;
    private JButton btnRemoveFloor;
    private LogPanel logPanel;

    public FloorManagementPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        floorListModel = new DefaultListModel<>();

        List<String> floors = FileManager.loadFloors();
        if (floors.isEmpty()) {
            floors.add("Low Floor");
            floors.add("Medium Floor");
            floors.add("High Floor");
        }
        for (String f : floors) {
            floorListModel.addElement(f);
        }

        floorList = new JList<>(floorListModel);
        add(new JScrollPane(floorList), BorderLayout.CENTER);

        JPanel panelSouth = new JPanel();
        txtFloor = new JTextField(15);
        btnAddFloor = new JButton("Add Floor");
        btnRemoveFloor = new JButton("Remove Floor");

        panelSouth.add(txtFloor);
        panelSouth.add(btnAddFloor);
        panelSouth.add(btnRemoveFloor);
        add(panelSouth, BorderLayout.SOUTH);

        btnAddFloor.addActionListener(e -> {
            String newFloor = txtFloor.getText().trim();
            if (!newFloor.isEmpty()) {
                floorListModel.addElement(newFloor);
                txtFloor.setText("");
                saveFloorsToFile();
                logPanel.addLog("Added floor: " + newFloor);
            }
        });

        btnRemoveFloor.addActionListener(e -> {
            int selectedIndex = floorList.getSelectedIndex();
            if (selectedIndex != -1) {
                String removedFloor = floorListModel.getElementAt(selectedIndex);
                floorListModel.remove(selectedIndex);
                saveFloorsToFile();
                logPanel.addLog("Removed floor: " + removedFloor);
            }
        });
    }

    private void saveFloorsToFile() {
        List<String> floors = new ArrayList<>();
        for (int i = 0; i < floorListModel.size(); i++) {
            floors.add(floorListModel.get(i));
        }
        FileManager.saveFloors(floors);
    }
}
