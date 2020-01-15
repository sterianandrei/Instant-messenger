package server;

import java.io.PrintWriter;
import java.util.Vector;

public class User
{
    String name;
    String username;
    Vector<String> friends;
    PrintWriter writer;

    public User(String names,PrintWriter writers) {
        this.username = names;
        this.writer = writers;
    }


    public String friendsToString()
    {
        String rez = "";
        for(String friend : friends)
            rez += ":" + friend;
        return rez;
    }

}
