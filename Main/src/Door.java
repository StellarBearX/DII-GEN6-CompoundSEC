class Door {
    private String doorId;
    private int requiredAccessLevel;


    public Door(String doorId, int requiredAccessLevel) {
        this.doorId = doorId;
        this.requiredAccessLevel = requiredAccessLevel;
    }


    public String getDoorId() {
        return doorId;
    }


    public int getRequiredAccessLevel() {
        return requiredAccessLevel;
    }
}
