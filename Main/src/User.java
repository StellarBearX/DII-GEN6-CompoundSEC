class User {
    private String name;
    private Keycard keycard;


    public User(String name, Keycard keycard) {
        this.name = name;
        this.keycard = keycard;
    }


    public String getName() {
        return name;
    }


    public Keycard getKeycard() {
        return keycard;
    }
}


