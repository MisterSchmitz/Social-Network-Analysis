/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.GraphLoader;

/**
 * @author Michael Schmitz
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
			System.out.print(v+": ");
			StringBuffer neighbors = new StringBuffer();
			for(Integer n : getNeighbors(v)) {
				neighbors.append(n+" ");
			}
			System.out.println(neighbors);
		}
		return vertices;
	}
	
	public HashMap<Integer, Integer> getSimilar(int node){
		HashSet<Integer> nodeNeighbors = new HashSet<Integer>(); 
		HashMap<Integer, Integer> similarities = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> similarNodes = new HashMap<Integer, Integer>();
		
		System.out.print("Papers similar to paper: "+node+"...\t");
				
		nodeNeighbors = getNeighbors(node);
		
		HashSet<Integer> otherNodes = new HashSet<Integer>(vertices.keySet());
		otherNodes.removeIf(p -> p == node);
		
		for(Integer v : otherNodes) {
			HashSet<Integer> intersection = new HashSet<Integer>(getNeighbors(v));
			intersection.retainAll(nodeNeighbors);	//Find the intersection between node's neihbors and other node's neighbors
			similarities.put(v, intersection.size());	//Count the number of similar neighbors
		}
		
		// Return the 10 nodes with the most similar citations
		similarNodes = (HashMap<Integer, Integer>) similarities.entrySet().stream()
			.filter(p -> p.getValue() > 0)
			.sorted(Collections.reverseOrder(HashMap.Entry.comparingByValue()))
			.limit(10)	
			.collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
		
		return similarNodes;
	}
	
	public static void main (String[] args) {
		long startTime, endTime, duration;
		
		
		CapGraph testGraph = new CapGraph();
		
		System.out.print("Loading graph...");
		startTime = System.nanoTime();
//		GraphLoader.loadGraph(testGraph, "data/gscholar_test.txt");
//		GraphLoader.loadGraph(testGraph, "data/gscholar_lite.txt");
		GraphLoader.loadGraph(testGraph, "data/gscholar.txt");
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.print(" DONE: "+duration+" ms\n");
		
//		testGraph.exportGraph();
		
		startTime = System.nanoTime();
		System.out.println(testGraph.getSimilar(57900));
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.print("Runtime: "+duration+" ms\t");
		
//		System.out.println(testGraph.getSimilar(73079));
//		System.out.println(testGraph.getSimilar(18802));
//		System.out.println(testGraph.getSimilar(73346));
//		System.out.println(testGraph.getSimilar(80782));
//		System.out.println("\negonet:");
//		testGraph.getEgonet(3).exportGraph();
	}

}
