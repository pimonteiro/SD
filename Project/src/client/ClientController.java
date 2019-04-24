package client;


import common.UserServerActions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClientController implements UserServerActions {

    private String email;

    public ClientController(){
        this.email = "";
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
        System.out.print("Email: ");
        String email = sn.next();
        System.out.print("Password: ");
        String password = sn.next();
        String req = "login-" + email + "-" + password;
        out.println(req);
        out.flush();

        try {
            String[] resp = in.readLine().split("-"); //wait for answer
            if(resp[0].contains("suc")) {
                this.email = resp[1];
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
        String req = "register-" + name + "-" + email + "-" + password;

        out.println(req);
        out.flush();

        try {
            String[] resp = in.readLine().split("-"); //wait for answer
            if(resp[0].contains("suc")){
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
        String req = "myaccount-" + email;
        out.println(req);
        out.flush();

        try{
            int n = Integer.parseInt(in.readLine());
            char[] resp = new char[n];
            int r = in.read(resp,0,n);
            String s = new String(resp);
            System.out.println("\n" + s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkMyServers(BufferedReader in, PrintWriter out){
        String req = "myservers-" + email;
        out.println(req);
        out.flush();

        try {
            int n = Integer.parseInt(in.readLine());
            char[] resp = new char[n];
            int r = in.read(resp,0,n);

            String s = new String(resp);
            System.out.println(s + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reserveServer(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);

        try {
            System.out.println("> Please choose the type of server:");
            System.out.println("[1] T3.micro  (5eur/dia)");
            System.out.println("[2] M5.large  (10eur/dia)");
            System.out.println("[3] H7.normal (7eur/dia)");
            System.out.println("[4] L2.large  (13eur/dia)");
            System.out.println("[5] P1.mega   (50eur/dia)");
            System.out.println("[6] F6.min    (4eur/dia)");

            System.out.print("Option: ");
            int op;
            try{
                op = sn.nextInt();
            }
            catch (InputMismatchException e){
                op = 0;
                System.out.println("> Please input a number.");
            }
            while (op > 6 || op < 1){
                System.out.println("> Wrong option. Try again");
                System.out.print("Option: ");
                try{
                    op = sn.nextInt();
                }
                catch (InputMismatchException e){
                    op = 0;
                    System.out.println("> Please input a number.");
                }
            }
            switch (op){
                case 1:
                    out.println("reserve-" + email + "-T3.micro");
                    break;
                case 2:
                    out.println("reserve-" + email + "-M5.large");
                    break;
                case 3:
                    out.println("reserve-" + email + "-H7.normal");
                    break;
                case 4:
                    out.println("reserve-" + email + "-L2.large");
                    break;
                case 5:
                    out.println("reserve-" + email + "-P1.mega");
                    break;
                case 6:
                    out.println("reserve-" + email + "-F6.min");
                    break;
            }
            out.flush();

            String[] resp = in.readLine().split("-");
            if(resp[0].contains("suc")){
                System.out.println("> Reservation successful!");
            }
            else{
                System.out.println("> No available servers for the given type. Please try again later.");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void cancelServer(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        System.out.println("ID Container: ");
        String idContainer = sn.next();
        String req = "cancel-" + email + "-" + idContainer;
        out.println(req);
        out.flush();

        try {
            String[] resp = in.readLine().split("-");
            if(resp[0].contains("suc")){
                System.out.println("> Reservation canceled with success. Please verify your current debt.");
            }
            else {
                System.out.println("> Error: " + resp[1]);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void auctionServer(BufferedReader in, PrintWriter out) {
        Scanner sn = new Scanner(System.in);
        try {
            System.out.println("> Please choose the type of server:");
            System.out.println("[1] T3.micro  (5eur/dia)");
            System.out.println("[2] M5.large  (10eur/dia)");
            System.out.println("[3] H7.normal (7eur/dia)");
            System.out.println("[4] L2.large  (13eur/dia)");
            System.out.println("[5] P1.mega   (50eur/dia)");
            System.out.println("[6] F6.min    (4eur/dia)");

            System.out.print("Option: ");
            int op;
            try {
                op = sn.nextInt();
            } catch (InputMismatchException e) {
                op = 0;
                System.out.println("> Please input a number.");
            }
            while (op > 6 || op < 1) {
                System.out.println("> Wrong option. Try again");
                System.out.print("Option: ");
                try {
                    op = sn.nextInt();
                } catch (InputMismatchException e) {
                    op = 0;
                    System.out.println("> Please input a number.");
                }
            }

            int money;
            do {
                System.out.print("Amount to spend: ");
                try {
                    money = sn.nextInt();
                } catch (InputMismatchException e) {
                    money = -1;
                }
                if (money < 0) {
                    System.out.println("> Invalid amount. Please insert a valid amount.");
                }

            } while (money < 0);

            StringBuilder req = new StringBuilder();
            req.append("auction-");
            req.append(email);

            switch (op) {
                case 1:
                    req.append("-T3.micro-");
                    break;
                case 2:
                    req.append("-M5.large-");
                    break;
                case 3:
                    req.append("-H7.normal-");
                    break;
                case 4:
                    req.append("-L2.large-");
                    break;
                case 5:
                    req.append("-P1.mega-");
                    break;
                case 6:
                    req.append("-F6.min-");
                    break;
            }
            req.append(money);
            out.println(req);
            out.flush();

            //tratar resposta

            String[] resp = in.readLine().split("-");
            if(resp[0].contains("suc")){
                System.out.println(">Server being fetched. Check back in later to see if you got lucky!");
            }
            else{
                System.out.println("Ups, something went bad.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
