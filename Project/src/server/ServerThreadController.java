package server;

import common.ContainerNotAvailableException;
import common.IDNotFoundException;
import common.UsernameTakenException;
import common.WrongPasswordException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ServerThreadController{
    private Middleware m;

    public ServerThreadController(Middleware m){
        this.m = m;
    }

    public void login(String input, PrintWriter out) {
        try {
            String tmp[] = input.split("-");
            String idUser = m.login(tmp[1], tmp[2]);
            out.println("suc-" + idUser);
            out.flush();
        } catch (WrongPasswordException e){
            out.println("error-" + e.getMessage());
            out.flush();
        }
    }

    public void register(String input, PrintWriter out) {
        try {
            String req[] = input.split("-");
            m.signUp(req[1], req[2], req[3]);
            out.println("suc-");
            out.flush();
        } catch (UsernameTakenException e){
            out.println("error-" + e.getMessage());
            out.flush();
        }
    }

    public void checkMyAccount(String input, PrintWriter out) {
        String req[] = input.split("-");
        String info = m.getUserInfo(req[1]);
        out.println(info);
        out.flush();
    }

    public void checkMyServers(String input, PrintWriter out) {
        String[] req = input.split("-");
        List<Container> containers = m.getUserAllocatedContainers(req[1]);
        StringBuilder resp = new StringBuilder();
        for(Container c : containers){
            resp.append("======================\n");
            resp.append(c.toString());
            resp.append("======================\n");
        }
        out.println(resp.toString()); //Maybe change becuase its gonna screw up the format while printing
        out.flush();
    }

    public void cancelServer(String input, PrintWriter out) {
        try {
            String[] req = input.split("-");
            m.closeReservation(Integer.parseInt(req[1]));
            out.println("suc-");
            out.flush();
        }catch (IDNotFoundException e){
            out.println("error-" + e.getMessage());
            out.flush();
        }
    }

    public void reserveServer(String input, PrintWriter out) {
        try {
            String[] req = input.split("-");
            m.startReservation(req[1], req[2]);
            out.println("suc-");
            out.flush();
        } catch (ContainerNotAvailableException e) {
            out.println("error-");
            out.flush();
        }
    }
}
