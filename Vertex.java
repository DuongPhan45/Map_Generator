package project3;

//Name: Duong Phan

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Vertex implements Comparable<Vertex> {
	public String ID;
	public int indexLabel;
	public double x; // longitude
	public double y;// latitude
	double distance;
	boolean settled;
	int rank; // Number of vertices have this as head
	Vertex lastVertex;// the last vertex that lead to this in finding shortest path
	Vertex head; // the head vertex that represents a set in Union Find
	public HashMap<String, Vertex> neighbor;// Store all the neighbors of the vertex

	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
		neighbor = new HashMap<String, Vertex>();
		head = this; // Initiate the Vertex itself as its head
		rank = 0;// Initial rank

	}

	public Vertex(String ID, double x, double y) {
		this(x, y);
		this.ID = ID;
	}

	@Override
	public int compareTo(Vertex v) {
		if (distance < v.distance)
			return -1;
		else if (distance > v.distance)
			return 1;
		return 0;
	}

}
