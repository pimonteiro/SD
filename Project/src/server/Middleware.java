package server;

import common.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private Lock userLock;
    private Lock auctionLock;

    public Middleware(Map<Integer, Container> c) {
        this.containers = c;
        this.users = new HashMap<>();
        this.auctions = new HashMap<>();
        this.reservations = new HashMap<>();
        this.idContainner = new ArrayList<>();
        this.userLock = new ReentrantLock();
        this.auctionLock = new ReentrantLock();
        this.queue = new HashMap<>();
        this.na = 0;
        this.nr = 0;
    }

    public void signUp(String username, String email, String password) throws UsernameTakenException {
        if (users.containsKey(username))
            throw new UsernameTakenException("The username that you have chosen is taken");
        userLock.lock();
        users.put(email, new User(users.size(), username, email, password));
        userLock.unlock();
    }

    public String login(String email, String password) throws WrongPasswordException {
        User user;
        userLock.lock();
        try {
            user = users.get(email);
            if (user == null || !user.authentication(email, password))
                throw new WrongPasswordException("Your username or password is incorrect.");
        } finally {
            userLock.unlock();
        }
        return email;
    }

    public int startAuction(String email, String type, float price) throws ContainerNotAvailableException {
        int id = -1;
        System.out.println(email);
        auctionLock.lock();
        System.out.println(email);
        try {
            User user = users.get(email);
            Auction a = null;
            List<Container> containerList = new ArrayList<>(containers.values());
            for (Container c : containerList) {
                if (c.getType().equals(type) && c.getUser() == null) {
                    this.na++;
                    id = this.na;
                    a = new Auction(na, user, c, price);
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
            auctionLock.unlock();
        }
        return id;
    }

    public void closeAuction(int id) throws IDNotFoundException {
        Auction auction;
        auctionLock.lock();
        try {
            if (!auctions.containsKey(id)) throw new IDNotFoundException("The auction id you inserted does not exist");
            auction = auctions.get(id);
            auctions.remove(id);
            Bid b = auction.closeAuction();
            this.idContainner.add(auction.getContainer().getId());
            auction.getContainer().alocateContainner(b.getBuyer(), LocalDateTime.now());
        } finally {
            auctionLock.unlock();
        }
    }

    public void closeAuctionReserve(int id) throws IDNotFoundException {
        userLock.lock();
        try {
            if (idContainner.contains(id)) {
                this.containers.get(id).freeContainner();
                this.idContainner.remove(id);

            } else throw new IDNotFoundException("The ID you inserted does not exist");
        } finally {
            userLock.unlock();
        }
    }

    public void startReservation(String id, String type) throws ContainerNotAvailableException {
        Container container;
        System.out.println("começou reserva");
        userLock.lock();
        try {
            Reservation r = null;
            System.out.println("lock");
            List<Container> containerList = new ArrayList<>(containers.values());
            for (Container c : containerList) {
                System.out.println("estou no for");
                if (c.getType().equals(type) && c.getUser() == null) {
                    container = c;
                    container.alocateContainner(this.users.get(id), LocalDateTime.now());
                    this.nr++;
                    r = new Reservation(this.nr, this.users.get(id), container);
                    reservations.put(this.nr, r);
                    System.out.println("reservou");
                    return;
                }
            }
            System.out.println("nao reservou");
            int i;
            try {
                System.out.println("estou a tentar roubar");
                i = idContainner.get(0); //TODO voltar a ver, porque nao é justo tirar ao que ta mais tempo?
                Container c = containers.get(i);
                idContainner.remove(0);
                this.closeAuctionReserve(c.getId());
                c.alocateContainner(this.users.get(id), LocalDateTime.now());
                this.nr++;
                r = new Reservation(this.nr, this.users.get(id), c);
                reservations.put(this.nr, r);
                System.out.println("roubei");
            } catch (IndexOutOfBoundsException | IDNotFoundException e) {
                throw new ContainerNotAvailableException("There are no containers available for reservation");
            }
        } finally {
            userLock.unlock();
        }
    }

    public void closeReservation(String email, int id) throws IDNotFoundException {
        userLock.lock();
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
            userLock.unlock();
        }
    }

    public List<Container> getUserAllocatedContainers(String email) {
        User u = users.get(email);
        String id = u.getId();
        List<Container> ret = new ArrayList<>();
        List<Reservation> r = new ArrayList<>(this.reservations.values());
        for (Reservation t : r) {
            if (t.getContainer().getUser().getId().equals(id)) {
                ret.add(t.getContainer());
            }
        }
        for (Integer i : this.idContainner) {
            if (containers.get(i).getUser().getId().equals(id)) {
                ret.add(containers.get(i));
            }
        }
        return ret;
    }

    public String getUserInfo(String email) {
        User u = this.users.get(email);
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
        if (!auctions.isEmpty()) {
            for (Auction a : auctions) {
                System.out.println(System.currentTimeMillis() + " " + a.getStart());
                if ((System.currentTimeMillis() - a.getStart()) > 10000) { //TODO deviamos por mais tempo
                    try {
                        this.closeAuction(a.getId());
                    } catch (IDNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (sleep < System.currentTimeMillis() - a.getStart()) {
                        sleep = System.currentTimeMillis() - a.getStart();
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
}
