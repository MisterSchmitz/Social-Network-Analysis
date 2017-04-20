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
	private HashMap<Integer, HashSet<Integer>> gph;
	private HashMap<Integer, Vertex> vertices;
	private int numVertices;
	
	public CapGraph()
	{
		gph = new HashMap<Integer, HashSet<Integer>>();
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
		gph.put(num, new HashSet<Integer>());
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
		gph.get(from).add(to);
		
		return;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		
		// Create a new graph
		CapGraph egoNet = new CapGraph();
				
		// Add center as a Vertex
		egoNet.addVertex(center);
		
		// Add all of center's neighbors from main graph into new egoNet
		for(Vertex origNeighbor : vertices.get(center).getNeighbors()) {
			egoNet.addVertex(origNeighbor.getNum());
			// And add these new vertices as center's neighbors in new egoNet
			egoNet.addEdge(center, origNeighbor.getNum());
			egoNet.addEdge(origNeighbor.getNum(), center);
		}
		
		// Then for each of the original neighbors, see if its neighbors are in new graph. If so, add these as edges for new neighbor.
		for(Vertex origNeighbor : vertices.get(center).getNeighbors()) {
			for(Vertex origNeighborsNeighbor : origNeighbor.getNeighbors()) {
				if(egoNet.vertices.containsKey(origNeighborsNeighbor.getNum())) {
					egoNet.addEdge(origNeighbor.getNum(), origNeighborsNeighbor.getNum());
					egoNet.addEdge(origNeighborsNeighbor.getNum(), origNeighbor.getNum());
				}
			}
		}
	
		return egoNet;
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
		return gph;
	}
	
	public static void main (String[] args) {
//		CapGraph testGraph = new CapGraph();
//		GraphLoader.loadGraph(testGraph, "data/small_test_graph.txt");
////		GraphLoader.loadGraph(testGraph, "data/facebook_1000.txt");
//		testGraph.exportGraph();
//		System.out.println("\negonet:");
//		testGraph.getEgonet(3).exportGraph();
	}

}
