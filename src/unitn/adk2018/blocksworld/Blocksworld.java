package unitn.adk2018.blocksworld;

import unitn.adk2018.pddl.PddlClause;

public class Blocksworld {
	
	public static PddlClause sayBlockOnTable ( String blockname ) {
		return PddlClause.say("on-table", blockname);
	}
	
	public static PddlClause sayBlockClear ( String blockName ) {
		return PddlClause.say("clear", blockName);
	}
	
	public static PddlClause sayBlockOn ( String above, String under ) {
		return PddlClause.say("on", above, under);
	}
	
	public static PddlClause sayFree ( String hand ) {
		return PddlClause.say("free", hand);
	}
	
	public static PddlClause sayHolding ( String hand, String blockName ) {
		return PddlClause.say("holding", hand, blockName);
	}
	
}
