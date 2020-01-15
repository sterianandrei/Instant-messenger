package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Vector;

public class ClientMenu extends JFrame
{


    static DefaultListModel<String> userModel = new DefaultListModel<String>();
    static JList<String> onlineUsers;
    static JScrollPane onlineUsersScrollPane;




    ClientMenu()
    {
        super("Users online");

        onlineUsers = new JList<String>(userModel);
        onlineUsers.setCellRenderer( new MyListRenderer() );
        onlineUsersScrollPane = new JScrollPane(onlineUsers);
        getContentPane().add(new JScrollPane(onlineUsersScrollPane), "Center");



        setMinimumSize(new Dimension(100,720));
        //centerFrame();
        setMouseListener();
        this.pack();


    }

    private void setMouseListener()
    {
        onlineUsers.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                @SuppressWarnings("unchecked")
                JList<String> list = (JList<String>)evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    String userName = onlineUsers.getModel().getElementAt(index);
                    boolean isFrameOpen = false;
                    for(UserFrame frame: Client.userDialogs)
                        if(frame.userName.equals(userName))
                        {
                            isFrameOpen = true;
                            break;
                        }

                    if(isFrameOpen)
                    {
                        for(UserFrame frame: Client.userDialogs)
                            if(frame.userName.equals(userName))
                            {
                                frame.setAlwaysOnTop(true);
                                frame.requestFocus();
                            }

                    }
                    else
                    {
                        UserFrame newUserFrame = new UserFrame(userName);
                        newUserFrame.setAlwaysOnTop(true);
                        newUserFrame.requestFocus();
                        Client.userDialogs.add(newUserFrame);
                    }
                }
            }
        });
    }

    private void centerFrame() {

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = windowSize.width;
        int dy = windowSize.height / 2;
        setLocation(dx, dy);
    }

    public void addFriendToList(String newUser)
    {
        if(!userModel.contains(newUser))
            userModel.addElement(newUser);
    }

    public void changeFriendToOnline(String newUser)
    {
        if(userModel.contains(newUser)) {
            Client.onlineFriends.add(newUser);

            System.out.println("Changed user " + newUser + " to onlne");
        }
        this.repaint();
    }
    public void changeFriendToOffline(String newUser)
    {
        if(userModel.contains(newUser)) {
            Client.onlineFriends.remove(newUser);

            System.out.println("Changed user " + newUser + " to oflne");
        }
        this.repaint();
    }


    private class MyListRenderer extends DefaultListCellRenderer
    {


        public Component getListCellRendererComponent( JList list,
                                                       Object value, int index, boolean isSelected,
                                                       boolean cellHasFocus )
        {
            super.getListCellRendererComponent( list, value, index,
                    isSelected, cellHasFocus );



            if( Client.onlineFriends.contains(value) )
            {
                setForeground( Color.red );
            }
            else
            {
                setForeground( Color.black );
            }
            return( this );
        }
    }
}
