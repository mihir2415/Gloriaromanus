package unsw.gloriaromanus;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class MainPlayer implements Player {
    Subject subject;
    private Faction f;
    String result;
    String name;


    /**
     * A constructor for the class 
     * @param g of type game 
     */
    public MainPlayer() {
        
    }
    
    // public MainPlayer(Game g) {
    //     List<String> factionList = g.getAvailFactionList();

    //     if(!factionList.contains("Rome")) {
    //         Random r = new Random();
    //         assert (factionList.size() > 0);
    //         int ran = r.nextInt(factionList.size());
    //         String faction = factionList.get(ran);
    //         for (Faction fac : g.getFactionList()) {
    //             if (fac.getName().equals(faction)) {
    //                 this.f = fac;
    //             }
    //         }
    //         assert (this.f != null);
    //         factionList.remove(ran);
    //         g.setAvailFactionList(factionList);
    //     }  
    //     else {
    //         for (Faction fac : g.getFactionList()) {
    //             if(fac.getName().equals("Rome")) {
    //                 this.f = fac;
    //                 factionList.remove("Rome");
    //                 g.setAvailFactionList(factionList);
    //             }
    //         }
    //     }

    //     List<MainPlayer> listPlayer = g.getPlayers();
    //     listPlayer.add(this);
    //     g.setPlayers(listPlayer);
    // }

    public MainPlayer(String faction,Game g) {
        List<String> factionList = g.getAvailFactionList();
        for (Faction f: g.getFactionList()) {
            if (f.getName().equals(faction)) {
                this.f = f;
            }
        }
        int index = factionList.indexOf(faction);
        factionList.remove(index);
        g.setAvailFactionList(factionList);
    }

    /**
     * This function updates all the required things in a turn
     * in the game factions list (aids in saving then)
     */

    @Override
    public void updateBattle(Players obj) {
        this.name = obj.getName();
        this.result = obj.getResult();
        display();
    }

    /**
     * This function will update the facitons 
     * that a turn has ended and do the required 
     * tasks 
     */
    
    @Override
    public void updateTurn(Endturn obj, Game t) {
        t.setYear(t.getYear() + 1);
        List<Unit> toRemove = new ArrayList<>();
        for (Faction h : t.getFactionList()) {
            if (h.getName().equals(f.getName())) {
                List<Province> conquered = h.getConquered();
                for (Province p : conquered) {

                    // Updating all the required things per
                    // turn in a provience
                    if (!p.getTraining().isEmpty()) {
                        for (Unit u : p.getTraining()) {
                            u.setRecruitTime(u.getRecruitTime() - 1);
                            if (u.getRecruitTime() == 0) {
                                p.addUnit(u);
                                toRemove.add(u);
                            }
                        }
                        for (Unit u: toRemove) {
                            p.removeTraining(u);
                        }
                    }
                    // Re-set the movment points of the Army unit 
                    if (! p.getUnitList().isEmpty()) {
                        for (Unit army: p.getUnitList()) {
                            if (army.getMovementPoints() == 0) {
                                army.setMovementPoints(army.getMovementPointStatic());
                            }
                        }
                    }
                }

                h.setConquered(conquered);
                // update faction per turn
                h.update_turn();
                Playergoals goalmet = new Playergoals(h);
                // Check if the player won 
                if (goalmet.getPlayerWon()){
                    System.out.println(h.getName() + "won the game");
                // check if the player lost game in turn
                } else if (h.getLost_game() == true) {
                    System.out.println(h.getName() + "lost the game");
                }
            }
        }
    }

  

    public void display() {
        System.out.printf("The faction %s has %s the battle", name, result);
    }

    public Faction getF() {
        return f;
    }

    public void setF(Faction f) {
        this.f = f;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}
