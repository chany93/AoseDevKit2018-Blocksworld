package unitn.adk2018.blocksworld;

import unitn.adk2018.Agent;
import unitn.adk2018.Environment;
import unitn.adk2018.Logger;
import unitn.adk2018.generic.agent.General_agent;
import unitn.adk2018.generic.goal.PostmanEverythingInParallel_intention;
import unitn.adk2018.generic.goal.Postman_goal;

/**
 * Very basic example of an agent only capable to deal with Postman_goal
 * @author Marco
 *
 */
public abstract class Example1 {
	
	public static void main(String[] args) throws InterruptedException {
		
		Logger.A_MAX = 1;
		Logger.I_MAX = 3;
		Logger.GANTT = true;
		
		Agent agent1 = new General_agent("agent1", true);
		
		// Always provide an intention to hanlde the Postman_goal which is automatically pushed to the agent 
		agent1.addSupportedEvent(Postman_goal.class, PostmanEverythingInParallel_intention.class);
		
		// For every agent, provide the environment with a reference of it !!!
		Environment.addAgent (agent1);
		
		// Always provide one environment agent !!!
		Environment.setEnvironmentAgent (agent1);
		
		// Always manually start the agent !!!
		agent1.startInSeparateThread();
		
	}
	
}
