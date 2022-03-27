package unsw.gloriaromanus;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class select_playersController extends MenuController {
    @FXML
    private Button three_players;

    @FXML
    private Button two_Players;

    @FXML
    public void clickedThreePlayers(ActionEvent e) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        getParent().threePlayerGame();
    }
    
    @FXML
    public void clickedTwoPlayers(ActionEvent e) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        getParent().twoPlayerGame();
    }
}
