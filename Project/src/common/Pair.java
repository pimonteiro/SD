package common;

import server.Container;

public class Pair {
   private User c;
   private float price;

    public Pair() {
    }
    public Pair(User c, float i) {
        this.c = c;
        this.price = i;
    }

    public User getC() {
        return c;
    }

    public float getPrice() {
        return price;
    }
}
