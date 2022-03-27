package unsw.gloriaromanus;

public interface Subject {
    
    public void add_player(Player p);
	public void remove_player(Player p);
    public void notifyPlayersGame(Game g);
    public void notifyPlayersPl();
    
}
