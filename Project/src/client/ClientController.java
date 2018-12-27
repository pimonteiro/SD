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

    //Verify Login method
    public int welcomeMessage(BufferedReader in, PrintWriter out){
        System.out.println("> Welcome to CloudComputing Hosting.");
        System.out.println("> Please choose one of the following:");
        System.out.println("[1] Login");
        System.out.println("[2] Register");
        Scanner sn = new Scanner(System.in);
        int op = sn.nextInt();
        int lFlag = 0;
        int rFlag = 0;
        if(op == 1){
            lFlag = login(in, out);
            if(lFlag == 1)
                return 2;
            return 1;
        }
        if(op == 2){
            rFlag = register(in, out);
            if(rFlag == 1)
                return 1;
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

    public int register(BufferedReader in, PrintWriter out){
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
            String[] resp = in.readLine().split("-"); //wait for answer
            if(resp[0].equals("suc")){
                System.out.println("> Registration Sucessfull!");
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

    public void checkMyAccount(BufferedReader in, PrintWriter out) {
        Scanner sn = new Scanner(System.in);
        String req = "myaccount - " + idUser;
        out.println(req);
        out.flush();

        try{
            String resp = in.readLine(); //wait for answer
            System.out.println(resp);
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
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelServer(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        System.out.println("ID Container: ");
        String idContainer = sn.next();
        String req = "cancel - " + idContainer;
        out.println(req);
        out.flush();

        try {
            String[] resp = in.readLine().split("-");
            if(resp[0].equals("suc")){
                System.out.println("> Reservation canceled with success. Please verify your current debt.");
            }
            else {
                System.out.println("> Error: " + resp[1]);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void reserveServer(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        try {
            String[] resp = in.readLine().split("-");
            System.out.println("> Please choose the type of server:");
            System.out.println("[1] T3.micro  (5eur/dia)");
            System.out.println("[2] M5.large  (10eur/dia)");
            System.out.println("[3] H7.normal (7eur/dia)");
            System.out.println("[4] L2.large  (13eur/dia)");
            System.out.println("[5] P1.mega   (50eur/dia)");
            System.out.println("[6] F6.min    (4eur/dia)");

            System.out.print("Option: ");
            int op = sn.nextInt();
            while (op > resp.length){
                System.out.println("> Wrong option. Try again");
                System.out.print("Option: ");
                op = sn.nextInt();
            }
            switch (op){
                case 1:
                    out.println("reserve - " + idUser + " T3.micro");
                case 2:
                    out.println("reserve - " + idUser + " M5.large");
                case 3:
                    out.println("reserve - " + idUser + " H7.normal");
                case 4:
                    out.println("reserve - " + idUser + " L2.large");
                case 5:
                    out.println("reserve - " + idUser + " P1.mega");
                case 6:
                    out.println("reserve - " + idUser + " F6.min");
            }
            out.flush();

            resp = in.readLine().split("-");
            if(resp[0].equals("suc")){
                System.out.println("> Reservation successful!");
            }
            else{
                System.out.println("> No available servers for the given type. Please try again later.");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
