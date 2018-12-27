package server;

import common.UserServerActions;
import common.UsernameTakenException;
import common.WrongPasswordException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerThreadController{
    private Middleware m;

    public ServerThreadController(Middleware m){
        this.m = m;
    }

    public void login(BufferedReader in, PrintWriter out) {
        try {
            String req = in.readLine();
            String tmp[] = req.split("-");
            String idUser = m.login(tmp[1], tmp[2]).getId();
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



        } catch (IOException e) {
            e.printStackTrace();
        } catch (UsernameTakenException e){

        }
    }

    public void checkMyAccount(BufferedReader in, PrintWriter out) {

    }

    public void checkMyServers(BufferedReader in, PrintWriter out) {

    }

    public void cancelServer(BufferedReader in, PrintWriter out) {

    }

    public void reserveServer(BufferedReader in, PrintWriter out) {

    }
}
