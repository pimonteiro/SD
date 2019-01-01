package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try {
            Socket s = new Socket("localhost",1234);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());

            ClientController cc = new ClientController();
            int flg;
            do {
                flg = cc.welcomeMessage(in, out);
            } while(flg == 1);


            Scanner sn = new Scanner(System.in);
            String c_input;

            System.out.println("\n=======Welcome back.=========\n");
            do{
                cc.menuScreen();
                c_input = sn.next().toLowerCase();

                if(c_input.equals("myaccount")){
                    cc.checkMyAccount(in, out);
                }
                if(c_input.equals("myservers")){
                    cc.checkMyServers(in, out);
                }
                if(c_input.equals("reserve")){
                    cc.reserveServer(in, out);
                }
                if(c_input.equals("cancel")){
                    cc.cancelServer(in, out);
                }
                if(c_input.equals("auction")){
                    cc.auctionServer(in,out);
                }


            } while(!c_input.equals("quit"));
            out.println("quit");
            out.flush();

            System.out.println("Have a great day!");
            s.shutdownInput();
            s.shutdownOutput();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
