import java.util.HashMap;
import java.util.Map;
class Admin extends User {
    private Map<String, Integer> userAccessControl;


    public Admin(String name, Keycard keycard) {
        super(name, keycard);
        this.userAccessControl = new HashMap<>();
    }


    public void grantAccess(User user, Door door) {
        userAccessControl.put(user.getName(), door.getRequiredAccessLevel());
    }


    public void revokeAccess(User user) {
        userAccessControl.remove(user.getName());
    }


    public void changeAccessLevel(User user, int level) {
        user.getKeycard().setAccessLevel(level);
    }


    public Map<String, Integer> getUserAccessControl() {
        return userAccessControl;
    }
}
