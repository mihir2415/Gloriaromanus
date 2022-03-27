package unsw.gloriaromanus;

public class Node {
    String province;
    Boolean visited;

	/**
	 * A constructor for the class
	 * @param province a destinaton 
	 */
	public Node(String province) {
		this.province = province;
		this.visited = false;
	}

    public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}


}