package partGii;
 
import java.util.List;
import java.util.Scanner;
import org.jgrapht.alg.DijkstraShortestPath;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import partGii.Flights;

public class FlightPlanner {
	/* Create new graph object; directed graph with vertices (airports) represented as String objects for the name of the airport
	   and Edges as DefaultWeightedEdge objects, with weight (double) representing the cost of the flight
	   Flight.class means the Flight class itself, not an obejct instance of the class */
	static Flights routeGraph = new Flights();
	// Load scanner to detect user keyboard input
	static Scanner scanner = new Scanner(System.in);
	
	// Empty constructor to ensure class cannot be instantiated
	private FlightPlanner(){}
 
	public static void main(String arg[]) {
		// First-run stuff
		init();
		// Start main functionality
		calculateRoutes();
	}

	private static void calculateRoutes() {
		// Display usage instructions; prompt for input
		System.out.println("\nEnter origin and destination airports separated by a space; then press enter: [Example: edinburgh heathrow]");
		// Read keyboard input, split on any number of whitespace characters - usually a single space
		String[] input = scanner.nextLine().toLowerCase().split("\\s+");
		// Check input for the keyword exit, quit program
		if(input[0].contains("exit")) quit(new String[]{});
		// If first input string is not valid airport, display message and restart method
		if(!routeGraph.containsVertex(input[0])) restart(new String[]{input[0]+" isn't a valid origin."});
		// If only one string entered, display message and restart method
		if(input.length<2) restart(new String[]{"A destination was not entered."});
		// If first input string is not valid airport, display message and restart method
		if(!routeGraph.containsVertex(input[1])) restart(new String[]{input[1]+" isn't a valid destination."});
		// Utilise the Dijkstra class to find the shortest path, giving it our graph, origin, destination
		DijkstraShortestPath<String, Flight> path = new DijkstraShortestPath<String, Flight>(routeGraph, input[0], input[1]);
		// Get list of edges which make up the shortest path
		List<Flight> flightList = path.getPathEdgeList();
		// If list of edges in the shortest path is null, there is no path; display message and restart 
		if(flightList==null) restart(new String[]{"There are no connecting flights from "+input[0]+" to "+input[1]+"."});
		
		// Print list of flights which make up the cheapest route
		int fCount = 0;
		Period totalAirPeriod = new Period();
		Period totalChangeoverPeriod = new Period();
		Period totalJourneyTime = new Period();
		DateTime previousArrival = new DateTime();
		System.out.format("%3s%15s%7s%7s%15s%9s%12s", "\nLeg", "Leave", "At", "On", "Arrive", "At", "Cost (GBP)\n");
		for(Flight f : flightList) {
			// Counter used for flight leg
			fCount++;
			DateTime departure = f.getDeparture();
			DateTime arrival = f.getArrival();
			// If more than one leg, add period between previous flight arrival and current flight departure to totals
			if(fCount>1) {
				/* changePeriod is the time between the current flight departure and the previous arrival 
				 * since we are not date-aware (assuming the same date for every single flight) in this program, 
				 * we can get negative values from this. To remedy negative period we add 24 hours (1 day) whenever 
				 * we encounter a negative value.
				 */
				Period changePeriod = new Period(previousArrival, departure);
				if(changePeriod.getHours()<0) {
					totalChangeoverPeriod = totalChangeoverPeriod.plusDays(1);
				}
				totalChangeoverPeriod = totalChangeoverPeriod.plus(changePeriod);
				//System.out.println("test changePeriod: "+changePeriod.toString());
			}
			previousArrival = arrival;
			Period flightPeriod = new Period(departure, arrival);
			totalAirPeriod = totalAirPeriod.plus(flightPeriod);
			System.out.format("%3s%15s%7s%7s%15s%9s%12s", fCount, f.getOrigin(), departure.toString("HH:mm"), f.getCode(), f.getDestination(), arrival.toString("HH:mm"), "£"+Double.toString(f.getWeight())+"\n");
		}
		// Calculate total journey time including changeovers
		totalJourneyTime = totalAirPeriod.plus(totalChangeoverPeriod);
		/*  Display total cost and total period of time in the air. 
		 *  Total cost is simply jGrapht Dijkstra class's getPathLength method,
		 *  Total period of time in the air uses Joda's PeriodFormatter to deal with getting correct hours and minutes */ 
		PeriodFormatter hoursMinutes = new PeriodFormatterBuilder()
			.appendDays()
		    .appendSuffix(" day", " days")
		    .appendSeparator(", ")
			.appendHours()
		    .appendSuffix(" hour", " hours")
		    .appendSeparator(" and ")
			.appendMinutes()
		    .appendSuffix(" minute", " minutes")
			.toFormatter();
		
		/* Restart main functionality after displaying final itinerary data; 
		 * normalizedStandard function of JodaTime ensures period values returned match our globally recognised 60 minute hours etc.
		 */
		restart(new String[]{
			"",
			"Total Journey Cost: £"+path.getPathLength(),
			"Total Time in the Air: "+hoursMinutes.print( totalAirPeriod.normalizedStandard()),
			"Total Change Time: "+hoursMinutes.print( totalChangeoverPeriod.normalizedStandard() ),
			"Total Journey Time: "+hoursMinutes.print( totalJourneyTime.normalizedStandard() ),
			"",
			"Thankyou for using the Flight Planner;"
		});
	}
	
