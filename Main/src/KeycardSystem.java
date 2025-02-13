public class KeycardSystem {
    public static void main(String[] args) {
        Keycard userKeycard = new Keycard("USER001", 2);
        User user = new User("John Doe", userKeycard);

        // แสดงหน้า User Panel สำหรับผู้ใช้
        new UserPanel(user);

        // หรือหากต้องการให้ทั้ง Admin และ User ใช้งาน
        Admin admin = new Admin("Bob", new Keycard("ADM001", 5));
        new AdminPanel(admin);
    }
}