package server;

import common.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class Container {
    private int id;
    private String type;
    private float price;
    private LocalDateTime startDate;
    private User user; //null if the container is not allocated to any user

    public Container() {
   }

    public Container(int id, String type, float price){
        this.id = id;
        this.price = price;
        this.type = type;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void alocateContainner(User user, LocalDateTime startDate){
        this.user= user;
        this.startDate = startDate;
    }

    public void freeContainner(){
        Duration duration = Duration.between(this.startDate, LocalDateTime.now());
        long diff = Math.abs(duration.toDays());
        this.user.setDebt(price*diff);
        this.user=null;
    }

    @Override
    public String toString(){
        Duration duration = Duration.between(this.startDate, LocalDateTime.now());
        long diff = Math.abs(duration.toDays());

        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + this.id + "\n");
        sb.append("Type: " + this.type + "\n");
        sb.append("UpTime: " + diff + "\n");

        return sb.toString();
    }
}
