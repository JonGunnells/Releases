import java.util.ArrayList;

public class User {
    String username;
    String password;
    ArrayList<Mob> mobs = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
