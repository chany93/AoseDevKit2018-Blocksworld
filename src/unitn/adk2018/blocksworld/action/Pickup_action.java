package unitn.adk2018.blocksworld.action;

import unitn.adk2018.blocksworld.Blocksworld;
import unitn.adk2018.pddl.PddlAction2Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class Pickup_action extends PddlAction2Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob) {
		return Blocksworld.sayBlockClear( ob ).isDeclaredIn( world )
			&& Blocksworld.sayBlockOnTable( ob ).isDeclaredIn( world )
			&& Blocksworld.sayFree( hand ).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob) {
		world.undeclare( Blocksworld.sayBlockClear( ob ) );
		world.undeclare( Blocksworld.sayBlockOnTable( ob ) );
		world.declare( Blocksworld.sayHolding( hand, ob ) );
		world.undeclare( Blocksworld.sayFree( hand ) );
		return true;
	}
	
}
