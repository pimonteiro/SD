package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class client {

    public static void main(String[] args) {

        //Inicialização do necessário ao cliente
        ArrayList<String> cm = new ArrayList<>();
        cm.add("- descobrir_iD");
        cm.add("- criar conta <saldo>");
        cm.add("- fechar conta <id>");
        cm.add("- consultar saldo <id>");
        cm.add("- levantar dinheiro <id> <valor>");
        cm.add("- depositar dinheiro <id> <valor>");
        cm.add("- transferir dinheiro <id_origem> <id_destino> <valor>");


        try {
            System.out.println("> Connecting to the Bank server......");
            Socket s = new Socket("localhost",1234);
            System.out.println("> Connected!\n");

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream());

            Scanner sn = new Scanner(System.in);
            String c_input;
            while(true){


                c_input = sn.next().toLowerCase();

                // Ação que pode fazer
                if(cm.get(1).contains(c_input)){
                    getID(in, out, s);
                }




            }


            s.shutdownInput();
            s.shutdownOutput();
            s.close();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
