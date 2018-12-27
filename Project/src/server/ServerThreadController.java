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

    public void login(BufferedReader in, PrintWriter out) {
        try {
            String req = in.readLine();
            String tmp[] = req.split("-");
            String idUser = m.login(tmp[1], tmp[2]);
            out.println("suc - " + idUser);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongPasswordException e){
            out.println("error - " + e.getMessage());
            out.flush();
        }
    }

    public void register(BufferedReader in, PrintWriter out) {
        try {
            String req[] = in.readLine().split("-");
            m.signUp("","","");
            out.println("suc - ");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UsernameTakenException e){
            out.println("error - " + e.getMessage());
            out.flush();
        }
    }

    public void checkMyAccount(BufferedReader in, PrintWriter out) {
        try {
            String req[] = in.readLine().split("-");
            String info = m.getUserInfo(req[1]);
            out.println(info);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkMyServers(BufferedReader in, PrintWriter out) {
        try {
            String[] req = in.readLine().split("-");
            List<Container> containers = m.getUserAllocatedContainers(req[1]);
            StringBuilder resp = new StringBuilder();
            for(Container c : containers){
                resp.append("======================\n");
                resp.append(c.toString());
                resp.append("======================\n");
            }
            out.println(resp.toString()); //Maybe change becuase its gonna screw up the format while printing
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void cancelServer(BufferedReader in, PrintWriter out) {
        try {
            String[] req = in.readLine().split("-");
            m.closeReservation(Integer.parseInt(req[1]));
            out.println("suc - ");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IDNotFoundException e){
            out.println("error - " + e.getMessage());
            out.flush();
        }
    }

    public void reserveServer(BufferedReader in, PrintWriter out) {
        try {
            String[] req = in.readLine().split("-");
            m.startReservation(req[1], req[2]);
            out.println("suc - ");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ContainerNotAvailableException e) {
            out.println("error - ");
            out.flush();
        }
    }
}
