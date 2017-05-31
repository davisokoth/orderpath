package ke.co.ipoint.structures;

import java.util.ArrayList;
import java.util.List;

import ke.co.ipoint.orderpath.App;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Graph {
    private final List<Vertex> vertexes;
    private final List<Edge> edges;
    private final boolean isDirected;
    private final boolean isWeighted;
    
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public Graph(boolean isDirected, boolean isWeighted) {
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
        edges = new ArrayList<Edge>();
        vertexes = new ArrayList<Vertex>();
        logger.debug("Ready to initialize...");
        initializeVertices();
        initializeEdges();
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

	public boolean isDirected() {
		return isDirected;
	}

	public boolean isWeighted() {
		return isWeighted;
	}
	
	private void initializeEdges(){
		edges.add(new Edge(0, vertexes.get(0), vertexes.get(1), 3));
		edges.add(new Edge(1, vertexes.get(0), vertexes.get(2), 6));
		edges.add(new Edge(2, vertexes.get(0), vertexes.get(5), 9));
		edges.add(new Edge(3, vertexes.get(1), vertexes.get(2), 5));
		edges.add(new Edge(4, vertexes.get(2), vertexes.get(3), 4));
		edges.add(new Edge(5, vertexes.get(1), vertexes.get(4), 4));
		edges.add(new Edge(6, vertexes.get(5), vertexes.get(6), 3));
		edges.add(new Edge(7, vertexes.get(3), vertexes.get(4), 4));
		edges.add(new Edge(8, vertexes.get(4), vertexes.get(5), 5));
		edges.add(new Edge(9, vertexes.get(3), vertexes.get(11), 7));
		edges.add(new Edge(10, vertexes.get(3), vertexes.get(10), 5));
		edges.add(new Edge(11, vertexes.get(4), vertexes.get(10), 2));
		edges.add(new Edge(12, vertexes.get(4), vertexes.get(8), 5));
		edges.add(new Edge(13, vertexes.get(5), vertexes.get(6), 3));
		edges.add(new Edge(14, vertexes.get(6), vertexes.get(7), 2));
		edges.add(new Edge(15, vertexes.get(6), vertexes.get(8), 4));
		edges.add(new Edge(16, vertexes.get(8), vertexes.get(9), 3));
		edges.add(new Edge(17, vertexes.get(9), vertexes.get(10), 5));
		edges.add(new Edge(18, vertexes.get(10), vertexes.get(13), 1));
		edges.add(new Edge(19, vertexes.get(10), vertexes.get(12), 7));
		edges.add(new Edge(20, vertexes.get(12), vertexes.get(13), 7));
		edges.add(new Edge(21, vertexes.get(11), vertexes.get(12), 5));
		edges.add(new Edge(22, vertexes.get(11), vertexes.get(17), 8));
		edges.add(new Edge(23, vertexes.get(12), vertexes.get(17), 4));
		edges.add(new Edge(24, vertexes.get(12), vertexes.get(16), 3));
		edges.add(new Edge(25, vertexes.get(17), vertexes.get(16), 4));
		edges.add(new Edge(26, vertexes.get(9), vertexes.get(13), 3));
		edges.add(new Edge(27, vertexes.get(13), vertexes.get(14), 4));
		edges.add(new Edge(28, vertexes.get(13), vertexes.get(15), 8));
		edges.add(new Edge(29, vertexes.get(15), vertexes.get(18), 5));
		edges.add(new Edge(30, vertexes.get(9), vertexes.get(14), 5));
		edges.add(new Edge(31, vertexes.get(16), vertexes.get(18), 7));
	}

	private void initializeVertices(){
		vertexes.add(new Vertex(1.0, 0.6));
		vertexes.add(new Vertex(2.5, 3.5));
		vertexes.add(new Vertex(6.2, 1.0));
		vertexes.add(new Vertex(7.5, 4.0));
		vertexes.add(new Vertex(4.8, 6.4));
		vertexes.add(new Vertex(1.0, 7.9));
		vertexes.add(new Vertex(2.2, 11.0));
		vertexes.add(new Vertex(0.8, 12.5));
		vertexes.add(new Vertex(5.3, 10.2));
		vertexes.add(new Vertex(7.4, 11.8));
		vertexes.add(new Vertex(8.8, 7.6));
		vertexes.add(new Vertex(11.1, 1.0));
		vertexes.add(new Vertex(12.1, 4.7));
		vertexes.add(new Vertex(9.9, 8.9));
		vertexes.add(new Vertex(11.0, 13.0));
		vertexes.add(new Vertex(13.9, 11.0));
		vertexes.add(new Vertex(13.5, 7.5));
		vertexes.add(new Vertex(15.5, 4.4));
		vertexes.add(new Vertex(17.4, 9.8));
		
	}
	
	public List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		// logger.info(edges.size() + " neighbors of " + node.toString());
        for (Edge edge : edges) {
        	// logger.info("Checking: " + edge.getSource() + ", " + edge.getDestination());
            if (edge.getSource().equals(node)
                    && (!edge.getDestination().isVisited())) {
            	// logger.info("Neigbors: (" + edge.getSource() + "," + edge.getDestination());
                neighbors.add(edge.getDestination());
            }
            if (edge.getDestination().equals(node)
                    && (!edge.getSource().isVisited())) {
            	// logger.info("Neigbors: (" + edge.getSource() + "," + edge.getDestination());
                neighbors.add(edge.getSource());
            }
        }
        return neighbors;
    }
	
    public int getDistance(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
            if (edge.getDestination().equals(node)
                    && edge.getSource().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }
	
    public Edge getVertexEdge(Vertex node, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge;
            }
        }
        throw new RuntimeException("Should not happen");
    }
	
    public boolean exists(double xcood, double ycood) {
        for (Vertex vertex : vertexes) {
            if ((vertex.getxCood()== xcood) && (vertex.getyCood() == ycood)){
                return true;
            }
        }
        return false;
    }

	
    public Vertex findVertex(Vertex vertex) {
        for (Vertex inGraph : vertexes) {
            if (inGraph.equals(vertex)) {
                return inGraph;
            }
        }
        throw new RuntimeException("Should not happen");
    }
    
    public void resetVertices(){
    	for(Vertex vertex : vertexes){
    		vertex.setVisited(false);
    		vertex.setCost(Integer.MAX_VALUE);
    	}
    }
}