import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {
    private JFrame parentFrame;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LogPanel logPanel;

    public AdminPanel(JFrame frame) {
        this.parentFrame = frame;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCreateUser = new JButton("Create User");
        JButton btnUserAccess = new JButton("User Access");
        JButton btnFloorManagement = new JButton("Floor Management");
        JButton btnRoomManagement = new JButton("Room Management");
        JButton btnRoomAccess = new JButton("Room Access");
        JButton btnLog = new JButton("Log");
        JButton btnLogout = new JButton("Logout");
        JButton btnCredentialRecord = new JButton("Credential Record");

        navBar.add(btnCreateUser);
        navBar.add(btnUserAccess);
        navBar.add(btnFloorManagement);
        navBar.add(btnRoomManagement);
        navBar.add(btnRoomAccess);
        navBar.add(btnCredentialRecord);
        navBar.add(btnLog);
        navBar.add(btnLogout);

        add(navBar, BorderLayout.NORTH);

        logPanel = new LogPanel();

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new CreateUserPanel(logPanel), "CreateUser");
        cardPanel.add(new UserAccessPanel(logPanel), "UserAccess");
        cardPanel.add(new FloorManagementPanel(logPanel), "FloorManagement");
        cardPanel.add(new RoomManagementPanel(logPanel), "RoomManagement");
        cardPanel.add(new RoomAccessPanel(), "RoomAccess");
        CredentialRecordPanel credentialRecordPanel = new CredentialRecordPanel();
        cardPanel.add(credentialRecordPanel, "CredentialRecord");
        cardPanel.add(logPanel, "Log");


        add(cardPanel, BorderLayout.CENTER);

        btnCreateUser.addActionListener(e -> cardLayout.show(cardPanel, "CreateUser"));
        btnUserAccess.addActionListener(e -> cardLayout.show(cardPanel, "UserAccess"));
        btnFloorManagement.addActionListener(e -> cardLayout.show(cardPanel, "FloorManagement"));
        btnRoomManagement.addActionListener(e -> cardLayout.show(cardPanel, "RoomManagement"));
        btnRoomAccess.addActionListener(e -> cardLayout.show(cardPanel, "RoomAccess"));
        btnCredentialRecord.addActionListener(e -> cardLayout.show(cardPanel, "CredentialRecord"));
        btnLog.addActionListener(e -> cardLayout.show(cardPanel, "Log"));
        btnLogout.addActionListener(e -> {
            parentFrame.setContentPane(new LoginPanel(parentFrame));
            parentFrame.revalidate();
        });
    }
}
