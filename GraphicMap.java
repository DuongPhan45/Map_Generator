package project3;

//Name: Duong Phan

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GraphicMap extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int HEIGHT = 600;
	static final int WIDTH = 600;
	MapGraph graph;
	ArrayList<Vertex> path;
	Edge[] mst;
	boolean connected = true;
	boolean doMst;

	// Method for print spanning tree
	public GraphicMap(String name, String method) throws FileNotFoundException {
		this.graph = MapReader.readMapFile(name);
		// Run the input method
		if (method.equals("spt"))
			doMst = true;
		mst = graph.MST();
	}

	// Method for print shortest path
	public GraphicMap(String name, String v1, String v2) throws FileNotFoundException {
		this.graph = MapReader.readMapFile(name);
		Vertex ver1 = graph.vertices.get(v1);
		Vertex ver2 = graph.vertices.get(v2);
		path = graph.printShortestPath(ver1, ver2);
		connected = (path != null);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// Draw the map
		g2.setColor(Color.BLACK);

		Collection<Edge> edges = graph.edges.values();

		for (Edge e : edges) {
			// Calculate the x coordinate in the graph
			int x1 = toX(e.v1.x);
			int x2 = toX(e.v2.x);

			// Calculate the y coordinate in the graph
			int y1 = toY(e.v1.y);
			int y2 = toY(e.v2.y);

			// Draw the road
			g2.drawLine(x1, y1, x2, y2);
		}

		// Draw the shortest path
		if ((!doMst) && connected) {
			g2.setColor(Color.RED);
			for (int i = 0; i < path.size() - 1; i++) {
				// Calculate the x coordinate in the graph
				int x1 = toX(path.get(i).x);
				int x2 = toX(path.get(i + 1).x);

				// Calculate the y coordinate in the graph
				int y1 = toY(path.get(i).y);
				int y2 = toY(path.get(i + 1).y);

				// Draw the road
				g.drawLine(x1, y1, x2, y2);

				// Draw the last and first points
				if (i == 0) {
					g2.drawString(path.get(i).ID, x1 - 10, y1 - 10);
					g2.fillOval(x1 - 3, y1 - 3, 8, 8);
				}
				if (i + 1 == path.size() - 1) {
					g2.drawString(path.get(i).ID, x2 - 10, y2 - 10);
					g2.fillOval(x2 - 3, y2 - 3, 8, 8);
				}
			}
		} else if (doMst) {
			// Draw minimum spanning tree path
			g2.setColor(Color.RED);
			for (int i = 0; i < mst.length; i++) {
				if (mst[i] != null) {
					// Calculate the x coordinate in the graph
					int x1 = toX(mst[i].v1.x);
					int x2 = toX(mst[i].v2.x);

					// Calculate the y coordinate in the graph
					int y1 = toY(mst[i].v1.y);
					int y2 = toY(mst[i].v2.y);

					// Draw the road
					g.drawLine(x1, y1, x2, y2);
				}
			}
		}
	}

// Return the x coordinate that correspond to the longitude
	public int toX(double x) {
		double dx = graph.max_x - graph.min_x;
		return (int) ((x - graph.min_x) * this.getWidth() / dx);
	}

	// Return the y coordinate that correspond to the latitude
	public int toY(double y) {
		double dy = graph.max_y - graph.min_y;
		return (int) (this.getHeight() - ((y - graph.min_y) * this.getHeight() / dy));
	}

	public static void main(String[] args) throws FileNotFoundException {

		JFrame frame = new JFrame("Map");
		frame.setSize(WIDTH, HEIGHT);

		// Input format: java ProgramName map.txt [-show] [-directions startIntersection
		// endIntersection] [-meridianmap]
		GraphicMap map;

		// args[1]: the map will show in any cases
		// args[2]: either show mst or shortest path
		if (args[2].equals("-meridianmap")) {
			map = new GraphicMap(args[0], "spt");
		} else {

			String v1 = args[3];
			String v2 = args[4];
			map = new GraphicMap(args[0], v1, v2);
		}

		frame.add(map);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
