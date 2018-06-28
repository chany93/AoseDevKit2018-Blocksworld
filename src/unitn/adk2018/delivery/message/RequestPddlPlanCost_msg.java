package unitn.adk2018.delivery.message;

import unitn.adk2018.event.Goal;
import unitn.adk2018.event.RequestMessage;

public final class RequestPddlPlanCost_msg extends RequestMessage {

	public final String client;
	public final String food;
	public String cost;
	public String rating;
	
	public RequestPddlPlanCost_msg(String _from, String _to, String _client, String _food, String _cost, String _rating) {
		super(_from, _to);
		client = _client;
		food = _food;
		cost = _cost;
		rating = _rating;

	}
	
	public String toString() {
		return super.toString() + ": " + client + "  " + food + " cost: " + cost + " rating: " + rating;
	}
	
}
