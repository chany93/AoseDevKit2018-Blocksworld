# AoseDevKit2018

The AoseDevKit2018 framework has been implemented for the course of Agent Oriented Software Engineering at the University Of Trento (2018).
Introductory slides on the framework are available in the doc folder in the AoseDevKit2018-MultiAgentSystem repository.
The framework includes three repositories. In the case of bugs, let us know or fix them and do a pull request. Start exploring by looking at readme files of the projects in this order:
- *AoseDevKit2018-Blocksworld: https://github.com/marcorobol/AoseDevKit2018-Blocksworld*
- *AoseDevKit2018-Generic: https://github.com/marcorobol/AoseDevKit2018-Generic*
- *AoseDevKit2018-MultiAgentSystem: https://github.com/marcorobol/AoseDevKit2018-MultiAgentSystem*

# AoseDevKit2018-Blocksworld

This project is an implementation of the blocksworld domain with the AoseDevKit2018 framework.
It contains Blocksworld domain-specific code: a Pddl domain file, java version of pddl actions, and a launcher script.

## Installing and running

Prerequisites: Git + Eclipse + JDK 1.8

This repository depends on *AoseDevKit2018-Generic* and *AoseDevKit2018-MultiAgentSystem*
so be sure to import all of them into your Eclipse workspace.
To do so follow these steps:

1. Fork and clone all the 3 repositories:
    > $ git clone xxx
2. Import the projects in Eclipse:
    > File -> Import -> Existing Projects into Workspace
3. **[Only for Linux/Mac users]** Give execution permission to blackbox executable in 2018-AoseDevKit-Blocksworld/blackbox:
    > $ chmod +x blackbox
4. **[Only if you have multiple versions of Java]** In the case Eclipse detects errors in the projects
    - in the Eclipse preferences set default compiler to Java 1.8
    - or right click on each of them and select:
      > Properties -> Java Compiler -> Enable project specific setting -> Compiler 1.8
  or 
5. To run right click on file unitn.adk2018.blocksworld.BlocksworldLauncher and select:
    > Run as -> Java Application

## Quick start guide for the development of a different domain

Follow these steps to develop a pddl / multi-agent system for a specific domain:

1. Create the pddl domain file in the root folder (see blocks-domain.pddl)
2. Possibly create a class with shortcut methods to act on your pddl domain (see Blocksworld.java)
3. Implement a Java version of all the actions available in your domain (see Pickup_action.java)
4. Eventually extend PddlStep_intention to provide implementations for specific actions of the domain (see PddlStepDoItByMyself_intention.java)
5. Implement a main launcher script where agents are configured, started, and then request messages are sent to them (see BlocksworldLauncher0.java)

### Implementation of a Pddl action in Java

Give a look at the methods in PddlClause and PddlWorld to interact with pddl agents beliefs.
You can declare, undeclare, or check whether a clause is declared in a given world.
A clause is composed by a predicate and zero, one, or more arguments.

An excerpt from the source code about the pickup action in the blocksworld domain follows:

```pddl
// blocks-domain.pddl
;; domain file: blocksworld-domain-complete.pddl
(define (domain blocks-domain)
	(:requirements :strips)
	(:predicates    (clear ?x)
              (on-table ?x)
              (holding ?x ?y)
              (on ?x ?y)
              (free ?x)
	)
	(:action pickup
		:parameters (?hand ?ob)
		:precondition (and (clear ?ob) (on-table ?ob) (free ?hand))
		:effect (and (not (clear ?ob))
                              (not (on-table ?ob))
                              (holding ?hand ?ob)
                              (not (free ?hand))
      )
  )
```

```java
// Pickup_action.java
public class Pickup_action extends PddlAction2Args {
	
	@Override
	public boolean checkPreconditions(PddlWorld world, String hand, String ob) {
		return PddlClause.say( "clear", ob ).isDeclaredIn( world )
			&& PddlClause.say( "on-table", ob ).isDeclaredIn( world )
			&& PddlClause.say( "free", hand ).isDeclaredIn( world );
	}
	
	@Override
	public boolean effects(PddlWorld world, String hand, String ob) {
		world.undeclare( PddlClause.say( "clear", ob ) );
		world.undeclare( PddlClause.say( "on-table", ob ) );
		world.declare( PddlClause.say( "holding", hand, ob ) );
		world.undeclare( PddlClause.say( "free", hand ) );
		return true;
	}
	
}
```

### Implementation of a launcher

A launcher is used to set-up the simulation and eventually control it.
Components to be configured are:

1. the pddl domain
	```java
	PddlDomain pddlDomain = new PddlDomain("whatever-domain");
	Environment.setPddlDomain(pddlDomain);
	pddlDomain.addSupportedAction ("whateverActionName", WhateverAction.class);
	```
2. variables for objects to be used in the specific problem
	```java
	String block_a = "a";
	```
3. environment agent and other agents
	- agent type
		```java
		Agent envAgent = new General_agent (env, true);
		```
	- supported goals and messages and available intentions to handle each of them
		```java
		envAgent.addSupportedEvent (Postman_goal.class, PostmanEverythingInParallel_intention.class);
		```
	- beliefset (the environment agent beliefset represents the world)
		```java
		envAgent.getBeliefs().declareObject ( block_a );
		envAgent.getBeliefs().declare ( Blocksworld.sayBlockOnTable(block_a) );
		```
4. register agents to the Environment and start their thread
	```java
	Environment.addAgent (envAgent);
	Environment.setEnvironmentAgent (envAgent);
	envAgent.startInSeparateThread ();
	```

The simulation is now configured and from the script it is possible to control it by interacting with the agents.
This can be done by:

- sending messages to environment agent or to other ones
	```java
	String[] pickupArgs = {r1, block_b, block_a};
	Message msg0 = new PddlAction_msg( "God", env, "unstack", pickupArgs );
	Environment.sendMessage ( msg0 );
	```
- forces changes on the environment agent beliefset (to be avoided, it is preferred to send PddlClause messages handled internally by the env agent itself)
	```java
	envAgent.getBeliefs().declareObject( block_a );
	envAgent.getBeliefs().declare( Blocksworld.sayBlockOnTable(block_a) );
	```
- wait for a specific amount of time according to simulation time
	```java
	System.err.println("First test wait, 2400 msecs, at " + envAgent.getAgentTime());
	Observer w = new Observer {
		@Override
		public void  update (Observable o, Object arg) {
			synchronized (this) {
				notifyAll();
			}
		}
	};
	envAgent.rescheduleTimer(w,  2400);
	synchronized (w) {
		w.wait();
	}
	System.err.println("End of first test wait at " + envAgent.getAgentTime());
	```





