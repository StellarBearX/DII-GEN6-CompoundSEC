import javax.swing.*;
import java.awt.*;

class UserPanel {
    private User user;
    private JFrame frame;
    private JTextArea userInfo;

    public UserPanel(User user) {
        this.user = user;
        frame = new JFrame("User Panel");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Text area to show user info
        userInfo = new JTextArea();
        refreshUserInfo();
        frame.add(new JScrollPane(userInfo), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton viewInfoButton = new JButton("View Info");

        // Button to refresh the user's information
        viewInfoButton.addActionListener(e -> refreshUserInfo());

        panel.add(viewInfoButton);
        frame.add(panel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void refreshUserInfo() {
        // Display user's information (CardId and Access Level)
        userInfo.setText("User Information:\n");
        userInfo.append("Name: " + user.getName() + "\n");
        userInfo.append("Card ID: " + user.getKeycard().getCardId() + "\n");
        userInfo.append("Access Level: " + user.getKeycard().getAccessLevel() + "\n");
    }
}
