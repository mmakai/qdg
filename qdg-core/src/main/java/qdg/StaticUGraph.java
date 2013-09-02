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
import qdg.api.MutableUGraph;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractIdEntity;
import qdg.bits.AbstractIdMap;
import qdg.bits.AbstractUGraph;
import qdg.bits.ConcatIterator;
import qdg.bits.IndexIterator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

public class StaticUGraph extends AbstractUGraph
		implements MutableUGraph {
	
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
	
	protected static class NodeData {
		
    	protected List<Edge> outUEdges = new ArrayList<Edge>();
    	
    	protected List<Edge> inUEdges = new ArrayList<Edge>();
    	
		@Override
		public String toString() {
			return "NodeData [outUEdges=" + outUEdges + ", inUEdges=" + inUEdges
					+ "]";
		}
	}
	
	public static class U extends AbstractIdEntity
			implements Edge {

		public U(int id) {
			super(id);
		}

		@Override
		public String toString() {
			return "U [id=" + id + "]";
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
	
	private static Function<Integer, Edge> constructEdge =
			new Function<Integer, Edge>() {

		@Override
		public Edge apply(Integer id) {
			return new U(id);
		}
	};
	
	@Override
	public Node getSource(Edge edge) {
		return uEdges.get(((U) edge).getId()).source;
	}

	@Override
	public Node getTarget(Edge edge) {
		return uEdges.get(((U) edge).getId()).target;
	}
	
	@Override
	public Iterator<Node> getNodeIterator() {
		return Iterators.transform(new IndexIterator<NodeData>(nodes), constructNode);
	}

	@Override
	public Iterator<Edge> getUEdgeIterator() {
		return Iterators.transform(new IndexIterator<EdgeData>(uEdges), constructEdge);
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
	public void remove(Node n) {
		throw new UnsupportedOperationException();	
	}
	
	@Override
	public void addNodeMutationHandler(NodeMutationHandler handler) {
		nodeMutationHandlers.put(handler, null);
	}
	
	@Override
	public Edge addUEdge(Node source, Node target) {
		U uEdge = new U(uEdges.size());
		nodes.get(((N) source).getId()).outUEdges.add(uEdge);
		nodes.get(((N) target).getId()).inUEdges.add(uEdge);
		uEdges.add(new EdgeData((N) source, (N) target));
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onAdd(uEdge);
		}
		return uEdge;
	}
	
	@Override
	public void remove(Edge uEdge) {
		throw new UnsupportedOperationException();	
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
}
