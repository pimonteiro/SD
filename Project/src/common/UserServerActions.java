package common;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface UserServerActions {
    int login(BufferedReader in, PrintWriter out);
    void register(BufferedReader in, PrintWriter out);
    void checkMyAccount(BufferedReader in, PrintWriter out);
    void checkMyServers(BufferedReader in, PrintWriter out);
    void cancelServer(BufferedReader in, PrintWriter out);
    void reserveServer(BufferedReader in, PrintWriter out);

}
