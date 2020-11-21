package project3;

//Name: Duong Phan

public class Edge implements Comparable<Edge> {
	public String ID;
	public Vertex v1, v2;
	public double len;

	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public Edge(String ID, Vertex v1, Vertex v2) {
		this(v1, v2);
		this.ID = ID;
	}

	public Edge(Vertex v1, Vertex v2, double len) {
		this(v1, v2);
		this.len = len;
	}

	@Override
	public int compareTo(Edge e) {
		if (len > e.len)
			return 1;
		else if (len < e.len)
			return -1;
		else {
			return 0;
		}

	}
}
