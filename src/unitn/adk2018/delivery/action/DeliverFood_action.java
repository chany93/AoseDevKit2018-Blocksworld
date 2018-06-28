package unitn.adk2018.delivery.action;

import unitn.adk2018.delivery.Delivery;
import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlAction4Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class DeliverFood_action extends PddlAction3Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String deliveryBoyName, String foodName, String clientName) {
		return Delivery.sayDeliveryBoy(deliveryBoyName).isDeclaredIn( world )
			//&& Delivery.sayRestaurant(restaurantName).isDeclaredIn( world )
			&& Delivery.sayClient(clientName).isDeclaredIn(world)
			&& Delivery.sayFood(foodName).isDeclaredIn( world )
			&& Delivery.sayAt(clientName, deliveryBoyName).isDeclaredIn( world )
			&& Delivery.sayWithFood(deliveryBoyName, foodName).isDeclaredIn(world)
			&& !Delivery.sayFree(deliveryBoyName).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String deliveryBoyName, String foodName, String clientName) {
		world.declare( Delivery.sayFree(deliveryBoyName) );
		world.declare( Delivery.sayServed(clientName, foodName) );
		world.undeclare( Delivery.sayWithFood(deliveryBoyName, foodName));
		return true;
	}
	
}
