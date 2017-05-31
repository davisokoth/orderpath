package ke.co.ipoint.orderpath;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ke.co.ipoint.structures.Vertex;
import ke.co.ipoint.structures.Graph;

public class DijkstraSP {
	// private int distances[];
    private Set<Vertex> settled;
    private PriorityQueue<Vertex> priorityQueue;
    private int numVertices;
    private Graph graph;
    private Vertex source;
    final static Logger logger = LoggerFactory.getLogger(App.class);
 
    public  DijkstraSP(Graph graph)
    {
        this.graph = graph;
        this.numVertices = graph.getVertexes().size();
        // distances = new int[numVertices + 1];
        settled = new HashSet<Vertex>();
        priorityQueue = new PriorityQueue<Vertex>(numVertices);
    }
 
    public void dijkstra_algorithm(Vertex vSource)
    {
    	Vertex evaluationNode;
    	priorityQueue.clear();
    	graph.resetVertices();
    	settled.clear();
 
    	this.source = vSource;
        source.setCost(0);
        priorityQueue.add(source);
        
        while (!priorityQueue.isEmpty()){
        	evaluationNode = getMinimumDistanceVertex();
            evaluationNode.setVisited(true);
            // Set the current item to be visited in the original set of vertices
            graph.findVertex(evaluationNode).setVisited(true);
            // settled.add(evaluationNode);
            // logger.info(evaluationNode.toString() + " added to settled nodes @ " + evaluationNode.getCost() + " !!!");
            evaluateNeighbours(evaluationNode);
        }
    } 
    
    public Set<Vertex> getSettledNodes(){
    	return settled;
    }
 
    private Vertex getMinimumDistanceVertex()
    {
        Vertex node = priorityQueue.remove();
        return node;
    }
 
    private void evaluateNeighbours(Vertex currentNode)
    {
        int edgeDistance = -1;
        int newDistance = -1;
        
        // logger.info("     (Currently looking for " + currentNode + "'s neighbors)");
 
        for (Vertex neighbor : graph.getNeighbors(currentNode)){
            if (!neighbor.isVisited()){
                edgeDistance = graph.getDistance(currentNode,  neighbor);
                newDistance = currentNode.getCost() + edgeDistance;
                if (newDistance < neighbor.getCost()){
                    neighbor.setCost(newDistance);
                    // logger.info("        " + neighbor.toString() + " cost is: " + neighbor.getCost());
                    settled.add(neighbor);
                    priorityQueue.add(neighbor);
                    // logger.info(" ");
                }
            }
        }
    }
    
    public void printAllNeigbors(){
    	logger.debug("The Shorted Path to all nodes are ");
        while(!priorityQueue.isEmpty()){
        	Vertex node = priorityQueue.remove();
            logger.debug(source.toString() + " to " + node.toString() + " is " + node.getCost());
        }
    }
}