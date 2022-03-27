package unsw.gloriaromanus;

public class conquestVictory implements VictoryInterface {  
    private Faction player;

    /**
     * 
     * @param player of faction type aka the user
     */

    public conquestVictory(Faction player) {
        this.player = player;
    }
    
    /**
     * Will return an int value depending if the goal is
     * completed or not
     */

    @Override
	public Boolean completedGoal() {
        Boolean goalcompleted = false;
		if (player.getConquest_goal()) {
            goalcompleted = true;
        }
        return goalcompleted;
	}
}
