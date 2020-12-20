/*
type = 1: propose message
type = 2: vote message
type = 3: commit message
*/
abstract class Message {
	public int type;
	private final int cycle;
	private final int nodeID;
	// private final String msg;

	Message(int type, int cycle, int id) {
		this.type = type;
		this.cycle = cycle;
		this.nodeID = id;
		// this.msg = msg;
	}

	@Override
	public String toString() {
		return ", type: " + type + ", cycle: " + cycle + ", nodeID:" + nodeID ;
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

class ProposeMessage extends Message {

	private String msg;

	ProposeMessage(int type, int cycle, int id, String msg) {
		super(type, cycle, id);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "{msg: " + msg + super.toString() + "}";
	}
}

class VoteMessage extends Message {
	
	private Vote vote;
	
	VoteMessage(int type, int cycle, int id, Vote vote) {
		super(type, cycle, id);
		this.vote = vote;
	}

	public Vote getVote() {
		return vote;
	}

	@Override
	public String toString() {
		return "{vote: " + vote + super.toString() + "}";
	}	

}

























