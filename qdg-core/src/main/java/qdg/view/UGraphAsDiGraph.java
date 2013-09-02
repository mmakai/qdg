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

package qdg.view;

import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import qdg.api.EntityMap;
import qdg.api.UGraph;
import qdg.bits.AbstractDiGraph;
import qdg.bits.ConcatIterator;

/**
 * Bidirected graph view of an undirected graph. Two arcs are created for each
 * undirected edge. A forward arc matching the inherent direction of the edge,
 * and a backward arc in the opposite direction.
 */
public class UGraphAsDiGraph extends AbstractDiGraph {

	public static class A implements Edge {

		private Edge original;

		private boolean backward;

		public A(Edge original, boolean backward) {
			this.original = original;
			this.backward = backward;
		}

		public Edge getOriginal() {
			return original;
		}

		public boolean isBackward() {
			return backward;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (backward ? 1231 : 1237);
			result = prime * result
					+ ((original == null) ? 0 : original.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			A other = (A) obj;
			if (backward != other.backward)
				return false;
			if (original == null) {
				if (other.original != null)
					return false;
			} else if (!original.equals(other.original))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "A [original=" + original + ", backward=" + backward + "]";
		}
	}

	private UGraph g;

	public UGraphAsDiGraph(UGraph g) {
		this.g = g;
	}

	private Function<Edge, Edge> forwardEdge = new Function<Edge, Edge>() {

		@Override
		public Edge apply(Edge edge) {
			return new A(edge, false);
		}
	};

	private Function<Edge, Edge> backwardEdge = new Function<Edge, Edge>() {

		@Override
		public Edge apply(Edge edge) {
			return new A(edge, true);
		}
	};

	@Override
	public Node getSource(Edge edge) {
		A a = (A) edge;
		if (!a.backward) {
			return g.getSource(a.original);
		} else {
			return g.getTarget(a.original);
		}
	}

	@Override
	public Node getTarget(Edge edge) {
		A a = (A) edge;
		if (!a.backward) {
			return g.getTarget(a.original);
		} else {
			return g.getSource(a.original);
		}
	}

	@Override
	public Iterator<Node> getNodeIterator() {
		return g.getNodeIterator();
	}

	@Override
	public <V> EntityMap<Node, V> createNodeMap() {
		return g.createNodeMap();
	}

	@Override
	public Iterator<Edge> getArcIterator() {
		Iterator<Edge> forwardUEdges = Iterators.transform(
				g.getUEdgeIterator(), forwardEdge);
		Iterator<Edge> backwardUEdges = Iterators.transform(
				g.getUEdgeIterator(), backwardEdge);
		return new ConcatIterator<Edge>(forwardUEdges, backwardUEdges);
	}

	private class OutArcIterator extends UnmodifiableIterator<Edge> {

		private Node source;

		private Iterator<Edge> incidentUEdges;

		public OutArcIterator(Node source) {
			this.source = source;
			incidentUEdges = g.getIncidentUEdgeIterator(source);
		}

		@Override
		public boolean hasNext() {
			return incidentUEdges.hasNext();
		}

		@Override
		public Edge next() {
			Edge edge = incidentUEdges.next();
			return new A(edge, !g.getSource(edge).equals(source));
		}
	}

	@Override
	public Iterator<Edge> getOutArcIterator(Node node) {
		return new OutArcIterator(node);
	}

	private class InArcIterator extends UnmodifiableIterator<Edge> {

		private Node target;

		private Iterator<Edge> incidentUEdges;

		public InArcIterator(Node target) {
			this.target = target;
			incidentUEdges = g.getIncidentUEdgeIterator(target);
		}

		@Override
		public boolean hasNext() {
			return incidentUEdges.hasNext();
		}

		@Override
		public Edge next() {
			Edge edge = incidentUEdges.next();
			return new A(edge, !g.getTarget(edge).equals(target));
		}
	}

	@Override
	public Iterator<Edge> getInArcIterator(Node node) {
		return new InArcIterator(node);
	}

	@Override
	public Iterator<Edge> getIncidentArcIterator(Node node) {
		Iterator<Edge> forwardUEdges = Iterators.transform(
				g.getIncidentEdgeIterator(node), forwardEdge);
		Iterator<Edge> backwardUEdges = Iterators.transform(
				g.getIncidentUEdgeIterator(node), backwardEdge);
		return new ConcatIterator<Edge>(forwardUEdges, backwardUEdges);
	}

	protected class ArcMap<V> implements EntityMap<Edge, V> {

		private EntityMap<Edge, V> forwardUEdges = g.createUEdgeMap();

		private EntityMap<Edge, V> backwardUEdges = g.createUEdgeMap();

		@Override
		public V put(Edge k, V v) {
			A a = (A) k;
			if (!a.backward) {
				return forwardUEdges.put((Edge) a.original, v);
			} else {
				return backwardUEdges.put((Edge) a.original, v);
			}
		}

		@Override
		public V get(Object k) {
			A a = (A) k;
			if (!a.backward) {
				return forwardUEdges.get((Edge) a.original);
			} else {
				return backwardUEdges.get((Edge) a.original);
			}
		}
	}

	@Override
	public <V> EntityMap<Edge, V> createArcMap() {
		return new ArcMap<V>();
	}
}