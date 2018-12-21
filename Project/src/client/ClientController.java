package client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ClientController{

    private String token;

    public ClientController(){
        this.token = "";
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void menuScreen(){
        System.out.println("Available commands:");
        System.out.println("- myaccount");
        System.out.println("- reserve");
        // much more
    }

    public int welcomeMessage(BufferedReader in, PrintWriter out{
        System.out.println("> Welcome to CloudComputing Hosting.");
        System.out.println("> Please choose one of the following:");
        System.out.println("[1] Login");
        System.out.println("[2] Register");
        Scanner sn = new Scanner(System.in);
        int op = sn.nextInt();
        if(op == 1){
            login(in, out, sn);
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

    public void login(BufferedReader in, PrintWriter out, Scanner sn){
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

    public void register(BufferedReader in, PrintWriter out, Scanner sn){
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
        } catch (IOException e) {

        }
    }

    public void
}
