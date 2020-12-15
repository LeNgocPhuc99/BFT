import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONObject;

public class Sender extends Thread {

	static int[] ports;
	String message;
	int noBroadCast;

	Sender(String msg, int noBroadcast) {
		this.message = msg;
		this.noBroadCast = noBroadcast;
	}

	@Override
	public void run() {

		int size = ports.length;

		/* Broadcast to all node in network */
		for (int i = 1; i < size; i++) {
			if (ports[i] != this.noBroadCast) {
				try {
					/* Make message */
					JSONObject json = new JSONObject();
					json.put("ID", this.noBroadCast);
					json.put("Message", this.message);

					/* Send message */
					Socket socket = new Socket("127.0.0.1", Sender.ports[i]);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(json.toString());
					socket.close();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
