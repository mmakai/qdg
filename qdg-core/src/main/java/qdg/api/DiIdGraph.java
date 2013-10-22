package qdg.api;

public interface DiIdGraph extends DiGraph {
	
	Node nodeFromId(int id);
	
	Edge arcFromId(int id);
}
