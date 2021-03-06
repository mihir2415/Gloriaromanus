package unsw.gloriaromanus;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class MenuController {
    private GloriaRomanusController parent;


    public void setParent(GloriaRomanusController parent) {
        if (parent == null){
            System.out.println("GOT NULL");
        }
        this.parent = parent;
    }

    public GloriaRomanusController getParent(){
        return parent;
    }
    
    @FXML 
    public void clickedDisplay() {
        getParent().clickedDisplayStats();
    }

    @FXML 
    public void releasedDisplay() {
        getParent().releasedDisplayStats();
    }

    @FXML
    public void clickedEndGame() {
        getParent().terminate();
        
        System.exit(0);
    }

    // @FXML
    // public void clickedSwitchMenu(ActionEvent e) throws Exception {
    //     parent.switchMenu();
    // }
}
