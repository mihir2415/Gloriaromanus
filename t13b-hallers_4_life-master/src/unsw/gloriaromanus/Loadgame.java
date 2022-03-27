package unsw.gloriaromanus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Loadgame {
    public Loadgame () {

    }
    
    /**
     * 
     * @param g
     * @return loads the game from the saved file
     * @throws IOException
     */
    public Game loadnew (Game g) throws IOException {
        Game loadgame = null;
        //ObjectMapper mapper = new ObjectMapper();
        try {
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/Save.json"));
       
            loadgame = new ObjectMapper().readValue(content, Game.class);
        
        

        } catch (Exception e) {
            System.out.println("Game Has not been saved before");
            return null;
        }
        return loadgame;
    }
}
