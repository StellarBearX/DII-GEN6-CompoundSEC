class Keycard {
    private String cardId;
    private int accessLevel;


    public Keycard(String cardId, int accessLevel) {
        this.cardId = encrypt(cardId);
        this.accessLevel = accessLevel;
    }


    private String encrypt(String data) {
        return new StringBuilder(data).reverse().toString(); // Simple encryption (reverse string)
    }


    public String getCardId() {
        return new StringBuilder(cardId).reverse().toString(); // Decrypt before returning
    }


    public int getAccessLevel() {
        return accessLevel;
    }


    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
