package unitn.adk2018.delivery;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.delivery.goal.PddlStepDoItByMyself_intention;
import unitn.adk2018.delivery.action.DeliverFood_action;
import unitn.adk2018.delivery.action.GoTo_action;
import unitn.adk2018.delivery.action.PickupFood_action;
import unitn.adk2018.delivery.action.PrepareFood_action;
import unitn.adk2018.delivery.goal.PddlStepAskHelp_intention;
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

/**
 * Overall example showing the use of goals and messages provided with the framework.
 * It includes the use of blackbox planner and domain actions.
 * @author Marco
 *
 */
public abstract class DeliveryLauncher {
	
	public static void main(String[] args) throws InterruptedException {

		Logger.A_MAX = 5;
		Logger.GANTT = true;
		
		/*
		 * Setup domain
		 */
		PddlDomain pddlDomain = new PddlDomain("delivery-domain");
		Environment.setPddlDomain(pddlDomain);
		// Actions of the PDDL domain
		pddlDomain.addSupportedAction ("prepare-food", PrepareFood_action.class);
		pddlDomain.addSupportedAction ("go-to", GoTo_action.class);
		pddlDomain.addSupportedAction ("pickup-food", PickupFood_action.class);
		pddlDomain.addSupportedAction ("deliver-food", DeliverFood_action.class);
		
		
		
		/*
		 * Setup problem
		 */
		String pizza = "pizza";
		String b1 = "b1";
		String env = "env";
		String r1 = "r1";
		String r2 = "r2";
		String c1 = "c1";
		
		
		
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
		envAgent.getBeliefs().declareObject( b1 );
		envAgent.getBeliefs().declareObject( pizza );
		envAgent.getBeliefs().declareObject( c1 );
		// Setup 1
		envAgent.getBeliefs().declare( Delivery.sayDeliveryBoy(b1) );
		envAgent.getBeliefs().declare( Delivery.sayRestaurant(r1) );
		envAgent.getBeliefs().declare( Delivery.sayRestaurant(r2) );
		envAgent.getBeliefs().declare( Delivery.sayLocation(r2) );
		envAgent.getBeliefs().declare( Delivery.sayLocation(r1) );
		envAgent.getBeliefs().declare( Delivery.sayClient(c1));
		envAgent.getBeliefs().declare( Delivery.sayFree(b1) );
		envAgent.getBeliefs().declare( Delivery.sayAt(r2, b1) );
		envAgent.getBeliefs().declare( Delivery.sayNextTo(r1, r2));
		envAgent.getBeliefs().declare( Delivery.sayNextTo(r2, r1));
		envAgent.getBeliefs().declare( Delivery.sayNextTo(r1, c1));
		envAgent.getBeliefs().declare( Delivery.sayNextTo(c1, r1));
		envAgent.getBeliefs().undeclare( Delivery.sayReadyAtFood(r1, pizza) );
		//envAgent.getBeliefs().undeclare( Delivery.sayServed(c1, pizza, r1) );
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
		r1Agent.getBeliefs().declare( Delivery.sayMe(r1) );
		r1Agent.getBeliefs().declare( Delivery.sayNotMe(r2) );
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
		r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
		r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
		// Beliefs
		r2Agent.getBeliefs().declare( Delivery.sayMe(r2) );
		r2Agent.getBeliefs().declare( Delivery.sayNotMe(r1) );
		// Env
		Environment.addAgent (r2Agent);
		r2Agent.startInSeparateThread();
		
		/*
		 * Setup robot 3 agent boy
		 */
		Agent b1Agent = new General_agent(b1, true);
		b1Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
		// Messages
		b1Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
		b1Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
		// Goals
		b1Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
		b1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
		// Step goals
		b1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
		b1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
		// Beliefs
		b1Agent.getBeliefs().declare( Delivery.sayMe(b1) );
		// Env
		Environment.addAgent (b1Agent);
		b1Agent.startInSeparateThread();
		
		
		/*
		 * Setup robot 4 agent client
		 */
		Agent c1Agent = new General_agent(c1, true);
		c1Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
		// Messages
		c1Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
		c1Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
		// Goals
		c1Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
		c1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
		// Step goals
		c1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
		c1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
		// Beliefs
		c1Agent.getBeliefs().declare( Delivery.sayMe(c1) );
		// Env
		Environment.addAgent (c1Agent);
		c1Agent.startInSeparateThread();
		
		
		

		/*
		 * Wait 1000ms on simulation timer
		 */
		System.err.println("First test wait, 1000 msecs, at " + envAgent.getAgentTime());
		Sleep.sleep(1000);
		System.err.println("End of first test wait at " + envAgent.getAgentTime());
		
		
	
		
		
		/*
		 * Send message Goal_msg with goal PddlAction_goal with action Pickup_action
		 * This will fail because a is not hold!
		 
	
		PddlClause[] pddlGoal1 = { Delivery.sayServed(c1, pizza, r1) };
		Goal g1 = new ReachPddlGoal_goal( pddlGoal1 );
		Message msg1 = new Request_msg( "God", c1, g1 );
		Environment.sendMessage ( msg1 );
*/
		/*
		 * Wait 2000ms on simulation timer
		 */
		Sleep.sleep(2000);
		
		
		
		/*
		 * Send message Goal_msg to blocksRobot with goal ReachPddlGoal_goal with pddlGoal (holding a)
		 */
	//	PddlClause[] pddlGoal = { Blocksworld.sayBlockOn( block_a, block_b ) };
	//	Goal g1 = new ReachPddlGoal_goal( pddlGoal );
	//	Message msg1 = new Request_msg( "God", r1, g1 );
	//	Environment.sendMessage ( msg1 );

		
		
		
		
		
		
	}
	
}
