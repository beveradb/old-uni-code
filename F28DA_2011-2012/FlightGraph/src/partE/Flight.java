package partE;

/* 	Flight class is basically a weighted edge with a few extra variables for extra data
	such as cost, departure time, arrival time and flight code */
public class Flight extends org.jgrapht.graph.DefaultWeightedEdge {
 
	// Silly java serialization. Don't understand it, but Eclipse is a moany little IDE.
	private static final long serialVersionUID = 1L;
	
	// Initialize variables; cost must be 1 rather than 0 as all edges in a graph have to have a weight
	private double weight = 1;
	private int departure = 0;
	private int arrival = 0;
	private String code = "";
 
	public String getOrigin(){
		return super.getSource().toString();
	}

	public String getDestination(){
		return super.getTarget().toString();
	}
	
	public double getWeight() {
		return weight;
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
 
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void setDeparture(int departure) {
		this.departure = departure;
	}

	public void setArrival(int arrival) {
		this.arrival = arrival;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	// Spit out concatenated variable value dump on one line
	public String toString(){
		return "("+getCode()+": "+getWeight()+" -> "+getArrival()+", \u00A3"+Double.toString(getWeight())+", (UTC) "+getDeparture()+"-"+getArrival()+")";
	}

}