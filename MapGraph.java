package project3;

//Name: Duong Phan

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

// Undirected Graph represent a geographic map
public class MapGraph {
	static final int R = 3959; // Radius of the Earth in miles
	private int verticeCount, edgeCount;
	public HashMap<String, Vertex> vertices; // Store the value of vertices
	public HashMap<String, Edge> edges; // Store the value of edges
	double max_x, max_y, min_x, min_y; // Max and min values of given vertices

// Create a empty Graph with given number of vertices
	public MapGraph(int verCount) {
		if (verCount < 0)
			throw new InvalidParameterException("Negative vertice number");
		this.verticeCount = verCount;
		vertices = new HashMap<String, Vertex>();
		edges = new HashMap<String, Edge>();

	}

// Return number of vertices
	public int vertices() {
		return verticeCount;
	}

// Return number of edges
	public int edges() {
		return edgeCount;
	}

// Add a vertex to the graph
	public void insert(String ID, Vertex v) {
		verticeCount++;
		// Return if the vertex already exists
		if (vertices.containsKey(v.ID))
			return;
		vertices.put(ID, v);
	}

// Add an edge to the graph
	public void insert(Edge e) {
		// Return if the edge already exists
		if (e.v1.neighbor.containsKey(e.v2.ID))
			return;

		e.v1.neighbor.put(e.v2.ID, e.v2);

		// Add edge v2-v1 in undirected graph
		e.v2.neighbor.put(e.v1.ID, e.v1);

		// Update edgeCount
		edgeCount++;
	}

	// Remove an edge
	public void delete(Edge e) {
		// Remove if the edge exists
		if (e.v1.neighbor.containsKey(e.v2.ID))
			edgeCount--;
		e.v1.neighbor.remove(e.v2.ID);
		// Remove edge v2-v1 in undirected graph
		e.v2.neighbor.remove(e.v1.ID);
	}

	// Return whether two vertices are connected
	public boolean isConnected(Vertex v1, Vertex v2) {
		return v1.neighbor.containsKey(v2.ID);
	}

	// Find the shortest path between two vertices
	public boolean findShortestPath(Vertex start, Vertex end) {

		// Set all distance from start to max value
		for (Vertex v : vertices.values()) {
			v.distance = Integer.MAX_VALUE;
			v.settled = false;
		}
		start.distance = 0;

		// Add start to unsettled nodes set
		PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
		queue.add(start);
		boolean connected = false;
		int settledCount = 0; // number of settled vertex
		// Do while loop until all vertices are settled
		while (settledCount != verticeCount) {

			if (queue.isEmpty())
				return connected;
			// Pick vertex with smallest distance from queue, ignore if it is settled
			Vertex current = queue.poll();
			while (current.settled) {
				if (queue.isEmpty())
					return connected;
				current = queue.poll();
			}
			current.settled = true;
			settledCount++;

			// Evaluate the vertex's neighbor, keep the mallest distance
			for (Vertex v : current.neighbor.values()) {
				if (!v.settled) {
					// Calculate the path length between two vertices
					double pathLen = findDis(current.x, current.y, v.x, v.y);

					if (current.distance + pathLen < v.distance) {
						v.distance = current.distance + pathLen;
						v.lastVertex = current;
					}
					if (v.equals(end))
						connected = true;
					// Add the updated neighbors to the queue
					queue.add(v);
				}
			}
		}
		return connected;
	}

	// Find distance in miles between two vertices using Haversine Formula
	public static double findDis(double x1, double y1, double x2, double y2) {
		double latDistance = Math.toRadians(y2 - y1);
		double lonDistance = Math.toRadians(x2 - x1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(y1))
				* Math.cos(Math.toRadians(y2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;
		return distance;
	}

	public ArrayList<Vertex> printShortestPath(Vertex start, Vertex end) {

		boolean connected = findShortestPath(start, end);
		if (!connected) {
			System.out.println("Two locations are not connected");
			return null;
		}
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		// Print the shortest path
		System.out.printf(start.ID + " to " + end.ID + " : ");

		Vertex v = end.lastVertex;
		path.add(end);
		while (v != null) {
			path.add(v);
			v = v.lastVertex;
		}

		for (int i = path.size() - 1; i > 0; i--) {
			System.out.printf("%-5s --->   ", path.get(i).ID);
		}
		System.out.printf("%-5s", end.ID);
		System.out.printf("\nTotal path length:%.4f miles\n ", end.distance);
		return path;
	}

	// Find which set a vertex belongs to (use path compression technique)
	public Vertex find(Vertex v) {
		if (!v.head.equals(v))
			v.head = find(v.head);
		return v.head;
	}

	// Union two sets of v1 and v2
	public void union(Vertex v1, Vertex v2) {

		// Attach the set with lower rank to the head of the set with higher rank
		if (v1.head.rank < v2.head.rank) {
			v1.head.head = v2.head;
			v2.head.rank += v1.head.rank;
		} else if (v1.head.rank > v2.head.rank) {
			v2.head.head = v1.head;
			v1.head.rank += v2.head.rank;
		} else {
			v2.head.head = v1.head;
			v1.head.rank++;
		}
	}

	// Find the minimum spanning tree using kruskal's algorithm
	public Edge[] MST() {
		Edge[] spt = new Edge[verticeCount - 1];// spanning tree with V-1 edges
		// Array stores the edges
		ArrayList<Edge> edgeArr = new ArrayList<Edge>(edges.values());
		// Sort the edge array to non decreasing order
		edgeArr.sort(null);

		int i = 0;// index of edge to evaluate below
		int sptIndex = 0; // index of the current edge in spt
		// i is smaller than edgeArr.size since in case the map is not connected,
		// sptIndex will always be smaller than verticeCount-1
		while (sptIndex < verticeCount - 1 && i < edgeArr.size()) {
			// Pick the smallest edge and the increment the index
			Edge current = edgeArr.get(i++);

			Vertex head1 = find(current.v1);
			Vertex head2 = find(current.v2);

			// If this new edge does not create a cycle then include it and include the
			// index of the spt for the next edge
			if (!head1.equals(head2)) {
				spt[sptIndex++] = current;
				union(head1, head2);
			}
			// Else ignore this edge
		}
		// Print the spanning tree
		System.out.println("Spanning tree: ");
		for (Edge e : spt) {
			if (e != null) {
				System.out.printf("%-5s ---> %-5s \n ", e.v1.ID, e.v2.ID);
			}
		}
		return spt;
	}

}
