/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
	private HashSet<Edge> edges;
	
	private int numVertices;
	private int nodeVisitCount;
	
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
			new Vertex(num);
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
//			Edge e = new Edge(from, to);
//			edges.add(e);
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
				
		nodeNeighbors = getNeighbors(node); // Find the neighbors for given node. Store these in HashSet.
		
		HashSet<Integer> otherNodes = new HashSet<Integer>(vertices.keySet());
		otherNodes.removeIf(p -> p == node); // Remove the given node from the set of other nodes
		
		for(Integer v : otherNodes) {
			HashSet<Integer> intersection = new HashSet<Integer>(getNeighbors(v));
			intersection.retainAll(nodeNeighbors);	// Find the intersection between node's neihbors and other node's neighbors
			similarities.put(v, intersection.size());	// Count the number of similar neighbors
		}
		
		// Return the 10 nodes with the most similar citations
		similarNodes = (HashMap<Integer, Integer>) similarities.entrySet().stream()
			.filter(p -> p.getValue() > 0)	// Remove nodes with 0 matches
			.sorted(Collections.reverseOrder(HashMap.Entry.comparingByValue())) // Reverse sort by value
			.limit(10)	// Select only the first 10
			.collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, // Keep the number of matches for testing purposes
	                (e1, e2) -> e1, 
	                LinkedHashMap::new // Preserve the order 
	              ));
		
		return similarNodes;
	}
	
	
	/** Helper method for constructing the bfs-found path from start to goal
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param parentMap Map linking nodes to their parent in path
	 * @return the path of Integers from start to goal
	 */
	private List<Integer> constructPath(Integer start, Integer goal, 
			HashMap<Integer, Integer> parentMap) {

		LinkedList<Integer> path = new LinkedList<Integer>();
		Integer curr = goal;
		while (!curr.equals(start)) {
			path.addFirst(curr);

//			// Increase the betweenness for the edge from curr to parent
//			Edge e = findEdge(parentMap.get(curr), curr);
//			e.setBetweenness(e.getBetweenness()+1);
//			System.out.println(e);
			
			curr = parentMap.get(curr);
		}
		path.addFirst(start);
		return path;
	}
	
//	private Edge findEdge(Integer from, Integer to) {
//		for (Edge edge : edges) {
//			if(edge.getNumTo()==from && edge.getNumFrom()==to) {
//				return edge;
//			}
//		}
//		return null;
//	}

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting vertex
	 * @param goal The goal vertex
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<Integer> bfs(int start, int goal) {

		HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
		
		// Search for a path using BFS
		if(!bfsSearch(start, goal, parentMap)) {
			return null;
		}
			
		// reconstruct the path
		return constructPath(start, goal, parentMap);
	}
	
	/** Helper method for searching for a path using BFS
	 * 
	 * @param start The starting vertex
	 * @param goal The goal vertex
	 * @param parentMap Map linking nodes to their parent in path
	 * @return boolean indicating whether a path was found using BFS
	 */
	private boolean bfsSearch(Integer start, Integer goal, 
			HashMap<Integer, Integer> parentMap) {
		
		boolean found = false;
		Queue<Integer> toVisit = new LinkedList<Integer>();
		HashSet<Integer> visited = new HashSet<Integer>();
		
		toVisit.add(start);
		
		while (!toVisit.isEmpty()) {
			Integer curr = toVisit.remove();
			
			if (curr.equals(goal)) {
				found = true;
				break;
			}
			for (Integer neighbor : vertices.get(curr)) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, curr);
					toVisit.add(neighbor);
				}
			}
		}
		return found;
	}
	
