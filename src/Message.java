/*
type = 1: propose message
type = 2: vote message
type = 3: commit message

*/
public class Message {
	public int type;
	private final int cycle;			/* lượt gửi mess */
	private final int nodeID;			/* ID của node gửi message */
	private final String msg;			/* nội dung mess: với type = 1 ==> msg = ID của node propose, với type = 2 ==> msg = yes/no, 
	type = 3 ==> msg = "commit" */ 

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
