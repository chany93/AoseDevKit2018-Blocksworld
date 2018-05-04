# AoseDevKit2018-Blocksworld

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

## Quick start guide

In order to develop a pddl / multi-agent system for a specific domain it is needed to:

1. Create the pddl domain file in the root folder (see blocks-domain.pddl)
2. Eventually create a class with shortcut methods to act on your pddl domain (see Blocksworld.java)
3. Implement a Java version of all the actions available in your domain (see Pickup_action.java)
4. Eventually extend PddlStep_intention to provide implementations for specific actions of the domain (see PddlStepDoItByMyself_intention)

### Implementation of a Pddl action in Java

Give a look at the methods in PddlClause and PddlWorld to interact with pddl agents beliefs.
You can declare, undeclare, or check whether a clause is declared in a given world.
A clause is composed by a predicate and zero, one, or more arguments.

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