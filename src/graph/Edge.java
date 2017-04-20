package graph;

public class Edge {
	private Vertex vertexIn;
	private Vertex vertexOut;
	
	public Edge(Vertex vIn, Vertex vOut) {
		this.vertexIn = vIn;
		this.vertexOut = vOut;
	}
	
	public Vertex getVertexIn() {
		return this.vertexIn;
	}
	
	public void setVertexIn(Vertex v) {
		this.vertexIn = v;
	}
	
	public Vertex getVertexOut() {
		return this.vertexOut;
	}
	
	public void setVertexOut(Vertex v) {
		this.vertexOut = v;
	}
	
	@Override
	public String toString() {
		return "E: "+this.getVertexIn()+", "+this.getVertexOut();
	}
}
