package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public String userName;
    JTextField textField = new JTextField(40);     
    JTextArea messageArea = new JTextArea(8, 40);
	
    UserFrame(String userName)
    {

    	super("Chat with " + userName);
    	this.userName = userName;
    	this.setLocationRelativeTo(Client.menu);
		this.getContentPane().add(textField, "North");
		this.getContentPane().add(new JScrollPane(messageArea), "Center");
		this.pack();
		this.addWindowListener(new java.awt.event.WindowAdapter() {

		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        Client.removeExistingFrame(userName);
		    }
		});
		
		textField.addActionListener(new ActionListener() {
		     
		public void actionPerformed(ActionEvent e) {
			String encryptedMessage = null;
			try {
				encryptedMessage = Encription.encrypt(textField.getText());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			if(Client.onlineFriends.contains(userName))
				Client.sendMessageToUser(userName,encryptedMessage);
			//Client.sendMessageToUser(userName,textField.getText());
			messageArea.append(Client.username+":"+textField.getText()+'\n');
			textField.setText("");
	     }
		});
		this.setVisible(true);
    }


    public static void showNewMessage(String line) throws Exception {
		String[] messages = line.split(":");
		String userDest = messages[1];
		boolean isFrameOpen = false;
		for(UserFrame frame: Client.userDialogs)
			if(frame.userName.equals(userDest))
			{
				isFrameOpen = true;
				break;
			}

		if(isFrameOpen)
		{
			for(UserFrame frame: Client.userDialogs)
				if(frame.userName.equals(userDest))
				{
					frame.setAlwaysOnTop(true);
					frame.appendMessage(messages[3]);
					break;
				}

		}
		else
		{
			UserFrame newUserFrame = new UserFrame(userDest);
			newUserFrame.setAlwaysOnTop(true);
			newUserFrame.appendMessage(messages[3]);
			Client.userDialogs.add(newUserFrame);
		}
	}



    public void appendMessage(String encryptedMessage) throws Exception
    {
    	messageArea.append(this.userName +":"+Encription.decrypt(encryptedMessage) + "\n");
		//messageArea.append(this.userName +":"+encryptedMessage + "\n");

	}
}
