package unitn.adk2018.blocksworld.action;

import unitn.adk2018.pddl.PddlAction2Args;
import unitn.adk2018.pddl.PddlWorld;

public class Putdown_action extends PddlAction2Args {

	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob) {
		return world.say("holding", hand, ob).isDeclared();
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob) {
		world.say("on-table", ob).declare();
		world.say("clear", ob).declare();
		world.say("holding", hand, ob).undeclare();
		world.say("free", hand).declare();
		return true;
	}
	
}
