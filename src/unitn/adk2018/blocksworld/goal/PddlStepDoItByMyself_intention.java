package unitn.adk2018.blocksworld.goal;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.blocksworld.Blocksworld;
import unitn.adk2018.event.Goal;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.goal.PddlStep_goal;
import unitn.adk2018.generic.goal.PddlStep_intention;
import unitn.adk2018.generic.goal.ReachPddlGoal_goal;
import unitn.adk2018.generic.message.Request_msg;
import unitn.adk2018.intention.Intention;
import unitn.adk2018.pddl.PddlClause;

public class PddlStepDoItByMyself_intention extends PddlStep_intention {
	
	@Override
	public boolean context(Agent a, PddlStep_goal g) {
		return ( g.action.equals("pickup") || g.action.equals("putdown") || g.action.equals("stack") || g.action.equals("unstack") ) //supported actions
				&& g.args[0].equals( a.getName() ); // I personally have to do the action
	}
	
}
