package server;

import common.User;

public class Reservation {
    private final int id;
    private User user;
    private Container container;

    public Reservation(int id, User user, Container container) {
        this.id = id;
        this.user = user;
        this.container = container;
    }

    public User getUser() {
        return user;
    }

    public Container getContainer() {
        return container;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContainer(Container container) {
        this.container = container;
    }
}
