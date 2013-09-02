/*
 * Copyright (C) 2013 Marton Makai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package qdg.jung;

import java.util.Arrays;
import java.util.Collection;

import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;
import qdg.api.MixedGraph;
import qdg.api.MutableMixedGraph;

import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import com.google.common.collect.Iterables;
	
public class MixedGraphAsJungGraph implements
		edu.uci.ics.jung.graph.Graph<Node, Edge> {

	private MixedGraph g;

	private EdgeType defaultEdgeType;
	
	public MixedGraphAsJungGraph() {
	}
	
	public MixedGraphAsJungGraph(MixedGraph g, EdgeType defaultEdgeType) {
		this.g = g;
		this.defaultEdgeType = defaultEdgeType;
	}

	public MixedGraph getQDGGraph() {
		return g;
	}
	
	public void setQDGGraph(MixedGraph g) {
		this.g = g;
	}

	public void setDefaultEdgeType(EdgeType defaultEdgeType) {
		this.defaultEdgeType = defaultEdgeType;
	}

	@Override
	public Collection<Edge> getEdges() {
		return new IterableWrapper<Edge>(g.getEdges());
	}

	@Override
	public Collection<Node> getVertices() {
		return new IterableWrapper<Node>(g.getNodes());
	}

	@Override
	public boolean containsVertex(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsEdge(Edge edge) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getEdgeCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getVertexCount() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.jung.graph.Hypergraph#getNeighbors(java.lang.Object)
	 * 
	 * If each vertex is to be considered at most one, then the implementation
	 * of this would require a map of the already seen vertices. It highly
	 * depends on the user that this is a requirement or not, and it is not
	 * specified in the JUNG interface. Hence we rather do not implement this.
	 */
	@Override
	public Collection<Node> getNeighbors(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Edge> getIncidentEdges(Node vertex) {
		return new IterableWrapper<Edge>(g.getIncidentEdges(vertex));
	}

	@Override
	public Collection<Node> getIncidentVertices(Edge edge) {
		return Arrays.asList(g.getSource(edge), g.getTarget(edge));
	}

	@Override
	public Edge findEdge(Node v1, Node v2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<Edge> findEdgeSet(Node v1, Node v2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addVertex(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addEdge(Edge edge, Collection<? extends Node> vertices) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addEdge(Edge edge, Collection<? extends Node> vertices,
			EdgeType edge_type) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.graph.Hypergraph#removeVertex(java.lang.Object)
	 * 
	 * Note that the return value violates the contract, but there is no way
	 * to implement it correctly
	 */
	@Override
	public boolean removeVertex(Node vertex) {
		((MutableMixedGraph) g).remove(vertex);
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.uci.ics.jung.graph.Hypergraph#removeEdge(java.lang.Object)
	 * 
	 * Note that the return value violates the contract, but there is no way
	 * to implement it correctly
	 */
	@Override
	public boolean removeEdge(Edge edge) {
		((MutableMixedGraph) g).remove(edge);
		return true;
	}

	@Override
	public boolean isNeighbor(Node v1, Node v2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isIncident(Node vertex, Edge edge) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int degree(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getNeighborCount(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getIncidentCount(Edge edge) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EdgeType getEdgeType(Edge edge) {
		if (g.isDirected(edge)) {
			return EdgeType.DIRECTED;
		} else {
			return EdgeType.UNDIRECTED;
		}
	}

	@Override
	public EdgeType getDefaultEdgeType() {
		return defaultEdgeType;
	}

	@Override
	public Collection<Edge> getEdges(EdgeType edge_type) {
		switch (edge_type) {
		case DIRECTED:
			return new IterableWrapper<Edge>(g.getArcs());
		case UNDIRECTED:
			return new IterableWrapper<Edge>(g.getUEdges());
		default:
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public int getEdgeCount(EdgeType edge_type) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.jung.graph.Graph#getInEdges(java.lang.Object)
	 * 
	 * NOTE Though the graph API does not specify what this function really
	 * should do with the undirected edges, according to the jung
	 * implementations all of them should be returned.
	 */
	@Override
	public Collection<Edge> getInEdges(Node vertex) {
		return new IterableWrapper<Edge>(Iterables.concat(g.getInArcs(vertex),
				g.getIncidentUEdges(vertex)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.jung.graph.Graph#getOutEdges(java.lang.Object)
	 * 
	 * NOTE Though the graph API does not specify what this function really
	 * should do with the undirected edges, according to the jung
	 * implementations all of them should be returned.
	 */
	@Override
	public Collection<Edge> getOutEdges(Node vertex) {
		return new IterableWrapper<Edge>(Iterables.concat(g.getOutArcs(vertex),
				g.getIncidentUEdges(vertex)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.jung.graph.Graph#getPredecessors(java.lang.Object)
	 * 
	 * If each vertex is to be considered at most one, then the implementation
	 * of this would require a map of the already seen vertices. It highly
	 * depends on the user that this is a requirement or not, and it is not
	 * specified in the JUNG interface. Hence we rather do not implement this.
	 */
	@Override
	public Collection<Node> getPredecessors(Node vertex) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.jung.graph.Graph#getSuccessors(java.lang.Object)
	 * 
	 * If each vertex is to be considered at most one, then the implementation
	 * of this would require a map of the already seen vertices. It highly
	 * depends on the user that this is a requirement or not, and it is not
	 * specified in the JUNG interface. Hence we rather do not implement this.
	 */
	@Override
	public Collection<Node> getSuccessors(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int inDegree(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int outDegree(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPredecessor(Node v1, Node v2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSuccessor(Node v1, Node v2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPredecessorCount(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSuccessorCount(Node vertex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node getSource(Edge directed_edge) {
		return g.getSource(directed_edge);
	}

	@Override
	public Node getDest(Edge directed_edge) {
		return g.getTarget(directed_edge);
	}

	@Override
	public boolean isSource(Node vertex, Edge edge) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDest(Node vertex, Edge edge) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addEdge(Edge e, Node v1, Node v2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addEdge(Edge e, Node v1, Node v2, EdgeType edgeType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pair<Node> getEndpoints(Edge edge) {
		Node source = g.getSource(edge);
		Node target = g.getTarget(edge);
		return new Pair<Node>(source, target);
	}

	@Override
	public Node getOpposite(Node vertex, Edge edge) {
		Node source = g.getSource(edge);
		if (source.equals(vertex)) {
			return g.getTarget(edge);
		} else {
			return source;
		}
	}
}
