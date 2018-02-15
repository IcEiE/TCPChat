package UDPChat.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//
// Source file for the server side. 
//
// Created by Sanny Syberfeldt
// Maintained by Marcus Brohede
//

import java.net.*;
//import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import JsonTest2.ChatMessage;

public class Server {

	private Map<String, ClientConnection> m_connectedClients = new HashMap<>();
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
			instance.startThreadForMailBox();
			instance.listenForNewClients();
		} catch (NumberFormatException e) {
			System.err.println("Error: port number must be an integer.");
			System.exit(-1);
		}
	}

	private Server(int portNumber) {
		listenerSocket = createServerSocket(portNumber);
	}

	public boolean addClient(String name, ClientConnection cc) {
		if (m_connectedClients.containsKey(name)) {
			return false;
		}
		m_connectedClients.put(name, cc);
		return true;
	}

	public void sendPrivateMessage(ChatMessage cm) {
		ClientConnection c = m_connectedClients.get(cm.getReceiver());
		c.sendMessage(cm);
		c = m_connectedClients.get(cm.getSender());
		c.sendMessage(cm);
	}

	public void broadcast(ChatMessage cm) {
		for (ClientConnection cc : m_connectedClients.values()) {
			cc.sendMessage(cm);
		}
	}

	/*
	 * ------------------------------------------------------------------------- --
	 * Private methods used for the public methods which is used for the
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

	private void startThreadForMailBox() {
		new Thread() {
			public void run() {
				System.out.println("Waiting for client messages... ");
				while (true) {
					try {
						ChatMessage cm = mailBox.take();
						executeMessage(cm);
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
				ObjectInputStream inStream = createObjectInputStream(newClient);
				ObjectOutputStream outStream = createObjectOutputStream(newClient);
				ChatMessage cm = null;
				do {
					try {
						cm = (ChatMessage) inStream.readObject();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} while (!cm.getCommand().trim().equals("/connect"));
				ClientConnection cc = new ClientConnection(cm.getSender(), newClient, outStream, inStream, mailBox);
				addClient(cm.getSender(), cc);
				new Thread(cc).start();
				broadcast(cm);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (true);
	}

	private void executeMessage(ChatMessage cm) {
		switch (cm.getCommand()) {
		case "/tell":
			if (m_connectedClients.containsKey(cm.getReceiver())) {
				sendPrivateMessage(cm);
			}
			break;

		case "/all":
			broadcast(cm);
			break;

		case "/list":
			m_connectedClients.get(cm.getSender()).sendMessage(getChatMessage("List of clients: " + m_connectedClients.keySet()));
			break;
		
		case "/help":
			switch(cm.getParameters()) {
			case "tell":
				m_connectedClients.get(cm.getSender()).sendMessage(getChatMessage("/tell is used to send a private message to another connected client. \nSyntax: /tell nameOfClient message"));
				break;
				
			case "all":
				m_connectedClients.get(cm.getSender()).sendMessage(getChatMessage("/all is used to broadcast a message to all members. \nSyntax: /all message"));
				break;
				
			case "list":
				m_connectedClients.get(cm.getSender()).sendMessage(getChatMessage("/list returns a list of all currently connected clients on the server. \nSyntax: /list"));
				break;
				
			case "roll":
				m_connectedClients.get(cm.getSender()).sendMessage(getChatMessage("/roll returns a random value between 1 and 100. \nSyntax: /roll"));
				break;
				
			default:
				m_connectedClients.get(cm.getSender()).sendMessage(getChatMessage("Commandos: help, tell, all, list, roll. To learn more about the commando write /help and after the command you want to read about. \nSyntax: /help commandoName"));
				break;
			}
			break;
			
		case "/roll":
			int randomNum = ThreadLocalRandom.current().nextInt(1, 100);
			broadcast(getChatMessage(cm.getSender() + " rolled " + randomNum));
			break;
		}
		

	}

	private ObjectInputStream createObjectInputStream(Socket clientSocket) {
		try {
			return new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ObjectOutputStream createObjectOutputStream(Socket clientSocket) {
		try {
			return new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ChatMessage getChatMessage(String message) {
		return new ChatMessage("Sever", "/list", message);
	}

}
