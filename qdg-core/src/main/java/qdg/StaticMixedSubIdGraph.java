/*
 * Copyright (C) 2013, 2014 Marton Makai
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

package qdg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;

import qdg.api.EntityMap;
import qdg.api.MixedIdGraph;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.HasId;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractIdMap;
import qdg.bits.AbstractMixedGraph;
import qdg.bits.ArcLace;
import qdg.bits.ArcLace.ArcData;
import qdg.bits.ArcLace.NodeData;
import qdg.bits.ConcatIterator;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

/**
 * Subgraph of a mixed graph with integer ids of the nodes and edges.
 * The node and edge objects are used from the underlying mixed graph.
 * 
 * Nodes and edges are stored in arrays with their ids corresponding to
 * indices, hence memory consumption is proportional to the maximum id.
 * 
 * @author Marton Makai
 */
public class StaticMixedSubIdGraph extends AbstractMixedGraph
		implements MixedIdGraph, Serializable {
	
	private static final long serialVersionUID = 4444550416599231556L;
	
	protected static class CombinedNodeData implements Serializable {
		
		private static final long serialVersionUID = 8855112085916381992L;

		protected NodeData arcData = new NodeData();
		
		protected NodeData uEdgeData = new NodeData();
    	
		@Override
		public String toString() {
			return "CombinedNodeData [arcData=" + arcData + ", uEdgeData="
					+ uEdgeData + "]";
		}
	}
	
	protected MixedIdGraph graph;
	
	public StaticMixedSubIdGraph() {
	}
	
	public StaticMixedSubIdGraph(MixedIdGraph graph) {
		this.graph = graph;
	}
	
	public void setGraph(MixedIdGraph graph) {
		this.graph = graph;
	}
	
	public MixedIdGraph getGraph() {
		return graph;
	}
	
	@Override
	public boolean isDirected(Edge edge) {
		return graph.isDirected(edge);
	}
	
	protected StaticSparseArrayList<CombinedNodeData> nodes =
			new StaticSparseArrayList<CombinedNodeData>();
	
	private class ArcNodeMap implements EntityMap<Integer, NodeData> {
		
		@Override
		public NodeData put(Integer k, NodeData v) {
			CombinedNodeData c = nodes.get(k);
			if (c == null) {
				c = new CombinedNodeData();
			}
			c.arcData = v;
			return v;
		}

		@Override
		public NodeData get(Object k) {
			CombinedNodeData c = nodes.get(k);
			if (c == null) {
				return null;
			}
			return c.arcData;
		}
	}
	
	protected transient EntityMap<Integer, NodeData> arcNodes =
			new ArcNodeMap();
	
	private class UEdgeNodeMap implements EntityMap<Integer, NodeData> {
		
		@Override
		public NodeData put(Integer k, NodeData v) {
			CombinedNodeData c = nodes.get(k);
			if (c == null) {
				c = new CombinedNodeData();
			}
			c.uEdgeData = v;
			return v;
		}

		@Override
		public NodeData get(Object k) {
			CombinedNodeData c = nodes.get(k);
			if (c == null) {
				return null;
			}
			return c.uEdgeData;
		}
	}
	
	protected transient EntityMap<Integer, NodeData> uEdgeNodes =
			new UEdgeNodeMap();
	
	protected StaticSparseArrayList<ArcData<Integer>> arcData =
			new StaticSparseArrayList<ArcData<Integer>>();
	
	protected transient ArcLace<Integer> arcLace =
			new ArcLace<Integer>(arcNodes, arcData);
	
	protected StaticSparseArrayList<ArcData<Integer>> uEdgeData =
			new StaticSparseArrayList<ArcData<Integer>>();
	
	protected transient ArcLace<Integer> uEdgeLace =
			new ArcLace<Integer>(uEdgeNodes, uEdgeData);
	
	private Function<Integer, Node> constructNode =
			new Function<Integer, Node>() {

		@Override
		public Node apply(Integer id) {
			return graph.nodeFromId(id);
		}
	};
	
	private Function<Integer, Edge> constructArc =
			new Function<Integer, Edge>() {

		@Override
		public Edge apply(Integer id) {
			return graph.arcFromId(id);
		}
	};
	
	private Function<Integer, Edge> constructUEdge =
			new Function<Integer, Edge>() {

		@Override
		public Edge apply(Integer id) {
			return graph.uEdgeFromId(id);
		}
	};
	
	@Override
	public Node getSource(Edge edge) {
		if (graph.isDirected(edge)) {
			return graph.nodeFromId(arcLace.getSource(((HasId) edge).getId()));
		} else {
			return graph.nodeFromId(uEdgeLace.getSource(((HasId) edge).getId()));
		}
	}

	@Override
	public Node getTarget(Edge edge) {
		if (graph.isDirected(edge)) {
			return graph.nodeFromId(arcLace.getTarget(((HasId) edge).getId()));
		} else {
			return graph.nodeFromId(uEdgeLace.getTarget(((HasId) edge).getId()));
		}
	}
	
	@Override
	public Iterator<Node> getNodeIterator() {
		return Iterators.transform(nodes.keyIterator(), constructNode);
	}
	
	@Override
	public Iterator<Edge> getArcIterator() {
		return Iterators.transform(arcLace.getArcIterator(), constructArc);
	}

	@Override
	public Iterator<Edge> getUEdgeIterator() {
		return Iterators.transform(uEdgeLace.getArcIterator(), constructUEdge);
	}

	@Override
	public Iterator<Edge> getOutArcIterator(Node node) {
		return Iterators.transform(
				arcLace.getOutArcIterator(((HasId) node).getId()), constructArc);
	}

	@Override
	public Iterator<Edge> getInArcIterator(Node node) {
		return Iterators.transform(
				arcLace.getInArcIterator(((HasId) node).getId()), constructArc);
	}

	@Override
	public Iterator<Edge> getIncidentArcIterator(Node node) {
		return Iterators.transform(new ConcatIterator<Integer>(
				arcLace.getOutArcIterator(((HasId) node).getId()),
				arcLace.getInArcIterator(((HasId) node).getId())), constructArc);
	}

	@Override
	public Iterator<Edge> getIncidentUEdgeIterator(Node node) {
		return Iterators.transform(new ConcatIterator<Integer>(
				uEdgeLace.getOutArcIterator(((HasId) node).getId()),
				uEdgeLace.getInArcIterator(((HasId) node).getId())),
				constructUEdge);
	}
	
	public Node addNode(int id) {
		nodes.put(id, new CombinedNodeData());
		return graph.nodeFromId(id);
	}
	
	public void remove(Node node) {
		HasId n = (HasId) node;
		while (arcLace.getOutArcIterator(n.getId()).hasNext()) {
			Edge u = graph.arcFromId(arcLace.getOutArcIterator(n.getId()).next());
			remove(u);
		}
		while (arcLace.getInArcIterator(n.getId()).hasNext()) {
			Edge u = graph.arcFromId(arcLace.getInArcIterator(n.getId()).next());
			remove(u);
		}
		while (uEdgeLace.getOutArcIterator(n.getId()).hasNext()) {
			Edge u = graph.uEdgeFromId(uEdgeLace.getOutArcIterator(n.getId()).next());
			remove(u);
		}
		while (uEdgeLace.getInArcIterator(n.getId()).hasNext()) {
			Edge u = graph.uEdgeFromId(uEdgeLace.getInArcIterator(n.getId()).next());
			remove(u);
		}
		nodes.remove(n.getId());
	}

	public Edge addArc(int id, Node source, Node target) {
		HasId s = (HasId) source;
		HasId t = (HasId) target;
		arcData.put(id, new ArcData<Integer>(s.getId(), t.getId()));
		arcLace.laceArc(id, s.getId(), t.getId());
		return graph.arcFromId(id);
	}
	
	public Edge addUEdge(int id, Node source, Node target) {
		HasId s = (HasId) source;
		HasId t = (HasId) target;
		uEdgeData.put(id, new ArcData<Integer>(s.getId(), t.getId()));
		uEdgeLace.laceArc(id, s.getId(), t.getId());
		return graph.uEdgeFromId(id);
	}
	
	public void remove(Edge edge) {
		HasId e = (HasId) edge;
		if (graph.isDirected(edge)) {
			arcLace.remove(e.getId());
			arcData.remove(e.getId());
		} else {
			uEdgeLace.remove(e.getId());
			uEdgeData.remove(e.getId());
		}
	}
	
	protected class NodeMap<V> extends AbstractIdMap<Node, V> {
		
		private static final long serialVersionUID = 1167032365456389038L;

		@Override
		public String toString() {
			return "NodeMap [" +
					Joiner.on(", ").join(Iterables.transform(getNodes(), f)) +
					"]";
		}
	}

	@Override
	public <V> EntityMap<Node, V> createNodeMap() {
		return new NodeMap<V>();
	}
	
	protected class EdgeMap<V> implements EntityMap<Edge, V>, Serializable {
		
		private static final long serialVersionUID = -3899482894397129726L;

		protected ArcMap<V> arcMap = new ArcMap<V>();
		
		protected UEdgeMap<V> uEdgeMap = new UEdgeMap<V>();
		
		@Override
		public V put(Edge k, V v) {
			if (graph.isDirected(k)) {
				return arcMap.put((Edge) k, v);
			} else {
				return uEdgeMap.put((Edge) k, v);
			}
		}

		@Override
		public V get(Object k) {
			if (graph.isDirected((Edge) k)) {
				return arcMap.get((Edge) k);
			} else {
				return uEdgeMap.get((Edge) k);
			}
		}
		
		private class PrintEntry implements Function<Edge, String> {

			@Override
			public String apply(Edge k) {
				return "" + k + "=" + get(k);
			}
		};
		
		protected transient Function<Edge, String> f = new PrintEntry();
		
		@Override
		public String toString() {
			return "EdgeMap [" +
					Joiner.on(", ").join(Iterables.transform(getEdges(), f)) +
					"]";
		}

		@GwtIncompatible("ObjectInputStream")
		private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
			in.defaultReadObject();
			f = new PrintEntry();
		}
		
		@GwtIncompatible("ObjectOutputStream")
		private void writeObject(ObjectOutputStream out) throws IOException {
			out.defaultWriteObject();
		}
	}

	@Override
	public <V> EntityMap<Edge, V> createEdgeMap() {
		return new EdgeMap<V>();
	}
	
	protected class ArcMap<V> extends AbstractIdMap<Edge, V> {
		
		private static final long serialVersionUID = -5275110474745662141L;

		@Override
		public String toString() {
			return "ArcMap [" +
					Joiner.on(", ").join(Iterables.transform(getArcs(), f)) +
					"]";
		}
	}

	protected class UEdgeMap<V> extends AbstractIdMap<Edge, V> {
		
		private static final long serialVersionUID = 7117159471854014975L;

		@Override
		public String toString() {
			return "EdgeMap [" +
					Joiner.on(", ").join(Iterables.transform(getUEdges(), f)) +
					"]";
		}
	}
	
	@Override
	public <V> EntityMap<Edge, V> createUEdgeMap() {
		return new UEdgeMap<V>();
	}
	
	@Override
	public <V> EntityMap<Edge, V> createArcMap() {
		return new ArcMap<V>();
	}
	
	@GwtIncompatible("ObjectInputStream")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		arcLace = new ArcLace<Integer>(arcNodes, arcData);
		uEdgeLace = new ArcLace<Integer>(uEdgeNodes, uEdgeData);
		arcNodes = new ArcNodeMap();
		uEdgeNodes = new UEdgeNodeMap();
	}
	
	@GwtIncompatible("ObjectOutputStream")
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	@Override
	public Node addNode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Edge addUEdge(Node source, Node target) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addNodeMutationHandler(NodeMutationHandler handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addEdgeMutationHandler(EdgeMutationHandler handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Edge addArc(Node source, Node target) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node nodeFromId(int id) {
		return graph.nodeFromId(id);
	}

	@Override
	public Edge arcFromId(int id) {
		return graph.arcFromId(id);
	}

	@Override
	public Edge uEdgeFromId(int id) {
		return graph.uEdgeFromId(id);
	}
}
