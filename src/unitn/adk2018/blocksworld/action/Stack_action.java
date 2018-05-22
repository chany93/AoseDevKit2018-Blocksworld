package unitn.adk2018.blocksworld.action;

import unitn.adk2018.blocksworld.Blocksworld;
import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class Stack_action extends PddlAction3Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob, String y) {
		return Blocksworld.sayHolding( hand, ob ).isDeclaredIn( world )
			&& Blocksworld.sayBlockClear( y ).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob, String y) {
		world.declare( Blocksworld.sayBlockOn( ob, y ) );
		world.declare( Blocksworld.sayBlockClear( ob ) );
		world.undeclare( Blocksworld.sayBlockClear( y ) );
		world.undeclare( Blocksworld.sayHolding( hand, ob ) );
		world.declare( Blocksworld.sayFree( hand ) );
		return true;
	}
	
}
