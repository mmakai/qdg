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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import qdg.EntityMaps;
import qdg.api.EntityMap;
import qdg.api.MixedGraph;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractMixedGraph;

public class SpanningSubMixedGraph extends AbstractMixedGraph
		implements MixedGraph {
	
	protected MixedGraph g;
	
	protected Predicate<Node> nodeFilterPredicate;
	
	protected Predicate<Edge> spanningEdgeFilterPredicate =
			new Predicate<Edge>() {

		@Override
		public boolean apply(Edge edge) {
			return nodeFilterPredicate.apply(g.getSource(edge)) &&
					nodeFilterPredicate.apply(g.getTarget(edge));
		}
	};
	
	public SpanningSubMixedGraph(MixedGraph g,
			Predicate<Node> nodeFilterPredicate) {
		this.g = g;
		this.nodeFilterPredicate = nodeFilterPredicate;
	}

	public SpanningSubMixedGraph(MixedGraph g,
			EntityMap<Node, Boolean> nodeFilterMap) {
		this(g, new EntityMaps.AsPredicate<Node>(nodeFilterMap));
	}

	@Override
	public boolean isDirected(Edge edge) {
		return g.isDirected(edge);
	}

	@Override
	public Node getSource(Edge edge) {
		return g.getSource(edge);
	}

	@Override
	public Node getTarget(Edge edge) {
		return g.getTarget(edge);
	}

	@Override
	public Iterator<Node> getNodeIterator() {
		return Iterators.filter(g.getNodeIterator(), nodeFilterPredicate);
	}

	@Override
	public <V> EntityMap<Node, V> createNodeMap() {
		return g.createNodeMap();
	}

	@Override
	public <V> EntityMap<Edge, V> createEdgeMap() {
		return g.createEdgeMap();
	}

	@Override
	public Iterator<Edge> getUEdgeIterator() {
		return Iterators.filter(g.getUEdgeIterator(),
				spanningEdgeFilterPredicate);
	}

	@Override
	public Iterator<Edge> getIncidentUEdgeIterator(Node node) {
		return Iterators.filter(g.getIncidentUEdgeIterator(node),
				spanningEdgeFilterPredicate);
	}

	@Override
	public <V> EntityMap<Edge, V> createUEdgeMap() {
		return g.createUEdgeMap();
	}

	@Override
	public Iterator<Edge> getArcIterator() {
		return Iterators.filter(g.getArcIterator(),
				spanningEdgeFilterPredicate);
	}

	@Override
	public Iterator<Edge> getOutArcIterator(Node node) {
		return Iterators.filter(g.getOutArcIterator(node),
				spanningEdgeFilterPredicate);
	}

	@Override
	public Iterator<Edge> getInArcIterator(Node node) {
		return Iterators.filter(g.getInArcIterator(node),
				spanningEdgeFilterPredicate);
	}

	@Override
	public Iterator<Edge> getIncidentArcIterator(Node node) {
		return Iterators.filter(g.getIncidentArcIterator(node),
				spanningEdgeFilterPredicate);
	}

	@Override
	public <V> EntityMap<Edge, V> createArcMap() {
		return g.createArcMap();
	}
	
	/* (non-Javadoc)
	 * @see qdg.api.DiGraph#addNode()
	 * 
	 * Optional operation. Same method of the underlying graph is called.
	 */
	@Override
	public Node addNode() {
		return g.addNode();
	}

	/* (non-Javadoc)
	 * @see qdg.api.DiGraph#remove(qdg.api.Graph.Node)
	 * 
	 * Optional operation. Same method of the underlying graph is called.
	 */
	@Override
	public void remove(Node n) {
		g.remove(n);
	}

	/* (non-Javadoc)
	 * @see qdg.api.DiGraph#addArc(qdg.api.Graph.Node, qdg.api.Graph.Node)
	 * 
	 * Optional operation. Same method of the underlying graph is called.
	 */
	@Override
	public Edge addArc(Node source, Node target) {
		return g.addArc(source, target);
	}

	/* (non-Javadoc)
	 * @see qdg.api.DiGraph#remove(qdg.api.Graph.Edge)
	 * 
	 * Optional operation. Same method of the underlying graph is called.
	 */
	@Override
	public void remove(Edge arc) {
		g.remove(arc);
	}

	@Override
	public void addNodeMutationHandler(NodeMutationHandler handler) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addEdgeMutationHandler(EdgeMutationHandler handler) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see qdg.api.UGraph#addUEdge(qdg.api.Graph.Node, qdg.api.Graph.Node)
	 * 
	 * Optional operation. Same method of the underlying graph is called.
	 */
	@Override
	public Edge addUEdge(Node source, Node target) {
		return g.addUEdge(source, target);
	}
}
