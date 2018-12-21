package server;

import common.*;

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
    private int na;
    private Map<Integer,Reservation> reservations;
    private int nr;
    private Lock userLock;
    private Lock auctionLock;

    public Middleware() {
        this.containers = new HashMap<>();
        this.users = new HashMap<>();
        this.auctions = new HashMap<>();
        this.userLock = new ReentrantLock();
        this.auctionLock = new ReentrantLock();
        this.na = 0;
        this.nr = 0;
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
    public int startAuction(Container container, String type) throws ContainerNotAvailableException{
        int id=-1;
        auctionLock.lock();
        try {
            Auction a=null;
            List<Container> containerList = new ArrayList<>(containers.values());
            for (Container c : containerList) {
                if (c.getType().equals(type) && c.getUser() == null) {
                    container = c;
                    this.na++;
                    id = this.nr;
                    a = new Auction(id, container);
                    auctions.put(id, a);
                }
            }
            if(a==null) throw new ContainerNotAvailableException("There are no containers available for auction");
        }
        finally {
            auctionLock.unlock();
        }
        return id;
    }
    public Bid closeAuction(int id) throws IDNotFoundException{
        Auction auction;
        auctionLock.lock();
        try {
            if(!auctions.containsKey(id)) throw new IDNotFoundException("The auction id you inserted does not exist");
            auction = auctions.get(id);
            auctions.remove(id);
        } finally {
            auctionLock.unlock();
        }

        return auction.closeAuction();
    }
    
    public void startReservation(User user, String type, float time) throws ContainerNotAvailableException{
        Container container;
        userLock.lock();
        try {
            Reservation r=null;
            List<Container> containerList = new ArrayList<>(containers.values());
            for (Container c : containerList) {
                if (c.getType().equals(type) && c.getUser() == null) {
                    container = c;
                    container.alocateContainner(user, time);
                    this.nr++;
                    int id = this.nr;
                    r = new Reservation(id, user, container);
                    reservations.put(id, r);
                }
            }
            if(r==null) throw new ContainerNotAvailableException("There are no containers available for reservation");
        }
        finally {
            userLock.unlock();
        }
    }

    public void closeReservation(int id) throws IDNotFoundException {
        userLock.lock();
        try {
            if(!reservations.containsKey(id)) throw new IDNotFoundException("The reservation id you inserted does not exist");
            Reservation r = reservations.get(id);
            r.getContainer().setUser(null);
            reservations.remove(id);
        }
        finally {
            userLock.unlock();
        }
    }
}
