package unitn.adk2018.delivery.action;

import unitn.adk2018.delivery.Delivery;
import unitn.adk2018.pddl.PddlAction2Args;
import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class GoTo_action extends PddlAction3Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String deliveryBoyName, String departure, String destination) {
		return Delivery.sayDeliveryBoy(deliveryBoyName).isDeclaredIn( world )
			&& Delivery.sayLocation(departure).isDeclaredIn( world )
			&& Delivery.sayLocation(destination).isDeclaredIn( world )
			&& Delivery.sayAt(departure, deliveryBoyName).isDeclaredIn( world )
			&& Delivery.sayNextTo(departure, destination).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String deliveryBoyName, String departure, String destination) {
		world.undeclare( Delivery.sayAt(departure, deliveryBoyName));
		world.declare( Delivery.sayAt(destination, deliveryBoyName) );
		return true;
	}
	
}
