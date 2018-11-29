// Travelling Salesman Problem, as tackled by Andrew Beveridge. YOLO.

public class DepthFirstTSP {
	// GLOBAL VARIABLES
	static int numCities = 8; // Total number of cities we want to loop through
	static int cityCount = 0; // How many nodes we've checked (for stats)
	static int completePathCount = 0; // How many leaves we've checked (for stats)
	static int shortestDistance = 1000000;  // Our best effort distance so far
	static int visitedCities[] = new int[numCities]; // Array of cities we've already visited
	static int shortestPath[] = new int[numCities]; // Array of cities on current shortest path 

	// Array of city names, for nice printout at the end
	static String[] cityNames = {"Edinburgh","Aberdeen","Ayr","Fort William","Glasgow","Inverness","St Andrews","Stirling"};

	// Matrix of distances between cities 
	// In the format (where numbers are distances in miles to):
	// {edinburgh, aberdeen, ayr, fort william, glasgow, inverness, st andrews, stirling}
	// The order of the columns matters - edinburgh has been put first because it's our starting city
	static int cityDistances[][] =	{
		{0,129,79,131,43,154,50,36}, 		// Edinburgh
		{129,0,179,157,146,105,79,119}, 	// Aberdeen
		{79,179,0,141,33,207,118,64}, 		// Ayr
		{131,157,141,0,116,74,134,96}, 		// Fort William
		{43,146,33,116,0,175,81,27}, 		// Glasgow
		{154,105,207,64,175,0,145,143}, 	// Inverness
		{50,79,118,134,81,145,0,52}, 		// St Andrews
		{36,119,64,96,27,143,52,0} 			// Stirling
	};

	// Recursive depth-first search function
	// currentCity: 			City number we just arrived at (range: 0 to numCities)
	// visitedCitiesCount: 		How many cities we've visited so far
	// currentTotalDistance: 	Total distance travelled so far
	static void search(int currentCity, int visitedCitiesCount, int currentTotalDistance) {
		// Count how many times we've run this search function (or nodes, if you're talking fancy DSA-speak)
		cityCount++; 
		// Add the current city number to the visitedCities array at a position which shows the order we visited them in
		visitedCities[visitedCitiesCount]=currentCity;
		// Check if we've visited all cities (it's numCities-1 because visitedCitiesCount starts at 0 but numCities doesn't) 
		if (visitedCitiesCount==(numCities-1)) {
			// Count how many complete paths we've found (we've visited all cities). AKA Leaves, in DSA-speak. 
			completePathCount++;
			// Since we've visited all cities, add the distance to our home city onto the current total
			int length = currentTotalDistance + cityDistances[currentCity][0];
			// If this length is less than our current shortestDistance, replace it!
			if (length < shortestDistance) {
				shortestDistance = length; 
				// Also since we've found out shortest path so far, 
				// let's add the cities we visited to get here to the shortestPath array so we can print it later
				for (int i=0; i<numCities; i++) shortestPath[i]=visitedCities[i]; 
			}              
			// We haven't visited all of the cities yet
		} else {
			// Loop through all cities
			for(int nextCity=0; nextCity < numCities; nextCity++) {
				// Ensure that the current total distance plus the distance to the next city is less than our current shortest distance,
				// to stop us wasting time calculating a path which is already going to be too long
				if (currentTotalDistance+cityDistances[currentCity][nextCity] < shortestDistance) {
					// Create boolean to store whether or not nextCity has already been visited
					boolean cityAlreadyVisited = false;
					// Check all cities in visitedCities to see if nextCity is one we've already been to. If it is, skip this for loop iteration
					for (int i=0; i<visitedCitiesCount+1; i++) 
						if (visitedCities[i]==nextCity) cityAlreadyVisited = true;
					// If we've found nextCity in visitedCities, skip this for loop iteration (since we can't use this city as our nextCity!)
					if(cityAlreadyVisited == true) continue;
					// nextCity has not been visited before, let's continue the search starting from that city!
					search(nextCity, visitedCitiesCount+1, currentTotalDistance+cityDistances[currentCity][nextCity]);
				} else {
					//System.out.println("Found nextCity which makes currentTotalDistance ("+currentTotalDistance+") > shortestDistance ("+shortestDistance+"). At: "+currentCity+" Next: "+nextCity+ " Distance: "+cityDistances[currentCity][nextCity]);
				}
			}    
		}    

	}

	public static void main(String[] args) {
		// Begin search from city 0 with 0 previously visited cities and a total distance of 0.
		// Since the function is recursive this is all we need to do - once it has completed, the global variables such as shortestDistance will be updated
		search(0,0,0);            

		// Print statistics, shortest path
		System.out.println("Total cities visited while searching: "+cityCount);
		System.out.println("Complete paths found: "+completePathCount);
		System.out.println("Shortest Distance: "+shortestDistance+" miles"); 

		// Print array positions of shortest path
		System.out.print("Shortest Path (array positions): ");
		for (int i=0; i<numCities; i++) System.out.print(shortestPath[i] + " ");
		System.out.println("0");

		// Print more informative shortest path with city names and distances between
		System.out.print("Shortest Path (names / distances): ");
		for (int i=0; i<numCities; i++) {
			int j;
			if(i==numCities-1) {
				j=0;
			} else {
				j=i+1;
			}
			System.out.print("[" + cityNames[shortestPath[i]] + "] -" + cityDistances[shortestPath[i]][shortestPath[j]] + "- ");
		}
		System.out.println("[Edinburgh]");
	}
}
