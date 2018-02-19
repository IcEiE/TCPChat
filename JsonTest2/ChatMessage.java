package JsonTest2;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class ChatMessage implements Serializable {

	private JSONObject obj = new JSONObject();

	public ChatMessage(String sender, String command, String parameters) {
		obj.put("command", command.trim());
		obj.put("sender", sender.trim());
		obj.put("timestamp", System.currentTimeMillis());
		switch (command) {
		case "/connect":
			obj.put("parameters", sender + " has connected to the server");
			break;
		case "/tell":
			String[] messageList1 = parameters.split("\\s+", 2);
			obj.put("receiver", messageList1[0]);
			obj.put("parameters", sender + " to " + this.getReceiver() + ": " + parameters);
			break;
			
		case "/all":
			obj.put("parameters", sender + " to all: " + parameters);
			obj.put("receiver", "all");
			break;
		
		default:
			obj.put("parameters", parameters);
		}
	}

	public String getCommand() {
		return (String) obj.get("command");
	}

	public String getParameters() {
		return (String) obj.get("parameters");
	}

	public String getTimeStamp() {
		return obj.get("timestamp").toString();
	}

	public String getSender() {
		return (String) obj.get("sender");
	}

	public String getReceiver() {
		return (String) obj.get("receiver");
	}
}