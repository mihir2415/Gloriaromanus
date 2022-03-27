package unsw.gloriaromanus;

import java.util.*;

public class Battle {

    private Random randomUnit = new Random();
    private int attack_strength;
    private int defend_strength;
    private int num_soldiers_attacking = 0;
    private int num_soldiers_defending = 0;
    private int tot_attack_attacking = 0;
    private int tot_attack_defending = 0;
    private int tot_defense_attacking = 0;
    private int tot_defense_defending = 0;
    private double chancewin_attack = 0;
    private String res;

    /**
     * 
     * @param attackingProvince the attacking army's provience 
     * @param defendingProvince the defending army's proveience
     * @param g the main game file 
     */

    public Battle(Province attackingProvince, Province defendingProvince, Game g) {
        // function to compare army and declare which one is victorious
        // Also randomly battles unit by unit
        //randomUnit = new Random();
        //while (check_status() != false) {  ///////////////// HD ///////////////////
            //choosing a random one from attacking army
            /*int index = randomUnit.nextInt(attackingArmy.size());
            Unit attacking_unit = attackingArmy.get(index);

            //choosing a random one for defending army
            int index_another = randomUnit.nextInt(defendingArmy.size());
            Unit defending_unit = defendingArmy.get(index_another);*/

            //Calculating the total number of soldiers, attack and defence for the attacking army
        List<Unit> attackingArmy = attackingProvince.getUnitList(); // getting the att_province list
        List<Unit> defendingArmy = defendingProvince.getUnitList(); // getting the def_province list
        if (!attackingArmy.isEmpty() && !defendingArmy.isEmpty()) {
            for (Unit u : attackingArmy) {
                num_soldiers_attacking = num_soldiers_attacking + u.getNumTroops();
                tot_attack_attacking = tot_attack_attacking + u.getAttack();
                tot_defense_attacking = tot_defense_attacking + ((u.getShieldDefense() + u.getDefenseSkill())/2);
            }
    
            //Calculating the total number of soldiers, attack and defence for the defending army
            for (Unit o : defendingArmy) {
                num_soldiers_defending = num_soldiers_defending + o.getNumTroops();
                tot_attack_defending = tot_attack_defending + o.getAttack();
                tot_defense_defending = tot_defense_defending + (o.getShieldDefense() + o.getDefenseSkill())/2;
            }
            attack_strength = Calc_strength(num_soldiers_attacking, tot_attack_attacking, tot_defense_attacking);
            defend_strength = Calc_strength(num_soldiers_defending, tot_defense_attacking, tot_defense_defending);
            chancewin_attack = Calc_winchance(attack_strength, defend_strength);
    
            int result = battle_result(chancewin_attack);
            
            if (result == 1) { //Case for defence win
                int perc_win = eliminate_proportion_win(defend_strength, attack_strength);
                int perc_lose = eliminate_proportion_lose(defend_strength, attack_strength);
                removeUnits(perc_win, perc_lose, defendingProvince, attackingProvince);
                res = "Defence";
                // g.getPlayers().setName(defendingProvince.getFaction());
                // g.getPlayers().setResult("won");
                // g.getPlayers().setName(attackingProvince.getFaction());
                // g.getPlayers().setResult("lost");
    
            } else {    // Case for Attack win
                int sec_perc_win = eliminate_proportion_win(attack_strength, defend_strength);
                int sec_perc_lose = eliminate_proportion_lose(attack_strength, defend_strength);
                removeUnits(sec_perc_win, sec_perc_lose, attackingProvince, defendingProvince);
                Faction winner = null;
                for (Faction f : g.getFactionList()) {
                    if (f.getName().equals(attackingProvince.getFaction())) {
                        f.addProvince(defendingProvince);
                        winner = f;
                    }
                }
                for (Faction f : g.getFactionList()) {
                    if (f.getName().equals(defendingProvince.getFaction())) {
                        f.removeProvince(defendingProvince);
                    }
                }
                defendingProvince.setFaction(winner.getName());
                List<Province> conquered = winner.getConquered();
                conquered.add(defendingProvince);
                winner.setConquered(conquered);
                // g.getPlayers().setName(attackingProvince.getFaction());
                // g.getPlayers().setResult("won");
                // g.getPlayers().setName(defendingProvince.getFaction());
                // g.getPlayers().setResult("lost");
                res = "Attack";
            }
        }
        if (attackingArmy.isEmpty() && !defendingArmy.isEmpty()) {
            this.res = "Defence";
        }
        if (!attackingArmy.isEmpty() && defendingArmy.isEmpty()) {
            this.res = "Attack";
        }
        if (attackingArmy.isEmpty() && defendingArmy.isEmpty()) {
            this.res = "null";
        }
    }

    /**
     * 
     * @param chancewin_attack the chance of the attacking army to win the attack
     * @return a flag is returned which helps in the determinaiton if the army won 
     * or lost 
     */

