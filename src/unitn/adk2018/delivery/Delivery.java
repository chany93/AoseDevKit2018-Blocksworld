package unitn.adk2018.delivery;

import unitn.adk2018.pddl.PddlClause;

public class Delivery {
	
	public static PddlClause sayNotMe ( String restaurantName ) {
		return PddlClause.say("not-me", restaurantName);
	}
	
	public static PddlClause sayMe ( String restaurantName ) {
		return PddlClause.say("me", restaurantName);
	}
	
	public static PddlClause sayRestaurant ( String restaurantName ) {
		return PddlClause.say("restaurant", restaurantName);
	}
	
	public static PddlClause sayFood ( String foodName ) {
		return PddlClause.say("food", foodName);
	}
	
	public static PddlClause sayClient ( String clientName) {
		return PddlClause.say("client", clientName);
	}
	
	public static PddlClause sayDeliveryBoy ( String deliveryBoyName ) {
		return PddlClause.say("delivery-boy", deliveryBoyName);
	}
	
	public static PddlClause sayLocation ( String locationName ) {
		return PddlClause.say("location", locationName);
	}
	
	public static PddlClause sayWaiting ( String restaurantName ) {
		return PddlClause.say("waiting", restaurantName);
	}
	public static PddlClause sayFree ( String deliveryBoyName ) {
		return PddlClause.say("free", deliveryBoyName);
	}
	
	public static PddlClause sayWithFood ( String deliveryBoyName, String foodName ) {
		return PddlClause.say("with-food", deliveryBoyName, foodName);
	}
	
	public static PddlClause sayHasFood ( String restaurantName, String foodName ) {
		return PddlClause.say("has-food", restaurantName, foodName);
	}
	
	public static PddlClause sayReadyAtFood ( String restaurantName, String foodName ) {
		return PddlClause.say("ready-at-food", restaurantName, foodName);
	}
	
	public static PddlClause sayNextTo ( String departure, String destination ) {
		return PddlClause.say("next-to", departure, destination);
	}
	
	public static PddlClause sayAt ( String locationName, String deliveryBoyName ) {
		return PddlClause.say("at", locationName, deliveryBoyName);
	}
	
	public static PddlClause sayServed ( String clientName, String foodName ) {
		return PddlClause.say("served", clientName, foodName );
	}
	
	public static PddlClause sayCost ( String restaurantName, String cost ) {
		return PddlClause.say("cost-rest", restaurantName, cost );
	}
	
	public static PddlClause sayRating ( String restaurantName, String rating ) {
		return PddlClause.say("rating-rest", restaurantName, rating );
	}
	
	
}
