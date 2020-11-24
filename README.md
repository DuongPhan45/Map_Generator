# Map_Generator
A rudimentary mapping program in Java. The program could :
- Plot a map given a data set representing the roads and intersections in a specific geographic region
- Provide shortest path directions between any two arbitrary intersections chosen by the user using Dijkstra's algorithm
- Generate the minimum weight spanning tree for the entire map

## View
<img src="https://user-images.githubusercontent.com/47710293/100124353-8bed3100-2e49-11eb-94c9-6b7461e04d98.png" width = "700" height = "700">
<img src="https://user-images.githubusercontent.com/47710293/100124125-3f095a80-2e49-11eb-8160-d368acf4cccc.png" width = "400" height = "400">

## Java Files
The repo contains five java files, corresponding to five classes: 
- Egdge.java
- MapGraph.java
- Vertex.java
- MapReader.java : contains all methods to generate spanning trees, find shortest path, etc.
- GraphicMap.java: read in a txt file and draw map; generate a minimum spanning tree in red color
GraphicMap is the main executable class.

## Big O analysis:
- MapReader Class: readMapFile(): O(V+E)
- MapGraph Class:
	+ insert(), delete(), isConnected: O(1)
	+ findShortestPath(): O(VlogV)
	+ spt(): O(ElogE)
  
---> Total big O(nlogn)
