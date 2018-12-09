import java.util.List;

public class User {
    private String id;
    private String email;
    private String password;
    private List<Containner> history;

    public User() {

    }

    public User(String id, String email, String password, List<Containner> history) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.history = history;
    }


}
