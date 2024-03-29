package server;

import common.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Middleware implements CloseableAuction {
    //Logic of Business here
    private Map<Integer, Container> containers;
    private Map<String, User> users;
    private Map<Integer, Auction> auctions;
    private List<Integer> idContainner; // containers id of allocated container in auction
    private Map<Integer, Reservation> reservations;
    private Map<String, List<Pair>> queue;
    private int na;
    private int nr;
    private ReentrantReadWriteLock userLock;
    private ReentrantReadWriteLock auctionLock;

    public Middleware(Map<Integer, Container> c) {
        this.containers = c;
        this.users = new HashMap<>();
        this.auctions = new HashMap<>();
        this.reservations = new HashMap<>();
        this.idContainner = new ArrayList<>();
        this.userLock = new ReentrantReadWriteLock();
        this.auctionLock = new ReentrantReadWriteLock();
        this.queue = new HashMap<>();
        this.na = 0;
        this.nr = 0;
    }

    public void signUp(String username, String email, String password) throws UsernameTakenException {
        userLock.writeLock().lock();
        if (users.containsKey(username)) {
            throw new UsernameTakenException("The username that you have chosen is taken");
        }
        users.put(email, new User(users.size(), username, email, password));
        userLock.writeLock().unlock();
    }

    public String login(String email, String password) throws WrongPasswordException {
        User user;
        userLock.readLock().lock();
        try {
            user = users.get(email);
            if (user == null || !user.authentication(email, password))
                throw new WrongPasswordException("Your username or password is incorrect.");
        } finally {
            userLock.readLock().unlock();
        }
        return email;
    }

    public int startAuction(String email, String type, float price) throws ContainerNotAvailableException, InsufficientMoneyException {
        int id = -1;
        auctionLock.writeLock().lock();
        try {
            User user = users.get(email);
            Auction a = null;
            List<Container> containerList = new ArrayList<>(containers.values());
            for (Auction t : this.auctions.values()) {
                if (t.getContainer().getType().equals(type)) {
                    t.bid(user, price);
                    break;
                }
            }
            for (Container c : containerList) {
                if (c.getType().equals(type) && c.getUser() == null) {
                    this.na++;
                    id = this.na;
                    a = new Auction(na, user, c, price);
                    c.setUser(User.actioning);
                    auctions.put(id, a);
                    idContainner.add(c.getId());
                    break;
                }
            }
            if (a == null) {
                if (this.queue.containsKey(type)) {
                    this.queue.get(type).add(new Pair(user, price));
                } else {
                    List<Pair> u = new ArrayList<>();
                    u.add(new Pair(user, price));
                    queue.put(type, u);
                }
                throw new ContainerNotAvailableException("There are no containers available for auction, you are queued");
            }
        } finally {
            auctionLock.writeLock().unlock();
        }
        return id;
    }

    public void closeAuction(int id) throws IDNotFoundException {
        Auction auction;
        auctionLock.writeLock().lock();
        try {
            if (!auctions.containsKey(id)) throw new IDNotFoundException("The auction id you inserted does not exist");
            auction = auctions.get(id);
            auctions.remove(id);
            Bid b = auction.closeAuction();

            this.nr++;
            auction.getContainer().alocateContainner(b.getBuyer(), LocalDateTime.now());
            auction.getContainer().setAuction_price(b.getPrice());
            Reservation r = new Reservation(this.nr, b.getBuyer(), auction.getContainer());
            reservations.put(this.nr, r);
        } finally {
            auctionLock.writeLock().unlock();
        }
    }

    public void startReservation(String id, String type) throws ContainerNotAvailableException {
        Container container;
        userLock.writeLock().lock();
        try {
            Reservation r = null;
            List<Container> containerList = new ArrayList<>(containers.values());
            for (Container c : containerList) {
                if (c.getType().equals(type) && c.getUser() == null) {
                    container = c;
                    container.alocateContainner(this.users.get(id), LocalDateTime.now());
                    this.nr++;
                    r = new Reservation(this.nr, this.users.get(id), container);
                    reservations.put(this.nr, r);
                    return;
                }
            }
            int i;
            if (idContainner.isEmpty())
                throw new ContainerNotAvailableException("There are no containers available for reservation");
            i = idContainner.remove(0);
            Container c = containers.get(i);
            c.alocateContainner(this.users.get(id), LocalDateTime.now());
            this.nr++;
            r = new Reservation(this.nr, this.users.get(id), c);
            reservations.put(this.nr, r);
        } finally {
            userLock.writeLock().unlock();
        }
    }

    public void closeReservation(String email, int id) throws IDNotFoundException, InsufficientMoneyException {
        userLock.writeLock().lock();
        try {
            boolean flag = false;
            for (Reservation r : reservations.values()) {
                Container c = r.getContainer();
                if (c.getId() == id && c.getUser().getEmail().equals(email)) {
                    c.freeContainner();
                    reservations.remove(r.getId());
                    if (queue.containsKey(containers.get(id).getType()) && queue.get(containers.get(id).getType()).get(0) != null) { // ir buscar leiloes a queue
                        Pair best = null;
                        for (Pair p : queue.get(containers.get(id).getType())) {
                            if (best == null || p.getPrice() > best.getPrice()) {
                                best = p;
                            }
                        }
                        this.startAuction(best.getC().getEmail(), containers.get(id).getType(), best.getPrice());
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) throw new IDNotFoundException("The container id you inserted is not allocated to you");
        } catch (ContainerNotAvailableException e) {
            e.printStackTrace();
        } finally {
            userLock.writeLock().unlock();
        }
    }

    public List<Container> getUserAllocatedContainers(String email) {
        userLock.readLock().lock();
        User u = users.get(email);
        String id = u.getId();
        List<Container> ret = new ArrayList<>();
        for (Container c : this.containers.values()) {
            if (c.getUser() != null && c.getUser().getId().equals(id)) {
                ret.add(c);
            }
        }
        userLock.readLock().unlock();
        return ret;
    }

    public String getUserInfo(String email) {
        userLock.readLock().lock();
        User u = this.users.get(email);
        userLock.readLock().unlock();
        return "ID: " + u.getId() + "\n" + "Name: " + u.getName() + "\n" + "Email: " + u.getEmail() + "\n" + "Debt: " + u.getDebt();
    }

    public List<Integer> getIdContainner() {
        return this.idContainner;
    }

    public Auction getAuction(int id) {
        return auctions.get(id);
    }

    @Override
    public void closeAuctions() {
        Collection<Auction> auctions = this.auctions.values();
        long sleep = 0;
        if (auctions.isEmpty()) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        for (Auction a : auctions) {
            if ((currentTimeMillis - a.getStart()) > 10000) {
                try {
                    this.closeAuction(a.getId());
                } catch (IDNotFoundException e) {
                    e.printStackTrace();
                }
                if (sleep < currentTimeMillis - a.getStart()) {
                    sleep = currentTimeMillis - a.getStart();
                }
            }
        }
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
