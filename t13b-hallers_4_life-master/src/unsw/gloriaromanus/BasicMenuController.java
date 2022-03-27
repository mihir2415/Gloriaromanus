package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BasicMenuController extends MenuController{
    @FXML
    private TextField selected_province;
    @FXML 
    private TextField treasury_avail;
    @FXML
    private Button trainElephants;
    @FXML
    private Button trainMelee;
    @FXML
    private Button  trainDruid;
    @FXML
    private Button  trainHorse;
    @FXML
    private Button  trainPikemen;
    @FXML
    private Button  trainMeleeCavalry;
    @FXML
    private Button  trainLegionary;
    @FXML
    private Button  trainGermanic;
    @FXML
    private Button  trainCeltic;
    @FXML   
    private Button  trainGallic;
    @FXML   
    private Button trainJavelin;
    @FXML
    private TextArea resultBox;

    @FXML 
    private Button displayButton;


    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void setProvince(String p) {
        selected_province.setText(p);
    }

    public void setGold(String p) {
        treasury_avail.setText(p);
    }

    public void setResult(String p) {
        resultBox.appendText(p);
    }
    public void addResult(String p) {
        resultBox.setText(p);
    }

    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createElephants(ActionEvent e) {
        clickedTrainButton(e, "Elephants");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createMelee(ActionEvent e) {
        clickedTrainButton(e, "Melee");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createDruid(ActionEvent e) {
        clickedTrainButton(e, "Druid");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createHorse(ActionEvent e) {
        clickedTrainButton(e, "Horse-Archer");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createPikemen(ActionEvent e) {
        clickedTrainButton(e, "Pikemen");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createMeleeCavalry(ActionEvent e) {
        clickedTrainButton(e, "Melee Cavalry");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createLegionary(ActionEvent e) {
        clickedTrainButton(e, "Roman legionary");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createGermanic(ActionEvent e) {
        clickedTrainButton(e, "Germanic berserker");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createCeltic(ActionEvent e) {
        clickedTrainButton(e, "Celtic Briton berserker");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createGallic(ActionEvent e) {
        clickedTrainButton(e, "Gallic berserker");
    }
    /**
     * 
     * @param e Action event
     */
    @FXML
    public void createJavelin(ActionEvent e) {
        clickedTrainButton(e, "Javelin Skirmishers");
    }

    /**
     * 
     * @param e
     * @param unit
     */
    public void clickedTrainButton(ActionEvent e, String unit) {
        int result = getParent().clickedTrainButton(e, unit);

        switch (result) {
            case -1:
                setResult("Could not find province!\n");
                break;
            case 0:
                setResult("Created  " + unit + " unit and added to training list!\n");
                Music m = new Music("src/unsw/gloriaromanus/TrainingAudio.wav");
                m.play();
                break;
            case 1:
                setResult("Unit can not be created as there is not enough gold in the province!\n"); 
                break;
            case 2: 
                setResult("Unit can not be created as there are no available training spots left in the unit!\n");
                break;
        }
    }

    /**
     * 
     * @param e Action event
     */
    @FXML
    public void clickedInavsionMenuButton (ActionEvent e) throws IOException {
        getParent().switchToInvasionMenu(e, "training");
    }

    /**
     * 
     * @param e Action event
     */
    @FXML
    public void clickedUnitMenuButton (ActionEvent e) throws IOException {
        getParent().switchToUnitMenu(e, "training");
    }

    /**
     * 
     * @param e Action event
     */
    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }

}
