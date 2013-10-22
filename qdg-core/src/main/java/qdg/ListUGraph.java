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
import java.util.WeakHashMap;

import qdg.api.EntityMap;
import qdg.api.UIdGraph;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractIdEntity;
import qdg.bits.AbstractIdMap;
import qdg.bits.AbstractUGraph;
import qdg.bits.ArcLace;
import qdg.bits.ArcLace.ArcData;
import qdg.bits.ArcLace.NodeData;
import qdg.bits.ConcatIterator;

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
public class ListUGraph extends AbstractUGraph
		implements UIdGraph, Serializable {
	
	private static final long serialVersionUID = 8729615163564836464L;

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
	
	protected SparseArrayList<NodeData> nodes =
			new SparseArrayList<NodeData>();
	
	protected SparseArrayList<ArcData<Integer>> uEdgeData =
			new SparseArrayList<ArcData<Integer>>();
	
	protected transient ArcLace<Integer> uEdgeLace = new ArcLace<Integer>(
			nodes, uEdgeData);
	
	private transient WeakHashMap<NodeMutationHandler, Object> nodeMutationHandlers =
			new WeakHashMap<NodeMutationHandler, Object>();
	
	private transient WeakHashMap<EdgeMutationHandler, Object> edgeMutationHandlers =
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
		return new N(uEdgeLace.getSource(((U) edge).getId()));
	}

	@Override
	public Node getTarget(Edge edge) {
		return new N(uEdgeLace.getTarget(((U) edge).getId()));
	}
	
	@Override
	public Iterator<Node> getNodeIterator() {
		return Iterators.transform(nodes.keyIterator(), constructNode);
	}

	@Override
	public Iterator<Edge> getUEdgeIterator() {
		return Iterators.transform(uEdgeLace.getArcIterator(), constructEdge);
	}

	@Override
	public Iterator<Edge> getIncidentUEdgeIterator(Node node) {
		return Iterators.transform(new ConcatIterator<Integer>(
				uEdgeLace.getOutArcIterator(((N) node).getId()),
				uEdgeLace.getInArcIterator(((N) node).getId())), constructEdge);
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
		while (uEdgeLace.getOutArcIterator(n.getId()).hasNext()) {
			U u = new U(uEdgeLace.getOutArcIterator(n.getId()).next());
			remove(u);
		}
		while (uEdgeLace.getInArcIterator(n.getId()).hasNext()) {
			U u = new U(uEdgeLace.getInArcIterator(n.getId()).next());
			remove(u);
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
	public Edge addUEdge(Node source, Node target) {
		N s = (N) source;
		N t = (N) target;
		int id = uEdgeData.add(new ArcData<Integer>(s.getId(), t.getId()));
		uEdgeLace.laceArc(id, s.getId(), t.getId());
		Edge uEdge = new U(id);
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onAdd(uEdge);
		}
		return uEdge;
	}
	
	@Override
	public void remove(Edge uEdge) {
		U u = (U) uEdge;
		for (EdgeMutationHandler handler : edgeMutationHandlers.keySet()) {
			handler.onRemove(uEdge);
		}
		uEdgeLace.remove(u.getId());
		uEdgeData.remove(u.getId());
	}
	
	@Override
	public void addEdgeMutationHandler(EdgeMutationHandler handler) {
		edgeMutationHandlers.put(handler, null);
	}
	
	protected class NodeMap<V> extends AbstractIdMap<Node, V>
			implements NodeMutationHandler {
		
		private static final long serialVersionUID = -3170458638427724449L;

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
	
	protected class UEdgeMap<V> extends AbstractIdMap<Edge, V>
			implements EdgeMutationHandler {
		
		private static final long serialVersionUID = -6185889652004433598L;

		@Override
		public String toString() {
			return "EdgeMap [" +
					Joiner.on(", ").join(Iterables.transform(getUEdges(), f)) +
					"]";
		}
	}
	
	@Override
	public <V> EntityMap<Edge, V> createUEdgeMap() {
		UEdgeMap<V> map = new UEdgeMap<V>();
		addEdgeMutationHandler(map);
		return map;
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		uEdgeLace = new ArcLace<Integer>(nodes, uEdgeData);
		nodeMutationHandlers = new WeakHashMap<NodeMutationHandler, Object>();
		edgeMutationHandlers = new WeakHashMap<EdgeMutationHandler, Object>();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	@Override
	public Node nodeFromId(int id) {
		return new N(id);
	}

	@Override
	public Edge uEdgeFromId(int id) {
		return new U(id);
	}
}
