import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
//import java.util.Scanner;
import java.util.Random;

//import org.json.JSONObject;
import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws IOException, JSONException {
		ArrayList<Node> nodes = new ArrayList<Node>();

		/* Number node of network */
		int number = 4;
		int[] ports = new int[number];

		/* Add node to network */
		for (int i = 0; i < number; i++) {
			ports[i] = 8080 + i;
			int addport = ports[i];
			Node node = new Node(addport, i);
			node.start();
			nodes.add(node);
			System.out.println("Node with port number " + addport + " has been added");
		}

		// Random betrayed nodes
		Random rand = new Random();
		// 2 nodes lỗi
		for(int i = 0; i < 2; ++i)
		{
			if(!nodes.get(i).getStatus())
			{
				int randomBetrayedIndex = rand.nextInt(number);
				nodes.get(randomBetrayedIndex).setBetrayed();
			}	
		}
		
		Node.nodeCount = ports.length;
		Node.cycle = 0;
		Sender.ports = ports;

		/*
		 * Case1: all loyal nodes
		 * 
		 * String message = "test"; Thread th1 = new Sender(message, 0); th1.start();
		 */

		/* Case2: node 8080 propose message */
		for (int i = 0; i < ports.length; i++) {
//			Socket socket = new Socket("127.0.0.1", ports[i]);
//			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//			ProposeMessage message = new ProposeMessage(1, 0, (ports[0] - 8080), Integer.toString(ports[0] - 8080));
//			out.println(message.toString());
//			socket.close();
			
			nodes.get(i).propose(ports);
			
			// Node lỗi gửi không đúng round ngẫu nhiên
			for(int j = 0; j < ports.length; j ++)
			{
				if(nodes.get(j).getStatus() && (j != i))
				{
					// Tỉ lệ gửi của node lỗi là 40%
					if(Math.random() < 0.4)
					{
						nodes.get(j).propose(ports);
					}
				}
			}
			
			try {
				Thread.sleep(15 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Node.cycle += 1;
		}

	}

}
