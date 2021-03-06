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
import java.util.Map;
import java.util.WeakHashMap;

import qdg.api.DiIdGraph;
import qdg.api.EntityMap;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractDiGraph;
import qdg.bits.AbstractIdEntity;
import qdg.bits.AbstractIdMap;
import qdg.bits.IndexIterator;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

public class StaticOutArcDiGraph extends AbstractDiGraph
		implements DiIdGraph {
	
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
		
    	protected List<Edge> outArcs = new ArrayList<Edge>();
    	
		@Override
		public String toString() {
			return "NodeData [outArcs=" + outArcs + "]";
		}
	}
	
	public static class A extends AbstractIdEntity
			implements Edge {

		public A(int id) {
			super(id);
		}

		@Override
		public String toString() {
			return "Arc [id=" + id + "]";
		}
	}
	
	protected static class ArcData {
		
		protected N source, target;
		
		private ArcData(N source, N target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public String toString() {
			return "ArcData [source=" + source + ", target=" + target + "]";
		}
	}
	
	protected List<NodeData> nodes = new ArrayList<NodeData>();
	
	protected List<ArcData> arcs = new ArrayList<ArcData>();
	
	private Map<NodeMutationHandler, Object> nodeMutationHandlers;

	private Map<EdgeMutationHandler, Object> edgeMutationHandlers;
	
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
			return new A(id);
		}
	};
	
	@GwtIncompatible("WeakHashMap")
	public StaticOutArcDiGraph() {
		nodeMutationHandlers = new WeakHashMap<NodeMutationHandler, Object>();
		edgeMutationHandlers = new WeakHashMap<EdgeMutationHandler, Object>();
	}
	
	public StaticOutArcDiGraph(Map<NodeMutationHandler, Object> nodeMutationHandlers,
			Map<EdgeMutationHandler, Object> edgeMutationHandlers) {
		this.nodeMutationHandlers = nodeMutationHandlers;
		this.edgeMutationHandlers = edgeMutationHandlers;
	}
	
	@Override
	public Node getSource(Edge edge) {
		return arcs.get(((A) edge).getId()).source;
	}

	@Override
	public Node getTarget(Edge edge) {
		return arcs.get(((A) edge).getId()).target;
	}
	
	@Override
	public Iterator<Node> getNodeIterator() {
		return Iterators.transform(new IndexIterator<NodeData>(nodes), constructNode);
	}
	
	@Override
	public Iterator<Edge> getArcIterator() {
		return Iterators.transform(new IndexIterator<ArcData>(arcs), constructEdge);
	}
	
	@Override
	public Iterator<Edge> getOutArcIterator(Node node) {
		return nodes.get(((N) node).getId()).outArcs.iterator();
	}

	@Override
	public Iterator<Edge> getInArcIterator(Node node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Edge> getIncidentArcIterator(Node node) {
		throw new UnsupportedOperationException();
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
		A arc = new A(arcs.size());
		nodes.get(((N) source).getId()).outArcs.add(arc);
		arcs.add(new ArcData(((N) source), ((N) target)));
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onAdd(arc);
		}
		return arc;
	}
	
	@Override
	public void remove(Edge arc) {
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
	
	protected class ArcMap<V> extends AbstractIdMap<Edge, V> {
		
		@Override
		public String toString() {
			return "ArcMap [" +
					Joiner.on(", ").join(Iterables.transform(getArcs(), f)) +
					"]";
		}
	}
	
	@Override
	public <V> EntityMap<Edge, V> createArcMap() {
		return new ArcMap<V>();
	}

	@Override
	public Node nodeFromId(int id) {
		return new N(id);
	}

	@Override
	public Edge arcFromId(int id) {
		return new A(id);
	}
}
