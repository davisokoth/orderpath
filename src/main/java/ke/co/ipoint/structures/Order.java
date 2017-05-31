package ke.co.ipoint.structures;

public class Order {
	
	private Vertex pickup;
	private Vertex dropoff;
	private String name = "";
	private double costCombined = 0d;
	private double costSeparate = 0d;

	public Order(String name, double x1, double y1, double x2, double y2) {
		this.pickup = new Vertex(x1, y1);
		this.dropoff = new Vertex(x2, y2);
		this.name = name;
	}

	public Vertex getPickup() {
		return pickup;
	}

	public void setPickup(Vertex pickup) {
		this.pickup = pickup;
	}

	public Vertex getDropoff() {
		return dropoff;
	}

	public void setyCood(Vertex dropoff) {
		this.dropoff = dropoff;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDropoff(Vertex dropoff) {
		this.dropoff = dropoff;
	}

	public double getCostCombined() {
		return costCombined;
	}

	public void setCostCombined(double costCombined) {
		this.costCombined = costCombined;
	}

	public double getCostSeparate() {
		return costSeparate;
	}

	public void setCostSeparate(double costSeparate) {
		this.costSeparate = costSeparate;
	}

}
