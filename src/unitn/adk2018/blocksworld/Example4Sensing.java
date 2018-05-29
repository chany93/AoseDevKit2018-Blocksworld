package unitn.adk2018.blocksworld;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.blocksworld.action.Pickup_action;
import unitn.adk2018.blocksworld.action.Putdown_action;
import unitn.adk2018.blocksworld.action.Stack_action;
import unitn.adk2018.blocksworld.action.Unstack_action;
import unitn.adk2018.event.Goal;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.agent.General_agent;
import unitn.adk2018.generic.goal.ExecutePddlPlanParallel_intention;
import unitn.adk2018.generic.goal.ExecutePddlPlan_goal;
import unitn.adk2018.generic.goal.PddlStep_goal;
import unitn.adk2018.generic.goal.PddlStep_intention;
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


/**
 * This is to show a limitation of sensing
 * and how to correctly interpret clause messages
 * @author Marco
 *
 */
public abstract class Example4Sensing {
	
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
		String block_c = "c";
		String block_d = "d";
		String env = "env";
		String r1 = "r1";
		
		
		
		/*
		 * Setup robot 1 agent
		 */
		Agent r1Agent = new General_agent(r1, true);
		r1Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
		// Messages
//		r1Agent.addSupportedEvent(Clause_msg.class, ClauseBlockOn_intention.class);
//		r1Agent.addSupportedEvent(Clause_msg.class, ClauseHolding_intention.class);
		r1Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
		r1Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
		// Goals
		r1Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
		r1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlanParallel_intention.class);
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStep_intention.class);
		// Beliefs
		r1Agent.getBeliefs().declareObject( r1 );
		r1Agent.getBeliefs().declare( Blocksworld.sayMe(r1) );
		// Env
		Environment.addAgent (r1Agent);
		r1Agent.startInSeparateThread();
		
		
		
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
		/*
		 * Initial state of the world:
		 * R1: -
		 * R2: -
		 *         D
		 *         C
		 *         B
		 * Table:  A
		 */
		envAgent.getBeliefs().declare( Blocksworld.sayFree(r1) );					// R1
		envAgent.getBeliefs().declare( Blocksworld.sayBlockOnTable(block_a) );		// A
		envAgent.getBeliefs().declare( Blocksworld.sayBlockOn(block_b, block_a) );	// B
		envAgent.getBeliefs().declare( Blocksworld.sayBlockOn(block_c, block_b) );	// C
		envAgent.getBeliefs().declare( Blocksworld.sayBlockOn(block_d, block_c) );	// D
		envAgent.getBeliefs().declare( Blocksworld.sayBlockClear(block_d) );		// D
		// Env
		Environment.addAgent (envAgent);
		Environment.setEnvironmentAgent (envAgent);
		envAgent.startInSeparateThread();
		
		
		
		Sleep.sleep(200);
		
		

		System.err.println( "Beliefs of r1:  " + r1Agent.getBeliefs().pddlClauses() );
		System.err.println( "Beliefs of env: " + envAgent.getBeliefs().pddlClauses() );
		
		
		
		/*
		 * Send message Request_msg to r1 with goal PddlStep_goal
		 */
		PddlClause[] pddlGoal1 = { Blocksworld.sayHolding(r1, block_d) };
		Goal g1 = new ReachPddlGoal_goal( pddlGoal1 );
		Message msg1 = new Request_msg( "God", r1, g1 );
		Environment.sendMessage ( msg1 );
		
		
		
		Sleep.sleep(1000);
		
		
		
		System.err.println( "Beliefs of r1:  " + r1Agent.getBeliefs().pddlClauses() );
		System.err.println( "Beliefs of env: " + envAgent.getBeliefs().pddlClauses() );
		
		
		
		Sleep.sleep(4000);
		
		
		
		System.err.println( "Beliefs of r1:  " + r1Agent.getBeliefs().pddlClauses() );
		System.err.println( "Beliefs of env: " + envAgent.getBeliefs().pddlClauses() );
		
		
		
		/*
		 * Send message Request_msg to r1 with goal PddlStep_goal
		 */
		PddlClause[] pddlGoal2 = { Blocksworld.sayBlockOn(block_d, block_c) };
		Goal g2 = new ReachPddlGoal_goal( pddlGoal2 );
		Message msg2 = new Request_msg( "God", r1, g2 );
		Environment.sendMessage ( msg2 );
		
		
		
		Sleep.sleep(1000);
		
		
		
		System.err.println( "Beliefs of r1:  " + r1Agent.getBeliefs().pddlClauses() );
		System.err.println( "Beliefs of env: " + envAgent.getBeliefs().pddlClauses() );
		
		

		Sleep.sleep(4000);
		
		
		
		System.err.println( "Beliefs of r1:  " + r1Agent.getBeliefs().pddlClauses() );
		System.err.println( "Beliefs of env: " + envAgent.getBeliefs().pddlClauses() );
		
		
		
	}
	
}
