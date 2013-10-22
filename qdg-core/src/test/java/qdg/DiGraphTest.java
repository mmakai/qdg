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

import java.util.List;

import com.google.common.collect.Lists;

import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;

public class DiGraphTest {

	public static void sourceAndTargetTest(DiGraph g) {
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge a0 = g.addArc(n0, n1);
		assertEquals(n0, g.getSource(a0));
		assertEquals(n1, g.getTarget(a0));
		
		Node n2 = g.addNode();
		Edge a1 = g.addArc(n1, n2);
		Edge a2 = g.addArc(n0, n2);
		assertEquals(n1, g.getSource(a1));
		assertEquals(n2, g.getTarget(a1));
		assertEquals(n0, g.getSource(a2));
		assertEquals(n2, g.getTarget(a2));
	}
	
	public static void nodeIteratorTest(DiGraph g) {
		assertFalse(g.getNodeIterator().hasNext());
		
		Node n0 = g.addNode();
		List<Node> nodes = Lists.newArrayList(g.getNodeIterator());
		assertEquals(1, nodes.size());
		assertEquals(n0, nodes.get(0));
		
		Node n1 = g.addNode();
		nodes = Lists.newArrayList(g.getNodeIterator());
		assertEquals(2, nodes.size());
		assertEquals(n0, nodes.get(0));
		assertEquals(n1, nodes.get(1));
	}
	
	public static void arcIteratorTest(DiGraph g) {
		assertFalse(g.getArcIterator().hasNext());
		
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge a0 = g.addArc(n0, n1);
		List<Edge> arcs = Lists.newArrayList(g.getArcIterator());
		assertEquals(1, arcs.size());
		assertEquals(a0, arcs.get(0));
		
		Node n2 = g.addNode();
		Edge a1 = g.addArc(n1, n2);
		arcs = Lists.newArrayList(g.getArcIterator());
		assertEquals(2, arcs.size());
		assertEquals(a0, arcs.get(0));
		assertEquals(a1, arcs.get(1));
	}
	
	public static void outArcIteratorTest(DiGraph g) {
		Node n0 = g.addNode();
		assertFalse(g.getOutArcIterator(n0).hasNext());
		
		Node n1 = g.addNode();
		Edge a0 = g.addArc(n0, n1);
		List<Edge> n0Out = Lists.newArrayList(g.getOutArcIterator(n0));
		assertEquals(1, n0Out.size());
		assertEquals(a0, n0Out.get(0));
		List<Edge> n1Out = Lists.newArrayList(g.getOutArcIterator(n1));
		assertEquals(0, n1Out.size());
		
		Node n2 = g.addNode();
		Edge a1 = g.addArc(n1, n2);
		Edge a2 = g.addArc(n0, n2);
		n0Out = Lists.newArrayList(g.getOutArcIterator(n0));
		assertEquals(2, n0Out.size());
		assertEquals(a0, n0Out.get(0));
		assertEquals(a2, n0Out.get(1));
		
		n1Out = Lists.newArrayList(g.getOutArcIterator(n1));
		assertEquals(1, n1Out.size());
		assertEquals(a1, n1Out.get(0));
		List<Edge> n2Out = Lists.newArrayList(g.getOutArcIterator(n2));
		assertEquals(0, n2Out.size());
	}
	
	public static void inArcIteratorTest(DiGraph g) {
		Node n0 = g.addNode();
		assertFalse(g.getInArcIterator(n0).hasNext());
		
		Node n1 = g.addNode();
		Edge a0 = g.addArc(n0, n1);
		List<Edge> n0In = Lists.newArrayList(g.getInArcIterator(n0));
		assertEquals(0, n0In.size());
		List<Edge> n1In = Lists.newArrayList(g.getInArcIterator(n1));
		assertEquals(1, n1In.size());
		assertEquals(a0, n1In.get(0));
		
		Node n2 = g.addNode();
		Edge a1 = g.addArc(n1, n2);
		Edge a2 = g.addArc(n0, n2);
		n0In = Lists.newArrayList(g.getInArcIterator(n0));
		assertEquals(0, n0In.size());
		n1In = Lists.newArrayList(g.getInArcIterator(n1));
		assertEquals(1, n1In.size());
		assertEquals(a0, n1In.get(0));
		List<Edge> n2In = Lists.newArrayList(g.getInArcIterator(n2));
		assertEquals(2, n2In.size());
		assertEquals(a1, n2In.get(0));
		assertEquals(a2, n2In.get(1));
	}
	
	public static void incidentArcIteratorTest(DiGraph g) {
		Node n0 = g.addNode();
		assertFalse(g.getIncidentArcIterator(n0).hasNext());
		
		Node n1 = g.addNode();
		Edge a0 = g.addArc(n0, n1);
		List<Edge> n0Incident = Lists.newArrayList(g.getIncidentArcIterator(n0));
		assertEquals(1, n0Incident.size());
		assertEquals(a0, n0Incident.get(0));
		List<Edge> n1Incident = Lists.newArrayList(g.getIncidentArcIterator(n1));
		assertEquals(1, n1Incident.size());
		assertEquals(a0, n1Incident.get(0));
		
		Node n2 = g.addNode();
		Edge a1 = g.addArc(n1, n2);
		Edge a2 = g.addArc(n0, n2);
		n0Incident = Lists.newArrayList(g.getIncidentArcIterator(n0));
		assertEquals(2, n0Incident.size());
		assertEquals(a0, n0Incident.get(0));
		assertEquals(a2, n0Incident.get(1));
		n1Incident = Lists.newArrayList(g.getIncidentArcIterator(n1));
		assertEquals(2, n1Incident.size());
		assertEquals(a1, n1Incident.get(0));
		assertEquals(a0, n1Incident.get(1));
		List<Edge> n2Incident = Lists.newArrayList(g.getIncidentArcIterator(n2));
		assertEquals(2, n2Incident.size());
		assertEquals(a1, n2Incident.get(0));
		assertEquals(a2, n2Incident.get(1));
	}
	
	public static void nodeMapTest(DiGraph g) {
		EntityMap<Node, Integer> map = g.createNodeMap();
		
		Node n0 = g.addNode();
		assertNull(map.get(n0));
		map.put(n0, 3);
		assertEquals(3, map.get(n0).intValue());
		
		Node n1 = g.addNode();
		assertNull(map.get(n1));
		map.put(n1, 5);
		assertEquals(3, map.get(n0).intValue());
		assertEquals(5, map.get(n1).intValue());
	}
	
	public static void arcMapTest(DiGraph g) {
		EntityMap<Edge, Integer> map = g.createArcMap();
		
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge a0 = g.addArc(n0, n1);
		assertNull(map.get(a0));
		map.put(a0, 3);
		assertEquals(3, map.get(a0).intValue());
		
		Node n2 = g.addNode();
		Edge a1 = g.addArc(n1, n2);
		assertNull(map.get(a1));
		map.put(a1, 5);
		assertEquals(3, map.get(a0).intValue());
		assertEquals(5, map.get(a1).intValue());
	}
	
	public static void edgeMapTest(DiGraph g) {
		EntityMap<Edge, Integer> map = g.createEdgeMap();
		
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge a0 = g.addArc(n0, n1);
		assertNull(map.get(a0));
		map.put(a0, 3);
		assertEquals(3, map.get(a0).intValue());
		
		Node n2 = g.addNode();
		Edge a1 = g.addArc(n1, n2);
		assertNull(map.get(a1));
		map.put(a1, 5);
		assertEquals(3, map.get(a0).intValue());
		assertEquals(5, map.get(a1).intValue());
	}
}
