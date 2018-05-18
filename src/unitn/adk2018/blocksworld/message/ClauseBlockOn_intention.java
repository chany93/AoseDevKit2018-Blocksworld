package unitn.adk2018.blocksworld.message;

import unitn.adk2018.Agent;
import unitn.adk2018.blocksworld.Blocksworld;
import unitn.adk2018.generic.message.Clause_msg;
import unitn.adk2018.intention.Intention;
import unitn.adk2018.pddl.PddlClause;

public final class ClauseBlockOn_intention extends Intention<Clause_msg> {

	@Override
	public boolean context(Agent a, Clause_msg g) {
		return g.clause.getPredicate().equals("on"); //check whether it is the case that clause predicate is "on"
	}
	
	@Override
	public Intention<Clause_msg>.Next step0(IntentionInput in) {
		
		agent.getBeliefs().declare(event.clause); //now A is on top of B
		
		String blockAbove = event.clause.getArgs()[0];
		String blockBelow = event.clause.getArgs()[1];
		
		agent.getBeliefs().undeclare( Blocksworld.sayBlockClear(blockBelow) ); //B cannot be clear
		
		agent.getBeliefs().undeclare( Blocksworld.sayBlockOnTable(blockAbove) ); //A cannot be on table
		
		for (PddlClause c : agent.getBeliefs().getACopyOfDeclaredClauses().values()) {
			if (c.getPredicate().equals("holding"))
				if (c.getArgs()[0].equals(blockAbove) || c.getArgs()[1].equals(blockBelow) )
					agent.getBeliefs().undeclare( c ); //B and A cannot be clear
		}
		
		return null;
	}
	
	@Override
	public void pass(IntentionInput in) {
	}
	
	@Override
	public void fail(IntentionInput in) {
	}
	
}
