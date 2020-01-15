package client;

import javax.swing.*;
import java.awt.*;

public class ClientLogin
{



        public static void getCredentials(String message) {
            JTextField xField = new JTextField(24);
            JPasswordField yField = new JPasswordField(24);

            JPanel myPanel = new JPanel(new GridLayout(0,1));
            if(message != null) {
                JLabel error = new JLabel(message);
                myPanel.add(error);
            }
            myPanel.add(new JLabel("User:"));
            myPanel.add(xField);
            myPanel.add(new JLabel("Pass:"));
            myPanel.add(yField);
            int result = JOptionPane.showConfirmDialog(Client.menu, myPanel,
                    "Please Enter User and Password:", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Client.username = xField.getText();
                Client.password = new String(yField.getPassword());
            }
            else
            {
                System.exit(0);
            }
        }

        public static void getServerAddress() {
            Client.server = JOptionPane.showInputDialog(
                    Client.menu,
                    "Enter IP Address of the Server:",
                    "Welcome to the Chatter",
                    JOptionPane.QUESTION_MESSAGE);
        }
}