    public int battle_result(double chancewin_attack) {
        int flag = -1;
        int chance = randomUnit.nextInt(100);
        int chance_towin = (int) Math.round(chancewin_attack * 100);
        if (chance <= chance_towin) {
            flag = 0; //attack army won
        } else {
            flag = 1;   // defence army won
        }
        return flag;
    }

    /**
     * 
     * @param first_strength the strenght of the attacking army
     * @param sec_strength the strenght of the defending army 
     * @return a proportion/perctentage 
     */

    public int eliminate_proportion_win(int first_strength, int sec_strength) {
        double ran_proportion = (double) first_strength/(double)(first_strength + sec_strength);
        int fin_proportion = (int) Math.round(ran_proportion * 100); 
        int ranm = Math.abs(randomUnit.nextInt(100 - fin_proportion));
        int proportion = fin_proportion + ranm;
        return proportion;
    }

    /**
     * 
     * @param first_strength the strenght of the attacking army
     * @param sec_strength  the strencht of the defending army
     * @return a proportion/perctentage 
     */

    public int eliminate_proportion_lose(int first_strength, int sec_strength) {
        double ran_proportion = (double)sec_strength/(double)(first_strength + sec_strength);
        int fin_proportion = (int) Math.round(ran_proportion * 100); 
        int ranm = randomUnit.nextInt(fin_proportion);
        return ranm;
    }

    /**
     * 
     * @param prop_win the propotion of wining 
     * @param prop_lose the propotion of losing 
     * @param attackingProvince the attacking provience
     * @param defendingProvince the defending provience 
     *  it will remove the required units from the loosing army
     */

    public void removeUnits(int prop_win, int prop_lose, Province attackingProvince, Province defendingProvince) {
        // If army is destroyed.. remove from overall array(so some overall units array should also be passed)
        int Army_count = attackingProvince.countTroops(); // getting the att_province list
        int Army_sec_count = defendingProvince.countTroops();

        List<Unit> Army = attackingProvince.getUnitList(); // getting the att_province list
        List<Unit> Army_sec = defendingProvince.getUnitList();

        //System.out.println("this is it ..." + prop_win);
        double units_remove_winner = (double) (prop_win/100.0)* (double) Army_sec_count; //defending prov remove
        double units_remove_loser = (double) (prop_lose/100.0)* (double) Army_count; //attacking prov remove
        
        int fin_units_winner = (int) Math.round(units_remove_winner);
        int fin_units_loser = (int) Math.round(units_remove_loser);

        int chance;
        for (int i = 0; i < fin_units_winner; i++) {
            chance = randomUnit.nextInt(Army_sec.size());
            int num_of_troops = Army_sec.get(chance).getNumTroops();
            if (num_of_troops == 0) {
                i--;
                Army_sec.remove(chance);
            }
            else {
                Army_sec.get(chance).setNumTroops(num_of_troops - 1);
            }
        }
        for (int i = 0; i < fin_units_loser; i++) {
            chance = randomUnit.nextInt(Army.size());
            int num_of_troops = Army.get(chance).getNumTroops();
            if (num_of_troops == 0) {
                i--;
                Army.remove(chance);
            }
            else {
                Army.get(chance).setNumTroops(num_of_troops - 1);
            }
        }

        attackingProvince.setUnitList(Army);
        defendingProvince.setUnitList(Army_sec);
    }

    /*public void Adjust_ability(Unit attacking_unit, Unit defending_unit) {
        //implementation for high grades...
        
    }*/

    /**
     * This funciton will calculate the strenght and return it 
     * @param no_sol number of soldiers to calc strenght 
     * @param tot_attack total attack of unit
     * @param tot_defence total defence of unit 
     * @return an integer which will be the strenght 
     */

    public int Calc_strength(int no_sol, int tot_attack, int tot_defence) {
        return (no_sol * tot_attack * tot_defence);
    }

    /**
     * This function will calculate the chance of wining 
     * @param armystrength_first the strenght of one of the armies in the battle
     * @param armystrength_sec the strenght of the second army in the battle
     * @return an int which will give the chance of wining 
     */

    public double Calc_winchance(int armystrength_first, int armystrength_sec) {
        return (double)armystrength_first/(double) (armystrength_first + armystrength_sec);
    }

    /**
     * 
     * @return the result of the battle
     */
    public String get_res() {
        return res;
    }

    // public void defending_won() {
    //     System.out.println("defending won");
    //     System.out.println("attacking lost");
    // }

    /*public boolean check_status() {
        if (attackingArmy.isEmpty() && !(defendingArmy.isEmpty())) {
            defending_won();
            return false;
        }
        if (defendingArmy.isEmpty() && !(attackingArmy.isEmpty())) {
            attacking_won();
            return false;
        }
        if (defendingArmy.isEmpty() && attackingArmy.isEmpty()) {
            tie();
            return false;
        }
        return true;
    }*/


    // public void attacking_won() {
    //     System.out.println("attacking won");
    //     System.out.println("defending lost");
    // }

    /*public void tie() {
        System.out.println("Its a tie");
    }*/

    // public void fleeUnit() {
    //     // If the unit can flee... determine how many soldiers are remaining in the particular unit
    // }
}