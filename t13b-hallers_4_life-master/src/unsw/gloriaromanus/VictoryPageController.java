package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class VictoryPageController extends MenuController{
    
    @FXML
    void continueGame(ActionEvent e) {
        getParent().continueGame();
    }
}
