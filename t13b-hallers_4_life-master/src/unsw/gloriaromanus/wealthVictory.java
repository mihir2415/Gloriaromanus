package unsw.gloriaromanus;

public class wealthVictory implements VictoryInterface {

    private Faction player;

    /**
     * A constructor for the class
     * @param player the player aka the user 
     */
    public wealthVictory(Faction player) {
        this.player = player;
    }
    
    /**
     * Check if the wealth goal is acheived 
     * if yes return 2
     */
	@Override
	public Boolean completedGoal() {
        Boolean goalcompleted = false;
        if (player.getWealth_goal()) {
            goalcompleted = true;
        }
        return goalcompleted;
    }
}

