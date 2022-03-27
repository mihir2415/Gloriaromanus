package unsw.gloriaromanus;

import java.io.File;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Load {
    
    public Load() {

    }

    /**
     * 
     */
    public Game loaddata(Game tolaod) {
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            tolaod = mapper.readValue(new File("src/unsw/gloriaromanus/SavedGame.json"),Game.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tolaod;
    }

    // public Game getG() {
    //     return g;
    // }

    // public void setG(Game g) {
    //     this.g = g;
    // }

    
}
