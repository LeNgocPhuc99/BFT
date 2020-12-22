import java.io.IOException;

import java.util.ArrayList;

import java.util.Random;

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
		
		// 1 nodes lỗi
		for (int i = 0; i < 1; ++i) {
			if (!nodes.get(i).getStatus()) {
				int randomBetrayedIndex = rand.nextInt(number);
				nodes.get(randomBetrayedIndex).setBetrayed();
			}
		}
		
		
		//nodes[1].setBettray();
		
		Node.nodeCount = ports.length;
		Node.cycle = 0;
		Sender.ports = ports;

		// Start
		int flag = 1;
		while (true) {
			if (flag == 3)
				break;
			
			for (int i = 0; i < ports.length; i++) {
				System.out.println("In cycle: " + Node.cycle);
				nodes.get(i).propose(ports);
				
				// Node lỗi gửi không đúng round ngẫu nhiên
				for (int j = 0; j < ports.length; j++) {
					if (nodes.get(j).getStatus() && (j != i)) {
						// Tỉ lệ gửi của node lỗi là 30%
						if (Math.random() < 0.3) {
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
			++flag;
		}
		// End
		for (int j = 0; j < ports.length; j++) {
			nodes.get(j).stop();
		}
	}

}
