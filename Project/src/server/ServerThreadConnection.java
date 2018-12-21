package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreadConnection extends Thread{
    private Socket s;

    public ServerThreadConnection(Socket s){
        this.s = s;
    }

    @Override
    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            PrintWriter out = new PrintWriter(s.getOutputStream());

            String t;
            do {

            } while (t != null);

            s.shutdownInput();
            s.shutdownOutput();
            s.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
