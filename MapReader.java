package project3;

//Name: Duong Phan

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class MapReader {

	// Read a formatted file and return a map
	public static MapGraph readMapFile(String FileName) throws FileNotFoundException {

		File file = new File(FileName);
		Scanner fileScanner = new Scanner(file);

		// Create a graph
		MapGraph graph = new MapGraph(0);

		double max_x = -Double.MAX_VALUE;
		double max_y = -Double.MAX_VALUE;
		double min_x = Double.MAX_VALUE;
		double min_y = Double.MAX_VALUE;

		while (fileScanner.hasNextLine()) {

			String line = fileScanner.nextLine();
			String[] words = line.trim().split("\\s+");

			if (words[0].equals("i")) {
				String ID = words[1]; // Read in Intersection's ID

				double latitude = Double.valueOf(words[2]); // Read in y coordinator
				double longitude = Double.valueOf(words[3]); // Read in x coordinator

				if (longitude < min_x)
					min_x = longitude;
				else if (longitude > max_x) {
					max_x = longitude;
				}
				if (latitude < min_y)
					min_y = latitude;
				else if (latitude > max_y)
					max_y = latitude;

				// Create a new vertex
				Vertex vertex = new Vertex(ID, longitude, latitude);
				graph.insert(ID, vertex);

			} else {

				String ID = words[1];// Road ID

				String v1_ID = words[2]; // Read in first intersection's ID
				String v2_ID = words[3]; // Read in second intersection's ID

				Vertex v1 = graph.vertices.get(v1_ID);
				Vertex v2 = graph.vertices.get(v2_ID);
				double len = MapGraph.findDis(v1.x, v1.y, v2.x, v2.y);

				Edge e = new Edge(v1, v2, len);

				graph.insert(e);
				graph.edges.put(ID, e);
			}

		}
		// Store the edges and vertices value to the graph

		graph.max_x = max_x;
		graph.max_y = max_y;
		graph.min_x = min_x;
		graph.min_y = min_y;

		fileScanner.close();

		return graph;
	}

	public static void main(String[] args) throws FileNotFoundException {

		MapGraph graph = readMapFile("C:\\Users\\phanh\\Downloads\\CSC172SP15_project4_data(1)\\ur.txt");
//		Vertex v1 = graph.vertices.get("SUEB");
//		Vertex v2 = graph.vertices.get("RUSH-RHEES");
//		graph.printShortestPath(v1, v2);
//		System.out.println(graph.vertices());
		graph.MST();
//		
	}

}
