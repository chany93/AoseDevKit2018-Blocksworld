package unitn.adk2018.blocksworld;

import java.util.Observable;
import java.util.Observer;

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
import unitn.adk2018.pddl.PddlWorld;

class WakeUpOnTimer implements Observer {
	
	@Override
	public void  update (Observable o, Object arg) {
		synchronized (this) {
			notifyAll();
		}
	}
	
}

public abstract class BlocksworldLauncher {
	
	
	// @params: [-d] actionInitializer domainFile problemFile 
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
		String block_b = "b";
		String env = "env";
		String r1 = "r1";
		String r2 = "r2";
		
		
		
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
		envAgent.getBeliefs().declareObject( block_b );
		// Setup 1
//		envAgent.getBeliefs().declare( Blocksworld.sayFree(r1) );
//		envAgent.getBeliefs().declare( Blocksworld.sayBlockOnTable(block_a) );
//		envAgent.getBeliefs().declare( Blocksworld.sayBlockOn(block_b, block_a) );
//		envAgent.getBeliefs().declare( Blocksworld.sayBlockClear(block_b) );
		// Setup 2
		envAgent.getBeliefs().declare( Blocksworld.sayFree(r1) );
		envAgent.getBeliefs().declare( Blocksworld.sayBlockOnTable(block_a) );
		envAgent.getBeliefs().declare( Blocksworld.sayBlockClear(block_a) );
		envAgent.getBeliefs().declare( Blocksworld.sayHolding(r2, block_b) );
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
		
		
		
		/*
		 * Setup robot 2 agent
		 */
		Agent r2Agent = new General_agent(r2, true);
		r2Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
		// Messages
		r2Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
		r2Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
		// Goals
		r2Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
		r2Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
		// Step goals
		r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepPickupAskHelp_intention.class);
		r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepPutdownAskHelp_intention.class);
		r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepStackAskHelp_intention.class);
		r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepUnstackAskHelp_intention.class);
		r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
		// Env
		Environment.addAgent (r2Agent);
		r2Agent.startInSeparateThread();
		
		
		
		Thread.sleep(200);
		
		
		
		/*
		 * Send message Goal_msg with goal PddlAction_goal with action Pickup_action
		 */
		String[] pickupArgs = {r1, block_b, block_a};
		Message msg0 = new PddlAction_msg( "God", env, "unstack", pickupArgs );
		Environment.sendMessage ( msg0 );
		
		WakeUpOnTimer w = new WakeUpOnTimer();
		System.err.println("First test wait, 2400 msecs, at " + envAgent.getAgentTime());
		envAgent.rescheduleTimer(w,  2400);
		synchronized (w) {
			w.wait();
		}
		System.err.println("End of first test wait at " + envAgent.getAgentTime());
		
		
		
		/*
		 * Notify blocksRobot of everything in the world with Clause INFORM Messages
		 */
		for(PddlClause clause : envAgent.getBeliefs().getACopyOfDeclaredClauses().values()) {
			Environment.sendMessage ( new Clause_msg( "God", r1, clause ) );
		}
		
		
		System.err.println("Second test wait, 500 msecs, at " + envAgent.getAgentTime());		
		envAgent.rescheduleTimer(w,  500);
		synchronized (w) {
			w.wait();
		}
		System.err.println("End of second test wait at " + envAgent.getAgentTime());		
		
		
		
		/*
		 * Send message Goal_msg to blocksRobot with goal ReachPddlGoal_goal with pddlGoal (holding a)
		 */
		PddlClause[] pddlGoal = { Blocksworld.sayHolding(r1, block_b) };
		Goal g1 = new ReachPddlGoal_goal( pddlGoal );
		Message msg1 = new Request_msg( "God", r1, g1 );
		Environment.sendMessage ( msg1 );
		
		System.err.println("Third test wait, 5000 msecs, at " + envAgent.getAgentTime());		
		envAgent.rescheduleTimer(w,  5000);
		synchronized (w) {
			w.wait();
		}
		System.err.println("End of third test wait at " + envAgent.getAgentTime());		
		
		
		
//		System.err.println("########## FULL DUMP ########");
//		r1Agent.printFullState();
//		r2Agent.printFullState();
//		envAgent.printFullState();
		
		
		
//		Environment.pauseSimulationTime();
//		Thread.sleep(5000);
//		Environment.resumeSimulationTime();
//		Thread.sleep(5000);
//		System.exit(0);
		
		
		
	}
	
}
