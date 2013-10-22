package qdg.api;

public interface UIdGraph extends UGraph {

	Node nodeFromId(int id);
	
	Edge uEdgeFromId(int id);
}
