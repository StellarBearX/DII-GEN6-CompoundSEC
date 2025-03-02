import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CredentialManager {
    private static final String CREDENTIAL_FILE = "credentials.txt";
    private static final String SECRET_KEY = "mySecretKey";
    private static final int EXPIRATION_DAYS = 7; // กำหนดเวลาหมดอายุบัญชี 7 วัน

    public static void saveCredential(String username, String password, String role) {
        TimeBasedEncryption tbe = new TimeBasedEncryption(SECRET_KEY);
        String encryptedPassword = tbe.encrypt(password);
        // เพิ่มเวลาที่บัญชีถูกสร้าง
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String line = username.trim() + "," + encryptedPassword.trim() + "," + role.trim() + "," + timestamp;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIAL_FILE, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean authenticateCredential(String username, String password, String role) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIAL_FILE))) {
            String line;
            TimeBasedEncryption tbe = new TimeBasedEncryption(SECRET_KEY);
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String storedUsername = parts[0].trim();
                    String storedEncryptedPassword = parts[1].trim();
                    String storedRole = parts[2].trim();
                    String storedTimestamp = parts[3].trim();
                    // เช็คว่า account หมดอายุหรือไม่
                    if (isAccountExpired(storedTimestamp)) {
                        continue;
                    }
                    if (storedUsername.equals(username.trim()) && storedRole.equalsIgnoreCase(role.trim())) {
                        String decrypted = tbe.decrypt(storedEncryptedPassword).trim();
                        if (decrypted.equals(password.trim())) {
                            return true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isAccountExpired(String timestamp) {
        try {
            LocalDateTime created = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime expired = created.plusDays(EXPIRATION_DAYS);
            return LocalDateTime.now().isAfter(expired);
        } catch(Exception e) {
            return true; // ถ้า parse ไม่ได้ ถือว่าหมดอายุ
        }
    }

    public static void ensureDefaultAdmin() {
        File file = new File(CREDENTIAL_FILE);
        boolean hasAdmin = false;
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.split(",");
                    if (parts.length == 4 && parts[2].trim().equalsIgnoreCase("Admin")) {
                        hasAdmin = true;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!hasAdmin) {
            saveCredential("admin", "admin", "Admin");
        }
    }

    public static List<String> getUserList() {
        List<String> userList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIAL_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[2].trim().equalsIgnoreCase("User")) {
                    // เช็คหมดอายุ
                    if (!isAccountExpired(parts[3].trim())) {
                        userList.add(parts[0].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }
}
