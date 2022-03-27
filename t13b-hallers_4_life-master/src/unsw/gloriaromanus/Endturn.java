package unsw.gloriaromanus;


import java.util.ArrayList;

public class Endturn implements Subject {
    private int turn;
    private ArrayList<Player> players;
    
    public Endturn(){
        //endturn(g);
        this.turn = 0;
        this.players = new ArrayList<Player>();
    }
	@Override
    public void add_player(Player p) {
        if (!players.contains(p)) {
            players.add(p);
        }
        
    }

    @Override
    public void remove_player(Player p) {
        players.remove(p);
    }

    @Override
    public void notifyPlayersGame(Game g) {
        players.get(turn).updateTurn(this, g);
    }

    @Override
    public void notifyPlayersPl() {
        
    }

    /**
     * 
     * @param g Game 
     */
    public void endturn(Game g) {
        if (players.size() == 1) {
            this.turn = 0;
        }
        else if (this.turn == (players.size())) {
            this.turn = 0;
        }
        notifyPlayersGame(g);
        this.turn += 1;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public ArrayList<Player> getPlArray() {
        return players;
    }
	public int getTurn() {
		return turn;
	}
}


/// Make a int for loop, increment it each tiome 