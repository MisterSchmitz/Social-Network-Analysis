package graph;

import java.util.HashSet;

public class Vertex {
	private int num;
	private HashSet<Vertex> neighbors;
	
	public Vertex(int v) {
		this.num = v;
		this.neighbors = new HashSet<Vertex>();
	}
	
	public int getNum() {
		return this.num;
	}
	
	public void setNum(int v) {
		this.num = v;
	}
	
	public HashSet<Vertex> getNeighbors() {
		return this.neighbors;
	}
	
	public void addNeighbor(Vertex v) {
		this.neighbors.add(v);
	}
	
	@Override
	public String toString() {
		return "V: "+this.getNum();
	}
}
