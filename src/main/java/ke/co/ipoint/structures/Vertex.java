package ke.co.ipoint.structures;

import ke.co.ipoint.orderpath.App;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vertex implements Comparable<Vertex>{
	    
	private Double xCood;
    private Double yCood;
    private boolean visited;
    private int cost;
    private Vertex closestParent;
    private Vertex next;
    private int bluffCost;
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public Vertex(double xCood, double yCood) {
        this.xCood = xCood;
        this.yCood = yCood;
        this.visited = false;
        this.cost = Integer.MAX_VALUE;
        this.bluffCost = Integer.MAX_VALUE;
        this.closestParent = null;
    }
    
    @Override
    public boolean equals(Object obj) {
    	/**
    	if (this == obj)
            return true;
    	if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        **/
        Vertex other = (Vertex) obj;
        // logger.info("(" + this.getxCood() + "," + this.getyCood() +
    	//		") : (" + other.getxCood() + "," + other.getyCood() + ")");
        // settled.getxCood() == destination.getxCood() & 
		// settled.getyCood() == destination.getyCood()
        if (this.getxCood() == other.getxCood() && (this.getyCood() == other.getyCood())){
        	// logger.info("Got stuff!");
            return true;
        } 
        return false;
    }

    @Override
    public String toString() {
    	return "(" + xCood + "," + yCood + ")";
    }
	public double getxCood() {
		return xCood;
	}
	public void setxCood(double xCood) {
		this.xCood = xCood;
	}
	public double getyCood() {
		return yCood;
	}
	public void setyCood(double yCood) {
		this.yCood = yCood;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void setxCood(Double xCood) {
		this.xCood = xCood;
	}

	public void setyCood(Double yCood) {
		this.yCood = yCood;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getBluffCost() {
		return bluffCost;
	}

	public void setBluffCost(int bluffCost) {
		this.bluffCost = bluffCost;
	}

	public static Logger getLogger() {
		return logger;
	}

	public Vertex getClosestParent() {
		return closestParent;
	}

	public void setClosestParent(Vertex closestParent) {
		this.closestParent = closestParent;
	}

	public Vertex getNext() {
		return next;
	}

	public void setNext(Vertex next) {
		this.next = next;
	}
 
    public int compare(Vertex node1, Vertex node2)
    {
        if (node1.cost < node2.cost)
            return -1;
        if (node1.cost > node2.cost)
            return 1;
        return 0;
    }

	public int compareTo(Vertex vertex) {
		// TODO Auto-generated method stub
		if (this.cost < vertex.cost)
			return -1;
		if (this.cost > vertex.cost)
            return 1;
		return 0;
	}

}
