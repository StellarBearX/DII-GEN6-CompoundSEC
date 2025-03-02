import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class CredentialRecordPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private static final String CREDENTIAL_FILE = "credentials.txt";

    public CredentialRecordPanel() {
        initialize();
        loadCredentials();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"Username", "Role", "Created Time"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadCredentials());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadCredentials() {
        tableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIAL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String username = parts[0].trim();
                    String role = parts[2].trim();
                    String timestamp = parts[3].trim();
                    tableModel.addRow(new Object[]{username, role, timestamp});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading credential records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
