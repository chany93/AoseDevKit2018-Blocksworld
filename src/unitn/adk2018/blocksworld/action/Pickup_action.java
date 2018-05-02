package unitn.adk2018.blocksworld.action;

import unitn.adk2018.pddl.PddlAction2Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class Pickup_action extends PddlAction2Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob) {
		return PddlClause.say( "clear", ob ).isDeclaredIn( world )
			&& PddlClause.say( "on-table", ob ).isDeclaredIn( world )
			&& PddlClause.say( "free", hand ).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob) {
		world.undeclare( PddlClause.say( "clear", ob ) );
		world.undeclare( PddlClause.say( "on-table", ob ) );
		world.declare( PddlClause.say( "holding", hand, ob ) );
		world.undeclare( PddlClause.say( "free", hand ) );
		return true;
	}
	
}
