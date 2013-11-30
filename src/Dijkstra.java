import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class Dijkstra {
	private VertexHeap Q; // priority queue minHeap;
	public Vertex[] vertices;

	public Dijkstra(int num_vertices) {
		vertices = new Vertex[num_vertices];
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vertex();
		}
	}

	public int findShortestPathBeginWithSource(int source) {
		vertices[source].dis = 0;
		buildPriorityQueue();
		int u_index, v_index;
		int u_v_weight;
		Vertex u, v;

		while (!Q.isEmpty()) {
			u_index = Q.deleteMin();
			u = vertices[u_index];
			u.known = true;
			Iterator<Integer> adj_itor = u.adj.iterator();
			Iterator<Integer> adjWeight_itor = u.adjWeight.iterator();
			while(adj_itor.hasNext()) {
				v_index = adj_itor.next();
				v = vertices[v_index];
				u_v_weight = adjWeight_itor.next();
				if ((!v.known) && v.dis > (u.dis + u_v_weight)) {
					v.dis = u.dis + u_v_weight;
					dereaseKey(v_index, v.dis);
					v.pred = u_index;
				}
			}
		}

		return 0;
	}

	private void buildPriorityQueue() {
		Q = new VertexHeap(vertices);
		Q.buildHeap();
	}

	private void dereaseKey(int index, int dis) {
		Q.decreaseElement(index, dis); // decrease dis and percolateUp
	}

	public void outputResult(BufferedWriter writer) throws IOException {
		for (int i = 0; i < vertices.length; i++) {
			if (!vertices[i].known)
				writer.write(i + " " + "No path from s to v\n");
			else
				writer.write(i + " " + vertices[i].dis + " " + vertices[i].pred
						+ "\n");
		}
	}

	public static void main(String[] args) throws IOException {
		String in50 = "in.50";
		String out = "output.50";
		BufferedReader input = null;

		try {
			input = new BufferedReader(new FileReader(in50));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String titleLine = input.readLine();
		String statsticsLine = input.readLine();
		String[] parameters = statsticsLine.split(" ");
		if (parameters.length != 2)
			throw new IOException("Title line length = " + parameters.length);
		int num_vertices = Integer.valueOf(parameters[0]);
		int source = Integer.valueOf(parameters[1]);

		Dijkstra dijkstra = new Dijkstra(num_vertices);

		String thisLine = null;
		while ((thisLine = input.readLine()) != null) {
			String[] vertexEdge = thisLine.split(" ");
			if (vertexEdge.length != 3)
				throw new IOException("Input file format error");
			int vertex = Integer.valueOf(vertexEdge[0]);
			int adjVertex = Integer.valueOf(vertexEdge[1]);
			int weight = Integer.valueOf(vertexEdge[2]);
			Vertex u = dijkstra.vertices[vertex];
			u.addAdj(adjVertex, weight);
		}

		dijkstra.findShortestPathBeginWithSource(source);
		BufferedWriter output = null;

		output = new BufferedWriter(new FileWriter(out));
		output.write("Jun Yu\n");
		output.write(titleLine + "\n");

		dijkstra.outputResult(output);

		output.close();
	}
}
