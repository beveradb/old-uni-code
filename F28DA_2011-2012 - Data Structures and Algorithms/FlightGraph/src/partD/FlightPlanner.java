package partD;
 
import java.util.List;
import java.util.Scanner;
import org.jgrapht.alg.DijkstraShortestPath;
import partD.Flight;
import partD.Flights; 

public class FlightPlanner {
	/* Create new graph object; directed graph with vertices (airports) represented as String objects for the name of the airport
	   and Edges as DefaultWeightedEdge objects, with weight (double) representing the cost of the flight
	   Flight.class means the Flight class itself, not an obejct instance of the class */
	static Flights routeGraph = new Flights();
	// Load scanner to detect user keyboard input
	static Scanner scanner = new Scanner(System.in);
	
	// Empty constructor to ensure class cannot be instantiated
	private FlightPlanner(){}
 
	public static void main(String arg[]){
		// First-run stuff
		init();
		// Start main functionality
		calculateRoutes();
	}

	private static void calculateRoutes() {
		// Display usage instructions; prompt for input
		System.out.println("\nEnter origin and destination airports separated by a space; then press enter: [Example: Edinburgh Heathrow]");
		// Read keyboard input, split on any number of whitespace characters - usually a single space
		String[] input = scanner.nextLine().split("\\s+");
		// Check input for the keyword exit, quit program
		if(input[0].contains("exit")) quit(new String[]{});
		// If first input string is not valid airport, display message and restart method
		if(!routeGraph.containsVertex(input[0])) restart(new String[]{input[0]+" isn't a valid origin."});
		// If only one string entered, display message and restart method
		if(input.length<2) restart(new String[]{"Please enter a valid destination."});
		// If first input string is not valid airport, display message and restart method
		if(!routeGraph.containsVertex(input[1])) restart(new String[]{input[1]+" isn't a valid destination."});
		// Utilise the Dijkstra class to find the shortest path, giving it our graph, origin, destination
		DijkstraShortestPath<String, Flight> path = new DijkstraShortestPath<String, Flight>(routeGraph, input[0], input[1]);
		// Get list of edges which make up the shortest path
		List<Flight> list = path.getPathEdgeList();
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
		for(String airportName: routeGraph.vertexSet())
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
		System.out.println(routeGraph.toString());
		System.exit(0);
	}

	public static void addRoutes() {		
		// Add airports (vertices) to graph						
		routeGraph.addVertex("Edinburgh");
		routeGraph.addVertex("Hong Kong");
		routeGraph.addVertex("Mumbai");
		routeGraph.addVertex("Delhi");
		routeGraph.addVertex("Toronto");
		routeGraph.addVertex("Montreal");
		routeGraph.addVertex("Pittsburgh");
		routeGraph.addVertex("Boston");
		routeGraph.addVertex("Amsterdam");
		routeGraph.addVertex("Heathrow");
		routeGraph.addVertex("Bombay");
		
		// Add possible routes to graph; addFlight takes origin and destination airports, 
		// departure and arrival times, flight code and cost.
		routeGraph.addFlight("Edinburgh", "Heathrow", 1000, 1055, "BA907", 100);
		routeGraph.addFlight("Heathrow", "Amsterdam", 1135, 1230, "BA555", 120);
		routeGraph.addFlight("Heathrow", "Boston", 800, 1545, "BA951", 230);
		routeGraph.addFlight("Boston", "Pittsburgh", 915, 1015, "DA441", 80);
		routeGraph.addFlight("Boston", "Montreal", 1235, 1445, "DA568", 110);
		routeGraph.addFlight("Montreal", "Toronto", 945, 1120, "CA332", 70);
		routeGraph.addFlight("Edinburgh", "Pittsburgh", 855, 1705, "DA879", 560);
		routeGraph.addFlight("Delhi", "Hong Kong", 1015, 1415, "IN117", 130);
		routeGraph.addFlight("Delhi", "Bombay", 1135, 1435, "IN321", 230);
		routeGraph.addFlight("Amsterdam", "Heathrow", 1000, 1055, "BA903", 100);
		routeGraph.addFlight("Heathrow", "Edinburgh", 1135, 1230, "BA556", 120);
		routeGraph.addFlight("Pittsburgh", "Boston", 800, 1545, "BA952", 230);
		routeGraph.addFlight("Boston", "Heathrow", 915, 1015, "DA440", 80);
		routeGraph.addFlight("Montreal", "Boston", 1235, 1445, "DA569", 110);
		routeGraph.addFlight("Toronto", "Montreal", 945, 1120, "CA333", 70);
		routeGraph.addFlight("Pittsburgh", "Edinburgh", 855, 1705, "DA871", 560);
		routeGraph.addFlight("Hong Kong", "Delhi", 1015, 1415, "IN118", 130);
		routeGraph.addFlight("Bombay", "Delhi", 1135, 1435, "IN320", 230);
		
	}

}