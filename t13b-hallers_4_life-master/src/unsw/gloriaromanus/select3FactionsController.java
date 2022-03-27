package unsw.gloriaromanus;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class select3FactionsController extends MenuController {
    private int count = 0;

    @FXML
    private ComboBox<String> player1ComboBox;

    @FXML
    private ComboBox<String> player2ComboBox;

    @FXML 
    private ComboBox<String> player3ComboBox;

    void addToPlayer2(String p) {
        player2ComboBox.getItems().add(p);
    }

    void addToPlayer1(String p) {
        player1ComboBox.getItems().add(p);
    }

    void addToPlayer3(String p) {
        player3ComboBox.getItems().add(p);
    }

    @FXML
    public void clickPlayer3() {
        List<String> factionList = getParent().getFactionList();
        for (String s : factionList) {
            addToPlayer3(s);
        }
    }

    @FXML
    public void clickPlayer1() {
        List<String> factionList = getParent().getFactionList();
        for (String s : factionList) {
            addToPlayer1(s);
        }
    }

    @FXML
    public void clickPlayer2() {
        List<String> factionList = getParent().getFactionList();
        for (String s : factionList) {
            addToPlayer2(s);
        }
    }

    public void clearComboBox() {
        player1ComboBox.getItems().clear();
        player2ComboBox.getItems().clear();
        player3ComboBox.getItems().clear();
    }

    @FXML
    public void clickedAddPlayer(ActionEvent e) throws JsonParseException, JsonMappingException, IOException, InterruptedException {

        if (player1ComboBox.getValue() != null) {
            getParent().addPlayer(player1ComboBox.getValue());
            count++;
            clearComboBox();
        }
        if (player2ComboBox.getValue() != null) {
            getParent().addPlayer(player2ComboBox.getValue());
            count++;
            clearComboBox();
        }

        if (player3ComboBox.getValue() != null) {
            getParent().addPlayer(player3ComboBox.getValue());
            count++;
            clearComboBox();
        }


        if(count == 3) {
            getParent().newGame(e);
        }
    }


}
