import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
class AdminPanel {
    private Admin admin;
    private JFrame frame;
    private JTextArea userList;


    public AdminPanel(Admin admin) {
        this.admin = admin;
        frame = new JFrame("Admin Panel");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        userList = new JTextArea();
        refreshUserList();
        frame.add(new JScrollPane(userList), BorderLayout.CENTER);


        JPanel panel = new JPanel();
        JButton addButton = new JButton("Add User");
        JButton removeButton = new JButton("Remove User");
        JButton changeAccessButton = new JButton("Change Access");


        addButton.addActionListener(e -> addUser());
        removeButton.addActionListener(e -> removeUser());
        changeAccessButton.addActionListener(e -> changeAccess());


        panel.add(addButton);
        panel.add(removeButton);
        panel.add(changeAccessButton);
        frame.add(panel, BorderLayout.SOUTH);


        frame.setVisible(true);
    }


    private void refreshUserList() {
        userList.setText("User Access List:\n");
        for (Map.Entry<String, Integer> entry : admin.getUserAccessControl().entrySet()) {
            userList.append("User: " + entry.getKey() + " - Access Level: " + entry.getValue() + "\n");
        }
    }


    private void addUser() {
        String name = JOptionPane.showInputDialog("Enter User Name:");
        int level = Integer.parseInt(JOptionPane.showInputDialog("Enter Access Level:"));
        admin.getUserAccessControl().put(name, level);
        refreshUserList();
    }


    private void removeUser() {
        String name = JOptionPane.showInputDialog("Enter User Name to Remove:");
        admin.getUserAccessControl().remove(name);
        refreshUserList();
    }


    private void changeAccess() {
        String name = JOptionPane.showInputDialog("Enter User Name:");
        int level = Integer.parseInt(JOptionPane.showInputDialog("Enter New Access Level:"));
        admin.getUserAccessControl().put(name, level);
        refreshUserList();
    }
}


