package ke.co.ipoint.orderpath;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ke.co.ipoint.structures.Graph;
import ke.co.ipoint.structures.Order;
import ke.co.ipoint.structures.Vertex;

public class App 
{
	private Graph graph;
    
    final static Logger logger = LoggerFactory.getLogger(App.class);
    
    // Initialize sets
    private ArrayList<Order> orders;
    private Set<Order> acceptedOrders;
    private ArrayList<Vertex> possibleNodes;
    private Set<Vertex> settledNodes;
    private int establishedCost = 0;
    private DijkstraSP dsp;
    private Order currentOrder;
    
    private Scanner in;
    
    public App(){
    	graph = new Graph(true, true);
    	dsp = new DijkstraSP(graph);
    	in = new Scanner(System.in);
		orders = new ArrayList<Order>();
		acceptedOrders = new HashSet<Order>();
		possibleNodes = new ArrayList<Vertex>();
		settledNodes = new HashSet<Vertex>();
    }
    
    public static void umain( String[] args )
    {
    	App app = new App();
    	app.run();
    }
    
    private void run(){
    	boolean listen = true;
    	Scanner inScan = getScanner();
    	logger.info(" Launching... ");
    	
		// listen for orders
		while(listen){
			// Create order and check if it fits optimally
			currentOrder = getNewOrder(inScan);
			if(currentOrder == null)
				continue;
			orders.add(currentOrder);
			resetPossibleNodes();
			if(orders.size() == 1){
				establishedCost += costSingleOrder(currentOrder);
				currentOrder.setCostCombined(establishedCost);
				currentOrder.setCostSeparate(establishedCost);
				accept(currentOrder);
			}else{
				// possibleNodes.clear();
				boolean isViable = checkViability(currentOrder);
				if(isViable){
					accept(currentOrder);
				}else{
					reject(currentOrder);
				}
			}
			logger.info("==================");
			logger.info("  Combined: " + currentOrder.getCostCombined());
			logger.info("  Separate: " + currentOrder.getCostSeparate());
			logger.info("==================");
			
			// Just so we exit at some point...
			if(acceptedOrders.size() > 3) listen = false;
		}
		inScan.close();
    }
    
    private int costSingleOrder(Order order){
    	ArrayList<Vertex> nodesToUse = new ArrayList<Vertex>();
    	int cost = 0;
    	nodesToUse.add(currentOrder.getPickup());
    	nodesToUse.add(currentOrder.getDropoff());
		cost = getOptimalCost(nodesToUse, false);
		return cost;
    }
    
    private Scanner getScanner(){
    	return in;
    };
	
	
	private boolean checkViability(Order order){
		// Set possible node to the current order and get sum of optimal cost
		// and already established cost
		int  combinedCost = 0;
		int costSingleOrder = 0;
		boolean isCombinedCosting = false;
		logger.info("=================> Costing separate");
		costSingleOrder = costSingleOrder(order) + establishedCost;
		
		// Now get the optimal cost of visiting all nodes
		// setPossibleNodes();
		logger.info("No of nodes to traverse: " + possibleNodes.size());
		logger.info("=================> Costing combined");
		isCombinedCosting = true;
		combinedCost = getOptimalCost(possibleNodes, isCombinedCosting);
		logger.info("No of Possible Nodes: "  + possibleNodes.size());
		order.setCostCombined(combinedCost);
		order.setCostSeparate(costSingleOrder);
		if(combinedCost <= costSingleOrder)
			return true;
		else return false;
	}
	
	private Order getNewOrder(Scanner in){
		logger.info("Waiting for an order...");
		logger.info(" ***   Provide name, x,y,x,y respectively:");
		// Read in the coordinates
		try{
			String name = in.nextLine();
			Double x1 = in.nextDouble();
			Double y1 = in.nextDouble();
			Double x2 = in.nextDouble();
			Double y2 = in.nextDouble();
			// Validate the vertices:
			if(graph.exists(x1, y1) && graph.exists(x2, y2)){
				// Create order and check if it fits optimally
				Order order = new Order(name, x1, y1, x2, y2);
				return order;
			}else{
				logger.error("The provided coordinates are not valid!");
				getNewOrder(in);
			}
		} catch (InputMismatchException ime){
			logger.error("Invalid input!");
		}
		return null;
	}
	
	private void resetPossibleNodes(){
		possibleNodes.clear();
		if(orders.size() > 0){
			for(int x = 0; x < orders.size(); x++){
				possibleNodes.add(orders.get(x).getPickup());
				possibleNodes.add(orders.get(x).getDropoff());
			}
		}
	}
	
