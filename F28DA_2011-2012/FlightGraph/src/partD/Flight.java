package partD;

/* 	Flight class is basically a weighted edge with a few extra variables for extra data
	such as cost, departure time, arrival time and flight code */
public class Flight extends org.jgrapht.graph.DefaultWeightedEdge {
 
	// Silly java serialization. Don't understand it, but Eclipse is a moany little IDE.
	private static final long serialVersionUID = 1L;
	
	// Initialize variables; cost must be 1 rather than 0 as all edges in a graph have to have a weight
	double cost = 1;
	int departure = 0;
	int arrival = 0;
	String code = "";
 
	public double getCost(){
		return cost;
	}
 
	public int getDeparture(){
		return departure;
	}
 
	public int getArrival(){
		return arrival;
	}
 
	public String getCode(){
		return code;
	}
 
	// Spit out concatenated variable value dump on one line
	public String toString(){
		return "("+code+": "+getCode()+" -> "+getArrival()+", \u00A3"+Double.toString(getCost())+", (UTC) "+getDeparture()+"-"+getArrival()+")";
	}
	
}