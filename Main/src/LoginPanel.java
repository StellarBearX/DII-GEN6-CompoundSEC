import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JFrame parentFrame;

    public LoginPanel(JFrame frame) {
        this.parentFrame = frame;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton btnAdminLogin = new JButton("Login Admin");
        JButton btnUserLogin = new JButton("Login User");

        btnAdminLogin.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Enter Admin Username:");
            if (username == null || username.trim().isEmpty()) return;
            String password = JOptionPane.showInputDialog(this, "Enter Admin Password:");
            if (password == null || password.trim().isEmpty()) return;

            boolean authenticated = CredentialManager.authenticateCredential(username.trim(), password.trim(), "Admin");
            if (authenticated) {
                JOptionPane.showMessageDialog(this, "Admin login successful!");
                parentFrame.setContentPane(new AdminPanel(parentFrame));
                parentFrame.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUserLogin.addActionListener(e -> {
            String username = JOptionPane.showInputDialog(this, "Enter User Username:");
            if (username == null || username.trim().isEmpty()) return;
            String password = JOptionPane.showInputDialog(this, "Enter User Password:");
            if (password == null || password.trim().isEmpty()) return;

            boolean authenticated = CredentialManager.authenticateCredential(username.trim(), password.trim(), "User");
            if (authenticated) {
                JOptionPane.showMessageDialog(this, "User login successful!");
                parentFrame.setContentPane(new UserPanel(parentFrame, username.trim()));
                parentFrame.revalidate();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User Credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        add(btnAdminLogin, gbc);
        gbc.gridy = 1;
        add(btnUserLogin, gbc);
    }
}
