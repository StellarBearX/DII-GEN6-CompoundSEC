import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // ตรวจสอบ Default Admin
        CredentialManager.ensureDefaultAdmin();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Compound Security Access Control");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new LoginPanel(frame));
            frame.setVisible(true);
        });
    }
}
