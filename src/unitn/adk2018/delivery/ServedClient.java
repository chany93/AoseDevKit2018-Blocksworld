package unitn.adk2018.delivery;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.delivery.action.DeliverFood_action;
import unitn.adk2018.delivery.action.GoTo_action;
import unitn.adk2018.delivery.action.PickupFood_action;
import unitn.adk2018.delivery.action.PrepareFood_action;
import unitn.adk2018.delivery.goal.CalculateCost_goal;
import unitn.adk2018.delivery.goal.CalculateCost_intention;
import unitn.adk2018.delivery.goal.PddlStepAskHelp_intention;
import unitn.adk2018.delivery.goal.PddlStepDoItByMyself_intention;
import unitn.adk2018.delivery.message.RequestPddlPlanCost_intention;
import unitn.adk2018.delivery.message.RequestPddlPlanCost_msg;
import unitn.adk2018.event.Goal;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.agent.General_agent;
import unitn.adk2018.generic.goal.ExecutePddlPlanParallel_intention;
import unitn.adk2018.generic.goal.ExecutePddlPlan_goal;
import unitn.adk2018.generic.goal.ExecutePddlPlan_intention;
import unitn.adk2018.generic.goal.PddlStepWithPostCondition_intention;
import unitn.adk2018.generic.goal.PddlStep_goal;
import unitn.adk2018.generic.goal.PostmanEverythingInParallel_intention;
import unitn.adk2018.generic.goal.PostmanOneRequestAtTime_intention;
import unitn.adk2018.generic.goal.Postman_goal;
import unitn.adk2018.generic.goal.ReachPddlGoal_goal;
import unitn.adk2018.generic.goal.ReachPddlGoal_intention;
import unitn.adk2018.generic.message.ActionSuccessInform_intention;
import unitn.adk2018.generic.message.ActionSuccessInform_msg;
import unitn.adk2018.generic.message.Clause_intention;
import unitn.adk2018.generic.message.Clause_msg;
import unitn.adk2018.generic.message.PddlActionWithSuccessBroadcast_intention;
import unitn.adk2018.generic.message.PddlAction_intention;
import unitn.adk2018.generic.message.PddlAction_msg;
import unitn.adk2018.generic.message.Request_intention;
import unitn.adk2018.generic.message.Request_msg;
import unitn.adk2018.generic.message.Sensing_intention;
import unitn.adk2018.generic.message.Sensing_msg;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlDomain;
import unitn.adk2018.utils.Sleep;

public abstract class ServedClient {
	
