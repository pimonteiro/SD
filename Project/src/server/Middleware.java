package server;

import common.User;
import common.UsernameTakenException;
import common.WrongPasswordException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Middleware {
    //Logic of Business here
    private Map<Integer,Container> containers;
    private Map<String,User> users;
    private Map<Integer,Auction> auctions;
    private Map<Integer,Reservation> reservations;
    private Lock userLock;
    private Lock auctionLock;

    public Middleware() {
        this.containers = new HashMap<>();
        this.users = new HashMap<>();
        this.auctions = new HashMap<>();
        this.userLock = new ReentrantLock();
        this.auctionLock = new ReentrantLock();
    }

    public void signUp(String username, String email, String password) throws UsernameTakenException {
        userLock.lock();
        try {
            if (users.containsKey(username))
                throw new UsernameTakenException("The username that you have chosen is taken");

            users.put(username, new User(users.size(), username, email, password));
        } finally {
            userLock.unlock();
        }
    }

    public User login(String email, String password) throws WrongPasswordException {
        User user;
        userLock.lock();
        try {
            user = users.get(email);
            if (user == null || !user.authentication(user.getEmail(), user.getPassword()))
                throw new WrongPasswordException("Your username or password is incorrect");
        } finally {
            userLock.unlock();
        }
        return user;
    }
    public int startAuction(Container container, String type){
        int id;
        auctionLock.lock();
        try {
            id = this.auctions.size() + 1;
            Auction auction = new Auction(id, container);
            this.auctions.put(id,auction);
        }
        finally {
            auctionLock.unlock();
        }
        return id;
    }
    public Bid closeAuction(int id) {
        Auction auction;
        auctionLock.lock();
        try {
            auction = auctions.get(id);
            auctions.remove(id);
        } finally {
            auctionLock.unlock();
        }

        return auction.closeAuction();
    }
    
    public void startReservation(User user, String type, float time){
        Container container;
        userLock.lock();
        List<Container> containerList = new ArrayList<>(containers.values());
        for (Container c: containerList) {
            if(c.getType().equals(type) && c.getUser()==null){
                container = c;
                container.alocateContainner(user,time);
                int id = this.reservations.size()+1;
                Reservation r = new Reservation(id,user,container);
                reservations.put(reservations.size()+1,r);
            }
        }
        userLock.unlock();
    }

    public void closeReservation(int id){
        userLock.lock();
        Reservation r = reservations.get(id);
        r.getContainer().setUser(null);
        reservations.remove(id);
        userLock.unlock();
    }
}
