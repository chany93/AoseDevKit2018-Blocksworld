package unitn.adk2018.blocksworld;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.blocksworld.action.Pickup_action;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.agent.General_agent;
import unitn.adk2018.generic.goal.PostmanEverythingInParallel_intention;
import unitn.adk2018.generic.goal.Postman_goal;
import unitn.adk2018.generic.message.PddlAction_intention;
import unitn.adk2018.generic.message.PddlAction_msg;
import unitn.adk2018.pddl.PddlDomain;


/**
 * Use of PddlAction_msg
 * @author Marco
 *
 */
public abstract class Example3 {
	
	public static void main(String[] args) throws InterruptedException {

		Logger.A_MAX = 1;
		Logger.I_MAX = 3;
		Logger.GANTT = true;

		/*
		 * Setup domain
		 */
		PddlDomain pddlDomain = new PddlDomain("blocks-domain"); // this must match the name of the domain file!!!
		Environment.setPddlDomain(pddlDomain);
		// Actions of the PDDL domain
		// Provide java implementations of actions of the domain
		pddlDomain.addSupportedAction ("pickup", Pickup_action.class); 
		
		/*
		 * Setup agent1
		 */
		Agent agent1 = new General_agent("agent1", true);
		agent1.addSupportedEvent(Postman_goal.class, PostmanEverythingInParallel_intention.class);
		agent1.addSupportedEvent(PddlAction_msg.class, PddlAction_intention.class);
		// Manually setup initial beliefs of the agent
		agent1.getBeliefs().declare( Blocksworld.sayFree("agent1") );
		agent1.getBeliefs().declare( Blocksworld.sayBlockOnTable("a") );
		agent1.getBeliefs().declare( Blocksworld.sayBlockClear("a") );
		Environment.addAgent (agent1);
		Environment.setEnvironmentAgent (agent1);
		agent1.startInSeparateThread();

		/*
		 * Check beliefs at beginning
		 */
		System.err.println( "Initial beliefs: " + agent1.getBeliefs().pddlClauses() );
		
		Thread.sleep(200);
		
		/*
		 * Send PddlAction_msg to agent1 (pickup agent1 a)
		 */
		String[] actionArgs = { "agent1", "a" };
		Message msg = new PddlAction_msg( "God", "agent1", "pickup", actionArgs );
		Environment.sendMessage ( msg );	

		/*
		 * Check beliefs immediately
		 */
		System.err.println( "Beliefs right after msg sent: " + agent1.getBeliefs().pddlClauses() );

		/*
		 * Check beliefs when msg will be handled
		 */
		msg.wasHandled().addObserver( new Observer () {
			public void update(Observable o, Object arg) {
				System.err.println( "Beliefs once msg has been handled: " + agent1.getBeliefs().pddlClauses() );
			}
		});
		
	}
	
}
