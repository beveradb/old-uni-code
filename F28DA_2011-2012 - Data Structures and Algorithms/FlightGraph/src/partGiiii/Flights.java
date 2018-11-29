package partGiiii;
import org.joda.time.DateTime;

/* Flights class is basically a SimpleDirectedWeightedGraph with */
public class Flights extends org.jgrapht.graph.SimpleDirectedWeightedGraph<String,Flight> {

	// Silly java serialization. Don't understand it, but Eclipse is a moany little IDE.
	private static final long serialVersionUID = 1L;

	// Constructor autocreated by Eclipse to fix error message. Not really too sure of the inheritance issues here, but it works.
	public Flights() {
		super(Flight.class);
	}	
 
	/* Meat of the class - enables adding flights. Requires origin and destination airports,
	   departure and arrival time, flight code and cost */
	public void addFlight(String origin, String destination, DateTime departure, DateTime arrival, String code, double weight) {
		// Create new edge using EdgeFactory, with capacity for more data in variables defined in Flight
		Flight flight = getEdgeFactory().createEdge(origin, destination);
		// Set flight extra data values
		flight.setWeight(weight);
		flight.setDeparture(departure);
		flight.setArrival(arrival);
		flight.setCode(code);
		// Add flight edge to graph
		addEdge(origin,destination,flight);
	}

}