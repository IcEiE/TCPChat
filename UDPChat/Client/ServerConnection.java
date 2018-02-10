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
		sendChatMessage(name + " " + "/connect");

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

    public void sendChatMessage(String message) {
		try {
			outStream.writeObject(getChatMessage(message));
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
		try {
			return new Socket(createInetAddress(address), port);
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
			return InetAddress.getByName(hostName);
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
    
    private ChatMessage getChatMessage(String message) {
    	String[] messageList = message.split("//s+", 3); 
    	String sender = messageList[0];
    	String commando = messageList[1];
    	message = messageList[2];
    	return new ChatMessage(sender, commando, message);
    }
    
}
