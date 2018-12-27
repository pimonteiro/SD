package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(1234);
            Middleware m = new Middleware();
            //TODO Povoate Containers and stuff
            while(true){
                ServerThreadConnection thc = new ServerThreadConnection(ss.accept(), m);
                thc.start();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
