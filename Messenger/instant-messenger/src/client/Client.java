package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class Client {

	static String myName;
	static String username;
    static String password;
    static String server = "127.0.0.1";

	static PrintWriter out;
	static Vector<UserFrame> userDialogs = new Vector<UserFrame>();
    static BufferedReader in;
    static Vector<String> onlineFriends = new Vector<String>();
            
    static ClientMenu menu = new ClientMenu();


    public static void sendMessageToUser(String to, String message)
    {
    	out.println(Client.username +":" +to+":"+message);
    }


    private static void run() throws Exception {
        Socket socket = new Socket(Client.server, 9031);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
        	
            String line = in.readLine();
            MessageHandler.MESSAGE_TYPE msgType = MessageHandler.decodeMessage(line);
            //System.out.println(msgType);

            switch (msgType)
            {
                case LOGIN:
                    ClientLogin.getCredentials(null);
                    out.println(Client.username+":"+Client.password);
                    break;
                case LOG_FAIL:
                    String logFail = line.split(":")[1];
                    ClientLogin.getCredentials(logFail);
                    out.println(Client.username+":"+Client.password);
                    break;
                case LOG_SUCCS:
                    String[] parts = line.split(":");
                    menu.setTitle(parts[1]);
                    for (int i=2;i<parts.length;i++)
                        menu.addFriendToList(parts[i]);
                    break;
                case ONL_USR:
                    String[] online = line.split(":");
                    menu.changeFriendToOnline(online[1]);
                    break;
                case OFF_USR:
                    String[] offline = line.split(":");
                    menu.changeFriendToOffline(offline[1]);
                    break;
                case NEW_USR:
                    //update friends
                    //System.out.println(line);
                    String[] users = line.split(":");
                    if(!Client.username.equals(users[1]))
                        menu.changeFriendToOnline(users[1]);
                    break;
                case NEW_MSG:
                    System.out.println(line);
                    UserFrame.showNewMessage(line);
                    break;
            }

        }
    }
    
    public static void removeExistingFrame(String userName) {
		for(UserFrame frame: userDialogs)
        	if(frame.userName.equals(userName))
        	{
        		userDialogs.remove(frame);
        		break;
        	}
	}
    
    public static void main(String[] args) throws Exception {
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);
        run();
    }

}
