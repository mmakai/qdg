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

package qdg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import qdg.api.EntityMap;
import qdg.api.MutableMixedGraph;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractIdEntity;
import qdg.bits.AbstractIdMap;
import qdg.bits.AbstractMixedGraph;
import qdg.bits.ConcatIterator;
import qdg.bits.IndexIterator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

public class StaticMixedGraph extends AbstractMixedGraph
		implements MutableMixedGraph {
	
	public static class N extends AbstractIdEntity
			implements Node {
		
		public N(int id) {
			super(id);
		}
	
		@Override
		public String toString() {
			return "Node [id=" + id + "]";
		}
	}

	@Override
	public boolean isDirected(Edge edge) {
		return ((E) edge).directed;
	}
	
	protected static class NodeData {
		
    	protected List<Edge> outArcs = new ArrayList<Edge>();
    	
    	protected List<Edge> inArcs = new ArrayList<Edge>();

    	protected List<Edge> outUEdges = new ArrayList<Edge>();
    	
    	protected List<Edge> inUEdges = new ArrayList<Edge>();
    	
		@Override
		public String toString() {
			return "NodeData [outArcs=" + outArcs + ", inArcs=" + inArcs
					+ ", outUEdges=" + outUEdges + ", inUEdges=" + inUEdges
					+ "]";
		}
	}
	
	public static class E extends AbstractIdEntity
			implements Edge {

		private boolean directed;
		
		public E(int id, boolean directed) {
			super(id);
			this.directed = directed;
		}

		@Override
		public String toString() {
			return "Edge [directed=" + directed + ", id=" + id + "]";
		}
	}
	
	protected static class EdgeData {

		protected N source, target;

		private EdgeData(N source, N target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public String toString() {
			return "EdgeData [source=" + source + ", target=" + target + "]";
		}
	}

	protected List<NodeData> nodes = new ArrayList<NodeData>();
	
	protected List<EdgeData> arcs = new ArrayList<EdgeData>();
	
	protected List<EdgeData> uEdges = new ArrayList<EdgeData>();
	
	private WeakHashMap<NodeMutationHandler, Object> nodeMutationHandlers =
			new WeakHashMap<NodeMutationHandler, Object>();

	private WeakHashMap<EdgeMutationHandler, Object> edgeMutationHandlers =
			new WeakHashMap<EdgeMutationHandler, Object>();
	
	private static Function<Integer, Node> constructNode =
			new Function<Integer, Node>() {

		@Override
		public Node apply(Integer id) {
			return new N(id);
		}
	};
	
	private static Function<Integer, Edge> constructArc =
			new Function<Integer, Edge>() {

		@Override
		public Edge apply(Integer id) {
			return new E(id, true);
		}
	};
	
	private static Function<Integer, Edge> constructUEdge =
			new Function<Integer, Edge>() {

		@Override
		public Edge apply(Integer id) {
			return new E(id, false);
		}
	};
	
	@Override
	public Node getSource(Edge edge) {
		if (((E) edge).directed) {
			return arcs.get(((E) edge).getId()).source;
		} else {
			return uEdges.get(((E) edge).getId()).source;
		}
	}

	@Override
	public Node getTarget(Edge edge) {
		if (((E) edge).directed) {
			return arcs.get(((E) edge).getId()).target;
		} else {
			return uEdges.get(((E) edge).getId()).target;
		}
	}
	
	@Override
	public Iterator<Node> getNodeIterator() {
		return Iterators.transform(new IndexIterator<NodeData>(nodes), constructNode);
	}
	
	@Override
	public Iterator<Edge> getArcIterator() {
		return Iterators.transform(new IndexIterator<EdgeData>(arcs), constructArc);
	}

	@Override
	public Iterator<Edge> getUEdgeIterator() {
		return Iterators.transform(new IndexIterator<EdgeData>(uEdges), constructUEdge);
	}

	@Override
	public Iterator<Edge> getOutArcIterator(Node node) {
		return nodes.get(((N) node).getId()).outArcs.iterator();
	}

	@Override
	public Iterator<Edge> getInArcIterator(Node node) {
		return nodes.get(((N) node).getId()).inArcs.iterator();
	}

	@Override
	public Iterator<Edge> getIncidentArcIterator(Node node) {
		return new ConcatIterator<Edge>(
				nodes.get(((N) node).getId()).outArcs.iterator(),
				nodes.get(((N) node).getId()).inArcs.iterator());
	}

	@Override
	public Iterator<Edge> getIncidentUEdgeIterator(Node node) {
		return new ConcatIterator<Edge>(
				nodes.get(((N) node).getId()).outUEdges.iterator(),
				nodes.get(((N) node).getId()).inUEdges.iterator());
	}
	
	@Override
	public Node addNode() {
		N node = new N(nodes.size());
		nodes.add(new NodeData());
		for (NodeMutationHandler handler : nodeMutationHandlers.keySet()) {
			handler.onAdd(node);
		}
		return node;
	}
	
	@Override
	public void remove(Node node) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void addNodeMutationHandler(NodeMutationHandler handler) {
		nodeMutationHandlers.put(handler, null);
	}
	
	@Override
	public Edge addArc(Node source, Node target) {
		E arc = new E(arcs.size(), true);
		nodes.get(((N) source).getId()).outArcs.add(arc);
		nodes.get(((N) target).getId()).inArcs.add(arc);
		arcs.add(new EdgeData(((N) source), ((N) target)));
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onAdd(arc);
		}
		return arc;
	}
	
	@Override
	public void remove(Edge edge) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Edge addUEdge(Node source, Node target) {
		Edge uEdge = new E(uEdges.size(), false);
		nodes.get(((N) source).getId()).outUEdges.add(uEdge);
		nodes.get(((N) target).getId()).inUEdges.add(uEdge);
		uEdges.add(new EdgeData((N) source, (N) target));
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onAdd(uEdge);
		}
		return uEdge;
	}
	
	@Override
	public void addEdgeMutationHandler(EdgeMutationHandler handler) {
		edgeMutationHandlers.put(handler, null);
	}
	
	protected class NodeMap<V> extends AbstractIdMap<Node, V> {
		
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
	
	protected class EdgeMap<V> implements EntityMap<Edge, V> {
		
		protected ArcMap<V> arcMap = new ArcMap<V>();
		
		protected UEdgeMap<V> uEdgeMap = new UEdgeMap<V>();
		
		@Override
		public V put(Edge k, V v) {
			if (((E) k).directed) {
				return arcMap.put((Edge) k, v);
			} else {
				return uEdgeMap.put((Edge) k, v);
			}
		}

		@Override
		public V get(Object k) {
			if (((E) k).directed) {
				return arcMap.get((Edge) k);
			} else {
				return uEdgeMap.get((Edge) k);
			}
		}
		
		protected Function<Edge, String> f = new Function<Edge, String>() {

			@Override
			public String apply(Edge k) {
				return "" + k + "=" + get(k);
			}
		};
		
		@Override
		public String toString() {
			return "EdgeMap [" +
					Joiner.on(", ").join(Iterables.transform(getEdges(), f)) +
					"]";
		}
	}

	@Override
	public <V> EntityMap<Edge, V> createEdgeMap() {
		return new EdgeMap<V>();
	}
	
	protected class ArcMap<V> extends AbstractIdMap<Edge, V> {
		
		@Override
		public String toString() {
			return "ArcMap [" +
					Joiner.on(", ").join(Iterables.transform(getArcs(), f)) +
					"]";
		}
	}

	protected class UEdgeMap<V> extends AbstractIdMap<Edge, V> {
		
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
}
