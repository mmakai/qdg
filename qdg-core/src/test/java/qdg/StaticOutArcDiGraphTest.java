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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;

import com.google.common.collect.Iterators;

public class StaticOutArcDiGraphTest {

	private StaticOutArcDiGraph g;
	
	@Before
	public void setUp() {
		g = new StaticOutArcDiGraph();
	}
	
	@Test
	public void emptyTest() {
		assertFalse(g.getNodeIterator().hasNext());
		assertFalse(g.getEdgeIterator().hasNext());
		assertFalse(g.getArcIterator().hasNext());
	}
	
	@Test
	public void oneNodeTest() {
		Node n = g.addNode();
		List<Node> nodes = new ArrayList<Node>();
		Iterators.addAll(nodes, g.getNodeIterator());
		assertEquals(1, nodes.size());
		assertEquals(n, nodes.get(0));
		
		List<Edge> outArcs = new ArrayList<Edge>();
		Iterators.addAll(outArcs, g.getOutArcIterator(n));
		assertEquals(0, outArcs.size());
		
		assertFalse(g.getEdgeIterator().hasNext());
		assertFalse(g.getArcIterator().hasNext());
	}
	
	@Test
	public void twoNodesTest1() {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		List<Node> nodes = new ArrayList<Node>();
		Edge e = g.addArc(n1, n2);
		Iterators.addAll(nodes, g.getNodeIterator());
		assertEquals(2, nodes.size());
		assertEquals(n1, nodes.get(0));
		assertEquals(n2, nodes.get(1));
		
		List<Edge> n1OutArcs = new ArrayList<Edge>();
		Iterators.addAll(n1OutArcs, g.getOutArcIterator(n1));
		assertEquals(1, n1OutArcs.size());
		assertEquals(e, n1OutArcs.get(0));
		
		List<Edge> n2OutArcs = new ArrayList<Edge>();
		Iterators.addAll(n2OutArcs, g.getOutArcIterator(n2));
		assertEquals(0, n2OutArcs.size());
		
		List<Edge> edges = new ArrayList<Edge>();
		Iterators.addAll(edges, g.getEdgeIterator());
		assertEquals(1, edges.size());
		
		List<Edge> arcs = new ArrayList<Edge>();
		Iterators.addAll(arcs, g.getArcIterator());
		assertEquals(1, arcs.size());
	}
	
	@Test
	public void threeNodesTest() {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		Node n3 = g.addNode();
		List<Node> nodes = new ArrayList<Node>();
		Iterators.addAll(nodes, g.getNodeIterator());
		Edge e1 = g.addArc(n1, n2);
		Edge e2 = g.addArc(n1, n3);
		Edge e3 = g.addArc(n2, n3);
		assertEquals(3, nodes.size());
		assertEquals(n1, nodes.get(0));
		assertEquals(n2, nodes.get(1));
		assertEquals(n3, nodes.get(2));
		
		List<Edge> n1OutArcs = new ArrayList<Edge>();
		Iterators.addAll(n1OutArcs, g.getOutArcIterator(n1));
		assertEquals(2, n1OutArcs.size());
		assertEquals(e1, n1OutArcs.get(0));
		assertEquals(e2, n1OutArcs.get(1));

		List<Edge> n2ArcArcs = new ArrayList<Edge>();
		Iterators.addAll(n2ArcArcs, g.getOutArcIterator(n2));
		assertEquals(1, n2ArcArcs.size());
		assertEquals(e3, n2ArcArcs.get(0));
		
		List<Edge> n3OutArcs = new ArrayList<Edge>();
		Iterators.addAll(n3OutArcs, g.getOutArcIterator(n3));
		assertEquals(0, n3OutArcs.size());
		
		List<Edge> edges = new ArrayList<Edge>();
		Iterators.addAll(edges, g.getEdgeIterator());
		assertEquals(3, edges.size());
		
		List<Edge> arcs = new ArrayList<Edge>();
		Iterators.addAll(arcs, g.getArcIterator());
		assertEquals(3, arcs.size());
	}
}
