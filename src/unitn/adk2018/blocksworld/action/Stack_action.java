package unitn.adk2018.blocksworld.action;

import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class Stack_action extends PddlAction3Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob, String y) {
		return PddlClause.say( "holding", hand, ob ).isDeclaredIn( world )
			&& PddlClause.say( "clear", y ).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob, String y) {
		world.declare( PddlClause.say( "on", ob, y ) );
		world.declare( PddlClause.say( "clear", ob ) );
		world.undeclare( PddlClause.say( "holding", hand, ob ) );
		world.declare( PddlClause.say( "free", hand ) );
		return true;
	}
	
}
