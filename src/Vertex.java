import java.util.ArrayList;


public class Vertex implements Comparable<Vertex> {
	public ArrayList<Integer> adj = new ArrayList<>();
	public ArrayList<Integer> adjWeight = new ArrayList<>();
	public int indegree, outdegree;
	public int top;
	public int dis;
	public boolean known;
	public int pred;
	
	public Vertex() {
		known = false;
		dis = Integer.MAX_VALUE;
		pred = -1;
	}
	
	public void addAdj(int adjIndex, int weight) {
		if(hasBeenAdded(adjIndex) == false) {
			adj.add(adjIndex);
			adjWeight.add(weight);
		}
	}
	
	private boolean hasBeenAdded(int adjIndex) {
		
		for(int tmp : adj) {
			if(tmp == adjIndex)
				return true;
		}
		return false;
	}

	@Override
	public int compareTo(Vertex o) {
		// TODO Auto-generated method stub
		return this.dis - o.dis;
	}
}


