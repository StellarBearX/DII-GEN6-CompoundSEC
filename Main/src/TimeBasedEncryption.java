import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeBasedEncryption extends Security {
    public TimeBasedEncryption(String secretKey) {
        super(secretKey);
    }

    @Override
    public String encrypt(String data) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String combined = data + "_" + timeStamp;
        return super.encrypt(combined);
    }

    @Override
    public String decrypt(String data) {
        String decrypted = super.decrypt(data);
        int index = decrypted.lastIndexOf("_");
        if (index != -1) {
            return decrypted.substring(0, index);
        }
        return decrypted;
    }

    // Overloaded method (Ad-Hoc Polymorphism)
    public String encrypt(String data, String format) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
        String combined = data + "_" + timeStamp;
        return super.encrypt(combined);
    }
}
