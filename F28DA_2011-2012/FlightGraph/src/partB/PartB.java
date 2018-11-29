package partB;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class PartB {
	
	// Empty constructor to ensure class cannot be instantiated
	private PartB(){}
	
	public static void main(String arg[]){	
		
		// Create new graph object; directed graph with vertices (airports) represented as String objects for the name of the airport
		// and Edges as DefaultWeightedEdge objects, with weight (double) representing the cost of the flight
		SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> airportGraph = new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		// Add airport and route data to the graph
		addRoutes(airportGraph);
		
		// Display graph as string
		System.out.println(airportGraph.toString());
	}
	
	public static void addRoutes(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> airportGraph) {

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
		Graphs.addEdge(airportGraph, "Edinburgh", "Heathrow", 100);
		Graphs.addEdge(airportGraph, "Heathrow", "Amsterdam", 120);
		Graphs.addEdge(airportGraph, "Heathrow", "Boston", 230);
		Graphs.addEdge(airportGraph, "Boston", "Pittsburgh", 80);
		Graphs.addEdge(airportGraph, "Boston", "Montreal", 110);
		Graphs.addEdge(airportGraph, "Montreal", "Toronto", 70);
		Graphs.addEdge(airportGraph, "Edinburgh", "Pittsburgh", 560);
		Graphs.addEdge(airportGraph, "Delhi", "Hong Kong", 130);
		Graphs.addEdge(airportGraph, "Delhi", "Mumbai", 230);
	}
}
