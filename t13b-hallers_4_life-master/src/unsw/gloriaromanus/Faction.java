package unsw.gloriaromanus;

import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Faction {
	private String name;
	private List<Province> conquered;
	private int total_wealth;
	private int total_treasury;
	private String goal;
	private Boolean treasury_goal;
	private Boolean conquest_goal;
	private Boolean wealth_goal;
	private Boolean lost_game;

	/**
	 * Did the faction loose every provience if it has conqured
	 */

	public void checkPlayerlost() {
		if (this.conquered.isEmpty()) {
			this.lost_game = true;
		}
	}

	/**
	 * A constructor for the class
	 * 
	 * @param name one wants to assign the faction
	 * @param g    the game which the faction is a part of
	 */

	public Faction(String name, Game g) {
		this.total_treasury = 0;
		this.total_wealth = 1000;
		this.name = name;
		this.treasury_goal = false;
		this.conquest_goal = false;
		this.wealth_goal = false;
		this.lost_game = false;
		this.conquered = new ArrayList<Province>();
		setGoal();
	}

	public Faction () {

	}

	public int findTotal_Treasury() {
		int tt = 0;
		for (Province p : this.conquered) {
			tt += p.getTreasury();
		}
		return tt;
	}

	public int findTotal_Wealth() {
		int tw = 0;
		for (Province p : this.conquered) {
			tw += p.getTownWealth();
		}
		return tw;
	}

	// Loop through the list of proviences and calc total weath
	public String getGoal() {
		return this.goal;
	}

	/**
	 * This function will set a random goal to the faction
	 */

	public void setGoal() {
		try {
			Random rand = new Random();
			String line = Files.readString(Paths.get("src/unsw/gloriaromanus/Goals.json"));
			JSONArray goalList = new JSONArray(line);
			// This will give me an random object from the array list of goals
			// which are there and set it for the goals of the faciton
			JSONObject goal = goalList.getJSONObject(rand.nextInt(goalList.length()));
			 this.goal = goal.toString();
		} catch (IOException e) {
			System.out.println("Could not d find file ");
		}

	}

	/**
	 * Is used in turns to changes stats in factions
	 */

	public void update_turn() {
		// Each turn incr treasurry and wealth
		calcallFinancials();
		// Check if the player lost
		checkPlayerlost();
	}

	/**
	 * This function will loop through the list of proviences and update
	 */

	public void calcallFinancials() {
		int wealth = 0;
		int treasury = 0;

		for (Province p : this.conquered) {
			p.newturn();
			wealth += p.getTownWealth();

			treasury += p.getTreasury();

		}
		this.setTotal_treasury(this.getTotal_treasury() + treasury);
		this.setTotal_wealth(this.getTotal_wealth() + wealth);

		if (wealth >= 400000) {
			this.wealth_goal = true;
		}
		if (treasury >= 100000) {
			this.treasury_goal = true;
		}
	}

	/**
	 * This will check if the conqoured goal is acheieved
	 */

	public void checkConqGoal() {
		if (this.getConquered().size() == 53) {
			this.conquest_goal = true;
		}
	}

	public String getName() {
		return name;
	}

	public List<Province> getConquered() {
		return conquered;
	}

	public void setConquered(List<Province> conquered) {
		this.conquered = conquered;
	}

	public int getWealth() {

		return findTotal_Wealth();
	}

	public void setWealth(int total_wealth) {
		this.total_wealth = total_wealth;
	}

	public int getTreasury() {
		return total_treasury;
	}

	public void setTreasury(int total_treasury) {
		this.total_treasury = total_treasury;
	}

	public Boolean getTreasury_goal() {
		return treasury_goal;
	}

	public void setTreasury_goal(Boolean treasury_goal) {
		this.treasury_goal = treasury_goal;
	}

	public Boolean getConquest_goal() {
		return conquest_goal;
	}

	public void setConquest_goal(Boolean conquest_goal) {
		this.conquest_goal = conquest_goal;
	}

	public Boolean getWealth_goal() {
		return wealth_goal;
	}

	public void setWealth_goal(Boolean wealth_goal) {
		this.wealth_goal = wealth_goal;
	}

	public void addProvince(Province p) {
		this.conquered.add(p);
	}

	public void removeProvince(Province p) {
		this.conquered.remove(p);
	}

	public Province getProvince(String name) {
		for (Province c : conquered) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public int getTotal_wealth() {
		return findTotal_Wealth();
	}

	public void setTotal_wealth(int total_wealth) {
		this.total_wealth = total_wealth;
	}

	public int getTotal_treasury() {
		return findTotal_Treasury();
	}

	public void setTotal_treasury(int total_treasury) {
		this.total_treasury = total_treasury;
	}

	public Boolean getLost_game() {
		return lost_game;
	}

	public void setLost_game(Boolean lost_game) {
		this.lost_game = lost_game;
	}

	@Override
	public String toString() {
		
		return "Faction [Name=" + name + ", Conquered Provinces=" + conquered + ", Total Wealth=" + total_wealth +
		 ", Total Treasury=" + total_treasury + ", Treasury Goal=" + treasury_goal + ",Conquest Goal=" + conquest_goal + ", Wealth Goal=" + wealth_goal + 
		 ", Lost Game=" + lost_game + "]";

	}

	public void setName(String name) {
		this.name = name;
	}

	// public void setGoal(JSONObject goal) {
	// 	this.goal = goal;
	// }

	public void changegoal(String goal){
		this.goal = goal;
	}
	
}
