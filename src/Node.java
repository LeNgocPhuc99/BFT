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

public class Node extends Thread {

	// ArrayList<String> message = new ArrayList<String>();
	private TreeMap<Integer, Vote> votes = new TreeMap<>();

	int port;
	ServerSocket server;
	public static int nodeCount;
	public static int cycle;

	Node(int port) throws IOException {
		this.port = port;
		this.server = new ServerSocket(this.port);
	}

	public void propose(int[] ports) throws UnknownHostException, IOException {
		for (int i = 0; i < ports.length; i++) {
			Socket socket = new Socket("127.0.0.1", ports[i]);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			ProposeMessage message = new ProposeMessage(1, cycle, (port - 8080), Integer.toString(port - 8080));
			out.println(message.toString());
			socket.close();
		}
	}

	public void vote(JSONObject json, int portID) throws JSONException {
		boolean check;
		Vote vote;
		check = verifyMessage(json, portID);
		if (check) {
			vote = Vote.YES;
		} else {
			vote = Vote.NO;
		}

		VoteMessage message = new VoteMessage(2, cycle, (port - 8080), vote);

		// broadcast vote to all
		Thread t = new Sender(message.toString(), this.port);
		t.start();
	}

	public void commit(String message) {
		System.out.println("Commit: " + message);
	}

	private boolean verifyMessage(JSONObject json, int portID) throws JSONException {
		int nodeID = json.getInt("nodeID");
		/*
		 * (cycle % nodeCount == nodeID): send in correct round, ( (portID - 8080) ==
		 * nodeID): proposer is correct
		 */
		if ((cycle % nodeCount == nodeID) && ((portID - 8080) == nodeID)) {
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

	@Override
	public void run() {
		try {

			int count = 0;
			String messagePropose = null;
			while (true) {
				/* Socket to receive incoming requests */
				Socket socket = server.accept();
				int rePort = socket.getPort();
				/* Read message */
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String rawMsg = in.readLine();
				JSONObject json = new JSONObject(rawMsg);
				System.out.println("Node " + Integer.toString(this.port - 8080) + " receive: " + rawMsg);
				/* message not match its round */
				if (json.getInt("cycle") != cycle) {
					continue;
				}

				count++;

				switch (json.getInt("type")) {
				case 1: /* receive propose message from proposer */
					/* broadcast voting message */
					messagePropose = rawMsg;
					vote(json, rePort);
					break;
				case 2: /* receive voting message from another node */
					/* Add new vote */
					Vote newVote = json.getString("vote").equals("YES") ? Vote.YES : Vote.NO;
					votes.put(json.getInt("nodeID"), newVote);

					// votes.put(json.getInt("nodeID"), ));
					if (count % nodeCount == 0) {
						/* get majority vote */
						Vote vote = getMajorityVote();
						if (vote.equals(Vote.YES)) {
							/* commit message + write log if majority vote is YES */
							commit(messagePropose);
						}

					}

					break;
				}

				socket.close();
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
