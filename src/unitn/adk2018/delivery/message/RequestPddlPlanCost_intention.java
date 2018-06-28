package unitn.adk2018.delivery.message;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import unitn.adk2018.Environment;
import unitn.adk2018.delivery.Delivery;
import unitn.adk2018.delivery.goal.CalculateCost_goal;
import unitn.adk2018.event.Goal;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.goal.ExecutePddlPlan_goal;
import unitn.adk2018.generic.goal.ReachPddlGoal_goal;
import unitn.adk2018.generic.message.Request_msg;
import unitn.adk2018.intention.Intention;
import unitn.adk2018.pddl.PddlAction;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlPlan;
import unitn.adk2018.pddl.PddlStep;

public final class RequestPddlPlanCost_intention extends Intention<RequestPddlPlanCost_msg> {
	
	Set<String> contactedRestaurant =  new HashSet<String>(); 
	Integer minSteps = 100;
	PddlPlan bestPlan = null;
	String agentName;
	String bestAgent;
	CalculateCost_goal g;
	ExecutePddlPlan_goal p;
	Integer cost = 0;
	Integer rating = 0;

	
	
	@Override
	public Next step0(IntentionInput in) {
		contactedRestaurant.add("r0"); //added so set is not null
		
		/*
		 * find every agent inside beliefs
		 */
		
		
		for(PddlClause cFood : agent.getBeliefs().getACopyOfDeclaredClauses().values()) { 
			if(cFood.getPredicate().equals("has-food") && cFood.getArgs()[1].equals(in.event.food)) {
				
				agentName = cFood.getArgs()[0]; //nome dell'agente ristorante
				
				for(PddlClause cCost :agent.getBeliefs().getACopyOfDeclaredClauses().values()) {
				if (cCost.getPredicate().equals("cost-rest") && cCost.getArgs()[0].equals(agentName)
							&& Integer.parseInt(cCost.getArgs()[1]) <= Integer.parseInt(in.event.cost)) {
						
					
						for(PddlClause cRating :agent.getBeliefs().getACopyOfDeclaredClauses().values()) {
							if (cRating.getPredicate().equals("rating-rest") && cRating.getArgs()[0].equals(agentName)
									&& Integer.parseInt(cRating.getArgs()[1]) >= Integer.parseInt(in.event.rating)) {
					
					
								//ignore if not already contacted
								if (!contactedRestaurant.contains(agentName)) { 
									contactedRestaurant.add(agentName);//add to contacted rest		
									
									/*
									 * contact current rest asking him to calculate its cost (in steps)
									 */
									PddlClause[] pddlGoal = { Delivery.sayServed(in.event.client, in.event.food ) };
									g = new CalculateCost_goal(pddlGoal, minSteps, bestPlan);
									Message msg = new Request_msg( "God", agentName, g );
									Environment.sendMessage ( msg );
									//System.out.println("I'M IN STEP 0");
					
									return waitFor( this::step1, 1000);
									//return waitUntil( this::step1, in.event.wasHandledWithSuccess());
								}
								
							}
						}
				//System.out.println("Agent to contact: " + agentName);
					}
				}
			}
		}
	/*
	 * when finished asking all agents, go to step2 and to execute best plan
	 */
	return waitFor(this::step2, 1000);
	//esce dal for e vai a step2 (finale) in cui invio la richiesta al bestplan agent
	
	}
	
	public Next step1(IntentionInput in) {
		
		if(g.wasHandledWithSuccess().isTrue()) {
			int stepsFromRest = g.getMinSteps();
			PddlPlan planFromRest = g.getBestPlan();
			System.out.println("before if in step1......." + stepsFromRest + "  ----  " + minSteps);
			
			/*
			 * check whether number of step of current agent's plan is less than the minimum yet
			 */
			if (stepsFromRest < minSteps) {
				minSteps = stepsFromRest;
				bestPlan = planFromRest;
				bestAgent = agentName;
				System.out.println("best agent in step 1: " + bestAgent);
			}
		}
		
	
		return  waitFor(this::step0, 1000); //success go back to step0 for next agent
	}
	
	public Next step2(IntentionInput in) {
		
	//	System.out.println("I'M IN STEP 2");
	
		System.out.println("best agent: " + bestAgent);
		cost = Integer.parseInt(in.event.cost);
		rating = Integer.parseInt(in.event.rating);

		/*
		 * if no restaurant matches cost and rating requirements, lower/increase the values
		 */
		if (bestAgent == null && cost < 3 && rating > 0) {
			
			cost++;
			rating--;
			in.event.cost = Integer.toString(cost);
			in.event.rating = Integer.toString(rating);
			
			return waitFor(this::step0, 100); //go back to step0 and try with new requirements
			
		}
		
	
		/*
		 * give the goal to the best agent
		 */
		
		PddlClause[] pddlGoal = { Delivery.sayServed(in.event.client, in.event.food ) };
		Goal g = new ReachPddlGoal_goal(pddlGoal);
		Message msg = new Request_msg( "God", bestAgent, g );
		Environment.sendMessage ( msg );
		
		
		
		
		return null; //success
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
