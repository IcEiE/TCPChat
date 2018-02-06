/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UDPChat.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * @author brom
 */
public class ClientConnection implements Runnable{
	
	private final String m_name;
	private final InetAddress m_address;
	private final ObjectInputStream inStream;
	private final ObjectOutputStream outStream;
	private final Socket clientSocket;
	private final LinkedBlockingQueue messageList;

	public ClientConnection(String name, InetAddress address, Socket socket, LinkedBlockingQueue lbq) {
		m_name = name;
		m_address = address;
		clientSocket = socket;
		inStream = createObjectInputStream();
		outStream = createObjectOutputStream();
		messageList = lbq;
	}

	public void sendMessage(String message, DatagramSocket socket) {
				
	}

	public boolean hasName(String testName) {
		return testName.equals(m_name);
	}

	@Override
	public void run() {
		
		
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
