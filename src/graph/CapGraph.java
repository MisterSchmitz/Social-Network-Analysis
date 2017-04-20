/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import util.GraphLoader;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	private HashMap<Integer, HashSet<Integer>> graph;
	private HashMap<Integer, Vertex> vertices;
	private int numVertices;
	
	public CapGraph()
	{
		graph = new HashMap<Integer, HashSet<Integer>>();
		vertices = new HashMap<Integer, Vertex>();
		numVertices = 0;
	}
	
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		// Validate Inputs
		if(vertices.containsKey(num)) {
			return;
		}

		// Create new Vertex and add vertex to graph
		graph.put(num, new HashSet<Integer>());
		vertices.put(num, new Vertex(num));		
		numVertices++;
		
		return;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		Vertex vFrom = vertices.get(from);
		Vertex vTo = vertices.get(to);
		if(vFrom == null || vTo == null) {
			return;
		}

		if(vFrom.getNeighbors().contains(vTo)) {
			return;
		}
		
		vFrom.addNeighbor(vTo);
		graph.get(from).add(to);
		
		return;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		for(Vertex v : vertices.values()) {
			System.out.print("\n"+v.getNum()+": ");
			StringBuffer neighbors = new StringBuffer();
			for(Vertex n : v.getNeighbors()) {
				neighbors.append(n.getNum()+" ");
			}
			System.out.print(neighbors);
		}
		return graph;
	}
	
	public static void main (String[] args) {
		CapGraph testGraph = new CapGraph();
//		GraphLoader.loadGraph(testGraph, "data/small_test_graph.txt");
		GraphLoader.loadGraph(testGraph, "data/facebook_1000.txt");
		testGraph.exportGraph();
	}

}
