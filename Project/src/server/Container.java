package server;

import common.User;

public class Container {
    private int id;
    private String type;
    private float price;
    private float timeAloc;
    private User user; //null if the container is not allocated to any user

    public Container() {
   }

    public Container(int id, String type, float price){
        this.id = id;
        this.price = price;
        this.type = type;
        this.timeAloc = 0;
        this.user = null;
    }
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public float getTimeAloc() {
        return timeAloc;
    }

    public User getUser() {
        return user;
    }

    public void setTimeAloc(float timeAloc) {
        this.timeAloc = timeAloc;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void alocateContainner(User user, float time){
        this.user= user;
        user.setDebt(time*this.price+user.getDebt());
    }
}
