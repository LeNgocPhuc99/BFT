import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONObject;

public class Node extends Thread {

	ArrayList<String> message = new ArrayList<String>();
	int port;
	ServerSocket server;
	static int nodeCount;

	Node(int port) throws IOException {
		this.port = port;
		this.server = new ServerSocket(this.port);
	}

	@Override
	public void run() {
		try {
			/* the number of incoming request ?? */
			int count = 0;
			while (true) {
				/* Socket to receive incoming requests */
				Socket socket = server.accept();
				/* Read message */
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String rawMsg = in.readLine();
				JSONObject json = new JSONObject(rawMsg);
				String message = json.getString("ID")+ ": " + json.getString("Message");
				this.message.add(message);
				System.out.println(this.port + this.message.toString());

				/* Send verification upon receiving all replies */
				
				if (count % Node.nodeCount == 0) {
					/* Broadcast to all */
					Thread t = new Sender(message, this.port);
					t.start();
				}
				count++;

				socket.close();
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
