package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class Handler extends Thread {
    private String name;
    private String pass;
    private String line;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            //login

            out.println("LOGIN");
            boolean breakWhile = false;
            while (true) {

                if(breakWhile)
                    break;

                line = in.readLine();
                if (line == null) {
                    out.println("LOGERR:Please enter a valid username");
                }
                name = line.split(":")[0];
                pass = line.split(":")[1];

                DatabaseConnection.DB_RETURN loginValid = Server.dbconn.findUser(name,pass);
                switch (loginValid)
                {
                    case NO_USER:
                        out.println("LOGERR:User doesn't exist! You will be added to database");
                        DatabaseConnection.insertInHistory(name, "User nou creat");
                        DatabaseConnection.insertUser(name, pass);
                        break;
                    case WRONG_PASS:
                        out.println("LOGERR:Wrong password!");
                        DatabaseConnection.insertInHistory(name, "Incercare de autentificare cu parola gresita");
                        break;
                    case CONN_SUCCESS:
                        if (Server.users.isUserOnline(name))
                        {
                            out.println("LOGERR:User already logged in!");
                            DatabaseConnection.insertInHistory(name, "User deja logat");
                            break;
                        }

                        User newUser = new User(name, out);
                        getUserData(newUser);
                        Server.users.addUser(newUser);

                        out.println("LOGSUCCESS:"+newUser.name+newUser.friendsToString());
                        DatabaseConnection.insertInHistory(name, "User logat cu succes");
                        notifyOnlineFriends(newUser);
                        notifyOtherUsers(newUser);

                        breakWhile = true;
                        break;

                }
            }

            while (true) {
                String input = in.readLine();
                String sender, receiver, message;
                if (input == null) {
                    return;
                }

                System.out.println("Server:"+input);

                sender = input.split(":")[0];
                receiver = input.split(":")[1];
                message = input.split(":")[2];

                User user = Server.users.getUser(receiver);
                if(user != null) {
                    user.writer.println("MESSAGE:"+sender + ":" + receiver+":" + message);
                    DatabaseConnection.insertInSent(name, message);
                    DatabaseConnection.insertInReceived(receiver, message);
                }

            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if(name != null) {
                Server.users.removeUser(name);
                notifyOfflineFriends(name);
            }
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    private static void notifyOtherUsers(User newUser)
    {
        for(User user: Server.users)
        {
            if(!user.username.equals(newUser.username))
            {
                notifyUser(user, newUser);
            }
        }
    }

    private static void notifyOfflineFriends(String offlineUser)
    {
        for (User user : Server.users)
            if(user.friends.contains(offlineUser))
                user.writer.println( "OFFUSR:" + offlineUser);
    }

    private static void notifyOnlineFriends(User newUser)
    {
        for (User user : Server.users)
            if(newUser.friends.contains(user.username))
                newUser.writer.println( "ONLNUSR:" + user.username);
    }

    private static void notifyUser(User user, User newUser)
    {
        user.writer.println("NEWUSER:"+newUser.username);
    }

    public static void getUserData(User newUser) throws SQLException {
        newUser.friends = Server.dbconn.getFriends(newUser.username);
        newUser.name = Server.dbconn.getName(newUser.username);
    }

}

