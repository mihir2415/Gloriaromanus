package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;

public class Players implements Subject {
    
    List<Player> players = new ArrayList<Player>();
    private String result;
    private String name;

    /**
     * This funciton basically adds a player to the 
     * players list 
     */
    public Players() {}

    @Override
    public void add_player(Player p) {
        if (! players.contains(p)) {
            players.add(p);
        }
    
    }

    /**
     * Removes a player from the players 
     * list
     */

    @Override
    public void remove_player(Player p) {
        players.remove(p);
    }

    /**
     * This function will notify all 
     *  players in the list
     */

    @Override
    public void notifyPlayersPl() {
        for (Player pl : players) {
            pl.updateBattle(this);
        }
    }

    @Override
    public void notifyPlayersGame(Game g) {
    }
    
    public void setResult(String result) {
        this.result = result;
        notifyPlayersPl();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}
    
    
}
