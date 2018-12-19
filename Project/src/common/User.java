package common;

import server.Container;

import java.util.List;

public class User {
    private String id;
    private String email;
    private String password;
    private List<Container> history;

    public User() {

    }

    public User(String id, String email, String password, List<Container> history) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.history = history;
    }


}
