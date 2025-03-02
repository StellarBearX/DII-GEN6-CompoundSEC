import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LogPanel extends JPanel {
    private JTextArea taLogs;
    private static final String LOG_FILE = "activity_log.txt";

    public LogPanel() {
        initialize();
        loadLogFromFile();
    }

    private void initialize() {
        setLayout(new BorderLayout());
        taLogs = new JTextArea();
        taLogs.setEditable(false);
        add(new JScrollPane(taLogs), BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> resetLog());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void addLog(String log) {
        String logEntry = log + "\n";
        taLogs.append(logEntry);
        writeLogToFile(logEntry);
    }

    private void writeLogToFile(String logEntry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetLog() {
        taLogs.setText("");
        loadLogFromFile();
    }

    private void loadLogFromFile() {
        StringBuilder sb = new StringBuilder("Activity logs:\n");
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            sb.append("No logs found.");
            taLogs.setText(sb.toString());
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            sb.append("No logs found.");
        }
        taLogs.setText(sb.toString());
    }
}
