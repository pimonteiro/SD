package common;

import server.Container;

public class Pair {
   private Container c;
   private int id; //-1 when not alocated <0 when atributed to certain user

    public Pair() {
    }
    public Pair(Container c, int id) {
        this.c = c;
        this.id = id;
    }

    public Container getC() {
        return c;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