	public static void main(String[] args) throws InterruptedException {
		
		Logger.A_MAX = 6;
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
		String sushi = "sushi";
		String b1 = "b1";
		String env = "env";
		String r1 = "r1";
		String r2 = "r2";
		String r3 = "r3";
		String c1 = "c1";
		String c2 = "c2";
		String broker = "broker";
		
		
		
		/*
		 * Setup environmentAgent
		 */
		
			Agent envAgent = new General_agent(env, true);
			// Goals
			envAgent.addSupportedEvent(Postman_goal.class, PostmanEverythingInParallel_intention.class);
			// Messages
			//envAgent.addSupportedEvent(PddlAction_msg.class, PddlAction_intention.class);
			envAgent.addSupportedEvent(PddlAction_msg.class, PddlActionWithSuccessBroadcast_intention.class);
			envAgent.addSupportedEvent(Sensing_msg.class, Sensing_intention.class);
			// Beliefs
			envAgent.getBeliefs().declareObject( r1 );
			envAgent.getBeliefs().declareObject( r2 );
			envAgent.getBeliefs().declareObject( b1 );
			envAgent.getBeliefs().declareObject( pizza );
			envAgent.getBeliefs().declareObject( c1 );
			// Setup 1
			envAgent.getBeliefs().declare( Delivery.sayDeliveryBoy(b1) );
			envAgent.getBeliefs().declare( Delivery.sayRestaurant(r1) );
			envAgent.getBeliefs().declare( Delivery.sayRestaurant(r2) );
			envAgent.getBeliefs().declare( Delivery.sayRestaurant(r3) );
			envAgent.getBeliefs().declare( Delivery.sayClient(c1));
			envAgent.getBeliefs().declare( Delivery.sayClient(c2));
			envAgent.getBeliefs().declare( Delivery.sayLocation(r2) );
			envAgent.getBeliefs().declare( Delivery.sayLocation(r1) );
			envAgent.getBeliefs().declare( Delivery.sayLocation(r3) );
			envAgent.getBeliefs().declare( Delivery.sayLocation(c1) );
			envAgent.getBeliefs().declare( Delivery.sayLocation(c2) );
			envAgent.getBeliefs().declare( Delivery.sayFood(pizza));	
			envAgent.getBeliefs().declare( Delivery.sayFood(sushi));	
			envAgent.getBeliefs().declare( Delivery.sayNextTo(r1, r2));
			envAgent.getBeliefs().declare( Delivery.sayNextTo(r2, r1));
			envAgent.getBeliefs().declare( Delivery.sayNextTo(c2, r2));
			envAgent.getBeliefs().declare( Delivery.sayNextTo(r2, c2));
			envAgent.getBeliefs().declare( Delivery.sayNextTo(r1, c1));
			envAgent.getBeliefs().declare( Delivery.sayNextTo(c1, r1));
			envAgent.getBeliefs().declare( Delivery.sayNextTo(r3, c1));
			envAgent.getBeliefs().declare( Delivery.sayNextTo(c1, r3));
			envAgent.getBeliefs().declare( Delivery.sayHasFood(r1, pizza));
			envAgent.getBeliefs().declare( Delivery.sayHasFood(r2, pizza));
			envAgent.getBeliefs().declare( Delivery.sayHasFood(r3, sushi));
			envAgent.getBeliefs().declare( Delivery.sayWaiting(r1) );
			envAgent.getBeliefs().declare( Delivery.sayWaiting(r2) );
			envAgent.getBeliefs().declare( Delivery.sayWaiting(r3) );
			envAgent.getBeliefs().declare( Delivery.sayFree(b1) );
			envAgent.getBeliefs().declare( Delivery.sayAt(r1, b1) );
			envAgent.getBeliefs().declare( Delivery.sayCost(r1, "2") );
			envAgent.getBeliefs().declare( Delivery.sayRating(r1, "1") );
			envAgent.getBeliefs().declare( Delivery.sayCost(r2, "2") );
			envAgent.getBeliefs().declare( Delivery.sayRating(r2, "3") );
			envAgent.getBeliefs().declare( Delivery.sayCost(r3, "1") );
			envAgent.getBeliefs().declare( Delivery.sayRating(r3, "2") );
			//envAgent.getBeliefs().declare( Delivery.sayReadyAtFood(r1, pizza) );
			//envAgent.getBeliefs().declare( Delivery.sayServed(c1, pizza, r1) );
			envAgent.getBeliefs().undeclare( Delivery.sayReadyAtFood(r1, pizza) );
			envAgent.getBeliefs().undeclare( Delivery.sayReadyAtFood(r2, pizza) );
			envAgent.getBeliefs().undeclare( Delivery.sayReadyAtFood(r3, sushi) );
			//envAgent.getBeliefs().undeclare( Delivery.sayServed(c1, pizza, r1) );
			// Env
			Environment.addAgent (envAgent);
			Environment.setEnvironmentAgent (envAgent);
			envAgent.startInSeparateThread();
			
	
		
		/*
		 * Setup robot restaurant 1
		 */
		
			Agent r1Agent = new General_agent(r1, true);
			r1Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			// Messages
			r1Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			r1Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
			r1Agent.addSupportedEvent(ActionSuccessInform_msg.class, ActionSuccessInform_intention.class);
			// Goals
			r1Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
			r1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
			//r1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlanParallel_intention.class);
			r1Agent.addSupportedEvent(CalculateCost_goal.class, CalculateCost_intention.class);
			// Step goals
			r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
			//r1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepWithPostCondition_intention.class);
			// Beliefs
			r1Agent.getBeliefs().declare( Delivery.sayMe(r1) );
			r1Agent.getBeliefs().declare( Delivery.sayNotMe(r2) );
			r1Agent.getBeliefs().declare( Delivery.sayNotMe(r3) );
			// Env
			Environment.addAgent (r1Agent);
			r1Agent.startInSeparateThread();
		
		
		/*
		 * Setup robot restaurant 2
		 */
		
			Agent r2Agent = new General_agent(r2, true);
			r2Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			// Messages
			r2Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			r2Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
			r2Agent.addSupportedEvent(ActionSuccessInform_msg.class, ActionSuccessInform_intention.class);
			// Goals
			r2Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
			r2Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
			//r2Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlanParallel_intention.class);
			r2Agent.addSupportedEvent(CalculateCost_goal.class, CalculateCost_intention.class);
			// Step goals
			r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
			// Beliefs
			r2Agent.getBeliefs().declare( Delivery.sayMe(r2) );
			r2Agent.getBeliefs().declare( Delivery.sayNotMe(r1) );
			r2Agent.getBeliefs().declare( Delivery.sayNotMe(r3) );
			// Env
			Environment.addAgent (r2Agent);
			r2Agent.startInSeparateThread();
			
		/*
		 * Setup robot restaurant 3 
		 */
			
			Agent r3Agent = new General_agent(r3, true);
			r3Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			// Messages
			r3Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			r3Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
			r3Agent.addSupportedEvent(ActionSuccessInform_msg.class, ActionSuccessInform_intention.class);
			// Goals
			r3Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
			r3Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
			//r3Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlanParallel_intention.class);
			r3Agent.addSupportedEvent(CalculateCost_goal.class, CalculateCost_intention.class);
			// Step goals
			r3Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			r3Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
			// Beliefs
			r3Agent.getBeliefs().declare( Delivery.sayMe(r3) );
			r3Agent.getBeliefs().declare( Delivery.sayNotMe(r1) );
			r3Agent.getBeliefs().declare( Delivery.sayNotMe(r2) );
			// Env
			Environment.addAgent (r3Agent);
			r3Agent.startInSeparateThread();
		
		
		/*
		 * Setup robot delivery boy 1
		 */
		
			Agent b1Agent = new General_agent(b1, true);
			b1Agent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			// Messages
			b1Agent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			b1Agent.addSupportedEvent(Request_msg.class, Request_intention.class);
			b1Agent.addSupportedEvent(ActionSuccessInform_msg.class, ActionSuccessInform_intention.class);
			// Goals
			b1Agent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
			b1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
			//b1Agent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlanParallel_intention.class);
			// Step goals
			b1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			b1Agent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
			// Beliefs
			b1Agent.getBeliefs().declare( Delivery.sayMe(b1) );
			// Env
			Environment.addAgent (b1Agent);
			b1Agent.startInSeparateThread();
			
		/*
		 * Setup robot broker
		 */
			
			Agent brokerAgent = new General_agent(broker, true);
			brokerAgent.addSupportedEvent(Postman_goal.class, PostmanOneRequestAtTime_intention.class);
			//brokerAgent.addSupportedEvent(Postman_goal.class, PostmanEverythingInParallel_intention.class);
			// Messages
			brokerAgent.addSupportedEvent(Clause_msg.class, Clause_intention.class);
			brokerAgent.addSupportedEvent(Request_msg.class, Request_intention.class);
			brokerAgent.addSupportedEvent(RequestPddlPlanCost_msg.class, RequestPddlPlanCost_intention.class);
			brokerAgent.addSupportedEvent(ActionSuccessInform_msg.class, ActionSuccessInform_intention.class);
			// Goals
			brokerAgent.addSupportedEvent(ReachPddlGoal_goal.class, ReachPddlGoal_intention.class);
			brokerAgent.addSupportedEvent(CalculateCost_goal.class, CalculateCost_intention.class);
			brokerAgent.addSupportedEvent(ExecutePddlPlan_goal.class, ExecutePddlPlan_intention.class);
			// Step goals
			brokerAgent.addSupportedEvent(PddlStep_goal.class, PddlStepAskHelp_intention.class);
			//brokerAgent.addSupportedEvent(PddlStep_goal.class, PddlStepDoItByMyself_intention.class);
			//r2Agent.addSupportedEvent(PddlStep_goal.class, PddlStepWithoutPostCondition_intention.class);
			// Beliefs
			brokerAgent.getBeliefs().declareObject( r1 );
			brokerAgent.getBeliefs().declareObject( r2 );
			brokerAgent.getBeliefs().declareObject( r3 );
			brokerAgent.getBeliefs().declareObject( b1 );
			brokerAgent.getBeliefs().declareObject( c1 );
			//brokerAgent.getBeliefs().declareObject( b2 );
			// Env
			Environment.addAgent (brokerAgent);
			brokerAgent.startInSeparateThread();
		
		

	
		
		Thread.sleep(200);
		
		/*
		 * Send message Sensing_msg to env simulating to be r1
		 */
		Environment.sendMessage ( new Sensing_msg( broker, env ) );
		
		
		
		Sleep.sleep(1000);
		
		
		
		System.err.println( "Beliefs of broker:  " + brokerAgent.getBeliefs().pddlClauses() );
		System.err.println( "Beliefs of env: " + envAgent.getBeliefs().pddlClauses() );
		
		
		/*
		 * Send message Goal_msg with goal PddlAction_goal with action Pickup_action
		 * This will fail because a is not hold!
		 */
		
		Environment.sendMessage ( new RequestPddlPlanCost_msg(env, broker, c1, pizza, "2", "1") );
		//RequestPddlPlanCost_msg req = new RequestPddlPlanCost_msg(env, broker, c1, pizza);
		
		
		Sleep.sleep(20000);
		
		System.err.println( "Beliefs of broker:  " + brokerAgent.getBeliefs().pddlClauses() );
		System.err.println( "Beliefs of env: " + envAgent.getBeliefs().pddlClauses() );
		 
		//envAgent.getBeliefs().undeclare(Delivery.sayAt(r1, b1));
		Environment.sendMessage ( new RequestPddlPlanCost_msg(env, broker, c2, sushi, "2", "2") );
		
	/*
		PddlClause[] pddlGoal1 = { Delivery.sayServed(c1, pizza) };
		Goal g1 = new ReachPddlGoal_goal( pddlGoal1 );
		Message msg1 = new Request_msg( "God", c1, g1 );
		Environment.sendMessage ( msg1 );
*/
		/*
		 * Wait 2000ms on simulation timer
		 */
		
		
		
		
		
		Sleep.sleep(500);
		
		
		
	
		
	}
	
}
