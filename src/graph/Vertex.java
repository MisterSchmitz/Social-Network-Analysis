package graph;

public class Vertex {
	private int num;
	
	public Vertex(int v) {
		this.num = v;
	}
	
	public int getNum() {
		return this.num;
	}
	
	public void setNum(int v) {
		this.num = v;
	}
	
	@Override
	public String toString() {
		return "V: "+this.getNum();
	}
}
