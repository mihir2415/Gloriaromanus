package unsw.gloriaromanus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Code adapted from
 * https://howtodoinjava.com/jackson/jackson-convert-object-to-from-json/
 */
public class Savegame {
    public Savegame(Game g) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // //int i = 0; 
        String json = "";
        // List<Faction> savefactions;
        // savefactions = g.getFactionList();
        json += mapper.writeValueAsString(g);
    
        FileWriter file = new FileWriter("src/unsw/gloriaromanus/Save.json"); 
        file.write(json);
        file.close();
    }
}


