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

public class Server {
	
    private ArrayList<ClientConnection> m_connectedClients = new ArrayList<ClientConnection>();
    private Socket m_socket;
    private ServerSocket listenerSocket;
    private LinkedBlockingQueue mailBox = new LinkedBlockingQueue();

    public static void main(String[] args){
	if(args.length < 1) {
	    System.err.println("Usage: java Server portnumber");
	    System.exit(-1);
	}
	try {
	    Server instance = new Server(Integer.parseInt(args[0]));
	    instance.listenForClientMessages();
	} catch(NumberFormatException e) {
	    System.err.println("Error: port number must be an integer.");
	    System.exit(-1);
	}
    }

    private Server(int portNumber) {
	// TODO: create a socket, attach it to port based on portNumber, and assign it to m_socket'
    	listenerSocket = createServerSocket(portNumber);
    }

    private void listenForClientMessages() {
	System.out.println("Waiting for client messages... ");

	do {
	    // TODO: Listen for client messages.
	    // On reception of message, do the following:
	    // * Unmarshal message
	    // * Depending on message type, either
	    //    - Try to create a new ClientConnection using addClient(), send 
	    //      response message to client detailing whether it was successful
	    //    - Broadcast the message to all connected users using broadcast()
	    //    - Send a private message to a user using sendPrivateMessage()
	} while (true);
    }

    public boolean addClient(String name, InetAddress address, Socket clientSocket) {
	ClientConnection c;
	for(Iterator<ClientConnection> itr = m_connectedClients.iterator(); itr.hasNext();) {
	    c = itr.next();
	    if(c.hasName(name)) {
		return false; // Already exists a client with this name
	    }
	}
	m_connectedClients.add(new ClientConnection(name, address, clientSocket, mailBox));
	return true;
    }

    public void sendPrivateMessage(String message, String name) {
	ClientConnection c;
//	for(Iterator<ClientConnection> itr = m_connectedClients.iterator(); itr.hasNext();) {
//	    c = itr.next();
//	    if(c.hasName(name)) {
//		c.sendMessage(message, m_socket);
//	    }
//	}
    }

    public void broadcast(String message) {
//	for(Iterator<ClientConnection> itr = m_connectedClients.iterator(); itr.hasNext();) {
//	    itr.next().sendMessage(message, m_socket);
//	}
    }
    
    
    /*
     * ---------------------------------------------------------------------------
     * Private methods used for the public methods which is used for the functionality of the client.
     */
    private ServerSocket createServerSocket(int port){
       	try {
    			return new ServerSocket(port);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
       	return null;
    }
    
    
}
