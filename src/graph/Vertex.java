package graph;

public class Vertex {
	private int num;
//	private String title;
	
	public Vertex(int v) {
		this.num = v;
	}
	
	public int getNum() {
		return this.num;
	}
	
	public void setNum(int v) {
		this.num = v;
	}
		
//	public String getTitle() {
//		return this.title;
//	}
//	
//	public void setTitle(String s) {
//		this.title = s;
//	}
//	
	@Override
	public String toString() {
		return "V: "+this.getNum();
	}
}
