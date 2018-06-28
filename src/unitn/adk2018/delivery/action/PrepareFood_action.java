package unitn.adk2018.delivery.action;

import unitn.adk2018.delivery.Delivery;
import unitn.adk2018.pddl.PddlAction2Args;
import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class PrepareFood_action extends PddlAction2Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String restaurantName, String foodName) {
		return Delivery.sayWaiting(restaurantName).isDeclaredIn( world )
			&& Delivery.sayHasFood(restaurantName, foodName).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String restaurantName, String foodName) {
		world.undeclare( Delivery.sayWaiting(restaurantName) );
		world.declare( Delivery.sayReadyAtFood(restaurantName, foodName) );
		return true;
	}
	
}
