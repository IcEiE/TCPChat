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

	public boolean handshake() {
		sendChatMessage("/connect");

	return true;
    }
	private Socket m_socket = null;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private final String m_name;

	public ServerConnection(String hostName, int port, String name) {
		m_name = name;
		m_socket = createSocket(hostName, port);
		outStream = createObjectOutputStream();
		inStream = createObjectInputStream();
	}

	public String receiveChatMessage() {
		ChatMessage cm;
		try {
			cm = (ChatMessage) inStream.readObject();
			return (cm.getParameters());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void sendChatMessage(String message) {
		try {
			ChatMessage cm = getChatMessage(message);
			if(!cm.getCommand().equals("/leave")) {
				outStream.writeObject(cm);
			}
			else{
				System.exit(-1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * -------------------------------------------------------------------------
	 * -- Private methods used for the public methods which is used for the
	 * functionality of the client.
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
    	String[] messageList = message.split("\\s+", 2);
    	String command = messageList[0];
    	String newMessage = "";
    	if(messageList.length > 1){
    		newMessage = messageList[1];
    	}
    	return new ChatMessage(m_name, command, newMessage);
    }

}
