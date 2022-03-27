package unsw.gloriaromanus;

public class treasuryVictory implements VictoryInterface {
    private Faction player; 
    
    /**
     * A constructor for the class
     * @param player the player aka the user 
     */
    public treasuryVictory (Faction player) {
        this.player = player;
    }

    /**
     * If the goal is acheived return true
     */
    @Override
	public Boolean completedGoal() {
        Boolean goalcompleted = false;
        if (player.getTreasury_goal()) {
            goalcompleted = true;
        }
        return goalcompleted;
	}
}
