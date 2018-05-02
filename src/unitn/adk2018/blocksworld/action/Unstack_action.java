package unitn.adk2018.blocksworld.action;

import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class Unstack_action extends PddlAction3Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob, String y) {
		return PddlClause.say( "on", ob, y ).isDeclaredIn( world )
			&& PddlClause.say( "clear", ob ).isDeclaredIn( world )
			&& PddlClause.say( "free", hand ).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob, String y) {
		world.undeclare( PddlClause.say( "on", ob, y ) );
		world.undeclare( PddlClause.say( "clear", ob ) );
		world.declare( PddlClause.say( "clear", y ) );
		world.undeclare( PddlClause.say( "free", hand ) );
		world.declare( PddlClause.say( "holding", hand, ob ) );
		return true;
	}
	
}
