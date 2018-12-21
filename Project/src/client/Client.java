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

            System.out.println("Welcome back.");
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
                    cc.reserverServer(in, out);
                }
                if(c_input.equals("cancel")){
                    cc.cancelServer(in, out);
                }
                if(c_input.equals("auction")){
                    // Have no idea
                }


            } while(c_input != "quit"); //quando fizer logout faço: c_input = "quit"; para conseguir sair do ciclo

            s.shutdownInput();
            s.shutdownOutput();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
