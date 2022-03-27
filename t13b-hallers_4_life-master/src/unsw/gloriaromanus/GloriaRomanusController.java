package unsw.gloriaromanus;

import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javafx.scene.media.*;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.util.Pair;

public class GloriaRomanusController{

  @FXML
  private MapView mapView;

  @FXML
  private StackPane stackPaneMain;

  // could use ControllerFactory?
  private ArrayList<Pair<MenuController, VBox>> controllerParentPairs;

  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private String humanFaction;
  private Faction faction;
  private Game g;
  private Endturn et;
  private Feature currentlySelectedHumanProvince;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;
  private String currentMenu;
  private int victory;

  @FXML 

  /**
   * Initializes the game with all the necessary components
   */
  private void initialize() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    currentMenu = null;
    this.victory = 0;
    g = new Game();
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();

    Random r = new Random();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, r.nextInt(500));
    }

    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)
    String menus = "start_page.fxml";
    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    System.out.println(menus);
    FXMLLoader loader = new FXMLLoader(getClass().getResource(menus));
    VBox root = (VBox)loader.load();
    MenuController menuController = (MenuController)loader.getController();
    menuController.setParent(this);
    controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));

    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());

    initializeProvinceLayers();
  }

  /**
   * Loads the game
   * @param e
   * @return
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   * @throws InterruptedException
   */
  public Boolean loadGame (ActionEvent e) throws JsonParseException, JsonMappingException, IOException,InterruptedException {
      Loadgame load = new Loadgame();
      g = load.loadnew(g);
      if (g == null) {
        return false;
      }
      stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
      this.currentMenu = "invasion";

      provinceToOwningFactionMap = getProvinceToOwningFactionMap();
      provinceToNumberTroopsMap = new HashMap<String, Integer>();
      for (Faction f : g.getFactionList()) {
        for (Province p : f.getConquered()){
          provinceToNumberTroopsMap.put(p.getName(), p.countTroops());
        }
      }
      humanFaction = g.getPlayers().get(0).getF().getName();

      currentlySelectedHumanProvince = null;
      currentlySelectedEnemyProvince = null;

      String []menus = {"invasion_menu.fxml", "basic_menu.fxml","unit_menu.fxml","Stats.fxml","Goalsetter.fxml","VictoryPage.fxml"};
      this.controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
      for (String fxmlName: menus) {
        System.out.println(fxmlName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
        VBox root = (VBox)loader.load();
        MenuController menuController = (MenuController)loader.getController();
        menuController.setParent(this);
        controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
      }

      stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
      initializeProvinceLayers();
      return true;

    }

    /**
     * Returns the number of players selected in the menu
     * @throws IOException
     */
    public void getNumberPlayers() throws IOException {
      stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
      String menus = "select_players.fxml";
      controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
      System.out.println(menus);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(menus));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
      stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    }

    /**
     * If user selects two players
     */
    public void twoPlayerGame() throws IOException {
      stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
      String menus = "select2Factions.fxml";
      controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
      System.out.println(menus);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(menus));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
      stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    }

    /**
     * If user selects three players
     */
    public void threePlayerGame() throws IOException {
      stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
      String menus = "select3Factions.fxml";
      controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
      System.out.println(menus);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(menus));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
      stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    }
    
    /**
     * If user selects "new game"
     * @param e
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws InterruptedException
     */
  public Boolean newGame(ActionEvent e) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    BackgroundMusic b = new BackgroundMusic("src/unsw/gloriaromanus/BackgroundAudio.wav");
    b.play();
    et = new Endturn();
    this.currentMenu = "invasion";
    List<MainPlayer> players = g.getPlayers();
    for (int i = 0; i < players.size(); i++) {
        et.add_player(players.get(i));
    }
    // TODO = you should rely on an object oriented design to determine ownership
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();

    provinceToNumberTroopsMap = new HashMap<String, Integer>();

    //Random r = new Random();
    for (Faction f : g.getFactionList()) {
      for (Province p : f.getConquered()){
        provinceToNumberTroopsMap.put(p.getName(), p.countTroops());
      }
    }
    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)
    // humanFaction = human.getF().getName();
    // Faction h = human.getF();
    //System.out.println(h.getGoal());
    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;
