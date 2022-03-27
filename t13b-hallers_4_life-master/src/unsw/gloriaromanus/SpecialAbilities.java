package unsw.gloriaromanus;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class SpecialAbilities {
    private List<Unit> attackingArmy;
    private List<Unit> defendingArmy;
    private Unit attackingUnit;
    private Unit defendingUnit;

    /**
     * A constructor for the class 
     * @param attackingArmy list of attacking army units
     * @param defendingArmy list of defanding army units
     */
    public SpecialAbilities(List<Unit> attackingArmy,List<Unit> defendingArmy) {
        this.attackingArmy = attackingArmy;
        this.defendingArmy = defendingArmy;
        this.attackingUnit = attackingUnit;
        this.defendingUnit = defendingUnit;
        applySpecialAttack();
        applySpecialDefend();
    }

    /**
     * Apply the attack accourding to the name 
     * of the unit 
     */

    void applySpecialAttack() {                                     //Check again for all units as they have not been implemented the correct way

        switch(attackingUnit.getName()) {
            case "Elephants":
                elephants(attackingUnit,attackingArmy);
                break;
            case "Javelin Skirmishers":                             //Changes have to be made when implementing for hd case as it inflicts damage only on the unit that it is fighting and not all.
                javelin(defendingUnit);
                break;
            case "Gallic berserker":
                berserker(attackingUnit);
                break;
            case "Celtic Briton berserker":
                berserker(attackingUnit);
                break;
            case "Germanic berserker":
                berserker(attackingUnit);
                break;
            case "Roman legionary":
                legionary(attackingArmy);
                break;
            case "Melee Cavalry":
                meleeCavalry(attackingUnit, attackingArmy, defendingArmy);
                break;
            case "Pikemen":
                pikemen(attackingUnit);
                break;
            case "Horse-Archer":
                horseArcher(defendingUnit);
                break;
            case "Druid":
                druid(attackingArmy,defendingArmy);
                break;
            case "Melee":
                melee(attackingUnit);
                break;
        }
    }

    /**
     * Apply the special defend for the specifc unit 
     * depending on the unit name 
     */

    void applySpecialDefend() {                                     //Check again for all units as they have not been implemented the correct way

        switch(attackingUnit.getName()) {
            case "Elephants":
                elephants(defendingUnit,defendingArmy);
                break;
            case "Javelin Skirmishers":                             //Changes have to be made when implementing for hd case as it inflicts damage only on the unit that it is fighting and not all.
                javelin(attackingUnit);
                break;
            case "Gallic berserker":
                berserker(defendingUnit);
                break;
            case "Celtic Briton berserker":
                berserker(defendingUnit);
                break;
            case "Germanic berserker":
                berserker(defendingUnit);
                break;
            case "Roman legionary":
                legionary(defendingArmy);
                break;
            case "Melee Cavalry":
                meleeCavalry(defendingUnit, defendingArmy, attackingArmy);
                break;
            case "Pikemen":
                pikemen(defendingUnit);
                break;
            case "Horse-Archer":
                horseArcher(attackingUnit);
                break;
            case "Druid":
                druid(defendingArmy,attackingArmy);
                break;
            case "Melee":
                melee(defendingUnit);
                break;
        }
    }
    ////////************Ideally this should be for every 4th engagement so change this up */

    /**
     * If a melee engagement make changes accourdingly 
     * @param attackingUnit 
     */

    void melee(Unit attackingUnit) {
        int shieldDefense = attackingUnit.getShieldDefense();
        int attack = attackingUnit.getAttack();
        attackingUnit.setAttack(attack + shieldDefense);
    }

    /**
     * Druid special ability
     * @param attackingArmy the list of atacking army 
     * @param defendingArmy this list of defending army 
     */

    void druid(List<Unit>attackingArmy,List<Unit>defendingArmy) {
        int numTroops = attackingUnit.getNumTroops();
        if (numTroops <= 5) {
            for (Unit a : attackingArmy) {
                adjustMorale(a.getMorale()* 1.1* numTroops, attackingArmy);
            }
            for (Unit a : defendingArmy) {
                adjustMorale(a.getMorale()* (-0.95)* numTroops, attackingArmy);
            }
        }
        else {
            for (Unit a : attackingArmy) {
                adjustMorale(a.getMorale()* 1.5, attackingArmy);
            }
            for (Unit a : defendingArmy) {
                adjustMorale(a.getMorale()* (-0.75), attackingArmy);
            }
        }
    }

    /**
     * @param defendingUnit the unit of defnending unit  
     */
    void horseArcher(Unit defendingUnit) {
        if (defendingUnit.getIsRanged()) {
            int attack = defendingUnit.getAttack();
            attack /= 2;
            defendingUnit.setAttack(attack);
        }
    }

    /**
     * @param u the unit for the pikeman 
     */

    void pikemen(Unit u) {
        int defense = (u.getDefenseSkill() + u .getShieldDefense())/2;
        u.setDefenseSkill(defense * 2);
        u.setShieldDefense(defense * 2);
        double speed = u.getSpeed();
        speed /= 2.0;
        u.setSpeed(speed);
    }

    /**
     * Changes stats accourding to meele Calvary
     * @param attackingUnit the attacking unit 
     * @param attackingArmy list of attakcking army
     * @param defendingArmy list of defending army 
     */

    void meleeCavalry(Unit attackingUnit, List<Unit> attackingArmy, List<Unit> defendingArmy){
        if(attackingArmy.size() < defendingArmy.size()/2) {
            adjustunitMorale(1.5 * attackingUnit.getMorale(), attackingUnit);
            int charge = attackingUnit.getCharge();
            charge *= 2.0;
            attackingUnit.setCharge(charge);
        }
    }

    /**
     * Adjust accourding to the type
     * @param army list of armies 
     */

    void legionary(List<Unit> army) {
        adjustMorale(1.0, army);           //0.2 lost for every unit lost implement with battle
    }

    /**
     * Apply changes to stats accourding to power 
     * @param u stats to apply to 
     */

    void berserker(Unit u) {
        u.setInfiniteMorale(true);
        u.setMorale(10.0);
        u.setShieldDefense(0);
    }

    /**
     * Apply changes to stats 
     * @param defendingUnit the unit stats need to be updated to
     */

    void javelin(Unit defendingUnit) {
        int shieldDefense = defendingUnit.getShieldDefense();
        shieldDefense /= 2;
        defendingUnit.setShieldDefense(shieldDefense);
    }

    /**
     * Adjust the morale of the unit 
     * @param change how much the change is by
     * @param u the unit which stats change 
     */
    void adjustunitMorale(double change, Unit u) {
        if(!u.getInfiniteMorale()) {
                double newMorale = u.getMorale() + change;
            if (newMorale >= 0.0 && newMorale <= 10.0) {
                u.setMorale(newMorale);
            }
            else if (newMorale < 0) {
                u.setMorale(0.0);
            }
            else if(newMorale > 10.0){
                u.setMorale(10.0);
            }
        }
            

    }

    /**
     * Adjust moral 
     * @param change change stat by 
     * @param army list of units 
     */

    void adjustMorale(double change, List<Unit> army) {
        for (Unit u: army) {
            if(!u.getInfiniteMorale()) {
                    double newMorale = u.getMorale() + change;
                if (newMorale >= 0.0 && newMorale <= 10.0) {
                    u.setMorale(newMorale);
                }
                else if (newMorale < 0) {
                    u.setMorale(0.0);
                }
                else if(newMorale > 10.0){
                    u.setMorale(10.0);
                }
            }
            
        }
    }

    /**
     * @param unit apply change in stat 
     * @param army list of units 
     */

    void elephants(Unit unit, List<Unit>army) {
        Random rand = new Random();
        int chance = rand.nextInt(100);
        if (chance <= 10) {
            Unit u = army.get(rand.nextInt(army.size()));
            int defense = (u.getDefenseSkill() + u.getShieldDefense())/2;
            int attack = (unit.getAttack() + unit.getCharge())/2;
            defense -= attack;

            if (defense < 0) {
                army.remove(u);
            }
            else {
                u.setDefenseSkill(defense);
                u.setShieldDefense(defense);
            }
        }
    }
}