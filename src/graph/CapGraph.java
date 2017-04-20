/**
 * 
 */
package graph;

import java.util.ArrayList;
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
	private int numVertices;
	
	public CapGraph()
	{
		vertices = new HashMap<Integer, HashSet<Integer>>();
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
		
		// Then for each of the original neighbors, see if its neighbors are in new graph. If so, add these as edges for new neighbor.
		for(Integer originalNeighbor : getNeighbors(center)) {
			for(Integer originalNeighborsNeighbor : getNeighbors(originalNeighbor)) {
				if(egoNet.vertices.containsKey(originalNeighborsNeighbor)) {
					egoNet.addEdge(originalNeighbor, originalNeighborsNeighbor);
					egoNet.addEdge(originalNeighborsNeighbor, originalNeighbor);
				}
			}
		}
	
		return egoNet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 * Returned graphs should not share any objects with the original graph.
	 */
	@Override
	public List<Graph> getSCCs() {
		
		// Initialize vertexStack
		Stack<Integer> vertexStack = new Stack<Integer>();
		
		for(Integer v : vertices.keySet()) {
			vertexStack.add(v);
		}
		
		// Initialize finished Stack
		Stack<Integer> finished = new Stack<Integer>();
		
		// Visit all vertexNodes, returning the ‘Finished’ stack
		finished = dfs(this, vertexStack);
				
		// Transpose the graph; Flip direction of all edges
		CapGraph gT = new CapGraph();	
		
		for(Integer v : this.vertices.keySet()) {
			gT.addVertex(v);
			for(Integer neighbor : vertices.get(v)) {
				gT.addVertex(neighbor);
				gT.addEdge(neighbor, v);
			}
		}
		
		// Visit all vertexNodes in 'Finished' stack, returning the  new ‘Finished’ stack
		return dfsSCC(gT, finished);
	}
	
	public Stack<Integer> dfs(CapGraph g, Stack<Integer> vertices) {
		
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		
		while (!vertices.isEmpty()) {
			Integer v = vertices.pop();
			List<Integer> sccList = new ArrayList<Integer>();
			
			if (!visited.contains(v)) {
				sccList.add(v);
				dfsVisit(g, v, visited, finished);
			}
		}
		
		return finished;
	}
	
	private void dfsVisit(CapGraph g, Integer v, HashSet<Integer> visited, Stack<Integer> finished) {
		visited.add(v);
		for (Integer n : g.getNeighbors(v)) {
			if (!visited.contains(n)) {
				dfsVisit(g, n, visited, finished);
			}
		}
		finished.push(v);
		return;
	}

	public List<Graph> dfsSCC(CapGraph g, Stack<Integer> vertices) {
		
		HashSet<Integer> visited = new HashSet<Integer>();
		List<Graph> sccList = new ArrayList<Graph>();
		
		while (!vertices.isEmpty()) {
			Integer v = vertices.pop();
			
			if (!visited.contains(v)) {
				// v is root of a new SCC
				CapGraph scc = new CapGraph();
				scc.addVertex(v);
				// Nodes visited during this dfsVisit call are added to the SCC graph for v
				dfsVisitSCC(g, v, visited, scc);
				sccList.add(scc);
			}
		}
		
		return sccList;
	}
	
	private void dfsVisitSCC(CapGraph g, Integer v, HashSet<Integer> visited, CapGraph scc) {
		visited.add(v);
		for (Integer n : g.getNeighbors(v)) {
			if (!visited.contains(n)) {
				dfsVisitSCC(g, n, visited, scc);
			}
		}
		scc.addVertex(v);
		return;
	}

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
		CapGraph testGraph = new CapGraph();
		GraphLoader.loadGraph(testGraph, "data/scc/test_1.txt");
//		GraphLoader.loadGraph(testGraph, "data/facebook_1000.txt");
		testGraph.exportGraph();
//		System.out.println("\negonet:");
//		testGraph.getEgonet(3).exportGraph();
	}

}
