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
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import JsonTest2.ChatMessage;

/**
 * 
 * @author brom
 */
public class ClientConnection implements Runnable{
	
	private final String m_name;
	private final ObjectInputStream inStream;
	private final ObjectOutputStream outStream;
	private final Socket clientSocket;
	private final LinkedBlockingQueue<ChatMessage> mailBox;

	public ClientConnection(String name, Socket socket, ObjectOutputStream out, ObjectInputStream in, LinkedBlockingQueue<ChatMessage> lbq) {
		m_name = name;
		clientSocket = socket;
		outStream = out;
		inStream = in;
		mailBox = lbq;
	}
	
	private void listenForClientMessages(){
		try {
			boolean activeClient = true;
			ChatMessage cm;
			
			do{
				try{
					cm = (ChatMessage) inStream.readObject();
					mailBox.add(cm);
				}
				catch (SocketException e){
					mailBox.add(getLeftChatMessage());
					clientSocket.close();
					activeClient = false;
				}
				
			}while(activeClient);
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public Socket getOutputStream() {
		return clientSocket;
	}
	
	public String getName() {
		return m_name;
	}
	
	public void sendMessage(ChatMessage cm) {
				try {
					outStream.writeObject(cm);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	public boolean hasName(String testName) {
		return testName.equals(m_name);
	}
	
	private ChatMessage getLeftChatMessage() {
		return new ChatMessage(m_name, "/leave", null);
	}

	@Override
	public void run() {
		listenForClientMessages();
	}

}
