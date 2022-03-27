// package test;

// import static org.junit.Assert.assertEquals;

// import org.junit.jupiter.api.Test;

// import static org.junit.Assert.assertEquals;

// import java.util.List;

// import unsw.gloriaromanus.*;

// public class UnitTest {
//     @Test
//     public void testUnit(){
//         Game g = new Game();
//         MainPlayer p = new MainPlayer(g);

//         Unit u = new Unit("Elephants");
//         assert(u.getName().equals("Elephants"));
//         assert(u.getType().equals("Cavalry"));
//         assertEquals(u.getNumTroops(), 4);
        
//         Faction fac = p.getF();
//         List<Province> listProv = fac.getConquered();
//         for (Province prov : listProv) {
//             prov.setTreasury(100);
//             assert(prov.getTreasury() == 100);
//             prov.creatUnit("Elephants");
//             assert(prov.getTraining().size() == 1);
//             assert(prov.getUnitList().size() == 0);
            
//         }
//         /**Set the treasury to 0 so the unit can not be created */
//         Province prov = listProv.get(0);
//         prov.setTreasury(0);
//         prov.creatUnit("Gallic berserker");
//         /**Treasury is decreased when the unit is created */
//         prov.setTreasury(100);
//         prov.creatUnit("Gallic berserker");
//         assertEquals(prov.getTreasury(),(100 - 6));
//         prov.creatUnit("Gallic berserker");
//         /**Does not add unit to trainig list as the training spots are full */
//         prov.creatUnit("Gallic berserker");
//         assertEquals(prov.getTrainingSpots(),0);

//         Unit u1 = new Unit("Elephants");
//         assertEquals(u1.getMovementPointStatic(), 15);

//         Unit u2 = new Unit("Celtic Briton berserker");
//         assertEquals(u2.getMovementPointStatic(),10);
        
//     }

//     @Test
//     public void testProvince(){
//         Game g = new Game();
//         Endturn turn = new Endturn();
//         MainPlayer player = new MainPlayer(g);
//         turn.add_player(player);
//         Faction f = player.getF();
//         Province p1 = f.getConquered().get(0);
//         Province p2 = f.getConquered().get(1);
//         Unit u1 = new Unit("Elephants");
//         p1.addUnit(u1);
//         Path p = new Path(p1.getName(), p2.getName(), g);
//         List <Node> neighbours = p.findNeighbours(new Node(p1.getName()), f);

//         int flag = 0;
        
//         for (Node n : neighbours) {
//             if (n.getProvince().equals(p2.getName())) flag = 1;
//         }

//         if (flag != 0) {
//             assertEquals(p1.moveUnit(u1, p2.getName(), f, g),1);
//         }

//         for (Province province : f.getConquered()) {
//             province.setLevelTax("High tax");
//             assertEquals(province.getLevelTax(),"High tax");
//             province.addToArmy(new Unit("Artillery"));
//             assertEquals(province.getArmy().size(), 1);
//         }

//     }
    
//     @Test
//     public void playerTest(){
//         Game g = new Game();
//         MainPlayer player_one = new MainPlayer(g);
//         MainPlayer player_two = new MainPlayer(g);
//         MainPlayer player_three = new MainPlayer(g);
        
//         Endturn turn = new Endturn();
//         turn.setTurn(0);
//         turn.add_player(player_one);
//         for (Province p : player_one.getF().getConquered()) {
//             p.setTreasury(100);
//             p.setLevelTax("Low tax");
//             p.creatUnit("Elephants");
//             assertEquals(p.getUnitList().size(), 0);
//         }

//         // turn.endturn(g);
//         // turn.endturn(g);turn.endturn(g);

//         // for (Province p : player_one.getF().getConquered()) {
//         //     assertEquals(p.getUnitList().size(), 1);
//         // }
//         // assert(player_one.getF().getTotal_wealth() != 0);
//         // assert(player_one.getF().getTotal_treasury() != 0);

//         Faction player_one_fac = player_one.getF();
//         Faction player_two_fac = player_two.getF();
//         Faction player_three_fac = player_three.getF();
        
//         assertEquals(false, player_one_fac.getConquest_goal()); 
//         assertEquals(false, player_two_fac.getConquest_goal());
//         assertEquals(false, player_three_fac.getConquest_goal());
//         player_one_fac.setConquest_goal(true);
//         assertEquals(true, player_one_fac.getConquest_goal()); 
//         player_one_fac.setTotal_treasury(10);
//         assertEquals(10, player_one_fac.getTotal_treasury()); 
//         player_two_fac.setWealth(15);
//         assertEquals(15, player_two_fac.getTotal_wealth()); 
 
//         Province p = player_three_fac.getConquered().get(0);
//         Province o = player_two_fac.getConquered().get(0);
//        // assertEquals(false, p.invadeArmy(p.getArmy(),o,g)); 

    


        
//     }

   

//  }

