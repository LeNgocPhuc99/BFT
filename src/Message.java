
public class Message {
	public int type;
	private final int cycle;
	private final int nodeID;
	private final String msg;

	Message(int type, int cycle, int id, String msg) {
		this.type = type;
		this.cycle = cycle;
		this.nodeID = id;
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Message [type=" + type + ", cycle=" + cycle + ", nodeID=" + nodeID + ", msg=" + msg + "]";
	}

	public String getMsg() {
		return msg;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCycle() {
		return cycle;
	}

	public int getNodeID() {
		return nodeID;
	}
	
}
