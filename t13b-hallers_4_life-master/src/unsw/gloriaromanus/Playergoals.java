package unsw.gloriaromanus;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;


public class Playergoals {
    private String combinaiton1;
    private String goal1;
    private String combination2;
    private String goal2;
    private String goal3;
    private String combinaiton;
    private String goal;
    private Boolean playerWon = false;

    /**
     * Player's goals
     */
    public Playergoals(Faction player) {
        
        String goalstring = player.getGoal();
        JSONObject goal = new JSONObject(goalstring);
        Boolean check = false;
        if ( goal.has("Combination1") ) {
            this.combinaiton1 = goal.getString("Combination1");
            this.goal1 = goal.getString("goal1");
            this.combination2 = goal.getString("Combination2");
            this.goal2 = goal.getString("goal2");
            this.goal3 = goal.getString("goal3");
            // make the function which adds to pattern
            check = combinaiton3(this.combinaiton1, this.goal1, this.combination2, this.goal2, this.goal3, player);
            if (check) {
                this.playerWon = true;
             }
        }
        else if (goal.has("Combination")) {
            this.combinaiton = goal.getString("Combination");
            this.goal1 = goal.getString("goal1");
            this.goal2 = goal.getString("goal2");
            // make the function which adds to pattern
            check = combination1(this.combinaiton, this.goal1, this.goal2, player);
            if (check) {
             this.playerWon = true;
          }
        }
        else if (goal.has("goal")) {
            this.goal = goal.getString("goal");
            check = oneGoal(this.goal, player); 
            if (check) {
                this.playerWon = true;
            }
        }
   }

   /**
    * Various combinations
    */
   public Boolean combination1(String combinaition, String goal1, String goal2, Faction player) {
        Boolean goalcompleted;
        if (combinaition.equals("OR")) {
            ComponentOR or = new ComponentOR();
            VictoryInterface goaltwo = leafCreate(goal2, player);
            VictoryInterface goalone = leafCreate(goal1, player);
            or.addgoal(goalone);
            or.addgoal(goaltwo);
            goalcompleted = or.completedGoal();
        } else {
            ComponentAND and = new ComponentAND();
            VictoryInterface goaltwo = leafCreate(goal2, player);
            VictoryInterface goalone = leafCreate(goal1, player);
            and.addgoal(goalone);
            and.addgoal(goaltwo);
            goalcompleted = and.completedGoal();
        }
        return goalcompleted;
   }


   /**
    * Various combinations
    */
   public Boolean oneGoal (String goal, Faction player) {
    Boolean done = false;
    if (goal.equals("TREASURY")) {
        if (player.getTreasury_goal()) {
            return done = true;
        }
    }
    else if (goal.equals("CONQUEST")) {
        if (player.getConquest_goal()) {
            return done = true;
        }
    }
    else if (goal.equals("WEALTH")) {
        if (player.getWealth_goal()) {
            return done = true;
        }
    }
    return done;
    }
    /**
    * Various combinations
    */
   public Boolean combinaiton3(String combination1, String goal1, String combination2, String goal2, String goal3, Faction player ){
       Boolean goalcompleated;
        // process first combination 
        if (combination1.equals("OR")) {
            ComponentOR or1 = new ComponentOR();
            // adding the first leaf goal 
            VictoryInterface goalone = leafCreate(goal1, player);
            or1.addgoal(goalone);
             // Now time to process second combinaiton 

            if (combination2.equals("OR")) {
                ComponentOR or2 = new ComponentOR();
                // added 2nd component of or to the first one
                or1.addgoal(or2);
                VictoryInterface goaltwo = leafCreate(goal2, player);
                VictoryInterface goalthree = leafCreate(goal3, player);
                or2.addgoal(goalthree);
                or2.addgoal(goaltwo);
                
            } else {
                ComponentAND and2 = new ComponentAND();
                // added 2nd component of and to first or 
                or1.addgoal(and2);
                VictoryInterface goaltwo = leafCreate(goal2, player);
                VictoryInterface goalthree = leafCreate(goal3, player);
                and2.addgoal(goalthree);
                and2.addgoal(goaltwo);
                
            }
            goalcompleated = or1.completedGoal();
        }
           
          else  {
            ComponentAND and1 = new ComponentAND();
            VictoryInterface goalone = leafCreate(goal1, player);
            and1.addgoal(goalone); 

            // Now time to process second combination 
            if (combination2.equals("OR")) {
                ComponentOR or2 = new ComponentOR();
                // added 2nd component of or to the first one
                and1.addgoal(or2);
                VictoryInterface goaltwo = leafCreate(goal2, player);
                VictoryInterface goalthree = leafCreate(goal3, player);
                or2.addgoal(goalthree);
                or2.addgoal(goaltwo);
            } else {
                ComponentAND and2 = new ComponentAND();
                // added 2nd component of and to first or 
                and1.addgoal(and2);
                VictoryInterface goaltwo = leafCreate(goal2, player);
                VictoryInterface goalthree = leafCreate(goal3, player);
                and2.addgoal(goalthree);
                and2.addgoal(goaltwo);
            }
            goalcompleated = and1.completedGoal();
        }

       return goalcompleated;
  }


    /**
     * Creats leaf node
     */
   public VictoryInterface leafCreate(String goal, Faction player) {
       if (goal.equals("WEALTH")) {
           wealthVictory wealth = new wealthVictory(player);
           return wealth;
       } else if (goal.equals("CONQUEST")) {
           conquestVictory conquest = new conquestVictory(player);
           return conquest;
       }
       else {
           treasuryVictory treasury = new treasuryVictory(player);
           return treasury;
       }
   }

   public Boolean getPlayerWon() {
       return playerWon;
   }

   public void setPlayerWon(Boolean playerWon) {
       this.playerWon = playerWon;
   }
   
}