	private int getOptimalCost(ArrayList<Vertex> vertices, boolean isCombined){
		HashSet<Vertex> eligibleNodes = new HashSet<Vertex>();
		ArrayList<Vertex> unvisitedNodes = new ArrayList<Vertex>();
		ArrayList<PathCost> paths = new ArrayList<PathCost>();
		ArrayList<Vertex> nearestNodes = new ArrayList<Vertex>();
		
		settledNodes.clear();
	
	    // We use ordered paths
	    for (Vertex node : vertices){
	        node.setVisited(false);
	        node.setCost(Integer.MAX_VALUE);
	        node.setClosestParent(null);
	        node.setNext(null);
	        // logger.info("Current Node: " + node.toString());
	    }
	    unvisitedNodes = vertices;
	    // candidateNodes = vertices;
	    
	    // Set the cost for the start node (for first order) to zero
	    unvisitedNodes.get(0).setCost(0);
	    unvisitedNodes.get(0).setBluffCost(0);
	    
	    while((unvisitedNodes.size() -1) > 0){
	    	logger.info(" --------------------- New Loop --------------------");
	        Vertex currentNode = leastCostNode(unvisitedNodes, true);

	        if(currentNode != null){
	        	logger.info("Current: " + currentNode.toString());
		        logger.info("Unvisited Nodes: " + unvisitedNodes.size());
		        unvisitedNodes.remove(currentNode);
	        	eligibleNodes = getEligibleNodes(currentNode, unvisitedNodes, isCombined);
	        	logger.info("Eligible Nodes: " + eligibleNodes.size());
	        	for(Vertex node : eligibleNodes){
	        		logger.info(node.toString());
	        	}
	        	settledNodes.add(currentNode);
		        // logger.info("Getting ready to loop through " + eligibleNodes.size() + " eligibles");
		        
		        for( Vertex node : eligibleNodes){
		
		        	logger.info("  ## Eligible Node: " + node.toString());
		        	Vertex closestNode = getClosestVisitedNode(node);
		            node.setCost(closestNode.getCost());
		            node.setClosestParent(closestNode);
		            nearestNodes.add(node);
		            logger.info("  ## AddNearest: " + closestNode.toString() + " @ " + closestNode.getCost());
		        }
		        
		        Vertex nextNode = leastCostNode(nearestNodes, false);
		        logger.info("New Path: " 
		        		+ currentNode.toString()
		        		+ " to "
		        		+ nextNode.toString() 
		        		+ " @ " + nextNode.getCost());
		        paths.add(new PathCost(currentNode, nextNode, nextNode.getCost()));
		        for(Vertex node : eligibleNodes){
		            if(node != nextNode)
		                node.setCost(Integer.MAX_VALUE);
		        }
		        setNodeCost(nextNode, unvisitedNodes, 0);
	        } else {
	        	logger.info("No paths found! Unvisited nodes: " + unvisitedNodes.size());
	        	// Set the bluff cost of the first unvisited source node found to zero
	        	// boolean isBreak = false;
	        	
	        	for(Order order : orders){
	        		for(Vertex node : unvisitedNodes){
	        			if(order.getPickup().equals(node)){
	        				node.setBluffCost(0);
	        			}
	        			
	        		}
	        		// if(isBreak) break;
	        	}
	        	// unvisitedNodes.clear();
	        }
	    }
	    int totalCost = getTotalPathCost(paths);
	    logger.info("------------ COST FOR SECTION ------------: " + totalCost);
	    return totalCost;
	}

	private Vertex leastCostNode(ArrayList<Vertex> nodes, boolean useBluff){
		Vertex node = null;
		int nCost = Integer.MAX_VALUE;
		for(Vertex _node :nodes){
			if(useBluff){
				if(_node.getBluffCost() < nCost){
					node = _node;
					nCost = _node.getBluffCost();
				}
			} else {
				if(_node.getCost() < nCost){
					node = _node;
					nCost = _node.getCost();
				}
			}
			// logger.info("Considering" + _node.toString() + ": " + nCost + " vs " + _node.getCost());
		}
		return node;
	}
	
	private HashSet<Vertex> getEligibleNodes(Vertex currentNode, 
			ArrayList<Vertex> unvisitedNodes, boolean isCombined){
		HashSet<Vertex> eligibleNodes = new HashSet<Vertex>();
		if(isCombined){
			for(Order order : orders){
				if(unvisitedNodes.contains(order.getPickup())){
					eligibleNodes.add(order.getPickup());
				} else {
					eligibleNodes.add(order.getDropoff());
				}
			}
		}else{
			eligibleNodes.add(currentOrder.getDropoff());
		}
		return eligibleNodes;
	}
	
