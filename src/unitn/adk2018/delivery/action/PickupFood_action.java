package unitn.adk2018.delivery.action;

import unitn.adk2018.delivery.Delivery;
import unitn.adk2018.pddl.PddlAction2Args;
import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlAction4Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class PickupFood_action extends PddlAction3Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String deliveryBoyName, String foodName, String restaurantName) {
		return Delivery.sayDeliveryBoy(deliveryBoyName).isDeclaredIn( world )
			&& Delivery.sayRestaurant(restaurantName).isDeclaredIn( world )
			&& Delivery.sayFood(foodName).isDeclaredIn( world )
			&& Delivery.sayAt(restaurantName, deliveryBoyName).isDeclaredIn( world )
			&& !Delivery.sayWithFood(deliveryBoyName, foodName).isDeclaredIn(world)
			&& Delivery.sayFree(deliveryBoyName).isDeclaredIn( world )
			&& Delivery.sayReadyAtFood(restaurantName, foodName).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String deliveryBoyName, String foodName, String restaurantName) {
		world.declare( Delivery.sayWaiting(restaurantName) );
		world.undeclare( Delivery.sayReadyAtFood(restaurantName, foodName) );
		world.undeclare( Delivery.sayFree(deliveryBoyName) );
		world.declare( Delivery.sayAt(restaurantName, deliveryBoyName) );
		world.declare( Delivery.sayWithFood(deliveryBoyName, foodName) );
		return true;
	}
	
}
