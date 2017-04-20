/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import util.GraphLoader;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	private HashMap<Integer, HashSet<Integer>> vertices;
//	private HashMap<Integer, Vertex> vertexNodes;
	private int numVertices;
	
	public CapGraph()
	{
		vertices = new HashMap<Integer, HashSet<Integer>>();
//		vertexNodes = new HashMap<Integer, Vertex>();
		numVertices = 0;
	}
	
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		
		// Add Vertex
		if(!vertices.containsKey(num)) {
			vertices.put(num, new HashSet<Integer>());
			numVertices++;
		}
		return;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// Verify that from and to vertices exist
		if(!vertices.containsKey(from) || !vertices.containsKey(to)) {
			return;
		}
		
		// Add Edge
		if(!getNeighbors(from).contains(to)) {
			getNeighbors(from).add(to);
		}
		
		return;
	}
	
	public HashSet<Integer> getNeighbors(int v) {
		return vertices.get(v);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 * Returned graphs should not share any objects with the original graph.
	 */
	@Override
	public Graph getEgonet(int center) {
		
		// Create a new graph
		CapGraph egoNet = new CapGraph();
				
		// Add center as a Vertex
		egoNet.addVertex(center);
		
		// Add all of center's neighbors from original graph into new egoNet graph
		for(Integer originalNeighbor : getNeighbors(center)) {
			egoNet.addVertex(originalNeighbor);
			// And add these new vertexNodes as center's neighbors in new egoNet
			egoNet.addEdge(center, originalNeighbor);
			egoNet.addEdge(originalNeighbor, center);
		}
		
		
//		for(Vertex origNeighbor : vertexNodes.get(center).getNeighbors()) {
//			egoNet.addVertex(origNeighbor.getNum());
//			// And add these new vertexNodes as center's neighbors in new egoNet
//			egoNet.addEdge(center, origNeighbor.getNum());
//			egoNet.addEdge(origNeighbor.getNum(), center);
//		}
		
		// Then for each of the original neighbors, see if its neighbors are in new graph. If so, add these as edges for new neighbor.
		for(Integer originalNeighbor : getNeighbors(center)) {
			for(Integer originalNeighborsNeighbor : getNeighbors(originalNeighbor)) {
				if(egoNet.vertices.containsKey(originalNeighborsNeighbor)) {
					egoNet.addEdge(originalNeighbor, originalNeighborsNeighbor);
					egoNet.addEdge(originalNeighborsNeighbor, originalNeighbor);
				}
			}
		}
		
//		for(Vertex origNeighbor : vertexNodes.get(center).getNeighbors()) {
//			for(Vertex origNeighborsNeighbor : origNeighbor.getNeighbors()) {
//				if(egoNet.vertexNodes.containsKey(origNeighborsNeighbor.getNum())) {
//					egoNet.addEdge(origNeighbor.getNum(), origNeighborsNeighbor.getNum());
//					egoNet.addEdge(origNeighborsNeighbor.getNum(), origNeighbor.getNum());
//				}
//			}
//		}
	
		return egoNet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 * Returned graphs should not share any objects with the original graph.
	 */
	@Override
	public List<Graph> getSCCs() {
		// Create a new graph to use for SCCs
//		CapGraph g = new CapGraph();		
//		for(Integer v : this.vertices.keySet()) {
//			g.addVertex(v);
//		}
//		for(Vertex v : g.vertexNodes.values()) {
//			for (Integer n : v.getNeighborNums()) {
//				g.addEdge(v.getNum(), n);
//			}
//		}
//		// Initialize verticesSCC Stack
//		Stack<Vertex> verticesSCC = new Stack<Vertex>();
//		for(Vertex v : g.vertexNodes.values()) {
//			verticesSCC.add(v);
//		}
//		// Initialize finished Stack
//		Stack<Vertex> finished = new Stack<Vertex>();
//		
//		
//		// Visit all vertexNodes, returning the ‘Finished’ stack
//		finished = dfs(g, verticesSCC);
//		
//				
//		// Transpose the graph; Flip direction of all edges
//		CapGraph gT = new CapGraph();
//		for(Integer v : this.vertices.keySet()) {
//			g.addVertex(v);
//		}
//		for(Vertex v : gT.vertexNodes.values()) {
//			for (Integer n : v.getNeighborNums()) {
//				g.addEdge(n, v.getNum());
//			}
//		}
//		
//		// Visit all vertexNodes in 'Finshed' stack, returning the  new ‘Finished’ stack
//
//		
		return null;
	}

//	public Stack<Integer> dfs(Graph g, Stack<Integer> vertexNodes) {
//		HashSet<Integer> visited = new HashSet<Integer>();
//		Stack<Integer> finished = new Stack<Integer>();
//		
//		while (!vertexNodes.isEmpty()) {
//			Integer v = vertexNodes.pop();
//			if (!visited.contains(v)) {
//				dfsVisit(g, v, visited, finished);
//			}
//		}
//		
//		return finished;
//	}
//	
//	private void dfsVisit(Graph g, Integer v, HashSet<Integer> visited, Stack<Integer> finished) {
//		visited.add(v);
//		for (Integer n : v.getNeighbors()) {
//			if (!visited.contains(n)) {
//				dfsVisit(g, n, visited, finished);
//			}
//		}
//		finished.push(v);
//		return;
//	}
	
//	
//	public Stack<Vertex> dfs(Graph g, Stack<Vertex> vertexNodes) {
//		HashSet<Vertex> visited = new HashSet<Vertex>();
//		Stack<Vertex> finished = new Stack<Vertex>();
//		
//		while (!vertexNodes.isEmpty()) {
//			Vertex v = vertexNodes.pop();
//			if (!visited.contains(v)) {
//				dfsVisit(g, v, visited, finished);
//			}
//		}
//		
//		return finished;
//	}
//	
//	private void dfsVisit(Graph g, Vertex v, HashSet<Vertex> visited, Stack<Vertex> finished) {
//		visited.add(v);
//		for (Vertex n : v.getNeighbors()) {
//			if (!visited.contains(n)) {
//				dfsVisit(g, n, visited, finished);
//			}
//		}
//		finished.push(v);
//		return;
//	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		for(Integer v : vertices.keySet()) {
			System.out.print("\n"+v+": ");
			StringBuffer neighbors = new StringBuffer();
			for(Integer n : getNeighbors(v)) {
				neighbors.append(n+" ");
			}
			System.out.print(neighbors);
		}
		return vertices;
	}
	
	
	public static void main (String[] args) {
//		CapGraph testGraph = new CapGraph();
//		GraphLoader.loadGraph(testGraph, "data/small_test_graph.txt");
//		GraphLoader.loadGraph(testGraph, "data/facebook_1000.txt");
//		testGraph.exportGraph();
//		System.out.println("\negonet:");
//		testGraph.getEgonet(3).exportGraph();
	}

}
