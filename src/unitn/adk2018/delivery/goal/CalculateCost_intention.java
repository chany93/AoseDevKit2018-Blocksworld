package unitn.adk2018.delivery.goal;

import java.util.Iterator;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.delivery.Delivery;
import unitn.adk2018.event.Goal;
import unitn.adk2018.generic.message.Sensing_msg;
import unitn.adk2018.intention.Intention;
import unitn.adk2018.pddl.PddlAction;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlPlan;
import unitn.adk2018.pddl.PddlStep;
import unitn.adk2018.utils.BlackboxUtils;

public class CalculateCost_intention extends Intention<CalculateCost_goal> {
	
	String firstLocationBoy;
	String lastLocationBoy;
	@Override
	public Next step0(IntentionInput in) {
		/*
		 * Do sensing
		 */
		Sensing_msg sensing = new Sensing_msg( agent.getName(), Environment.getEnvironmentAgent().getName() );
		Environment.sendMessage( sensing );
		return waitUntil( this::step1, sensing.wasHandled() );
	}
	
	public Next step1(IntentionInput in) {
		
		String pddlDomainFile = Environment.getPddlDomain().domainFile;
		PddlClause[] goal = in.event.pddlGoal;
		
		/*
		 
		for(PddlClause c : agent.getBeliefs().getACopyOfDeclaredClauses().values()) { 
			if(c.getPredicate().equals("at")) {
				
					//agent.getBeliefs().undeclare(c);
				firstLocationBoy = c.getArgs()[0];
			}
		}
		*/
		
		/*
		 * Generate Pddl Goal
		 */
		String pddlGoal = "( and "; 
		for(PddlClause c : goal) {
			pddlGoal += " (" + c + ")";
		}
		pddlGoal += " )";
		
		/*
		Iterator it = Environment.getAgents().values().iterator();
		
		 while(it.hasNext()) {
	         Agent agent = (Agent) it.next();
	         System.out.println(agent + " ");
	         
	         
	         PddlPlan plan = BlackboxUtils.doPlan(agent, pddlDomainFile, agent.getBeliefs(), pddlGoal);
	         
	      }
		*/
		
		/*
		 * Generate Pddl plan
		 */
		final PddlPlan plan = BlackboxUtils.doPlan(agent, pddlDomainFile, agent.getBeliefs(), pddlGoal);
		Integer numSteps = 0;
		
		if (agent.debugOn)
			System.out.println( agent.getName() + " planned? " + (plan!=null) );
			for (PddlStep s:plan.getSteps() ) {
				if (!s.parallelizableWithPrevious)  //ritorna falso quando è la prima esecuzione dello step
					numSteps++;
				PddlAction action = Environment.getPddlDomain().generatePddlAction ( s.action );
				//action.checkPreconditionsAndApply(agent.getBeliefs(), s.getArgs() ); //applica belief instantaneamente
				if(action.checkPreconditions(agent.getBeliefs(), s.getArgs() )) {
					System.out.println("possible");
				}
					
			}
		//	numSteps = plan.getSteps();
				
				/*
				 * set return values
				 */
				in.event.minSteps = numSteps; 
				in.event.bestPlan = plan;
			
			
			//String nameOfAgentToAskForHelp = in.event.step.args[0];
			
			System.out.println("Number of steps: " + numSteps);
			System.out.println("Plan: " + plan);
			//System.out.println("Best plan: " + in.event.bestPlan);
			
			/*
			for(PddlClause c : agent.getBeliefs().getACopyOfDeclaredClauses().values()) { 
				if(c.getPredicate().equals("at")) {
					if(c.getArgs()[0] != firstLocationBoy)
						agent.getBeliefs().undeclare(c);
					//lastLocationBoy = c.getArgs()[0];
				}
			}
			*/
				
			/*
			 * Send broker agent number of steps
			 */
			//NumberSteps_msg n = new NumberSteps_msg (agent.getName(), "broker", numSteps);
			
		
		if (plan==null)
			return waitFor ( null, 0 );
			//System.exit(1);
		
		/*
		 * agent belief of his own numebr of steps - not used
		 */
		//agent.getBeliefs().declare(Delivery.sayNumSteps(agent.getName(), Integer.toString(numSteps)));

	
	
		
		//agent.pushGoal ( g, in.event.wasNotHandled() );  
		        /// note that the PDDL plan will fail if its goal is retracted (possibly forcely because of a meta-decision)
//		GoalMsg_msg msg = new GoalMsg_msg( agent.getName(), Environment.getEnvironmentAgentName(), g );
//		Environment.getEnvironment().sendMessage( msg );
		return null;
	}
	



	@Override
	public void pass(IntentionInput in) {
	}

	@Override
	public void fail(IntentionInput in) {
	}
	
	public String toString() {
		return super.toString() + ": " +  event.getClass().getSimpleName();
	}
}
