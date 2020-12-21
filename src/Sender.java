import java.io.PrintWriter;
import java.net.Socket;

//import org.json.JSONObject;

public class Sender extends Thread {

	static int[] ports;
	String message;
	int noBroadCast;

	/* broadcast to all - proposer */

	Sender(String msg, int noBroadcast) {
		this.message = msg;
		this.noBroadCast = noBroadcast;
	}

	/* broadcast to all */
	Sender(String msg) {
		this.message = msg;
		this.noBroadCast = -1;
	}

	@Override
	public void run() {

		int size = ports.length;

		/* Broadcast to all node in network */
		for (int i = 0; i < size; i++) {
			if (ports[i] != this.noBroadCast) {
				try {
					/* Send message */
					Socket socket = new Socket("127.0.0.1", Sender.ports[i]);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(this.message);
					socket.close();
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
