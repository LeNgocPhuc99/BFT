import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

public class Tree_Map_Demo {
	
	private static Vote getMajorityVote(TreeMap<Integer, Vote> tree_map) {

		Map<Vote, Long> orderCounter = tree_map.values().stream()
				.collect(Collectors.groupingBy(s -> s, Collectors.counting()));

		Vote vote = orderCounter.getOrDefault(Vote.YES, 0L) > orderCounter.getOrDefault(Vote.NO, 0L) ? Vote.YES
				: Vote.NO;

		return vote;
	}

	public static void main(String[] args) throws JSONException {

		// Creating an empty TreeMap
		TreeMap<Integer, Vote> tree_map = new TreeMap<Integer, Vote>();

		// Mapping string values to int keys
		tree_map.put(1, Vote.YES);
		tree_map.put(2, Vote.NO);
		tree_map.put(3, Vote.NO);
		tree_map.put(4, Vote.NO);
		tree_map.put(5, Vote.YES);
		
		Vote vote = Vote.YES;
		String message = "{vote:" + vote + ",type:" + 2 + ", cycle:" + 0 + ", nodeID:" + 6 +"}";

		JSONObject json = new JSONObject(message);
		
		Vote newVote = json.getString("vote").equals("YES") ? Vote.YES : Vote.NO;
		
		tree_map.put(json.getInt("nodeID"), newVote);
		
		
		//Vote vote = getMajorityVote(tree_map);

		System.out.println(tree_map.toString());
		System.out.println(json.toString());

	}
}