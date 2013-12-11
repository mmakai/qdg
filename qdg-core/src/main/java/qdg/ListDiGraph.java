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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import qdg.api.DiIdGraph;
import qdg.api.EntityMap;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractDiGraph;
import qdg.bits.AbstractIdEntity;
import qdg.bits.AbstractIdMap;
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
 * Note that serialization skips the registry of maps registered into the
 * graph. The graph provided maps can be recreated by serializing, deserializing
 * and registering them again to the graph.
 * 
 * @author Marton Makai
 */
public class ListDiGraph extends AbstractDiGraph
		implements DiIdGraph, Serializable {
	
	private static final long serialVersionUID = -815385180481551292L;

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
	
	protected SparseArrayList<NodeData> nodes =
			new SparseArrayList<NodeData>();
	
	protected SparseArrayList<ArcData<Integer>> arcData =
			new SparseArrayList<ArcData<Integer>>();
	
	protected transient ArcLace<Integer> arcLace = new ArcLace<Integer>(nodes, arcData);
	
	private transient Map<NodeMutationHandler, Object> nodeMutationHandlers;

	private transient Map<EdgeMutationHandler, Object> edgeMutationHandlers;
	
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
	public ListDiGraph() {
		nodeMutationHandlers = new WeakHashMap<NodeMutationHandler, Object>();
		edgeMutationHandlers = new WeakHashMap<EdgeMutationHandler, Object>();
	}
	
	public ListDiGraph(Map<NodeMutationHandler, Object> nodeMutationHandlers,
			Map<EdgeMutationHandler, Object> edgeMutationHandlers) {
		this.nodeMutationHandlers = nodeMutationHandlers;
		this.edgeMutationHandlers = edgeMutationHandlers;
	}
	
	@Override
	public Node getSource(Edge edge) {
		return new N(arcLace.getSource(((A) edge).getId()));
	}

	@Override
	public Node getTarget(Edge edge) {
		return new N(arcLace.getTarget(((A) edge).getId()));
	}
	
	@Override
	public Iterator<Node> getNodeIterator() {
		return Iterators.transform(nodes.keyIterator(), constructNode);
	}
	
	@Override
	public Iterator<Edge> getArcIterator() {
		return Iterators.transform(arcLace.getArcIterator(), constructEdge);
	}

	@Override
	public Iterator<Edge> getOutArcIterator(Node node) {
		return Iterators.transform(
				arcLace.getOutArcIterator(((N) node).getId()), constructEdge);
	}

	@Override
	public Iterator<Edge> getInArcIterator(Node node) {
		return Iterators.transform(
				arcLace.getInArcIterator(((N) node).getId()), constructEdge);
	}

	@Override
	public Iterator<Edge> getIncidentArcIterator(Node node) {
		return Iterators.transform(new ConcatIterator<Integer>(
				arcLace.getOutArcIterator(((N) node).getId()),
				arcLace.getInArcIterator(((N) node).getId())), constructEdge);
	}
	
	@Override
	public Node addNode() {
		int id = nodes.add(new NodeData());
		Node n = new N(id);
		for (NodeMutationHandler handler : nodeMutationHandlers.keySet()) {
			handler.onAdd(n);
		}
		return n;
	}

	@Override
	public void remove(Node node) {
		N n = (N) node;
		while (arcLace.getOutArcIterator(n.getId()).hasNext()) {
			A a = new A(arcLace.getOutArcIterator(n.getId()).next());
			remove(a);
		}
		while (arcLace.getInArcIterator(n.getId()).hasNext()) {
			A a = new A(arcLace.getInArcIterator(n.getId()).next());
			remove(a);
		}
		for (NodeMutationHandler handler : nodeMutationHandlers.keySet()) {
			handler.onRemove(node);
		}
		nodes.remove(n.getId());
	}
	
	@Override
	public void addNodeMutationHandler(NodeMutationHandler handler) {
		nodeMutationHandlers.put(handler, null);
	}
	
	@Override
	public Edge addArc(Node source, Node target) {
		N s = (N) source;
		N t = (N) target;
		int id = arcData.add(new ArcData<Integer>(s.getId(), t.getId()));
		arcLace.laceArc(id, s.getId(), t.getId());
		Edge arc = new A(id);
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onAdd(arc);
		}
		return arc;
	}
	
	@Override
	public void remove(Edge arc) {
		A a = (A) arc;
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onRemove(arc);
		}
		arcLace.remove(a.getId());
		arcData.remove(a.getId());
	}
	
	@Override
	public void addEdgeMutationHandler(EdgeMutationHandler handler) {
		edgeMutationHandlers.put(handler, null);
	}
	
	protected class NodeMap<V> extends AbstractIdMap<Node, V>
			implements NodeMutationHandler {
		
		private static final long serialVersionUID = -2009626499356850611L;

		@Override
		public String toString() {
			return "NodeMap [" +
					Joiner.on(", ").join(Iterables.transform(getNodes(), f)) +
					"]";
		}
	}

	@Override
	public <V> EntityMap<Node, V> createNodeMap() {
		NodeMap<V> map = new NodeMap<V>();
		addNodeMutationHandler(map);
		return map;
	}
	
	protected class ArcMap<V> extends AbstractIdMap<Edge, V>
			implements EdgeMutationHandler {
		
		private static final long serialVersionUID = 5612668821316998753L;

		@Override
		public String toString() {
			return "ArcMap [" +
					Joiner.on(", ").join(Iterables.transform(getArcs(), f)) +
					"]";
		}
	}

	@Override
	public <V> EntityMap<Edge, V> createArcMap() {
		ArcMap<V> map = new ArcMap<V>();
		addEdgeMutationHandler(map);
		return map;
	}
	
	@GwtIncompatible("ObjectInputStream")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		arcLace = new ArcLace<Integer>(nodes, arcData);
		nodeMutationHandlers = new WeakHashMap<NodeMutationHandler, Object>();
		edgeMutationHandlers = new WeakHashMap<EdgeMutationHandler, Object>();
	}
	
	@GwtIncompatible("ObjectOutputStream")
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
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
