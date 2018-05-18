package unitn.adk2018.blocksworld.action;

import unitn.adk2018.blocksworld.Blocksworld;
import unitn.adk2018.pddl.PddlAction3Args;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlWorld;

public class Unstack_action extends PddlAction3Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob, String y) {
		return Blocksworld.sayBlockOn( ob, y ).isDeclaredIn( world )
			&& Blocksworld.sayBlockClear( ob ).isDeclaredIn( world )
			&& Blocksworld.sayFree( hand ).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob, String y) {
		world.undeclare( Blocksworld.sayBlockOn( ob, y ) );
		world.undeclare( Blocksworld.sayBlockClear( ob ) );
		world.declare( Blocksworld.sayBlockClear( y ) );
		world.undeclare( Blocksworld.sayFree( hand ) );
		world.declare( Blocksworld.sayHolding( hand, ob ) );
		return true;
	}
	
}
