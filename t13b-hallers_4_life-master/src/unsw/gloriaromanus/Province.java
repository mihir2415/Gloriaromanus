package unsw.gloriaromanus;

import java.util.List;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Province {
	private String name; // name of the faciton
	private String faction; // faciton this provience belongs to
	private int townWealth; // town wealth which will change per turn
	private int treasury; // treasury is the gold you spend on troops
	private List<Unit> unitList; // List of units avaibile
	private String levelTax; // level of tax, user input as string
	private int trainingSpots; // training spots avalible
	private List<Unit> training; // units which are in training
	// private Game g;
	// private int taxPerc; // percentage of tax

	/**
	 * A constructor for the Class
	 * 
	 * @param faction   the name of the faciotn which the provience belongs to
	 * @param name      the name of the provience
	 * @param taxwanted the desered taxlevel
	 * @param g         the game
	 */

	public Province(String faction, String name, String taxwanted, Game g) {
		this.faction = faction;
		this.name = name;
		this.treasury = 1000;
		this.townWealth = 1000;
		this.levelTax = taxwanted;
		this.trainingSpots = 2;
		// this.g = g;
		// this.taxPerc = 15;
		unitList = new ArrayList<Unit>();
		training = new ArrayList<Unit>();
	}
	public Province() {

	}

	/**
	 * Function which will increase the town wealth each turn depending on the taxes
	 * we will increse the town-wealth
	 */

	public void increaseTownWeatlhandTreasury() {
		// depending on Tax level
		switch (this.getLevelTax()) {
			case "Low tax":
				increaseTreasury(10);
				this.townWealth += 10;
				break;

			case "Normal tax":
				// no increase will happen
				increaseTreasury(15);
				break;

			case "High tax":
				increaseTreasury(20);
				this.townWealth -= 10;
				if (this.townWealth < 0) {
					// minimum townwealth can be 0
					this.townWealth = 0;
				}
				break;
			case "Very high tax":
				increaseTreasury(25);
				this.townWealth -= 30;
				if (this.townWealth < 0) {
					// min townwealth is 0
					this.townWealth = 0;
				}
				decreaseMorale();
				// add function which will decrease the moral of all units in the
				break;
		}
	}

	Province getProvince(String name, List<Province> provinceList) {
		for (Province p : provinceList) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * This function will decrease the moral of all troops by
	 */

	public void decreaseMorale() {
		for (Unit i : this.unitList) {
			double morale = i.getMorale() - 1.0;
			if (morale < 0) {
				morale = 0;
			}
			i.setMorale(morale);
		}
	}

	/**
	 * This function will increase the treasury of the provience depending on tax
	 * per turn
	 * 
	 * @param apply the int value of the tax to be applied
	 */

	public void increaseTreasury(int apply) {
		double m = (double) apply;
		double perc = m / 100;
		double rate = perc;
		double f = (double) this.townWealth;
		double taxRevenue = f * rate;
		int sss = (int) taxRevenue;
		this.treasury += sss;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFaction() {
		return faction;
	}

	public void setFaction(String faction) {
		this.faction = faction;
	}

	public int getTreasury() {
		return this.treasury;
	}

	public void setTreasury(int treasury) {
		this.treasury = treasury;
	}

	public List<Unit> getUnitList() {
		return unitList;
	}

	public void setUnitList(List<Unit> unitList) {
		this.unitList = unitList;
	}

	public String getLevelTax() {
		return levelTax;
	}

	public void setLevelTax(String levelTax) {
		this.levelTax = levelTax;
	}

	public void addUnit(Unit u) {
		unitList.add(u);
	}

	public void removeUnit(Unit u) {
		unitList.remove(u);
	}

	public int getTownWealth() {
		return this.townWealth;
	}

	public void setTownWealth(int townWealth) {
		this.townWealth = townWealth;
	}

	/**
	 * This a unit into the training lists
	 * 
	 * @param u which is requested to train
	 */

	public void addTraining(Unit u) {
		if (this.trainingSpots > 0) {
			this.training.add(u);
			this.unitList.add(u);
			this.trainingSpots-= 1;
		}
		else {
			System.out.println("Could not be added");
		}
	}

	/**
	 * Will remove a unit from training
	 * 
	 * @param u the unit which is to be removed
	 */

	public void removeTraining(Unit u) {
		if (training.contains(u)) {
			training.remove(u);
			this.trainingSpots += 1;
		}
	}

	/**
	 * This function will move a unit from point A to pont b
	 * 
	 * @param un          the unit which is wanted to move
	 * @param destination the destination of the movment
	 * @param f           the faction of the provience
	 * @param g           the game
	 * @return an int value of the path
	 */

	public int moveUnit(Unit un, String destination, Faction f, Game g) {
		Path p = new Path(name, destination, g);
		int path = p.bfs(f);
		if (path != -1) {
			for (Province prov : f.getConquered()) {
				if (prov.getName().equals(destination) && un.getMovementPoints()- 4 * path >= 0) {
					unitList.remove(un);
					un.setMovementPoints(un.getMovementPoints()-4 * path);
					prov.unitList.add(un);
					return path;
				}
			}
		} else {
			System.out.println("Path not possible");
		}

		return -1;
	}

	/**
	 * This funciton will create a unit
	 * 
	 * @param name this function will create a unit
	 */

	public int creatUnit(String name) {
		Unit u = new Unit(name);
		if (trainingSpots > 0) {
			if (u.getRecruitCost() > treasury) {
				return 1;
			}
			else {
				training.add(u);
				this.trainingSpots--;
				this.treasury -= u.getRecruitCost();
				return 0;
			}
		}
		else {
			return 2;
		}
	}

	/**
	 * This funciton will invade with the army
	 * 
	 * @param unitList         the units which are trained in the provience
	 * @param province     the provience
	 * @param provinceList the list of proviences
	 * @param g            the game
	 * @return a boolean value regarding the output
	 */

	public Battle invadeArmy(Province prov,Game g) {
		//Path p = new Path(name,prov.getName(),g);
		//if(p.isProvinceAdj() && !(prov.getFaction().equals(this.faction))) {
		Battle b = new Battle(this,prov,g);
		return b;
		//}
		//return null;
	}

	public void newturn() {
		increaseTownWeatlhandTreasury();
	}

	public int getTrainingSpots() {
		return trainingSpots;
	}

	public void setTrainingSpots(int trainingSpots) {
		this.trainingSpots = trainingSpots;
	}

	public List<Unit> getTraining() {
		return training;
	}

	public void setTraining(List<Unit> training) {
		this.training = training;
	}


	@Override
	public String toString() {
		
		return "Province [Name=" + name + ", Faction=" + faction + ", Town Wealth=" +townWealth +
		", Treasury=" + treasury + ", Units Trained=" + unitList + ", Tax Level=" + levelTax +
		", Training Spots=" + trainingSpots + ", Units training=" + training + '\n' + "]";
	}

	public int countTroops() {
		int count = 0;
		for (Unit u : unitList) {
			count += u.getNumTroops();
		}
		return count;
	}

}
