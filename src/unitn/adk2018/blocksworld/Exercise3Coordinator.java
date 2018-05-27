package unitn.adk2018.blocksworld;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.blocksworld.action.Pickup_action;
import unitn.adk2018.blocksworld.action.Putdown_action;
import unitn.adk2018.blocksworld.action.Stack_action;
import unitn.adk2018.blocksworld.action.Unstack_action;
import unitn.adk2018.blocksworld.goal.PddlStepDoItByMyself_intention;
import unitn.adk2018.blocksworld.goal.PddlStepAskHelp_intention;
import unitn.adk2018.blocksworld.message.ClauseBlockOn_intention;
import unitn.adk2018.event.Goal;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.agent.General_agent;
import unitn.adk2018.generic.goal.ExecutePddlPlanParallel_intention;
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

public abstract class Exercise3Coordinator {
	
	public static void main(String[] args) throws InterruptedException {
		
		Logger.A_MAX = 4;
		Logger.GANTT = true;
		
		/*
		 * Setup domain
		 */
		PddlDomain pddlDomain = new PddlDomain("blocks-domain-coordinator");
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
		String r2 = "r2";
		String coordinator = "coordinator";
		
		
		
		/*
		 * Setup robot 1 agent
		 */
		{
			Agent r1Agent = new General_agent(r1, true);
			r1Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			// Messages
			r1Agent.addSupportedEvent(Clause_msg.class, ClauseBlockOn_intention.class);
			r1Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			r1Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
			// Goals
//			r1Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
//			r1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
			// Step goals
//			r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
			// Beliefs
			r1Agent.getBeliefs().declareObject( r1 );
			r1Agent.getBeliefs().declareObject( r2 );
			r1Agent.getBeliefs().declare( Blocksworld.sayMe(r1) );
			r1Agent.getBeliefs().declare( Blocksworld.sayNotMe(r2) );
			// Env
			Environment.addAgent (r1Agent);
			r1Agent.startInSeparateThread();
		}
		
		
		
		/*
		 * Setup robot 2 agent
		 */
		{
			Agent r2Agent = new General_agent(r2, true);
			r2Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			// Messages
			r2Agent.addSupportedEvent(Clause_msg.class, ClauseBlockOn_intention.class);
			r2Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			r2Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
			// Goals
//			r2Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
//			r2Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
			// Step goals
//			r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
			// Beliefs
			r2Agent.getBeliefs().declareObject( r1 );
			r2Agent.getBeliefs().declareObject( r2 );
			r2Agent.getBeliefs().declare( Blocksworld.sayMe(r2) );
			r2Agent.getBeliefs().declare( Blocksworld.sayNotMe(r1) );
			// Env
			Environment.addAgent (r2Agent);
			r2Agent.startInSeparateThread();
		}
		
		
		
		/*
		 * Setup coordinator agent
		 */
		{
			Agent coordinatorAgent = new General_agent(coordinator, true);
			coordinatorAgent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			// Messages
			coordinatorAgent.addSupportedEvent(Clause_msg.class, ClauseBlockOn_intention.class);
			coordinatorAgent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			coordinatorAgent.addSupportedEvent(Request_msg.class, Request_intention.class);
			// Goals
			coordinatorAgent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
			coordinatorAgent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlanParallel_intention.class);
			coordinatorAgent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			// Env
			Environment.addAgent (coordinatorAgent);
			coordinatorAgent.startInSeparateThread();
		}
		
		
		
		/*
		 * Setup environmentAgent
		 */
		{
			Agent envAgent = new General_agent(env, true);
			// Goals
			envAgent.addSupportedEvent(Postman_goal.class, PostmanEverythingInParallel_intention.class);
			// Messages
			envAgent.addSupportedEvent(PddlAction_msg.class, PddlAction_intention.class);
			envAgent.addSupportedEvent(Sensing_msg.class, Sensing_intention.class);
			// Beliefs
			envAgent.getBeliefs().declareObject( r1 );
			envAgent.getBeliefs().declareObject( r2 );
			envAgent.getBeliefs().declareObject( block_a );
			envAgent.getBeliefs().declareObject( block_b );
			envAgent.getBeliefs().declareObject( block_c );
			envAgent.getBeliefs().declareObject( block_d );
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
			envAgent.getBeliefs().declare( Blocksworld.sayFree(r2) );					// R2
			envAgent.getBeliefs().declare( Blocksworld.sayBlockOnTable(block_a) );		// A
			envAgent.getBeliefs().declare( Blocksworld.sayBlockOn(block_b, block_a) );	// B
			envAgent.getBeliefs().declare( Blocksworld.sayBlockOn(block_c, block_b) );	// C
			envAgent.getBeliefs().declare( Blocksworld.sayBlockOn(block_d, block_c) );	// D
			envAgent.getBeliefs().declare( Blocksworld.sayBlockClear(block_d) );		// D
			// Env
			Environment.addAgent (envAgent);
			Environment.setEnvironmentAgent (envAgent);
			envAgent.startInSeparateThread();
		}
		
		
		
		Thread.sleep(200);
		
		
		
		/*
		 * Send message Goal_msg to blocksRobot with goal ReachPddlGoal_goal with pddlGoal (holding a)
		 */
		/*
		 * Goal requested to r1:
		 * 
		 *         A
		 *         B
		 */
		PddlClause[] pddlGoal1 = { Blocksworld.sayBlockOn(block_a, block_b) };
		Goal g1 = new ReachPddlGoal_goal( pddlGoal1 );
		Message msg1 = new Request_msg( "God", coordinator, g1 );
		Environment.sendMessage ( msg1 );
		
		
		
	}
	
}
