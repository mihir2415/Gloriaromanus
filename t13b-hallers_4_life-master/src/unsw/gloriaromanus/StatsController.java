package unsw.gloriaromanus;

import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;

import javax.swing.JProgressBar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class StatsController extends MenuController {

// private Faction f;

    // public StatsController() {
    //     this.f = getParent().getFaction();
    // //     //System.out.println("Hi");
    // } 

    public void setFactionName(Faction f) {
        faction_name.setText(f.getName());
    }

    public void setTotal_wealth(Faction f) {
        total_wealth.setText(Integer.toString(f.getTotal_wealth()));
    }

    public void settotoal_treasury(Faction f) {
        totoal_treasury.setText(Integer.toString(f.getTotal_treasury()));
    }
    
    // Call this function to set values
    public void setall(Faction f) {
        setFactionName(f);
        setTotal_wealth(f);
        settotoal_treasury(f);
        settingTG(f);
        settingWG(f);
        settingCG(f);
        displaygoal(f);
        //System.out.println(f.getConquered());
    }

    public void settingTG(Faction f) {
        int Tg = f.getTotal_treasury();
        Double t = (double) Tg;
        treasury_goal.setProgress(t/100000.0);
    }

    public void settingWG(Faction f) {
        int wealth = f.getTotal_treasury();
        Double w = (double) wealth;
        wealth_goal.setProgress(w/400000.0);
    }


    public void settingCG(Faction f) {
        int conq = f.getConquered().size();
        Double c = (double) conq;
        conquest_goal.setProgress(c/53.0);
    }

    public void displaygoal(Faction f) {
        faction_goal.setText(f.getGoal());
    }
    
    //Done
    @FXML
    private TextField faction_name;
    //Done
    @FXML 
    private ProgressBar treasury_goal;
    // Done
    @FXML
    private ProgressBar wealth_goal;
    // Done
    @FXML
    private ProgressBar conquest_goal;
    // Done
    @FXML
    private TextField totoal_treasury;
    // Done
    @FXML
    private TextField total_wealth;
   
    @FXML
    private TextArea faction_goal; 

}