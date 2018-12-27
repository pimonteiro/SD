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
            Scanner sn = new Scanner(System.in);
            ServerThreadController sc = new ServerThreadController(m);

            do {
                c_input = sn.nextLine().toLowerCase();
                if(c_input.contains("login")){
                    sc.login(in, out);
                }
                if(c_input.contains("register")){
                    sc.register(in, out);
                }
                if(c_input.equals("myaccount")){
                    sc.checkMyAccount(in, out);
                }
                if(c_input.equals("myservers")){
                    sc.checkMyServers(in, out);
                }
                if(c_input.contains("cancel")){
                    sc.cancelServer(in, out);
                }
                if(c_input.contains("reserve")){
                    sc.reserveServer(in, out);
                }



                if(c_input.equals("auction")){
                    // Have no idea
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
