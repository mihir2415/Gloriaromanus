package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class StartMenuController extends MenuController {
    
     @FXML 
     private Button new_game;
     @FXML 
     private Button load_game;

     @FXML
     private TextArea result_Area;
     // https://stackoverflow.com/a/30171444
     @FXML
     private URL location; // has to be called location

     void setText(String p) {
          result_Area.setText(p);
     }

     @FXML
     public void clickedNewButton(ActionEvent e ) throws IOException,InterruptedException {
          getParent().getNumberPlayers();
     }

     @FXML
     public void clickedLoadButton(ActionEvent e ) throws IOException,InterruptedException {
          Boolean result = getParent().loadGame(e);
          if (result) {
               result_Area.setText("Successfully created loaded a  game!");
               getParent().newGame(e);
          }
          else {
               result_Area.setText("Failed to load a Game!");
          }

     }

//      @FXML
//      public void clickedInvadeButton(ActionEvent e) throws IOException {
//         getParent().clickedInvadeButton(e);
//     }
}
