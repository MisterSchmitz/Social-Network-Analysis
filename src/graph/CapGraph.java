/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
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
	private HashMap<Integer, HashSet<Integer>> verticesInNeighbors;
	private HashSet<Edge> edges;
	
	private int numVertices;
	private int numEdges;
	
	public CapGraph()
	{
		vertices = new HashMap<Integer, HashSet<Integer>>();
		edges = new HashSet<Edge>();
		verticesInNeighbors = new HashMap<Integer, HashSet<Integer>>();
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
		if(!verticesInNeighbors.containsKey(num)) {
			verticesInNeighbors.put(num, new HashSet<Integer>());
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
			numEdges++;
			Edge e = new Edge(from, to);
			edges.add(e);
		}
		
		// Add InNeighbor
		verticesInNeighbors.get(to).add(from);
		
		return;
	}
	
	/** Find the nodes which have an edge coming from a given node.
	 * 
	 * @param v The node for which to find the out-neighbors.
	 * @return HashSet of out-neighbor nodes
	 */
	public HashSet<Integer> getNeighbors(int v) {
		return vertices.get(v);
	}
	
	/** Find the nodes which have an edge coming into a given node.
	 * 
	 * @param v The node for which to find the in-neighbors.
	 * @return HashSet of in-neighbor nodes
	 */
	public HashSet<Integer> getInNeighbors(int v) {
		return verticesInNeighbors.get(v);
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
		System.out.println("numVertices: "+this.numVertices+"\tnumEdges: "+numEdges);
		return vertices;
	}
	
	/** Helper method used for testing. Prints all the nodes and edges for a graph.
	 * 
	 */
	public void exportGraphFlat() {
		for(Integer v : this.vertices.keySet()) {
			if(getNeighbors(v).size()==0) {
				System.out.println(v);
			} else {
				for(Integer n : getNeighbors(v)) {
					System.out.println(v+"\t"+n);		
				}
			}
		}
		System.out.println("numVertices: "+this.numVertices+"\tnumEdges: "+this.numEdges);
		return;
	}
	
	/** Identify 10 nodes most similar to a given node.
	 * 
	 * @param node The node for which to find similar nodes.
	 * @return HashMap of similar nodes as keys, with values representing number of similar citations between given node and key node.
	 */
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
			intersection.retainAll(nodeNeighbors);	// Find the intersection between node's neighbors and other node's neighbors
			similarities.put(v, intersection.size());	// Count the number of similar neighbors
		}
		
		// Return the 10 nodes with the most similar citations
		similarNodes = (HashMap<Integer, Integer>) similarities.entrySet().stream()
			.filter(p -> p.getValue() > 0)	// Remove nodes with 0 matches
			.sorted(Collections.reverseOrder(HashMap.Entry.comparingByValue())) // Reverse sort by value
			.limit(10)	// Select only the first 10
			.collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, // Keep the number of matches to more easily verify results
	                (e1, e2) -> e1, 
	                LinkedHashMap::new	 // Preserve the order
	              ));
		
		return similarNodes;
	}

	/** Identify communities in a graph.
	 * 
	 * @param numCommunities Desired maximum number of resulting communities
	 * @return List of CapGraphs which represent the different communities.
	 */
	public List<CapGraph> getCommunities(int numCommunities) {
		
		List<CapGraph> communities = new  ArrayList<CapGraph>();
		
		int splits = 1;
		
		Stack<Integer> rootNodes = new Stack<Integer>(); 
		
		// Initialize new graph
		CapGraph g = new CapGraph();
		for(Integer v : this.vertices.keySet()) {
			g.addVertex(v);
			for(Integer neighbor : vertices.get(v)) {
				g.addVertex(neighbor);
				g.addEdge(v, neighbor);
			}
		}
		
		// Do until we reach number of desired communities, or until there are no more edges
		while(splits < numCommunities && g.numEdges > 0) {
			// Compute the betweenness of all edges
			// For each node, v, distribute flow from v to all other nodes
			for (Integer v : g.vertices.keySet()) {
				for (Integer other : g.vertices.keySet()) {
					g.bfs(g, v, other);
				}
			}
			
			// Find and remove the Edge with the highest betweenness
			Edge e = g.getEdgeWithHighestBetweenness(g);
//			System.out.println("EdgeWithHighestBetweenness: "+e);
			int from = e.getNumFrom();
			int to = e.getNumTo();
			// Add nodes on either end of the edge to stack to return to later
			if(!rootNodes.contains(from)) {rootNodes.push(from);}
			if(!rootNodes.contains(to)) {rootNodes.push(to);}
			g.removeEdge(from, to);
						
			splits++;
		}
		
		while(!rootNodes.isEmpty()) {
			communities.add(g.getNodeReach(rootNodes.pop(), g.numVertices));	
		}
		
		return communities;
	}
	
	/** Helper method for getCommunities.
	 * 
	 * @param g the graph
	 * @return the Edge with the highest betweenness.
	 */
	private Edge getEdgeWithHighestBetweenness(CapGraph g) {
		int from=0;
		int to=0;
		int max = 0;
		for (Edge e : g.edges) {
			int b = e.getBetweenness();
			if (b > max) {
				from = e.getNumFrom();
				to = e.getNumTo();
				max = b;
			}
		}
		return g.findEdge(from, to);
	}

	/** Remove an edge from a given graph, given the from and to nodes.
	 * 
	 * @param from starting node for the Edge
	 * @param to ending node for the Edge
	 */
	private void removeEdge(int from, int to){
		// Verify that from and to vertices exist
		if(!vertices.containsKey(from) || !vertices.containsKey(to)) {
			return;
		}
		// Remove Edge
		if(getNeighbors(from).contains(to)) {
			getNeighbors(from).remove(to);
			this.numEdges--;
			edges.remove(this.findEdge(from, to));
		}
		
		// Remove InNeighbor
		verticesInNeighbors.get(to).remove(from);
		
		return;
	}
	
	/** Find the Edge object corresponding to two Integers.
	 * 
	 * @param from starting node for the Edge
	 * @param to ending node for the Edge
	 * @return The Edge corresponding to the given from and to integers
	 */
	private Edge findEdge(Integer from, Integer to) {
		for (Edge edge : edges) {
			if(edge.getNumTo()==to && edge.getNumFrom()==from) {
				return edge;
			}
		}
		return null;
	}
	
	/** Find all nodes connected to a given root node. Optionally, provide a rough node limit for the returned graph.
	 * 
	 * @param root The starting node
	 * @param roughGraphNodeLimit The rough size for the returned graph, in terms of nodes.
	 * @return A new graph, centered around the given root node. All nodes are connected to this node in some way.
	 */
	public CapGraph getNodeReach(int root, int roughGraphNodeLimit) {
		// Create a new graph
		CapGraph subGraph = new CapGraph();
		int node;
		
		// Initialize
		Stack<Integer> toVisit = new Stack<Integer>();
		toVisit.push(root);
		
		while (!toVisit.isEmpty() && subGraph.numVertices<roughGraphNodeLimit) {
			// Pull next node from the Stack
			node = toVisit.pop();
			subGraph.addVertex(node);
			
			// Add node's out-neighbors to subGraph and toVisit Stack
			HashSet<Integer> outNeighbors = this.getNeighbors(node);
			for(Integer n : outNeighbors) {
				if (!subGraph.vertices.containsKey(n)) {
					subGraph.addVertex(n);
					toVisit.push(n);
				}
				subGraph.addEdge(node, n);
			}
			
			// Add node's in-neighbors to subGraph and toVisit Stack
			HashSet<Integer> inNeighbors = this.getInNeighbors(node); 
			for(Integer n : inNeighbors) {
				if (!subGraph.vertices.containsKey(n)) {
					subGraph.addVertex(n);
					toVisit.push(n);
				}
				subGraph.addEdge(n, node);
			};
			
		}
		return subGraph;
	}


	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting vertex
	 * @param goal The goal vertex
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<Integer> bfs(CapGraph g, int start, int goal) {		
		HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();
		
		// Search for a path using BFS
		if(!bfsSearch(g, start, goal, parentMap)) {
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
	private boolean bfsSearch(CapGraph g, Integer start, Integer goal, 
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
			for (Integer neighbor : g.vertices.get(curr)) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, curr);
					toVisit.add(neighbor);
				}
			}
		}
		return found;
	}
	
	/** Helper method for constructing the bfs-found path from start to goal. Used for increasing an edge's "betweenness"
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

			// Increase the betweenness for the edge from curr to parent
			Edge e = findEdge(parentMap.get(curr), curr);
			e.setBetweenness(e.getBetweenness()+1);
			
			curr = parentMap.get(curr);
		}
		path.addFirst(start);
		return path;
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
		
		// Visit all vertexNodes, returning the �Finished� stack
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
		
		// Visit all vertexNodes in 'Finished' stack, returning the  new �Finished� stack
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
	
	public static void main (String[] args) {
		long startTime, endTime, duration;
		
		
		CapGraph testGraph = new CapGraph();
		
		System.out.print("Loading graph...");
		startTime = System.nanoTime();
//		GraphLoader.loadGraph(testGraph, "data/subset_test.txt");
//		GraphLoader.loadGraph(testGraph, "data/subset200_test.txt");
//		GraphLoader.loadGraph(testGraph, "data/gscholar_lite.txt");
		GraphLoader.loadGraph(testGraph, "data/gscholar.txt");
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.print(" DONE: "+duration+" ms\n");
		
		startTime = System.nanoTime();
//		CapGraph testSubGraph = testGraph.getNodeReach(11314, 1000);
//		testSubGraph.exportGraph();
		testGraph.getNodeReach(11314, testGraph.numVertices);
//		for (CapGraph g : testSubGraph.getCommunities(10)) { g.exportGraphFlat();};
//		System.out.println(testSubGraph.getSimilar(11314));
//		testSubGraph.exportGraphFlat();
//		testSubGraph.exportGraph();
//		System.out.println(testGraph.getConnected2(5));
//		System.out.println(testGraph.getSubGraph(73079));
//		testGraph.exportGraph();
//		System.out.println(testGraph.bfs(7, 12));
//		for (CapGraph g : testGraph.getCommunities(2)) { g.exportGraphFlat();};
		
//		System.out.println(testGraph.getNeighbors(73079));
//		System.out.println(testGraph.getInNeighbors(3));
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