	private static void init() {
		// Add airport and route data to the graph
		addRoutes();
		// Display startup message
		System.out.println("Cheapest Flight Path Calculator: version 4.3\n\nAirports available:");
		// For each vertex in the graph, print the name
		for(String airportName: routeGraph.vertexSet())
			System.out.println(" • "+airportName);		
	}

	private static void restart(String[] errorMessages) {
		// Print any optional error message(s) line by line
		for(String errorMessage : errorMessages) {
			System.out.println(errorMessage);
		}
		System.out.println("Please enter new values or type 'exit' to end the program.");
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
		routeGraph.addVertex("edinburgh");
		routeGraph.addVertex("hong kong");
		routeGraph.addVertex("mumbai");
		routeGraph.addVertex("delhi");
		routeGraph.addVertex("toronto");
		routeGraph.addVertex("montreal");
		routeGraph.addVertex("pittsburgh");
		routeGraph.addVertex("boston");
		routeGraph.addVertex("amsterdam");
		routeGraph.addVertex("heathrow");
		routeGraph.addVertex("bombay");
		
		// Add possible routes to graph; addFlight takes origin and destination airports, 
		// departure and arrival times, flight code and cost (weight).
		routeGraph.addFlight("edinburgh", "heathrow", 
			new DateTime(2011, 11, 27, 10, 00, 0, 0), 
			new DateTime(2011, 11, 27, 10, 55, 0, 0), "BA907", 100);
		 
		routeGraph.addFlight("heathrow", "amsterdam", 
			new DateTime(2011, 11, 27, 11, 35, 0, 0), 
			new DateTime(2011, 11, 27, 12, 30, 0, 0), "BA555", 120);
		
		routeGraph.addFlight("heathrow", "boston", 
			new DateTime(2011, 11, 27, 8, 00, 0, 0), 
			new DateTime(2011, 11, 27, 15, 45, 0, 0), "BA951", 230);
		
		routeGraph.addFlight("boston", "pittsburgh", 
			new DateTime(2011, 11, 27, 9, 15, 0, 0), 
			new DateTime(2011, 11, 27, 10, 15, 0, 0), "DA441", 80);
		
		routeGraph.addFlight("boston", "montreal", 
			new DateTime(2011, 11, 27, 12, 35, 0, 0), 
			new DateTime(2011, 11, 27, 14, 45, 0, 0), "DA568", 110);
		
		routeGraph.addFlight("montreal", "toronto", 
			new DateTime(2011, 11, 27, 9, 45, 0, 0), 
			new DateTime(2011, 11, 27, 11, 20, 0, 0), "CA332", 70);
		
		routeGraph.addFlight("edinburgh", "pittsburgh", 
			new DateTime(2011, 11, 27, 8, 55, 0, 0), 
			new DateTime(2011, 11, 27, 17, 05, 0, 0), "DA879", 560);
		
		routeGraph.addFlight("delhi", "hong kong", 
			new DateTime(2011, 11, 27, 10, 15, 0, 0), 
			new DateTime(2011, 11, 27, 14, 15, 0, 0), "IN117", 130);
		
		routeGraph.addFlight("delhi", "bombay", 
			new DateTime(2011, 11, 27, 11, 35, 0, 0), 
			new DateTime(2011, 11, 27, 14, 35, 0, 0), "IN321", 230);
		
		routeGraph.addFlight("amsterdam", "heathrow", 
			new DateTime(2011, 11, 27, 10, 00, 0, 0), 
			new DateTime(2011, 11, 27, 10, 55, 0, 0), "BA903", 100);

		routeGraph.addFlight("heathrow", "edinburgh", 
			new DateTime(2011, 11, 27, 11, 35, 0, 0), 
			new DateTime(2011, 11, 27, 12, 30, 0, 0), "BA556", 120);
		
		routeGraph.addFlight("pittsburgh", "boston", 
			new DateTime(2011, 11, 27, 8, 00, 0, 0), 
			new DateTime(2011, 11, 27, 15, 45, 0, 0), "BA952", 230);
		
		routeGraph.addFlight("boston", "heathrow", 
			new DateTime(2011, 11, 27, 9, 15, 0, 0),
			new DateTime(2011, 11, 27, 10, 15, 0, 0), "DA440", 80);
		
		routeGraph.addFlight("montreal", "boston", 
			new DateTime(2011, 11, 27, 12, 35, 0, 0), 
			new DateTime(2011, 11, 27, 14, 45, 0, 0), "DA569", 110);
		
		routeGraph.addFlight("toronto", "montreal", 
			new DateTime(2011, 11, 27, 9, 45, 0, 0), 
			new DateTime(2011, 11, 27, 11, 20, 0, 0), "CA333", 70);
		
		routeGraph.addFlight("pittsburgh", "edinburgh", 
			new DateTime(2011, 11, 27, 8, 55, 0, 0), 
			new DateTime(2011, 11, 27, 17, 05, 0, 0), "DA871", 560);
		
		routeGraph.addFlight("hong kong", "delhi", 
			new DateTime(2011, 11, 27, 10, 15, 0, 0), 
			new DateTime(2011, 11, 27, 14, 15, 0, 0), "IN118", 130);
		
		routeGraph.addFlight("bombay", "delhi", 
			new DateTime(2011, 11, 27, 11, 35, 0, 0), 
			new DateTime(2011, 11, 27, 14, 35, 0, 0), "IN320", 230);
		
	}

}