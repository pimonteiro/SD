package server;

import common.User;

public class Bid {
    private final User buyer;
    private final float price;

    Bid(User buyer, float value) {
        this.buyer = buyer;
        this.price = value;
    }

    public User getBuyer() {
        return buyer;
    }

    public float getPrice() {
        return price;
    }
}
