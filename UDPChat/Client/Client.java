package UDPChat.Client;

import java.awt.event.*;

import javax.swing.JOptionPane;

import JsonTest2.ChatMessage;
//import java.io.*;

public class Client implements ActionListener {

	private static String m_name = null;
	private final ChatGUI m_GUI;
	private ServerConnection m_connection = null;

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: java Client serverhostname serverportnumber username");
			System.exit(-1);
		}

		try {
			m_name = JOptionPane.showInputDialog(null, "Enter your name");
			Client instance = new Client(m_name);
			instance.connectToServer(args[0], Integer.parseInt(args[1]));
		} catch (NumberFormatException e) {
			System.err.println("Error: port number must be an integer.");
			System.exit(-1);
		}
	}

	private Client(String userName) {
		m_name = userName;

		// Start up GUI (runs in its own thread)
		m_GUI = new ChatGUI(this, m_name);
	}

	private void connectToServer(String hostName, int port) {
		// Create a new server connection
		m_connection = new ServerConnection(hostName, port, m_name);
		if (m_connection.handshake()) {
			listenForServerMessages();
		} else {
			System.err.println("Unable to connect to server");
		}
	}

	private void listenForServerMessages() {
		do {
			m_GUI.displayMessage(m_connection.receiveChatMessage());
		} while (true);
	}

	// Sole ActionListener method; acts as a callback from GUI when user hits enter
	// in input field
	@Override
	public void actionPerformed(ActionEvent e) {
		// Since the only possible event is a carriage return in the text input field,
		// the text in the chat input field can now be sent to the server.
		m_connection.sendChatMessage(m_GUI.getInput());
		m_GUI.clearInput();
	}
}
