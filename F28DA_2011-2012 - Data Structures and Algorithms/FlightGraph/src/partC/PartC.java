package partC;
 
import java.util.List;
import java.util.Scanner;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
 
public class PartC {
	// Create new graph object; directed graph with vertices (airports) represented as String objects for the name of the airport
	// and Edges as DefaultWeightedEdge objects, with weight (double) representing the cost of the flight
	static SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> airportGraph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	
	// Empty constructor to ensure class cannot be instantiated
	private PartC(){}
 
	public static void main(String arg[]){
		// First-run stuff
		init();
		// Start main functionality
		calculateRoutes();
	}

	private static void calculateRoutes() {
		// Load scanner to detect user keyboard input
		Scanner scanner = new Scanner(System.in);
		// Display usage instructions; prompt for input
		System.out.println("\nEnter origin and destination airports separated by a space; then press enter: [Example: Edinburgh Heathrow]");
		// Read keyboard input, split on any number of whitespace characters - usually a single space
		String[] input = scanner.nextLine().split("\\s+");
		// Check input for the keyword exit, quit program
		if(input[0].contains("exit")) quit(new String[]{});
		// If first input string is not valid airport, display message and restart method
		if(!airportGraph.containsVertex(input[0])) restart(new String[]{input[0]+" isn't a valid origin."});
		// If first input string is not valid airport, display message and restart method
		if(!airportGraph.containsVertex(input[1])) restart(new String[]{input[1]+" isn't a valid destination."});
		// Utilise the Dijkstra class to find the shortest path, giving it our graph, origin, destination
		DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<String, DefaultWeightedEdge>(airportGraph, input[0], input[1]);
		// Get list of edges which make up the shortest path
		List<DefaultWeightedEdge> list = path.getPathEdgeList();
		// If list of edges in the shortest path is null, there is no path; display message and restart 
		if(list==null) restart(new String[]{"There are no connecting flights from "+input[0]+" to "+input[1]+"."});
		
		// Print list of flights which make up the cheapest route
		System.out.println(list);
		// Display total cost and quit program. Total cost is Dijkstra class's getPathLength method
		quit(new String[]{"Total journey cost: "+path.getPathLength()});
	}
	
	private static void init() {
		// Add airport and route data to the graph
		addRoutes();
		// Display startup message
		System.out.println("Cheapest Flight Path Calculator: version 1\n\nAirports available:");
		// For each vertex in the graph, print the name
		for(String airportName: airportGraph.vertexSet())
			System.out.println(" • "+airportName);		
	}

	private static void restart(String[] errorMessages) {
		// Print any optional error message(s) line by line
		for(String errorMessage : errorMessages) {
			System.out.println(errorMessage);
		}
		System.out.println("Please try again or type 'exit' to end the program.");
		// Restart main functionality
		calculateRoutes();
	}

	private static void quit(String[] errorMessages) {
		// Print any optional error message(s) line by line
		for(String errorMessage : errorMessages) {
			System.out.println(errorMessage);
		}
		// Print one final goodbye message
		System.out.println("Thankyou for using this program. Goodbye");
		System.exit(0);
	}

	public static void addRoutes() {		
		// Add airports (vertices) to graph
		airportGraph.addVertex("Edinburgh");
		airportGraph.addVertex("Hong Kong");
		airportGraph.addVertex("Mumbai");
		airportGraph.addVertex("Delhi");
		airportGraph.addVertex("Toronto");
		airportGraph.addVertex("Montreal");
		airportGraph.addVertex("Pittsburgh");
		airportGraph.addVertex("Boston");
		airportGraph.addVertex("Amsterdam");
		airportGraph.addVertex("Heathrow");
		
		// Add possible routes to graph; addEdge takes the graph var, start vertex, end vertex and weight (cost) of route
		// Now with return journeys!
		Graphs.addEdge(airportGraph, "Edinburgh", "Heathrow", 100);
		Graphs.addEdge(airportGraph, "Heathrow", "Amsterdam", 120);
		Graphs.addEdge(airportGraph, "Heathrow", "Boston", 230);
		Graphs.addEdge(airportGraph, "Boston", "Pittsburgh", 80);
		Graphs.addEdge(airportGraph, "Boston", "Montreal", 110);
		Graphs.addEdge(airportGraph, "Montreal", "Toronto", 70);
		Graphs.addEdge(airportGraph, "Edinburgh", "Pittsburgh", 560);
		Graphs.addEdge(airportGraph, "Delhi", "Hong Kong", 130);
		Graphs.addEdge(airportGraph, "Delhi", "Mumbai", 230);
		Graphs.addEdge(airportGraph, "Heathrow", "Edinburgh", 100);
		Graphs.addEdge(airportGraph, "Amsterdam", "Heathrow", 120);
		Graphs.addEdge(airportGraph, "Boston", "Heathrow", 230);
		Graphs.addEdge(airportGraph, "Pittsburgh", "Boston", 80);
		Graphs.addEdge(airportGraph, "Montreal", "Boston", 110);
		Graphs.addEdge(airportGraph, "Toronto", "Montreal", 70);
		Graphs.addEdge(airportGraph, "Pittsburgh", "Edinburgh", 560);
		Graphs.addEdge(airportGraph, "Hong Kong", "Delhi", 130);
		Graphs.addEdge(airportGraph, "Mumbai", "Delhi", 230);
	}

}