package unsw.gloriaromanus;
import java.util.ArrayList; 
import java.util.List; 

public class ComponentOR implements VictoryInterface {

    private List <VictoryInterface> goals = new ArrayList<>();
    private int value = 1;
    @Override
    public Boolean completedGoal() {
        Boolean anygoal = false;
        for (VictoryInterface g : goals) {
            if (g.completedGoal()) {
                // if anyone goal is compleated return true 
                anygoal = true;
                break;
            }
        }
        return anygoal;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
