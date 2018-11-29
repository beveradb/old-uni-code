package partF;

import org.joda.time.DateTime;

/* 	Flight class is basically a weighted edge with a few extra variables for extra data
	such as cost, departure time, arrival time and flight code */
public class Flight extends org.jgrapht.graph.DefaultWeightedEdge {
 
	// Silly java serialization. Don't understand it, but Eclipse is a moany little IDE.
	private static final long serialVersionUID = 1L;
	
	// Initialize variables;
	private double weight;
	private DateTime departure;
	private DateTime arrival;
	private String code;
 
	// Getters and setters
	public String getOrigin(){
		return super.getSource().toString();
	}

	public String getDestination(){
		return super.getTarget().toString();
	}
	
	public double getWeight() {
		return weight;
	}

	public DateTime getDeparture(){
		return departure;
	}
 
	public DateTime getArrival(){
		return arrival;
	}
 
	public String getCode(){
		return code;
	}
 
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void setDeparture(DateTime departure) {
		this.departure = departure;
	}

	public void setArrival(DateTime arrival) {
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