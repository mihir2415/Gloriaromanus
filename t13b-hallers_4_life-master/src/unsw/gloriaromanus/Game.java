package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Game {
    private List<Faction> factionList;
    private int year;
    private List<String> availFactionList;
    //private List<Province> provinceList;
    private List<MainPlayer> players;

    /**
     * A constructor for the Class 
     */

    public Game(){
        this.factionList = new ArrayList<Faction>();
        this.year = 1;
        this.players = null;
        this.availFactionList = new ArrayList<String>();
        createFactions();
        for (Faction f : this.factionList) {
            availFactionList.add(f.getName());
        }
        this.players = new ArrayList<MainPlayer>();
    }


    // Constructor used for loading game
    public Game (List<Faction> factions, int year, List<String> liststo, List<MainPlayer> players) {
        this.factionList = factions;
        this.year = year;
        this.availFactionList = liststo;
        this.players = players;
    }
    
    /**
     * A void funciton which will create factions for the game 
     * and its proviences 
     */

    void createFactions() {
        //String line = "{\r\n  \"Rome\": [\r\n    \"Lusitania\",\r\n    \"Lycia et Pamphylia\",\r\n    \"Macedonia\",\r\n    \"Mauretania Caesariensis\"\r\n  ],\r\n  \"Gaul\": [\r\n    \"Alpes Graiae et Poeninae\",\r\n    \"Alpes Maritimae\",\r\n    \"Aquitania\",\r\n    \"Cyprus\"\r\n  ], \r\n\r\n  \"Carthaginians\": [\r\n    \"VI\",\r\n    \"VII\",\r\n    \"Lugdunensis\"\r\n  ],\r\n\r\n  \"Celtic Britons\" : [\r\n    \"Achaia\",\r\n    \"Aegyptus\",\r\n    \"Africa Proconsularis\"\r\n  ], \r\n\r\n  \"Spanish\" : [\r\n    \"Numidia\",\r\n    \"Pannonia Inferior\",\r\n    \"Pannonia Superior\"\r\n  ], \r\n\r\n  \"Numidians\" : [\r\n    \"Moesia Inferior\",\r\n    \"Moesia Superior\",\r\n    \"Belgica\"\r\n  ], \r\n\r\n  \"Egyptians\" : [\r\n    \"Armenia Mesopotamia\",\r\n    \"Asia\",\r\n    \"Baetica\"\r\n  ], \r\n\r\n  \"Seleucid Empire\" : [\r\n    \"Raetia\",\r\n    \"Sardinia et Corsica\",\r\n    \"Alpes Cottiae\"\r\n  ], \r\n  \r\n  \"Pontus\" : [\r\n    \"VIII\",\r\n    \"X\",\r\n    \"Galatia et Cappadocia\"\r\n  ], \r\n\r\n  \"Amenians\" : [\r\n    \"Sicilia\",\r\n    \"Syria\",\r\n    \"Germania Inferior\"\r\n  ], \r\n\r\n  \"Parthians\" : [\r\n    \"II\",\r\n    \"III\",\r\n    \"Thracia\"\r\n  ], \r\n\r\n  \"Germanics\" : [\r\n    \"IV\",\r\n    \"IX\",\r\n    \"Iudaea\"\r\n  ], \r\n\r\n  \"Greek City States\" : [\r\n    \"Tarraconensis\",\r\n    \"V\",\r\n    \"XI\"\r\n  ], \r\n\r\n  \"Macedonians\" : [\r\n    \"Dalmatia\",\r\n    \"Germania Superior\",\r\n    \"I\",\r\n    \"Mauretania Tingitana\"\r\n  ], \r\n\r\n  \"Thracians\" : [\r\n    \"Narbonensis\",\r\n    \"Noricum\",\r\n    \"Dacia\",\r\n    \"Creta et Cyrene\"\r\n  ], \r\n\r\n  \"Dacians\" : [\r\n    \"Bithynia et Pontus\",\r\n    \"Britannia\",\r\n    \"Cilicia\",\r\n    \"Arabia\"\r\n  ]\r\n}";
        try {
            String line = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
            JSONObject factions = new JSONObject(line);
            Iterator<String> keys = factions.keys();            
            while(keys.hasNext()) {
                String key = keys.next();
                Faction f = new Faction(key, this);
                if (factions.get(key) instanceof JSONArray) {
                    JSONArray provinces = (JSONArray) factions.get(key);
                    for (int i = 0; i < provinces.length();i++) {
                        String prov = provinces.getString(i);
                        f.addProvince(new Province(f.getName(), prov, "Normal tax",this));
                    }
                }
                factionList.add(f);
            }

        } catch (IOException e) {
            System.out.println("FOUND IOEXCEPTION");
        }
        
    }


	public List<Faction> getFactionList() {
		return factionList;
	}

	public void setFactionList(List<Faction> factionList) {
		this.factionList = factionList;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
    }

    public List<String> getAvailFactionList() {
        return availFactionList;
    }

    public void setAvailFactionList(List<String> availFactionList) {
        this.availFactionList = availFactionList;
    }

    public List<MainPlayer>getPlayers() {
        return players;
    }

    public void setPlayers(List<MainPlayer> players) {
        this.players = players;
    }

    public Province getProvince(String province) {
        Province prov = null;
        for (Faction f: factionList) {
            for (Province p: f.getConquered()){
                if (p.getName().equals(province)) {
                    prov = p;
                }
            }
        }
        return prov;
    }

    public Faction getFaction (String faction) {
        Faction fac = null;
        for (Faction f : factionList) {
            if (f.getName().equals(faction)) {
                fac = f;
            }
        }
        return fac;
    }
}

