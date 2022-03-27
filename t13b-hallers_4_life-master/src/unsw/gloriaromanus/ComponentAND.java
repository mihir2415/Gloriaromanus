package unsw.gloriaromanus;
import java.util.ArrayList; 
import java.util.List; 

public class ComponentAND implements VictoryInterface {

    private List <VictoryInterface> goals = new ArrayList<>();
    
    @Override
    public Boolean completedGoal() {
        // Loop through list, if a false value 
        // is found is break and return false 
        Boolean allgoals = true;
        for (VictoryInterface g : goals) {
           if (!g.completedGoal()) { // false (not completed)
                allgoals = false;
                break;
            } 
        }
        return allgoals;
    }

    /**
	 *  Will add to the list of goals 
	 * @param s the requried victory to be added to the list 
	 */

	public void addgoal(VictoryInterface s) {
		goals.add(s);
	}

	/**
	 * Will remove the goal from the list of goals
	 * @param s the wanted victory is removed from the list 
	 */
	public void removegoal(VictoryInterface s) {
		goals.remove(s);
	}
}