package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientController{

    private String token;
    private BufferedReader in;
    private PrintWriter out;

    public ClientController(){
        this.token = "";
    }

    public void setToken(String token) {
        this.token = token;
    }

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
        if(op == 1){
            login(in, out);
            System.out.println("> Login Sucessfull!"); //Maybe receive name to display?
            return 2;
        }
        if(op == 2){

            System.out.println("> Registration Sucessfull!");
            return 1;
        }
        System.out.println("> Wrong option.Please try again.");
        return 1;
    }

    public void login(BufferedReader in, PrintWriter out){
        Scanner sn = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sn.next();
        System.out.print("Password: ");
        String password = sn.next();
        String req = "login-" + username + "-" + password;
        out.println(req);
        out.flush();

        try {
            String resp = in.readLine(); //wait for answer
            if(true) {
                // Dividir resposta pelos espaços e tratar
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(BufferedReader in, PrintWriter out){
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

    public void reserverServer(BufferedReader in, PrintWriter out){
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
