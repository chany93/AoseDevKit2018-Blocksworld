package unitn.adk2018.delivery.goal;

import unitn.adk2018.event.Goal;
import unitn.adk2018.event.RequestMessage;
import unitn.adk2018.pddl.PddlClause;
import unitn.adk2018.pddl.PddlPlan;


public class CalculateCost_goal extends Goal {
	
	public final PddlClause[] pddlGoal;
	public Integer minSteps;
	public PddlPlan bestPlan;
	
	
	public CalculateCost_goal (PddlClause[] _pddlGoal, Integer _minSteps, PddlPlan _bestPlan) {
		// TODO Auto-generated constructor stub
		pddlGoal = _pddlGoal;
		minSteps = _minSteps;
		bestPlan = _bestPlan;
	}

	
	@Override
	public String toString() {
		String pddlGoalStr = super.toString() + " ( and "; 
		for(PddlClause c : pddlGoal) {
			pddlGoalStr += " (" + c + ")";
		}
		pddlGoalStr += " )";
		return pddlGoalStr;
	}
	
	public Integer getMinSteps() {
		return minSteps;
		
	}
	
	public PddlPlan getBestPlan() {
		return bestPlan;
		
	}
}
