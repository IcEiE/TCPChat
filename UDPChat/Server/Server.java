package UDPChat.Server;

import java.io.IOException;

//
// Source file for the server side. 
//
// Created by Sanny Syberfeldt
// Maintained by Marcus Brohede
//

import java.net.*;
//import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import JsonTest2.ChatMessage;

public class Server {

	private ArrayList<ClientConnection> m_connectedClients = new ArrayList<ClientConnection>();
	private Socket m_socket;
	private ServerSocket listenerSocket;
	private LinkedBlockingQueue<ChatMessage> mailBox = new LinkedBlockingQueue<>();

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Usage: java Server portnumber");
			System.exit(-1);
		}
		try {
			Server instance = new Server(Integer.parseInt(args[0]));
			instance.listenForNewClients();
		} catch (NumberFormatException e) {
			System.err.println("Error: port number must be an integer.");
			System.exit(-1);
		}
	}

	private Server(int portNumber) {
		listenerSocket = createServerSocket(portNumber);
	}

	public boolean addClient(String name, Socket clientSocket) {
		ClientConnection c;
		for (Iterator<ClientConnection> itr = m_connectedClients.iterator(); itr.hasNext();) {
			c = itr.next();
			if (c.hasName(name)) {
				return false; // Already exists a client with this name
			}
		}
		m_connectedClients.add(new ClientConnection(clientSocket, mailBox));
		return true;
	}

	public void sendPrivateMessage(String message, String name) {
		ClientConnection c;
		// for(Iterator<ClientConnection> itr = m_connectedClients.iterator();
		// itr.hasNext();) {
		// c = itr.next();
		// if(c.hasName(name)) {
		// c.sendMessage(message, m_socket);
		// }
		// }
	}

	public void broadcast(String message) {
		// for(Iterator<ClientConnection> itr = m_connectedClients.iterator();
		// itr.hasNext();) {
		// itr.next().sendMessage(message, m_socket);
		// }
	}

	/*
	 * -------------------------------------------------------------------------
	 * -- Private methods used for the public methods which is used for the
	 * functionality of the client.
	 */
	private ServerSocket createServerSocket(int port) {
		try {
			return new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void createThreadForMailBox() {
    	new Thread(){
			public void run(){
				System.out.println("Waiting for client messages... ");
				while(true){
					try {
						System.out.println("1) i run ska kolla mail");
						ChatMessage cm = mailBox.take();
						System.out.println("2) taken");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
    	}.start();
	}

	private void listenForNewClients() {
		do {
			try {
				Socket newClient = listenerSocket.accept();
				new Thread(new ClientConnection(newClient, mailBox)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (true);
	}
}
