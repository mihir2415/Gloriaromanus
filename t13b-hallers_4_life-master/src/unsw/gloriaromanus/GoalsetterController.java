package unsw.gloriaromanus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GoalsetterController extends MenuController {

    @FXML
    private Button button_setter;

    @FXML 
    private TextArea goal_input;

    @FXML
    private TextField return_succes;
    
    

    @FXML
    public void clickedgoal(ActionEvent e) {
        String goalstring = goal_input.getText(); 
        //JSONObject goal = new JSONObject(goalstring);
        Faction player = getParent().getFaction();   
        System.out.println(player.getGoal());
        try {
            String line = Files.readString(Paths.get("src/unsw/gloriaromanus/Goals.json"));
            JSONArray goalList = new JSONArray(line);
            int j = 0;
            for (int i = 0; i < goalList.length(); i++) {
                JSONObject checkGoal = goalList.getJSONObject(i);
                String checkg = checkGoal.toString();
                if (checkg.equals(goalstring) ){
                    player.changegoal(goalstring);
                    return_succes.setText("Goal Has been set");
                    j = 1;
                    break;
                }
            }
            if (j == 0) {
                return_succes.setText("Invalid Goal, Please enter again");
            }
            getParent().updateStats(player);
        } catch (IOException ee) {
           	System.out.println("Could not find the file ");

        }
        
    } 

    @FXML 
    public void clickedInvasionMenuButton(ActionEvent e) throws IOException {
        getParent().switchToInvasionMenu(e, "goal");
    }


}
