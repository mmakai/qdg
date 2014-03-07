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

package qdg.view;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import qdg.EntityMaps.NullMap;
import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractMixedGraph;

public class DiGraphAsMixedGraph extends AbstractMixedGraph {

	protected DiGraph graph;
	
	public DiGraphAsMixedGraph() {
	}
	
	public DiGraphAsMixedGraph(DiGraph graph) {
		this.graph = graph;
	}
	
	public void setGraph(DiGraph graph) {
		this.graph = graph;
	}
	
	public DiGraph getGraph() {
		return graph;
	}
	
	@Override
	public boolean isDirected(Edge edge) {
		return true;
	}

	@Override
	public Node addNode() {
		return graph.addNode();
	}

	@Override
	public void remove(Node n) {
		graph.remove(n);
	}

	@Override
	public Edge addUEdge(Node source, Node target) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Edge edge) {
		graph.remove(edge);
	}

	@Override
	public Node getSource(Edge edge) {
		return graph.getSource(edge);
	}

	@Override
	public Node getTarget(Edge edge) {
		return graph.getTarget(edge);
	}

	@Override
	public Iterator<Node> getNodeIterator() {
		return graph.getNodeIterator();
	}

	@Override
	public <V> EntityMap<Node, V> createNodeMap() {
		return graph.createNodeMap();
	}

	@Override
	public <V> EntityMap<Edge, V> createEdgeMap() {
		return graph.createEdgeMap();
	}

	@Override
	public Iterator<Edge> getUEdgeIterator() {
		return Iterators.emptyIterator();
	}

	@Override
	public Iterator<Edge> getIncidentUEdgeIterator(Node node) {
		return Iterators.emptyIterator();
	}

	@Override
	public <V> EntityMap<Edge, V> createUEdgeMap() {
		return new NullMap<Edge, V>();
	}

	@Override
	public void addNodeMutationHandler(NodeMutationHandler handler) {
		graph.addNodeMutationHandler(handler);
	}

	@Override
	public void addEdgeMutationHandler(EdgeMutationHandler handler) {
		graph.addEdgeMutationHandler(handler);
	}

	@Override
	public Edge addArc(Node source, Node target) {
		return graph.addArc(source, target);
	}

	@Override
	public Iterator<Edge> getArcIterator() {
		return graph.getArcIterator();
	}

	@Override
	public Iterator<Edge> getOutArcIterator(Node node) {
		return graph.getOutArcIterator(node);
	}

	@Override
	public Iterator<Edge> getInArcIterator(Node node) {
		return graph.getInArcIterator(node);
	}

	@Override
	public Iterator<Edge> getIncidentArcIterator(Node node) {
		return graph.getIncidentArcIterator(node);
	}

	@Override
	public <V> EntityMap<Edge, V> createArcMap() {
		return graph.createArcMap();
	}
}