// Add file name to the end
    String []menus = {"invasion_menu.fxml", "basic_menu.fxml","unit_menu.fxml","Stats.fxml","Goalsetter.fxml","VictoryPage.fxml"};
    // Add file name to the end
    this.controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    for (String fxmlName: menus) {
      System.out.println(fxmlName);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
      VBox root = (VBox)loader.load();
      MenuController menuController = (MenuController)loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
    }

    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    // ((StatsController)controllerParentPairs.get(3).getKey()).setall(h);

    initializeProvinceLayers();
    humanFaction = g.getPlayers().get(0).getF().getName();
    faction = g.getFaction(humanFaction);
    ((StatsController)controllerParentPairs.get(3).getKey()).setall(faction);
    return true;
  }

  /**
   * If user selects the "end turn" button
   * @param turn
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  void endTurn (int turn) throws JsonParseException, JsonMappingException, IOException {
    if (turn == g.getPlayers().size()) {
      turn = 0;
    }
    faction = g.getPlayers().get(turn).getF();
    humanFaction = faction.getName();
    ((StatsController)controllerParentPairs.get(3).getKey()).setall(faction);
    setAllTroops();
    resetSelections();  // reset selections in UI
    addAllPointGraphics(); // reset graphics
    Playergoals check = new Playergoals(faction);
    if (check.getPlayerWon() && victory == 0){
      stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
      stackPaneMain.getChildren().add(controllerParentPairs.get(5).getValue());
      victory = 1;
    }
  } 

  void setAllTroops() {
    for (Faction f: g.getFactionList()) {
      for (Province p : f.getConquered()) {
        provinceToNumberTroopsMap.put(p.getName(), p.countTroops());
      }
    }
    
  }
  /**
   * If user clicks the "Train button"
   */
  public int clickedTrainButton(ActionEvent e,String unit){
    if(currentlySelectedHumanProvince != null) {
      String humanProvince = (String) currentlySelectedHumanProvince.getAttributes().get("name");
      Province province = g.getProvince(humanProvince);
      int result = province.creatUnit(unit);
      if(result == 0) {
        int treasury =  g.getProvince(humanProvince).getTreasury();
        ((BasicMenuController)controllerParentPairs.get(1).getKey()).setGold((Integer.toString(treasury)));
      }
      
      ((StatsController) controllerParentPairs.get(3).getKey()).setall(g.getFaction(humanFaction));
      return result;
    }
    
      
    return -1;
  }

  /**
   * If user clciks the "Invade Button"
   * @param e
   * @throws IOException
   */
  public void clickedInvadeButton(ActionEvent e) throws IOException {
    if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      String humanProvince = (String)currentlySelectedHumanProvince.getAttributes().get("name");
      String enemyProvince = (String)currentlySelectedEnemyProvince.getAttributes().get("name");
      Province humanP = g.getProvince(humanProvince);
      Province enemyP = g.getProvince(enemyProvince);

      if (confirmIfProvincesConnected(humanProvince, enemyProvince)){
        // TODO = have better battle resolution than 50% chance of winning
        Battle b = humanP.invadeArmy(enemyP, g);
        provinceToNumberTroopsMap.put(enemyProvince, enemyP.countTroops());
        provinceToNumberTroopsMap.put(humanProvince,humanP.countTroops());
        String result = b.get_res();
        switch(result) {
          case "Attack":
            provinceToOwningFactionMap.put(enemyProvince, humanFaction);
            printMessageToTerminal("Won battle!");
            Music audio = new Music("src/unsw/gloriaromanus/InvadeAudio.wav");
            audio.play();
            break;
          case "Defence":
            printMessageToTerminal("Lost battle!");
            Music audio1 = new Music("src/unsw/gloriaromanus/InvadeAudio.wav");
            audio1.play();
            break;
          case "null":
            printMessageToTerminal("Battle could not take place!");
        }
        ((StatsController) controllerParentPairs.get(3).getKey()).setall(g.getFaction(humanFaction));
        // Random r = new Random();
        // int choice = r.nextInt(2);
        // if (choice == 0){
        //   // human won. Transfer 40% of troops of human over. No casualties by human, but enemy loses all troops
        //   int numTroopsToTransfer = provinceToNumberTroopsMap.get(humanProvince)*2/5;
        //   provinceToNumberTroopsMap.put(enemyProvince, numTroopsToTransfer);
        //   provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsToTransfer);
        //   provinceToOwningFactionMap.put(enemyProvince, humanFaction);
        //   printMessageToTerminal("Won battle!");
        // }
        // else{
        //   // enemy won. Human loses 60% of soldiers in the province
        //   int numTroopsLost = provinceToNumberTroopsMap.get(humanProvince)*3/5;
        //   provinceToNumberTroopsMap.put(humanProvince, provinceToNumberTroopsMap.get(humanProvince)-numTroopsLost);
        //   printMessageToTerminal("Lost battle!");
        // }
        resetSelections();  // reset selections in UI
        addAllPointGraphics(); // reset graphics
        
      }
      else{
        printMessageToTerminal("Provinces not adjacent, cannot invade!");
      }
      
    }
  }

  /**
   * If user clicks the "end turn" button
   * @param e
   * @throws IOException
   */
  public void clickedEndTurnbutton(ActionEvent e) throws IOException {
      et.endturn(g);
      endTurn(et.getTurn());
      printMessageToTerminal("Passed on to the other player!!");
      Music f = new Music("src/unsw/gloriaromanus/TurnAudio.wav");
        f.play();
  }

  /**
   * If user clicks save game
   */
  public void saveGame() throws IOException{
    new Savegame(g);
  }

  public List<String> getFactionList() {
    return g.getAvailFactionList();
  }

  public void addPlayer(String faction) {
    List<MainPlayer> listPlayer = g.getPlayers();
    listPlayer.add(new MainPlayer(faction, g));
    g.setPlayers(listPlayer);
  }

  // public void clickedAddPlayerButton(ActionEvent e) throws IOException {
  //     if (g.getPlayers().size() == 3) {
  //       printMessageToTerminal("Cannot add more players!");
  //     } else {
  //       MainPlayer newp = new MainPlayer(g);
  //       g.getPlayers().add(newp); 
  //       printMessageToTerminal("Added the player!");
  //     }
  // }

  

  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10,
            faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province), 0xFFFF0000,
            HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        switch (faction) {
          case "Gaul":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image((new File("images/Celtic_Druid.png")).toURI().toString()));
            break;
          case "Rome":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol (different to BufferedImage)
            s = new PictureMarkerSymbol("images/legionary.png");
            break;
            
          case "Spanish":
              s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/ArcherMan/Egyptian/Egyptian_Archer_NB.png");
              break;
          // TODO = handle all faction names, and find a better structure...
        }
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        Graphic gPic = new Graphic(curPoint, s);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }

  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp,
            screenPoint, 0, false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());

              if (features.size() > 1){
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              }
              else if (features.size() == 1){
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String)f.getAttributes().get("name");

                if (provinceToOwningFactionMap.get(province).equals(humanFaction)){
                  // province owned by human
                  if (currentlySelectedHumanProvince != null){
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  currentlySelectedHumanProvince = f;
                  switch (currentMenu) {
                    case "training":
                      ((BasicMenuController)controllerParentPairs.get(1).getKey()).setProvince(province);
                      int treasury =  g.getProvince(province).getTreasury();
                      ((BasicMenuController)controllerParentPairs.get(1).getKey()).setGold((Integer.toString(treasury)));
                    break;

                    case "unit":
                      ((UnitMenuController) controllerParentPairs.get(2).getKey()).clearComboBox();
                      ((UnitMenuController)controllerParentPairs.get(2).getKey()).setTextToField(province);
                      ((UnitMenuController)controllerParentPairs.get(2).getKey()).setListProvinceCombo();
                      ((UnitMenuController)controllerParentPairs.get(2).getKey()).setListUnitCombo();;
                    break;

                    case "invasion":
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                      
                    break;
                  }
                  
                }
                else{
                  if (currentlySelectedEnemyProvince != null){
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  switch (currentMenu) {
                    case "training":
                      ((BasicMenuController) controllerParentPairs.get(1).getKey()).setResult("You have selected an enemy province!\n");
                    break;
                    case "unit":
                      ((UnitMenuController) controllerParentPairs.get(2).getKey()).setResultBox("You have selected an enemy province!\n");
                    break;
                    case "invasion":
                      ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                    break;
                  }
                }
                featureLayer.selectFeature(f);                
            }
              
            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(humanFaction));
  }

  /**
   * returns query for arcgis to get features representing human provinces can
   * apply this to FeatureTable.queryFeaturesAsync() pass string to
   * QueryParameters.setWhereClause() as the query string
   */
  private String getHumanProvincesQuery() throws IOException {
    LinkedList<String> l = new LinkedList<String>();
    for (String hp : getHumanProvincesList()) {
      l.add("name='" + hp + "'");
    }
    return "(" + String.join(" OR ", l) + ")";
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections(){

    if (currentlySelectedEnemyProvince == null && currentlySelectedHumanProvince != null) {
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedHumanProvince));
      currentlySelectedEnemyProvince = null;
    } 
    else if (currentlySelectedHumanProvince == null && currentlySelectedEnemyProvince != null) {
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince));
      currentlySelectedEnemyProvince = null;

    } 
    else if (currentlySelectedHumanProvince != null && currentlySelectedEnemyProvince != null){
      featureLayer_provinces.unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
    }
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    
      switch (currentMenu) {
        case "training":
          ((BasicMenuController)controllerParentPairs.get(1).getKey()).setProvince("");
          ((BasicMenuController)controllerParentPairs.get(1).getKey()).setGold("");
          ((BasicMenuController)controllerParentPairs.get(1).getKey()).addResult("");
        break;
        case "unit":
          ((UnitMenuController)controllerParentPairs.get(2).getKey()).setTextToField("");
          ((UnitMenuController) controllerParentPairs.get(2).getKey()).setResultBox("");
          ((UnitMenuController) controllerParentPairs.get(2).getKey()).clearComboBox();
        break;
        case "invasion":
          ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setInvadingProvince("");
          ((InvasionMenuController)controllerParentPairs.get(0).getKey()).setOpponentProvince("");

        break;
      }

  }

  private void printMessageToTerminal(String message){
    switch(currentMenu) {
      case "invasion":
        ((InvasionMenuController)controllerParentPairs.get(0).getKey()).appendToTerminal(message);
        break;
    }
  }

  /**
   * Stops and releases all resources used in application.
   */
  void terminate() {
    if (mapView != null) {
      mapView.dispose();
    }
  }

  public void switchToGoalMenu(ActionEvent e , String fromMenu) {
    System.out.println("Trying to switch to Goal Menu");
    resetSelections();
    switch (fromMenu) {
      case "invasion":
        stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
        break;
    }

    stackPaneMain.getChildren().add(controllerParentPairs.get(4).getValue());  
    currentMenu = "goal";

  }

  public void switchToUnitMenu(ActionEvent e,String fromMenu) {
    System.out.println("trying to switch to unit menu");
    switch (fromMenu) {
      case "invasion":
        stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
        break;
      case "training":
      stackPaneMain.getChildren().remove(controllerParentPairs.get(1).getValue());
    }
    resetSelections();
    stackPaneMain.getChildren().add(controllerParentPairs.get(2).getValue());
    currentMenu = "unit";
  }

  public void switchToTrainingMenu(ActionEvent e,String fromMenu) {
    System.out.println("trying to switch to training menu");
    switch (fromMenu) {
      case "invasion":
        stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
        break;
      case "unit":
        stackPaneMain.getChildren().remove(controllerParentPairs.get(2).getValue());
        break;
      case "goal":
      stackPaneMain.getChildren().remove(controllerParentPairs.get(4).getValue());
        break;
    }
    resetSelections();
    stackPaneMain.getChildren().add(controllerParentPairs.get(1).getValue());
    currentMenu = "training";
  }

  public void switchToInvasionMenu(ActionEvent e,String fromMenu) {
    System.out.println("trying to switch to invasion menu");
    switch (fromMenu) {
      case "training":
        stackPaneMain.getChildren().remove(controllerParentPairs.get(1).getValue());
        break;
      case "unit":
        stackPaneMain.getChildren().remove(controllerParentPairs.get(2).getValue());
        break;
      case "goal":
        stackPaneMain.getChildren().remove(controllerParentPairs.get(4).getValue());
        break;
    }
    resetSelections();
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
    currentMenu = "invasion";
  }

  Faction getFaction() {
    return g.getFaction(humanFaction);
  }
  public void setTax(ActionEvent e , String tax) {
    String humanProvince = (String) currentlySelectedHumanProvince.getAttributes().get("name");
    Province province = g.getProvince(humanProvince);
    province.setLevelTax(tax);
    resetSelections();
  }

  public List<Province> getListProvince() {
    Faction f = g.getFaction(humanFaction);
    return f.getConquered();
  }

  public List<Unit> getListUnit() {
    String humanProvince = (String) currentlySelectedHumanProvince.getAttributes().get("name");
    Province province = g.getProvince(humanProvince);
    return province.getUnitList();
  }

  public Province getSelectedProvince() {
    String humanProvince = (String) currentlySelectedHumanProvince.getAttributes().get("name");
    Province province = g.getProvince(humanProvince);
    return province;
  }

  public Province getProvince(String province) {
    return g.getProvince(province);
  }

  public int moveUnit(Province p1, Province p2, String un) throws JsonParseException, JsonMappingException, IOException {
    Unit u = null;
    for (Unit unit : p1.getUnitList()) {
      if (unit.getName().equals(un)) u = unit;
    }
    int result = p1.moveUnit(u, p2.getName(),g.getFaction(p1.getFaction()), g);
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      if (provinceName.equals(p1.getName())) {
        provinceToNumberTroopsMap.put(provinceName, 0);
      }
    }
    provinceToNumberTroopsMap.put(p1.getName(), p1.countTroops());
    provinceToNumberTroopsMap.put(p2.getName(), p2.countTroops());
    resetSelections();  // reset selections in UI
    addAllPointGraphics(); // reset graphicss
    return result;
  }

  public void clickedDisplayStats() {
    stackPaneMain.getChildren().add(controllerParentPairs.get(3).getValue());
  }

  public void releasedDisplayStats() {
    stackPaneMain.getChildren().remove(controllerParentPairs.get(3).getValue());
  }

  public void continueGame() {
    stackPaneMain.getChildren().remove(controllerParentPairs.get(5).getValue());
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }

public void updateStats(Faction player) {
  ((StatsController)controllerParentPairs.get(3).getKey()).setall(player);
}

}