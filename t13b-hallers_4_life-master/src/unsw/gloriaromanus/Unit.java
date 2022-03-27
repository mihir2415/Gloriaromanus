package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import javax.swing.text.html.StyleSheet.BoxPainter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a basic unit of soldiers
 * 
 * incomplete - should have heavy infantry, skirmishers, spearmen, lancers,
 * heavy cavalry, elephants, chariots, archers, slingers, horse-archers,
 * onagers, ballista, etc... higher classes include ranged infantry, cavalry,
 * infantry, artillery
 * 
 * current version represents a heavy infantry unit (almost no range, decent
 * armour and morale)
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit {
	private int numTroops; // the number of troops in this unit (should reduce based on depletion)
	private String type; // type of the unit
	private String name;
	private int movementPoints;
	private int armour; // armour defense
	private double morale; // resistance to fleeing
	private double speed; // ability to disengage from disadvantageous battle
	private int attack; // can be either missile or melee attack to simplify. Could improve
	private int charge; // implementation by differentiating!
	private int defenseSkill; // skill to defend in battle. Does not protect from arrows!
	private int shieldDefense; // a shield
	private Boolean isRanged; // whether a unit is a ranged unit or not.
	private int recruitTime; // the number of turns taken to recruit a unit of troop
	private String specialAbility; // the special ability of the certain unit
	private Boolean infiniteMorale;
	private int recruitCost;
	private int movementPointStatic; // a static variable

	public Unit() {

	}

	/**
	 * Constructor for the class
	 * 
	 * @param name the name of the unit wanted
	 */

	public Unit(String name) {
		try {
			String content = Files.readString(Paths.get("src/unsw/gloriaromanus/Unit.json"));
			JSONObject unitList = new JSONObject(content);

			Iterator<String> keys = unitList.keys();

			while (keys.hasNext()) {
				String key = keys.next();
				if (unitList.get(key) instanceof JSONObject && name.equals(key)) {
					JSONObject unitObject = (JSONObject) unitList.get(key);
					parseUnitObject(unitObject);
				}
			}
		}
		catch(IOException e) {
			System.out.println("Could not open Unit.json file\n");
		}
	}

	/**
	 * Parse the JSON file
	 * 
	 * @param unitObject JSONobject requried to change
	 */

	private void parseUnitObject(JSONObject unitObject) {

		this.name = (String) unitObject.get("name");
		this.type = (String) unitObject.get("type");
		this.armour = (int) unitObject.get("armour");
		this.morale = (double) unitObject.get("morale");
		this.charge = (int) unitObject.get("charge");
		this.attack = (int) unitObject.get("attack");
		this.defenseSkill = (int) unitObject.get("defenseSkill");
		this.shieldDefense = (int) unitObject.get("shieldDefense");
		this.recruitTime = (int) unitObject.get("recruitTime");
		this.isRanged = (Boolean) unitObject.get("isRanged");
		this.numTroops = (int) unitObject.get("numTroops");
		this.specialAbility = (String) unitObject.get("specialAbility");
		this.speed = (double) unitObject.get("speed");
		this.infiniteMorale = false;
		this.recruitCost = (int) unitObject.get("recruitCost");

		switch (this.type) {
			case "Artillery":
				this.movementPoints = 4;
				this.movementPointStatic = 4;
				break;
			case "Cavalry":
				this.movementPoints = 15;
				this.movementPointStatic = 15;
				break;
			case "Infantry":
				this.movementPoints = 10;
				this.movementPointStatic = 10;
				break;
			case "Ranged-Infantry":
				this.movementPoints = 10;
				this.movementPointStatic = 10;
				break;
		}
	}

	public int getNumTroops() {
		return numTroops;
	}

	public void setNumTroops(int numTroops) {
		this.numTroops = numTroops;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getArmour() {
		return armour;
	}

	public void setArmour(int armour) {
		this.armour = armour;
	}

	public double getMorale() {
		return morale;
	}

	public void setMorale(double morale) {
		this.morale = morale;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefenseSkill() {
		return defenseSkill;
	}

	public void setDefenseSkill(int defenseSkill) {
		this.defenseSkill = defenseSkill;
	}

	public int getShieldDefense() {
		return shieldDefense;
	}

	public void setShieldDefense(int shieldDefense) {
		this.shieldDefense = shieldDefense;
	}

	public Boolean getIsRanged() {
		return isRanged;
	}

	public void setIsRanged(Boolean isRanged) {
		this.isRanged = isRanged;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public int getRecruitTime() {
		return recruitTime;
	}

	public void setRecruitTime(int recruitTime) {
		this.recruitTime = recruitTime;
	}

	public int getMovementPoints() {
		return movementPoints;
	}

	public void setMovementPoints(int movementPoints) {
		this.movementPoints = movementPoints;
	}

	public String getSpecialAbility() {
		return specialAbility;
	}

	public void setSpecialAbility(String specialAbility) {
		this.specialAbility = specialAbility;
	}

	public Boolean getInfiniteMorale() {
		return infiniteMorale;
	}

	public void setInfiniteMorale(Boolean infiniteMorale) {
		this.infiniteMorale = infiniteMorale;
	}

	public int getRecruitCost() {
		return recruitCost;
	}

	public void setRecruitCost(int recruitCost) {
		this.recruitCost = recruitCost;
	}

	public int getMovementPointStatic() {
		return movementPointStatic;
	}

	public void setMovementPointStatic(int movementPointStatic) {
		this.movementPointStatic = movementPointStatic;
	}

	@Override
	public String toString() {
		return "Unit [Number of Troops=" + numTroops + ", Type=" + type + ", Name=" + name +
		", Movement Points=" + movementPoints + ", Armour=" + armour + ", Morale=" + morale + 
		", Speed=" + speed + ", Attack=" + attack + ", Charge=" + charge + ", Defense Skill=" + defenseSkill +
		", Sheild Defense=" + shieldDefense + ", Ranged=" +isRanged + ", Recruit Time=" + recruitTime +
		", Special Ability=" + specialAbility + ", Infinite Morale=" + infiniteMorale + ", Recuritment Cost=" +
		recruitCost + ", Static Movement Points=" + movementPointStatic + "]";
	}


}
