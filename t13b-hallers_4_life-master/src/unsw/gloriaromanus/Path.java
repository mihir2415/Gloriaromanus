
package unsw.gloriaromanus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Path {
    private int length;
    private String destination; 
    private String starting;   
    private Boolean adjacency[][];
    private List<Node> nodeList;
    LinkedList<Integer> queue;

    public Path(String starting, String destination,Game g){
        this.starting = starting;
        this.length = 0;
        this.destination = destination;
        this.adjacency = new Boolean [53][53];
        for (int i = 0; i < adjacency.length ; i++) {
            for (int j = 0;j < adjacency.length; j++) {
                adjacency[i][j] = false;
            }
        }
        this.nodeList = new ArrayList<Node>();
        queue = new LinkedList<Integer>();
        createAdjecency(g);
    }

    void allProvinces(Game g) {
        for (Faction f :g.getFactionList()) {
            for(Province p : f.getConquered()) {
                nodeList.add(new Node (p.getName()));
            }
        }
    }
    
    void createAdjecency(Game g) {
        try {
            String content = Files.readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
            assert(content != null);
            JSONObject adjacentList = new JSONObject(content);
            Iterator<String> keys = adjacentList.keys();
            allProvinces(g);
            while(keys.hasNext()) {
                int index = -1;
                String key = keys.next();
                JSONObject obj = adjacentList.getJSONObject(key);
                for (Node n: nodeList) {
                    if (n.getProvince().equals(key)) {
                        index = nodeList.indexOf(n);
                    }
                }

                for (int i = 0; i < nodeList.size(); i++) {
                    Node n2 = nodeList.get(i);
                    Boolean adj = obj.getBoolean(n2.getProvince());
                    if (adj) {
                        adjacency[index][i] = true;
                    }
                }

            }
        }
        catch(IOException e) {
            System.out.println("IOException could not find");
        }        
    }

    public int  bfs(Faction f) {
        int v = adjacency.length;
        List<Province> conquered = f.getConquered();
        Boolean visited[] = new Boolean[v];
        int prev[] = new int[v];

        Arrays.fill(visited, Boolean.FALSE);
        Arrays.fill(prev,-1);
        int index = -1;
        int index2 = -1;

        for (int i = 0; i < nodeList.size(); i++) {
            Node n = nodeList.get(i);
            if (n.getProvince().equals(starting)) {
                index = i;
            }
        }

        for (int i = 0; i < nodeList.size(); i++) {
            Node n = nodeList.get(i);
            if (n.getProvince().equals(destination)) {
                index2 = i;
            }
        }

        visited[index]=true;

        queue.add(index);
        int count = 0;
        while(!queue.isEmpty()) {
            int x = queue.remove();
            if (!visited[x]) {
                visited[x] = true;
            }

            for (int i = 0; i < v ; i++) {
                if((adjacency[x][i]) && (!visited[i]) && checkName(nodeList.get(i).getProvince(), conquered)){
                    queue.add(i);
                    visited[i]=true;
                    prev[i] = x;
                    if (i == index2) {
                        return countLength(prev,index,index2);
                    }
                }
            }
        }
        return queue.size();
    }

    int countLength(int prev[], int index, int index2) {
        if(index == index2) {
            return 0;
        }
        int i = prev[index2];
        index2 = i;
        
        return 1 + countLength(prev, index, index2);
    }
    
    Boolean checkName(String name, List<Province> provinceList) {
        for (Province p : provinceList) {
            if (p.getName().equals(name)) {
                //System.out.println(p.getName());
                return true;
            }
        }
        return false;
    }

    public List<Node> findNeighbours(Node n, Faction f) {
        List<String> conquered = new ArrayList<String>();
        for(Province p: f.getConquered()) {
            conquered.add(p.getName());
        }

        List<Node> neighbours = new ArrayList<Node>();
        int nodeIndex=-1;

        for (int i = 0; i < nodeList.size(); i++) {
			if(nodeList.get(i).equals(n)){
				nodeIndex=i;
				break;
			}
        }

        if(nodeIndex != -1){
			for (int j = 0; j < nodeList.size(); j++) {
				if(adjacency[nodeIndex][j] && conquered.contains(nodeList.get(j).getProvince()) && nodeList.get(j) != n){
					neighbours.add(nodeList.get(j));
				}
			}
		}
        return neighbours;
    }

    void invade(){
        
    }

    Boolean isProvinceAdj() {
        int startIndex = -1;
        int destIndex = -1;
        for (Node n: nodeList) {
            if (starting.equals(n.getProvince())) {
                startIndex = nodeList.indexOf(n);
            }
            if (destination.equals(n.getProvince())) {
                destIndex = nodeList.indexOf(n);
            }
        }

        return adjacency[startIndex][destIndex];
    }
}
