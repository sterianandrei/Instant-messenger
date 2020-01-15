package server;

import java.net.ServerSocket;
import java.util.HashSet;



public class Server {
    private static final int PORT = 9031;
    protected static UserSet users = new UserSet();
    protected static HashSet<String> usedNames = new HashSet<String>();
    protected static DatabaseConnection dbconn;
    public static void main(String[] args) throws Exception {

        dbconn = new DatabaseConnection();
        System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
                System.out.println("New connection");
            }
        } finally {
            listener.close();
        }

    }

}
