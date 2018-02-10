/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UDPChat.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import JsonTest2.ChatMessage;

/**
 * 
 * @author brom
 */
public class ClientConnection implements Runnable{
	
	private String m_name = null;
	private final ObjectInputStream inStream;
	private final ObjectOutputStream outStream;
	private final Socket clientSocket;
	private final LinkedBlockingQueue<ChatMessage> mailBox;

	public ClientConnection(Socket socket, LinkedBlockingQueue<ChatMessage> lbq) {
		clientSocket = socket;
		outStream = createObjectOutputStream();
		inStream = createObjectInputStream();
		mailBox = lbq;
	}
	
	private void listenForClientMessages(){
		try {
			ChatMessage ch = (ChatMessage) inStream.readObject();
			m_name = ch.getSender();
			
			do{
				mailBox.add(ch);
				ch = (ChatMessage) inStream.readObject();
			}while(true);
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void sendMessage(String message, DatagramSocket socket) {
				
	}

	public boolean hasName(String testName) {
		return testName.equals(m_name);
	}

	@Override
	public void run() {
		listenForClientMessages();
		
	}
	
    private ObjectInputStream createObjectInputStream() {
    	try {
			return new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    private ObjectOutputStream createObjectOutputStream() {
    	try {
			return new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

}
