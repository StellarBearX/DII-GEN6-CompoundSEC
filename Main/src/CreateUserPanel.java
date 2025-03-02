import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateUserPanel extends JPanel {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnCreate;
    private LogPanel logPanel;

    public CreateUserPanel(LogPanel logPanel) {
        this.logPanel = logPanel;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");
        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        btnCreate = new JButton("Create User");

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblUsername, gbc);
        gbc.gridx = 1;
        add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        add(btnCreate, gbc);

        btnCreate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword());
                if(username.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(CreateUserPanel.this, "Username or Password cannot be empty!");
                    return;
                }
                CredentialManager.saveCredential(username, password, "User");
                logPanel.addLog("Created user: " + username);
                JOptionPane.showMessageDialog(CreateUserPanel.this, "User " + username + " created successfully!");
                txtUsername.setText("");
                txtPassword.setText("");
            }
        });
    }
}
