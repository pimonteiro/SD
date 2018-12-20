package server;

import common.InsufficientMoneyException;
import common.User;

public class Auction {
    private final int id;
    private Container container;
    private User currentWinner;
    private float price;
    private Bid best;

    public Auction(int id, Container container) {
        this.id = id;
        this.container = container;
        this.price = 0;
        currentWinner = null;
        this.best = new Bid(null,0);
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
