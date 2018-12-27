package server;

import common.*;

import java.time.LocalDateTime;
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
    private List<Integer> idContainner; // containers id of allocated container in auction
    private Map<Integer,Reservation> reservations;
    private int na;
    private int nr;
    private Lock userLock;
    private Lock auctionLock;

    public Middleware(Map<Integer,Container> c) {
        this.containers = c;
        this.users = new HashMap<>();
        this.auctions = new HashMap<>();
        this.reservations = new HashMap<>();
        this.idContainner = new ArrayList<>();
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

    public String login(String email, String password) throws WrongPasswordException {
        User user;
        userLock.lock();
        try {
            user = users.get(email);
            if (user == null || !user.authentication(user.getEmail(), user.getPassword()))
                throw new WrongPasswordException("Your username or password is incorrect.");
        } finally {
            userLock.unlock();
        }
        return user.getId();
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
    public void closeAuction(int id) throws IDNotFoundException{
        Auction auction;
        auctionLock.lock();
        try {
            if(!auctions.containsKey(id)) throw new IDNotFoundException("The auction id you inserted does not exist");
            auction = auctions.get(id);
            auctions.remove(id);
            Bid b = auction.closeAuction();
            this.idContainner.add(auction.getContainer().getId());
            auction.getContainer().alocateContainner(b.getBuyer(),LocalDateTime.now());
        } finally {
            auctionLock.unlock();
        }
    }

    public void closeAuctionReserve(int id) throws IDNotFoundException{
        userLock.lock();
        try {
            if(idContainner.contains(id)){
                this.containers.get(id).freeContainner();
                this.idContainner.remove(id);
            }else throw new IDNotFoundException("The ID you inserted does not exist");
        }
        finally {
            userLock.unlock();
        }
    }
    
    public void startReservation(User user, String type) throws ContainerNotAvailableException{
        Container container;
        userLock.lock();
        try {
            Reservation r=null;
            List<Container> containerList = new ArrayList<>(containers.values());
            for (Container c : containerList) {
                if (c.getType().equals(type) && c.getUser() == null) {
                    container = c;
                    container.alocateContainner(user, LocalDateTime.now());
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
            r.getContainer().freeContainner();
            reservations.remove(id);
        }
        finally {
            userLock.unlock();
        }
    }

    public List<Container> getUserAllocatedContainers(String id){
        List<Container> ret = new ArrayList<>();
        List<Reservation> r = new ArrayList<>(this.reservations.values());
        for(Reservation t: r){
            if(t.getContainer().getUser().getId().equals(id)){
                ret.add(t.getContainer());
            }
        }
        for(Integer i: this.idContainner){
            if(containers.get(i).getUser().getId().equals(id)){
                ret.add(containers.get(i));
            }
        }
        return ret;
    }

    public String getUserInfo(String id){
        User u =this.users.get(id);
        return "ID: "+ u.getId()+"\n"+"Name: "+u.getName()+"\n"+"Debt: "+u.getDebt();
    }
}
