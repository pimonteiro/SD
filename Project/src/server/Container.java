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
    private float auction_price;

    public Container() {
   }

    public Container(int id, String type, float price){
        this.id = id;
        this.price = price;
        this.type = type;
        this.user = null;
        this.auction_price = 0;
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

    public float getAuction_price(){
        return this.auction_price;
    }

    public void setAuction_price(float auction_price) {
        this.auction_price = auction_price;
    }

    public void alocateContainner(User user, LocalDateTime startDate){
        this.user= user;
        this.startDate = startDate;
    }

    public void freeContainner(){
        Duration duration = Duration.between(this.startDate, LocalDateTime.now());
        long diff = Math.abs(duration.toMinutes());
        if(auction_price != 0)
            this.user.setDebt(auction_price*diff);
        else
            this.user.setDebt(price*diff);
        this.auction_price = 0;
        this.user=null;
    }

    @Override
    public String toString(){
        Duration duration = Duration.between(this.startDate, LocalDateTime.now());
        long diff = Math.abs(duration.toMinutes());

        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + this.id + "\n");
        sb.append("Type: " + this.type + "\n");
        sb.append("UpTime: " + diff + "sec\n");

        return sb.toString();
    }
}
