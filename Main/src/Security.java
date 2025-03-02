public class Security {
    private String secretKey;

    public Security(String secretKey) {
        this.secretKey = secretKey;
    }

    public String encrypt(String data) {
        return new StringBuilder(data + secretKey).reverse().toString();
    }

    public String decrypt(String data) {
        String reversed = new StringBuilder(data).reverse().toString();
        if (reversed.endsWith(secretKey)) {
            return reversed.substring(0, reversed.length() - secretKey.length());
        }
        return reversed;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
