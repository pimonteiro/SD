package common;

import server.Container;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String email;
    private String password;
    private List<Container> history;
    private float debt;

    public User() {

    }

    public User(int id, String email, String password) {
        this.id = String.valueOf(id+1);
        this.email = email;
        this.password = password;
        this.history = new ArrayList<>();
        this.debt=0;
    }

    public float getDebt() {
        return debt;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean authentication(String email, String password){
        return(this.email.equals(email) && this.password.equals(password));
    }


}
