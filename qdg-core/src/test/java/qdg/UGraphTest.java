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

import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;
import qdg.api.UGraph;

public class UGraphTest {

	public static void sourceAndTargetTest(UGraph g) {
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge u0 = g.addUEdge(n0, n1);
		assertEquals(n0, g.getSource(u0));
		assertEquals(n1, g.getTarget(u0));
		
		Node n2 = g.addNode();
		Edge u1 = g.addUEdge(n1, n2);
		Edge u2 = g.addUEdge(n0, n2);
		assertEquals(n1, g.getSource(u1));
		assertEquals(n2, g.getTarget(u1));
		assertEquals(n0, g.getSource(u2));
		assertEquals(n2, g.getTarget(u2));
	}
	
	public static void nodeIteratorTest(UGraph g) {
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
	
	public static void uEdgeIteratorTest(UGraph g) {
		assertFalse(g.getUEdgeIterator().hasNext());
		
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge u0 = g.addUEdge(n0, n1);
		List<Edge> uEdges = Lists.newArrayList(g.getUEdgeIterator());
		assertEquals(1, uEdges.size());
		assertEquals(u0, uEdges.get(0));
		
		Node n2 = g.addNode();
		Edge u1 = g.addUEdge(n1, n2);
		uEdges = Lists.newArrayList(g.getUEdgeIterator());
		assertEquals(2, uEdges.size());
		assertEquals(u0, uEdges.get(0));
		assertEquals(u1, uEdges.get(1));
	}
	
	public static void incidentUEdgeIteratorTest(UGraph g) {
		Node n0 = g.addNode();
		assertFalse(g.getIncidentUEdgeIterator(n0).hasNext());
		
		Node n1 = g.addNode();
		Edge u0 = g.addUEdge(n0, n1);
		List<Edge> n0Incident = Lists.newArrayList(g.getIncidentUEdgeIterator(n0));
		assertEquals(1, n0Incident.size());
		assertEquals(u0, n0Incident.get(0));
		List<Edge> n1Incident = Lists.newArrayList(g.getIncidentUEdgeIterator(n1));
		assertEquals(1, n1Incident.size());
		assertEquals(u0, n1Incident.get(0));
		
		Node n2 = g.addNode();
		Edge u1 = g.addUEdge(n1, n2);
		Edge u2 = g.addUEdge(n0, n2);
		n0Incident = Lists.newArrayList(g.getIncidentUEdgeIterator(n0));
		assertEquals(2, n0Incident.size());
		assertEquals(u0, n0Incident.get(0));
		assertEquals(u2, n0Incident.get(1));
		n1Incident = Lists.newArrayList(g.getIncidentUEdgeIterator(n1));
		assertEquals(2, n1Incident.size());
		assertEquals(u1, n1Incident.get(0));
		assertEquals(u0, n1Incident.get(1));
		List<Edge> n2Incident = Lists.newArrayList(g.getIncidentUEdgeIterator(n2));
		assertEquals(2, n2Incident.size());
		assertEquals(u1, n2Incident.get(0));
		assertEquals(u2, n2Incident.get(1));
	}
	
	public static void nodeMapTest(UGraph g) {
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
	
	public static void uEdgeMapTest(UGraph g) {
		EntityMap<Edge, Integer> map = g.createUEdgeMap();
		
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge u0 = g.addUEdge(n0, n1);
		assertNull(map.get(u0));
		map.put(u0, 3);
		assertEquals(3, map.get(u0).intValue());
		
		Node n2 = g.addNode();
		Edge u1 = g.addUEdge(n1, n2);
		assertNull(map.get(u1));
		map.put(u1, 5);
		assertEquals(3, map.get(u0).intValue());
		assertEquals(5, map.get(u1).intValue());
	}
	
	public static void edgeMapTest(UGraph g) {
		EntityMap<Edge, Integer> map = g.createEdgeMap();
		
		Node n0 = g.addNode();
		Node n1 = g.addNode();
		Edge u0 = g.addUEdge(n0, n1);
		assertNull(map.get(u0));
		map.put(u0, 3);
		assertEquals(3, map.get(u0).intValue());
		
		Node n2 = g.addNode();
		Edge u1 = g.addUEdge(n1, n2);
		assertNull(map.get(u1));
		map.put(u1, 5);
		assertEquals(3, map.get(u0).intValue());
		assertEquals(5, map.get(u1).intValue());
	}
}