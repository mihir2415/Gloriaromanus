package unsw.gloriaromanus;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UnitMenuController extends MenuController {
    @FXML
    private Button TrainingMenuButton;

    @FXML
    private Button InvasionMenuButton;

    @FXML
    private MenuButton setTaxButton;

    @FXML
    private ComboBox<String> unitComboBox;

    @FXML
    private TextField SelectedProvince;

    @FXML
    private MenuButton unitsMenuButton;

    @FXML
    private MenuButton provMenuButton;

    @FXML
    private ComboBox<String> provComboBox;

    @FXML
    private Button moveUnitButton;

    @FXML
    private TextArea resultBox;

    void setResultBox(String p) {
        resultBox.setText(p);
    }

    void appendResultBox(String p) {
        resultBox.appendText(p);
    }

    void setTextToField(String p) {
        SelectedProvince.setText(p);
    }

    void setListUnitCombo() {
        List<Unit> unitList = getParent().getListUnit();
        for (Unit u : unitList) {
            unitComboBox.getItems().add(u.getName());
        }
    }

    void setListProvinceCombo() {
        List<Province> provinceList = getParent().getListProvince();
        for (Province p : provinceList) {
            provComboBox.getItems().add(p.getName());
        }
    }

    void clearComboBox() {
        provComboBox.getItems().clear();
        unitComboBox.getItems().clear();
    }

    @FXML
    void clickedmoveUnitButton() throws JsonParseException, JsonMappingException, IOException {
        Province fromProvince = getParent().getSelectedProvince();
        String toProv = provComboBox.getValue();
        String u = unitComboBox.getValue();
        Province toProvince = getParent().getProvince(toProv);
        if (u != null && toProv != null) {
            int result = getParent().moveUnit(fromProvince,toProvince,u);
            if (result == -1)  {
                appendResultBox("Cant move unit to this location\n");
            }
            else if (result == 0) {
                appendResultBox("Cant move unit as the province selected is not connected!\n");
            }
            else {
                appendResultBox("Successfully moved the unit to the province!\n");
                Music audo = new Music("src/unsw/gloriaromanus/MovementAudio.wav");
                audo.play();
            }
        }
        else {
            appendResultBox("Please select the unit and the province to move to!\n");
        }
    }

    @FXML
    public void clickedLowTax(ActionEvent e) {
        setTax(e,"Low tax");
    }

    @FXML
    public void clickedNormalTax(ActionEvent e) {
        setTax(e,"Normal tax");
    }

    @FXML
    public void clickedHighTax(ActionEvent e) {
        setTax(e,"High tax");
    }

    @FXML
    public void clickedveryHighTax(ActionEvent e) {
        setTax(e,"Very high tax");
    }

    @FXML
    public void clickedTrainingMenu (ActionEvent e) {
        getParent().switchToTrainingMenu(e, "unit");
    }

    @FXML
    public void clickedInvasionMenu(ActionEvent e) {
        getParent().switchToInvasionMenu(e, "unit");
    }
    
    void setTax(ActionEvent e, String tax) {
        getParent().setTax(e, tax);
        appendResultBox("The tax of the province was set to " + tax + "!\n");
    }
}
