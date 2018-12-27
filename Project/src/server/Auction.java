package server;

import common.InsufficientMoneyException;
import common.User;

public class Auction {
    private final int id;
    private Container container;
    private float price;
    private Bid best;

    public Auction(int id, User user, Container container, float price) {
        this.id = id;
        this.container = container;
        this.price = price;
        this.best = new Bid(user,price);
    }

    public Container getContainer() {
        return container;
    }

    synchronized public void bid(User user, float price) throws InsufficientMoneyException {
        if(best.getPrice() >= price){
            best = new Bid(user,price);
        }
        else throw  new InsufficientMoneyException("Your bid is too low");
    }

    public Bid closeAuction(){
        return best;
    }
}
