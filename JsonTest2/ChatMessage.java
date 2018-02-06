package JsonTest2;
import org.json.simple.JSONObject;

public class ChatMessage{
		
	private JSONObject obj = new JSONObject();
		
	public ChatMessage(String sender, String command, String parameters){
		obj.put("command", command);
		obj.put("sender", sender);
		obj.put("timestamp", System.currentTimeMillis());
		if(command.equals("/tell")) {
			String[] messageList = parameters.split("//s+", 2); 
			String receiver = messageList[0];
			String message = messageList[1];
	    	obj.put("receiver", receiver);
		}
		else {
			obj.put("parameters", parameters);
		}
	}

	public String getCommand(){
		return (String)obj.get("command");	
	}
	
	public String getParameters(){
		return (String)obj.get("parameters");	
	}
	
	public String getTimeStamp(){
		return obj.get("timestamp").toString();
	}
	
	public String getSender() {
		return (String)obj.get("Sender");
	}
	
	public String getReceiver() {
		return (String)obj.get("Receiver");
	}
}