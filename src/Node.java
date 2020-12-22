import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

public class Node {

	private TreeMap<Integer, String> msgQueue = new TreeMap<>();
	private TreeMap<Integer, Vote> votes = new TreeMap<>();
	// Static
	public static int nodeCount;
	public static int cycle;

	//private String proposeMessage = null;
	private final int id;
	private int port;
	private ServerSocket server;
	// Node lỗi
	private boolean isBetrayed = false;
	// Ghi vào file
	private Logger logger;
	
	Node(int port, int id) throws IOException {
		this.port = port;
		this.server = new ServerSocket(this.port);
		this.id = id;
		logger = new Logger(id);
	}

	public void propose(int[] ports) throws UnknownHostException, IOException {
		for (int i = 0; i < ports.length; i++) {
			if (i == id)
				continue;
			Socket socket = new Socket("127.0.0.1", ports[i]);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			ProposeMessage message = new ProposeMessage(1, cycle, (port - 8080), Integer.toString(port - 8080));
			//proposeMessage = message.toString();
			msgQueue.put((this.port - 8080), message.toString());
			out.println(message.toString());
			socket.close();
		}
	}

	public void vote(boolean check, int proposeID) throws JSONException {
		Vote vote;
		if (check) {
			vote = Vote.YES;
		} else {
			vote = Vote.NO;
		}

		VoteMessage message = new VoteMessage(2, cycle, (port - 8080), vote,proposeID);

		// broadcast vote to all
		Thread t = new Sender(message.toString(), this.port);
		t.start();
	}

	public void commit(String message, int count) {
		System.out.println("Node " + Integer.toString(this.port - 8080) + " commit: " + message + " with count: " + Integer.toString(count));
		logger.LogMessage(message);
	}

	private boolean verifyMessage(JSONObject json) throws JSONException {
		int nodeID = json.getInt("nodeID");
		if ((cycle % nodeCount  == nodeID)) {
			return true;
		}
		return false;
	}

	private Vote getMajorityVote() {

		Map<Vote, Long> orderCounter = votes.values().stream()
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()));

		Vote vote = orderCounter.getOrDefault(Vote.YES, 0L) > orderCounter.getOrDefault(Vote.NO, 0L) ? Vote.YES
				: Vote.NO;

		return vote;

	}

	public void start() {
		Thread listener = new Thread(new Listener());
		listener.start();
	}

	public void stop() {
		try {
			server.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public void setBetrayed() {
		isBetrayed = true;
	}

	public boolean getStatus() {
		return isBetrayed;
	}

	private class Listener implements Runnable {

		@Override
		public void run() {

			try {
				int count = 0;
				while (true) {
					/* Socket to receive incoming requests */
					Socket socket = server.accept();
					/* Read message */
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String rawMsg = in.readLine();
					JSONObject json = new JSONObject(rawMsg);
					System.out.println("Node " + Integer.toString(port - 8080) + " receive: " + rawMsg + " in cycle: " + json.getInt("cycle"));

					/* message not match its round */
					if (json.getInt("cycle") != cycle) {
						continue;
					}

					count++;

					switch (json.getInt("type")) {
					case 1: /* receive propose message from proposer */
						/* broadcast voting message */
						boolean check = verifyMessage(json);
						//System.out.println(check);
						if (!check) {
							continue;
						}
						else {
							int nodeID = json.getInt("nodeID");
							msgQueue.put(nodeID, rawMsg);
							//proposeMessage = rawMsg;
							vote(check, nodeID);
							break;
						}
					case 2: /* receive voting message from another node */
						/* Add new vote */
						Vote newVote = json.getString("vote").equals("YES") ? Vote.YES : Vote.NO;
						votes.put(json.getInt("nodeID"), newVote);

						if (count % (nodeCount - 1) == 0) {
							/* get majority vote */
							Vote vote = getMajorityVote();
							if (vote.equals(Vote.YES)) {
								/* commit message + write log if majority vote is YES */
								//commit(proposeMessage);
								commit(msgQueue.get(json.getInt("proposeID")), count);
								votes.clear();
								msgQueue.clear();
							}
							count = 0;
							
						}

						break;
					}

					socket.close();
					in.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

	}
}