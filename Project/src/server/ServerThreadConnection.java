package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThreadConnection extends Thread{
    private Socket s;
    private Middleware m;

    public ServerThreadConnection(Socket s, Middleware m){
        this.s = s;
        this.m = m;
    }

    @Override
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());

            String c_input;
            ServerThreadController sc = new ServerThreadController(m);

            do {
                try {
                    c_input = in.readLine().toLowerCase();
                } catch (NullPointerException e){
                    c_input = "quit";
                }

                if(c_input.contains("login")){
                    sc.login(c_input, out);
                }
                if(c_input.contains("register")){
                    sc.register(c_input, out);
                }
                if(c_input.contains("myaccount")){
                    sc.checkMyAccount(c_input, out);
                }
                if(c_input.contains("myservers")){
                    sc.checkMyServers(c_input, out);
                }
                if(c_input.contains("cancel")){
                    sc.cancelServer(c_input, out);
                }
                if(c_input.contains("reserve")){
                    sc.reserveServer(c_input, out);
                }
                if(c_input.contains("auction")){
                    sc.auctionServer(c_input, out);
                }

            } while (!c_input.equals("quit"));

            s.shutdownInput();
            s.shutdownOutput();
            s.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
