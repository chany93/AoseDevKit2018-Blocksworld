package unitn.adk2018.blocksworld.action;

import unitn.adk2018.blocksworld.Blocksworld;
import unitn.adk2018.pddl.PddlAction2Args;
import unitn.adk2018.pddl.PddlWorld;

public class Putdown_action extends PddlAction2Args {

	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob) {
		return Blocksworld.sayHolding( hand, ob).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob) {
		Blocksworld.sayBlockOnTable( ob ).declareIn( world );
		Blocksworld.sayBlockClear( ob ).declareIn( world );
		Blocksworld.sayHolding( hand, ob ).undeclareIn( world );
		Blocksworld.sayFree( hand ).declareIn( world );
		return true;
	}
	
}
