package unitn.adk2018.blocksworld;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.blocksworld.action.Pickup_action;
import unitn.adk2018.blocksworld.action.Putdown_action;
import unitn.adk2018.blocksworld.action.Stack_action;
import unitn.adk2018.blocksworld.action.Unstack_action;
import unitn.adk2018.blocksworld.goal.PddlStepDoItByMyself_intention;
import unitn.adk2018.blocksworld.goal.PddlStepPickupAskHelp_intention;
import unitn.adk2018.blocksworld.goal.PddlStepPutdownAskHelp_intention;
import unitn.adk2018.blocksworld.goal.PddlStepStackAskHelp_intention;
import unitn.adk2018.blocksworld.goal.PddlStepUnstackAskHelp_intention;
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

public abstract class BlocksworldLauncher0 {
	
	
	public static void main(String[] args) throws InterruptedException {
		
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
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepPickupAskHelp_intention.class);
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepPutdownAskHelp_intention.class);
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepStackAskHelp_intention.class);
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepUnstackAskHelp_intention.class);
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepPutdownAskHelp_intention.class);
		r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
		// Env
		Environment.addAgent (r1Agent);
		r1Agent.startInSeparateThread();
		
		
		
		Thread.sleep(200);
		
		
		
		/*
		 * Send message Goal_msg to blocksRobot with goal ReachPddlGoal_goal with pddlGoal (holding a)
		 */
		PddlClause[] pddlGoal = { Blocksworld.sayHolding(r1, block_a) };
		Goal g1 = new ReachPddlGoal_goal( pddlGoal );
		Message msg1 = new Request_msg( "God", r1, g1 );
		Environment.sendMessage ( msg1 );
		
		
		
	}
	
}
