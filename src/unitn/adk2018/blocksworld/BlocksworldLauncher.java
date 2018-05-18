package unitn.adk2018.blocksworld;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.blocksworld.action.Pickup_action;
import unitn.adk2018.blocksworld.action.Putdown_action;
import unitn.adk2018.blocksworld.action.Stack_action;
import unitn.adk2018.blocksworld.action.Unstack_action;
import unitn.adk2018.blocksworld.goal.PddlStepDoItByMyself_intention;
import unitn.adk2018.blocksworld.goal.PddlStepAskHelp_intention;
import unitn.adk2018.event.Goal;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.agent.General_agent;
import unitn.adk2018.generic.goal.ExecutePddlPlan_goal;
import unitn.adk2018.generic.goal.ExecutePddlPlan_intention;
import unitn.adk2018.generic.goal.PddlStep_goal;
import unitn.adk2018.generic.goal.PostmanEverythingInParallel_intention;
import unitn.adk2018.generic.goal.PostmanOneRequestAtTime_intention;
import unitn.adk2018.generic.goal.Postman_goal;
import unitn.adk2018.generic.goal.ReachPddlGoal_goal;
import unitn.adk2018.generic.goal.ReachPddlGoal_intention;
import unitn.adk2018.generic.message.Clause_intention;
import unitn.adk2018.generic.message.Clause_msg;
import unitn.adk2018.generic.message.PddlAction_intention;
import unitn.adk2018.generic.message.PddlAction_msg;
import unitn.adk2018.generic.message.Request_intention;
import unitn.adk2018.generic.message.Request_msg;
import unitn.adk2018.generic.message.Sensing_intention;
import unitn.adk2018.generic.message.Sensing_msg;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlDomain;
import unitn.adk2018.utils.Sleep;

public abstract class BlocksworldLauncher {
	
	
	public static void main(String[] args) throws InterruptedException {

		Logger.A_MAX = 2;
		Logger.GANTT = true;
		
		/*
		 * Setup domain
		 */
		PddlDomain pddlDomain = new PddlDomain("blocks-domain");
		Environment.setPddlDomain(pddlDomain);
		// Actions of the PDDL domain
		pddlDomain.addSupportedAction ("pickup", Pickup_action.class);
		pddlDomain.addSupportedAction ("putdown", Putdown_action.class);
		pddlDomain.addSupportedAction ("stack", Stack_action.class);
		pddlDomain.addSupportedAction ("unstack", Unstack_action.class);
		
		
		
		/*
		 * Setup problem
		 */
		String block_a = "a";
		String block_b = "b";
		String env = "env";
		String r1 = "r1";
		
		
		
		/*
		 * Setup environmentAgent
		 */
		Agent envAgent = new General_agent(env, true);
		// Goals
		envAgent.addSupportedEvent(Postman_goal.class, PostmanEverythingInParallel_intention.class);
		// Messages
		envAgent.addSupportedEvent(PddlAction_msg.class, PddlAction_intention.class);
		envAgent.addSupportedEvent(Sensing_msg.class, Sensing_intention.class);
		// Beliefs
		envAgent.getBeliefs().declareObject( r1 );
		envAgent.getBeliefs().declareObject( block_a );
		// Setup 1
		envAgent.getBeliefs().declare( Blocksworld.sayFree(r1) );
		envAgent.getBeliefs().declare( Blocksworld.sayBlockOnTable(block_a) );
		envAgent.getBeliefs().declare( Blocksworld.sayBlockClear(block_a) );
		envAgent.getBeliefs().declare( Blocksworld.sayBlockOnTable(block_b) );
		envAgent.getBeliefs().declare( Blocksworld.sayBlockClear(block_b) );
		// Env
		Environment.addAgent (envAgent);
		Environment.setEnvironmentAgent (envAgent);
		envAgent.startInSeparateThread();
		
		
		
		/*
		 * Setup robot 1 agent
		 */
		Agent r1Agent = new General_agent(r1, true);
		r1Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
		// Messages
		r1Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
		r1Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
		// Goals
		r1Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
		r1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
		// Step goals
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
		// Beliefs
		r1Agent.getBeliefs().declare( Blocksworld.sayMe(r1) );
		// Env
		Environment.addAgent (r1Agent);
		r1Agent.startInSeparateThread();
		
		

		/*
		 * Wait 1000ms on simulation timer
		 */
		System.err.println("First test wait, 1000 msecs, at " + envAgent.getAgentTime());
		Sleep.sleep(1000);
		System.err.println("End of first test wait at " + envAgent.getAgentTime());
		
		
		
		/*
		 * Notify blocksRobot of everything in the world with Clause INFORM Messages
		 */
		for(PddlClause clause : envAgent.getBeliefs().getACopyOfDeclaredClauses().values()) {
			Environment.sendMessage ( new Clause_msg( "God", r1, clause ) );
		}

		
		
		Sleep.sleep(500);
		
		
		
		/*
		 * Send message Goal_msg with goal PddlAction_goal with action Pickup_action
		 * This will fail because a is not hold!
		 */
		String[] pickupArgs = {r1, block_a};
		Message msg0 = new PddlAction_msg( "God", env, "putdown", pickupArgs );
		Environment.sendMessage ( msg0 );
		
		

		/*
		 * Wait 2000ms on simulation timer
		 */
		Sleep.sleep(2000);
		
		
		
		/*
		 * Send message Goal_msg to blocksRobot with goal ReachPddlGoal_goal with pddlGoal (holding a)
		 */
		PddlClause[] pddlGoal = { Blocksworld.sayBlockOn( block_a, block_b ) };
		Goal g1 = new ReachPddlGoal_goal( pddlGoal );
		Message msg1 = new Request_msg( "God", r1, g1 );
		Environment.sendMessage ( msg1 );

		
		
		/*
		 * When msg1 is handled print full dump
		 */
		msg1.wasHandled().addObserver( new Observer() {
			public void update(Observable o, Object arg) {
				System.err.println("########## FULL DUMP ########");
				r1Agent.printFullState();
				envAgent.printFullState();
			}
		});
		
		
		
	}
	
}
