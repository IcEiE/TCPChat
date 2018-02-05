/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UDPChat.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import JsonTest2.ChatMessage;

/**
 *
 * @author brom
 */
public class ServerConnection {
	
    private Socket m_socket = null;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;

    public ServerConnection(String hostName, int port) {
	m_socket = createSocket(hostName, port);	
	inStream = createObjectInputStream();
	outStream = createObjectOutputStream();
    }

	public boolean handshake(String name) {
	// TODO:
	// * marshal connection message containing user name
	// * send message via socket
	// * receive response message from server
	// * unmarshal response message to determine whether connection was successful
	// * return false if connection failed (e.g., if user name was taken)
		sendChatMessage(getChatMessage("/connect", name, null, null));
		
		
	return true;
    }

    public String receiveChatMessage() {
	// TODO: 
	// * receive message from server
	// * unmarshal message if necessary
	
	// Note that the main thread can block on receive here without
	// problems, since the GUI runs in a separate thread
	
	// Update to return message contents
	return "";
    }

    public void sendChatMessage(ChatMessage messageObj) {
		try {
			outStream.writeObject(messageObj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*
     * ---------------------------------------------------------------------------
     * Private methods used for the public methods which is used for the functionality of the client.
     */
    
    private Socket createSocket(String address, int port) {
    	Socket s;
		try {
			s = new Socket(createInetAddress(address), port);
			return s;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
    
	private InetAddress createInetAddress(String hostName) {
		try {
			InetAddress address = InetAddress.getByName(hostName);
			return address;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    private ObjectInputStream createObjectInputStream() {
    	try {
			return new ObjectInputStream(m_socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private ObjectOutputStream createObjectOutputStream() {
    	try {
			return new ObjectOutputStream(m_socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private ChatMessage getChatMessage(String commando, String sender, String receiver, String message) {
    	return new ChatMessage(commando, sender, receiver, message);
    }
    
}
