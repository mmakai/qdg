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

import qdg.api.EntityMap;
import qdg.api.MixedGraph;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;
import qdg.bits.AbstractUGraph;

/**
 * An undirected view of a mixed graph. The arcs are considered in the view as
 * undirected edges simply by forgetting their orientation. Undirected edges
 * are left and viewed as they are.
 * 
 * @author Marton Makai
 *
 */
public class MixedGraphAsUGraph extends AbstractUGraph {

	private MixedGraph g;

	public MixedGraphAsUGraph(MixedGraph g) {
		this.g = g;
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
		return g.getNodeIterator();
	}

	@Override
	public <V> EntityMap<Node, V> createNodeMap() {
		return g.createNodeMap();
	}

	@Override
	public Iterator<Edge> getUEdgeIterator() {
		return g.getEdgeIterator();
	}

	@Override
	public Iterator<Edge> getIncidentUEdgeIterator(Node node) {
		return g.getIncidentEdgeIterator(node);
	}

	@Override
	public <V> EntityMap<Edge, V> createUEdgeMap() {
		return g.createEdgeMap();
	}

	@Override
	public Node addNode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Node n) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Edge addUEdge(Node source, Node target) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Edge uEdge) {
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
}