//	/** Find the path from start to goal using Dijkstra's algorithm
//	 * 
//	 * @param start The starting location
//	 * @param goal The goal location
//	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
//	 * @return The list of intersections that form the shortest path from 
//	 *   start to goal (including both start and goal).
//	 */
//	public List<Integer> dijkstra(Integer start, 
//										  Integer goal, Consumer<Integer> nodeSearched)
//	{
//		HashMap<Integer, Integer> shortestPathDistances;
//		
//		Comparator<Integer> comparator = new MapNodeComparatorAStar();
//		PriorityQueue<Integer> minHeap=new PriorityQueue<Integer>();
//		
//		minHeap.add(start);
//		
//		HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
//		
//		for (Vertex node : vertices.values()) {
//			node.setSearchDistance(Double.POSITIVE_INFINITY);
//			node.setEstimatedDistanceFromGoal(0);
//		}		
//		
//		// Search for a path using Dijkstra's algorithm
//		if(!aStarSearchSearch(start, goal, parentMap, nodeSearched)) {
//			return null;
//		}
//		
//		System.out.println("Nodes visited with Dijkstra: "+nodeVisitCount);
//		
//		// reconstruct the path
//		return constructPath(start, goal, parentMap);
//	}
//	
//	/** Find the path from start to goal using A-Star search
//	 * 
//	 * @param start The starting location
//	 * @param goal The goal location
//	 * @param parentMap Map linking nodes to their parent in path
//	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
//	 * @return boolean indicating whether a path was found using A* Search
//	 */
//	private boolean aStarSearchSearch(Integer start, Integer goal, 
//			HashMap<Integer, Integer> parentMap, Consumer<Integer> nodeSearched) {
//
//		boolean found = false;
//		
//		// Initialize
//		Comparator<Vertex> comparator = new VertexComparatorAStar();
//		PriorityQueue<Vertex> toVisit = new PriorityQueue<Vertex>(comparator);
//		HashSet<Vertex> visited = new HashSet<Vertex>();
//
//		Vertex startNode = vertices.get(start);
//		Vertex goalNode = vertices.get(goal);
//		startNode.setSearchDistance(0);
//		toVisit.add(startNode);
//		
//		nodeVisitCount = 0;
//		
//		// Search
//		while (!toVisit.isEmpty()) {
//			
//			// dequeue node curr from front of queue
//			Vertex curr = toVisit.remove();
//
//			// Testing number of visited nodes
////			System.out.println("[A*] "+curr);
//			nodeVisitCount++;
//			
//			// hook for visualization
//			nodeSearched.accept(curr.getLocation());
//			
//			if (!visited.contains(curr)) {
//				// add curr to visited set
//				visited.add(curr);
//				
//				// if curr is our goal
//				if (curr.equals(goalNode)) {
//					found = true;
//					break;
//				}
//				
//				// for each of curr's neighbors not in visited set:
//				for (Edge neighbor : curr.getEdges()) {
//					if (!visited.contains(neighbor.getLocationEnd())) {
//						
//						Vertex neighborNode = vertices.get(neighbor.getLocationEnd());
//						
////						System.out.println("[A*] Neighbor: "+neighborNode);					
//						double pathDistanceToNeighbor = curr.getSearchDistance() + neighbor.getDistance();
//
//						// If path through curr to neighbor is shorter
//						if(pathDistanceToNeighbor < neighborNode.getSearchDistance()) {
//							
//							// Update curr as neighbor's parent in parent map
//							parentMap.put(neighborNode.getLocation(), curr.getLocation());
//							
//							// Enqueue {neighbor,distance} into the PQ
//							neighborNode.setSearchDistance(pathDistanceToNeighbor);
//							toVisit.add(neighborNode);
//						}
//					}
//				}
//			}
//		}
//		return found;
//	}
	
	public static void main (String[] args) {
		long startTime, endTime, duration;
		
		
		CapGraph testGraph = new CapGraph();
		
		System.out.print("Loading graph...");
		startTime = System.nanoTime();
//		GraphLoader.loadGraph(testGraph, "data/gscholar_test.txt");
		GraphLoader.loadGraph(testGraph, "data/subset_test.txt");
//		GraphLoader.loadGraph(testGraph, "data/gscholar_lite.txt");
//		GraphLoader.loadGraph(testGraph, "data/gscholar.txt");
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.print(" DONE: "+duration+" ms\n");
		
//		testGraph.exportGraph();
		
		startTime = System.nanoTime();
		testGraph.exportGraph();
//		System.out.println(testGraph.getSimilar(11314));
//		System.out.println(testGraph.bfs(1, 5));
//		System.out.println(testGraph.bfs(11314, 73079));
		/* Expected output:
		Papers similar to paper: 11314...	{73079=8, 18802=7, 73346=7, 80782=7, 3752=6, 11899=6, 16371=6, 72968=6, 25793=5, 31031=5}
		*/
		
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
