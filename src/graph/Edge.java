package graph;

public class Edge {
	private int numTo;
	private int numFrom;
//	private Vertex vertexTo;
//	private Vertex vertexFrom;

	public Edge(int from, int to) {
		this.numFrom = from;
		this.numTo = to;
	}
	
	public Edge(Vertex vTo, Vertex vFrom) {
		this.numTo = vTo.getNum();
		this.numFrom = vFrom.getNum();
//		this.vertexTo = vTo;
//		this.vertexFrom = vFrom;
	}

	public int getNumTo() {
		return this.numTo;
	}
	
	public void setNumTo(int v) {
		this.numTo = v;
	}
	
	public int getNumFrom() {
		return this.numFrom;
	}
	
	public void setNumFrom(int v) {
		this.numFrom = v;
	}
	
//	public Vertex getVertexTo() {
//		return this.vertexTo;
//	}
//	
//	public void setVertexTo(Vertex v) {
//		this.vertexTo = v;
//	}
//	
//	public Vertex getVertexFrom() {
//		return this.vertexFrom;
//	}
//	
//	public void setVertexFrom(Vertex v) {
//		this.vertexFrom = v;
//	}
	
	@Override
	public String toString() {
		return "E: "+this.getNumFrom()+", "+this.getNumTo();
	}
}
