package common;

public class User {
    private final String id;
    private String name;
    private String email;
    private String password;
    // private List<Container> history;
    private float debt;


    public User(int id, String name, String email, String password) {
        this.id = String.valueOf(id+1);
        this.name = name;
        this.email = email;
        this.password = password;
        //this.history = new ArrayList<>();
        this.debt=0;
    }

    public String getId() {
        return id;
    }

    public float getDebt() {
        return debt;
    }

    public void setDebt(float debt) {
        this.debt = debt;
    }

    public String getName(){
        return this.name;
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