	private void setNodeCost(Vertex node, ArrayList<Vertex> vertices, int cost){
		for(Vertex _node : vertices){
			if(node.equals(_node)){
				_node.setBluffCost(cost);
				logger.info("Node: " + node.getCost() + ", _node: " + _node.getCost());
			} else _node.setBluffCost(Integer.MAX_VALUE);
		}
	}
	
	private Vertex getClosestVisitedNode(Vertex node){
		// logger.info("Analyzing Dijkstra...");
		// logger.info("# of settled nodes: " + settledNodes.size());
    	dsp.dijkstra_algorithm(node);
    	// logger.info("Edgar my friend, that was nice");
    	int _cost = Integer.MAX_VALUE;
    	int counter = 0;
		Vertex closestNode = null;
		Set<Vertex> queue = dsp.getSettledNodes();
		Iterator<Vertex> it = queue.iterator();
		while(it.hasNext()){
			Vertex destination = it.next();
			// logger.info(counter + " nCost value: " + _cost);
			for(Vertex settled : settledNodes){
				// logger.info("    Settled:: " + settled.toString() + " @ " + _cost);
				if(settled.equals(destination)){
                	if(destination.getCost() < _cost){
                		//logger.info("        " + node.toString() + " && " 
                		//		+ destination.toString() 
                		//		+ " @" + destination.getCost());
                		
                		closestNode = destination;
                		_cost = destination.getCost();
                	}
                }
    		}
			counter++;
        }
		logger.info("         -- fini --                  ");
		// dsp.printAllNeigbors();
		return closestNode;
	}
	
	private int getTotalPathCost(ArrayList<PathCost> paths){
		int totalCost = 0;
		
		// logger.info("Paths found: " + paths.size() + ", Current Cost Total: " + totalCost);
		
		// Loop through looking for intersections
		for(PathCost cost : getIntersections(paths)){
			// logger.info("Processing intersection...");
			totalCost += cost.getCost();
			if(getNextPath(paths, cost.getDestination()) == null){
				totalCost += cost.getCost();
				paths.remove(cost);
			}
		}
		while(!paths.isEmpty()){
			PathCost cost = paths.get(0);
			totalCost += cost.getCost();
			paths.remove(cost);
			// logger.info("Processing non-intersection. Cost: " + cost.getCost());
		}
		logger.info("Path Cost: " + totalCost);
		return totalCost;
	}
	
	private ArrayList<PathCost> getIntersections(ArrayList<PathCost> paths){
		ArrayList<PathCost> intersections = new ArrayList<PathCost>();
		int counter = 0;
		while(counter < paths.size()){
			// logger.info("Picking #" + counter);
			PathCost path = paths.get(counter);
			for(PathCost pCost : paths){
				if((path != pCost) && (pCost.getSource().equals(path.getSource()))){
					intersections.add(path);
					intersections.add(pCost);
					logger.info("Intersection added");
				}
			}
			counter++;
		}
		// logger.info("Intersections: " + intersections);
		return intersections;
		
	}
	
	private boolean checkNodeDestination(ArrayList<PathCost> paths, Vertex node){
		boolean hasDestination = false;
		for(PathCost pCost : paths){
			if(pCost.getSource().equals(node)){
				hasDestination = true;
			}
		}
		return hasDestination;
	}
	
	private PathCost getNextPath(ArrayList<PathCost> paths, Vertex node){
		int numSourceNodes = 0;
		PathCost nextPath = null;
		for(PathCost pCost : paths){
			if(pCost.getSource().equals(node)){
				if(checkNodeDestination(paths, pCost.getDestination())){
					numSourceNodes ++;
					nextPath = pCost;
				}
			}
		}
		if(numSourceNodes == 0){
			return null;
		}
		return nextPath;
	}
	
	private void accept(Order order){
		acceptedOrders.add(order);
		logger.info("==> " + order.getName() + " accepted! <===");
	}
	
	private void reject(Order order){
		logger.info("***** " + order.getName() + " Not viable! *****");
		reset();
	}
	
	private void reset(){
		orders.clear();
		possibleNodes.clear();
		settledNodes.clear();
	}
	
	class PathCost implements Comparator<PathCost>{
		int cost = 0;
		Vertex source, destination;
		
		public PathCost(Vertex source, Vertex destination, int cost){
			this.source = source;
			this.destination = destination;
			this.cost = cost;
		}

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public Vertex getSource() {
			return source;
		}

		public void setSource(Vertex source) {
			this.source = source;
		}

		public Vertex getDestination() {
			return destination;
		}

		public void setDestination(Vertex destination) {
			this.destination = destination;
		}

		public int compare(PathCost cost1, PathCost cost2) {
			// TODO Auto-generated method stub
			if(cost1.getCost() > cost2.getCost())
				return 1;
			if(cost1.getCost() < cost2.getCost())
				return -1;
			return 0;
		}
		
	}
	
}
