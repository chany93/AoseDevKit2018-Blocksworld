package unitn.adk2018.blocksworld.message;

import unitn.adk2018.Agent;
import unitn.adk2018.blocksworld.Blocksworld;
import unitn.adk2018.generic.message.Clause_msg;
import unitn.adk2018.intention.Intention;
import unitn.adk2018.pddl.PddlClause;

public final class ClauseHolding_intention extends Intention<Clause_msg> {

	@Override
	public boolean context(Agent a, Clause_msg g) {
		return g.clause.getPredicate().equals("holding"); //check clause predicate
	}
	
	@Override
	public Intention<Clause_msg>.Next step0(IntentionInput in) {
		
		// as in Clause_intention apply clause
		agent.getBeliefs().declare(event.clause);
		
		String block = event.clause.getArgs()[1];
		
		// block not clear
		agent.getBeliefs().undeclare( Blocksworld.sayBlockClear(block) );
		
		// block not on table
		agent.getBeliefs().undeclare( Blocksworld.sayBlockOnTable(block) );
		
		// block not on or under any other block
		for (PddlClause onClause : agent.getBeliefs().getACopyOfDeclaredClauses().values()) {
			if (onClause.getPredicate().equals("on"))
				if ( onClause.getArgs()[0].equals(block) || onClause.getArgs()[1].equals(block) )
					agent.getBeliefs().undeclare( onClause );
		}
		
		// do success
		return null;
	}
	
}
