package client;


import common.UserServerActions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientController implements UserServerActions {

    private String idUser;

    public ClientController(){
        this.idUser = "";
    }

    //Fix this
    public void menuScreen(){
        System.out.println("Available commands:");
        System.out.println("> myaccount -details of my account");
        System.out.println("> myservers -check my current reservations");
        System.out.println("> reserve -reserve server");
        System.out.println("> cancel -cancel reservation" );
        System.out.println("> auction -start auction");
        System.out.println("> quit -to exit");
    }

    public int welcomeMessage(BufferedReader in, PrintWriter out){
        System.out.println("> Welcome to CloudComputing Hosting.");
        System.out.println("> Please choose one of the following:");
        System.out.println("[1] Login");
        System.out.println("[2] Register");
        Scanner sn = new Scanner(System.in);
        int op = sn.nextInt();
        int lFlag = 0;
        if(op == 1){
            lFlag = login(in, out);
            if(lFlag == 1)
                return 2;
            return 1;
        }
        if(op == 2){
            register(in, out);
            System.out.println("> Registration Sucessfull!");
            return 1;
        }
        System.out.println("> Wrong option.Please try again.");
        return 1;
    }

    public int login(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sn.next();
        System.out.print("Password: ");
        String password = sn.next();
        String req = "login -" + username + " - " + password;
        out.println(req);
        out.flush();

        try {
            String[] resp = in.readLine().split("-"); //wait for answer
            if(resp[0].equals("suc")) {
                this.idUser = resp[1];
                System.out.println("> Login Sucessful!");
                return 1;
            }
            else {
                System.out.println("> Error: " + resp[1]);
                return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void register(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        System.out.print("Name: ");
        String name = sn.next();
        System.out.print("Email: ");
        String email = sn.next();
        System.out.print("Password: ");
        String password = sn.next();
        String req = "register - " + name + "-" + email + "-" + password;

        out.println(req);
        out.flush();

        try {
           String resp = in.readLine(); //wait for answer
            //Tratar Resposta
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkMyAccount(BufferedReader in, PrintWriter out) {
        Scanner sn = new Scanner(System.in);
        String req = "myaccount";
        out.println(req);
        out.flush();

        try{
            String resp = in.readLine(); //wait for answer

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkMyServers(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        String req = "myservers";
        out.println(req);
        out.flush();

        try {
            String resp = in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelServer(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        System.out.println("ID Container: ");
        String idContainer = sn.next();
        String req = "cancel-" + idContainer;

        try {
            String resp = in.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void reserveServer(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        String req = "reserve";

        try {
            String resp = in.readLine();
            System.out.println(resp);


        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
