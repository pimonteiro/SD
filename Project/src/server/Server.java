package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(1234);
            Map<Integer,Container> c = new HashMap<>();
            c.put(0,new Container(0, "t3.micro", 5));
            c.put(1, new Container(1, "t3.micro", 5));
            c.put(2, new Container(2, "t3.micro", 5));
            c.put(3, new Container(3, "m5.large", 10));
            c.put(4, new Container(4, "m5.large", 10));
            c.put(5, new Container(5, "h7.normal", 7));
            c.put(6, new Container(6, "h7.normal", 7));
            c.put(7, new Container(7, "h7.normal", 7));
            c.put(8, new Container(8, "l2.large", 13));
            c.put(9, new Container(9, "l2.large", 13));
            c.put(10, new Container(10, "p1.mega", 50));
            c.put(11, new Container(11, "p1.mega", 50));
            c.put(12, new Container(12, "p1.mega", 50));
            c.put(13, new Container(13, "f6.min", 4));
            c.put(14, new Container(14, "f6.min", 4));
            c.put(15, new Container(15, "f6.min", 4));
            Middleware m = new Middleware(c);
            Timer t = new Timer(m);
            Thread tr = new Thread(t);
            tr.start();
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            executor.setKeepAliveTime(2, TimeUnit.HOURS); //A verificar e talvez testar outros valores para ver improvments

            while(true){
                ServerThreadConnection thc = new ServerThreadConnection(ss.accept(), m);
                executor.submit(thc);
                //thc.start();
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
