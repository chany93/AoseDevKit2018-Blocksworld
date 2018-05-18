package unitn.adk2018.blocksworld;

import java.util.Observable;
import java.util.Observer;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.event.Message;
import unitn.adk2018.generic.agent.General_agent;
import unitn.adk2018.generic.goal.PostmanEverythingInParallel_intention;
import unitn.adk2018.generic.goal.Postman_goal;
import unitn.adk2018.generic.message.Clause_intention;
import unitn.adk2018.generic.message.Clause_msg;

/**
 * Use of Clause_msg
 * @author Marco
 *
 */
public abstract class Example2 {
	
	public static void main(String[] args) throws InterruptedException {
		
		Logger.A_MAX = 1;
		Logger.I_MAX = 3;
		Logger.GANTT = true;
		
		/*
		 * Setup agent1
		 */
		Agent agent1 = new General_agent("agent1", true);
		agent1.addSupportedEvent(Postman_goal.class, PostmanEverythingInParallel_intention.class);
		agent1.addSupportedEvent(Clause_msg.class, Clause_intention.class);
		Environment.addAgent (agent1);
		Environment.setEnvironmentAgent (agent1);
		agent1.startInSeparateThread();
		
		/*
		 * Check beliefs at beginning
		 */
		System.err.println( "Initial beliefs: " + agent1.getBeliefs().pddlClauses() );
		
		Thread.sleep(200);
		
		/*
		 * Send clause_msg to agent1 (on-table a)
		 */
		Message msg = new Clause_msg( "God", "agent1", Blocksworld.sayBlockOn( "a", "b" ) );
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
				System.err.println(msg.status.get());
			}
		});
		
	}
	
}
