import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
//import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws IOException, JSONException {
		ArrayList<Thread> nodes = new ArrayList<Thread>();

		/* Number node of network */
		int number = 3;
		int[] ports = new int[number];

		/* Add node to network */
		for (int i = 0; i < number; i++) {
			ports[i] = 8080 + i;
			int addport = ports[i];
			Thread node = new Node(addport);
			node.start();
			nodes.add(node);
			System.out.println("Node with port number " + addport + " has been added");
		}

		Node.nodeCount = ports.length;
		Node.cycle = 0;
		Sender.ports = ports;
		
		/*
		 * Case1: all loyal nodes
		 * 
		 * String message = "test"; Thread th1 = new Sender(message, 0); th1.start();
		 */

		/* Case2: node 8080 */
		for (int i = 0; i < ports.length; i++) {
			Socket socket = new Socket("127.0.0.1", ports[i]);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			JSONObject json = new JSONObject();
			json.put("Message", "test" + i);
			json.put("ID", 8080);
			out.println(json.toString());
			socket.close();
		}

	}

}
