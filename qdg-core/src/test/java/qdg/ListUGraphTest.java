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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterators;

import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;
import qdg.api.UGraph;
import qdg.api.bits.HasId;
import qdg.api.bits.NodeMutationHandler;

public class ListUGraphTest {
	
	private ListUGraph g;
	
	@Before
	public void setUp() {
		g = new ListUGraph();
	}

	@Test
	public void sourceAndTargetTest() {
		UGraphTest.sourceAndTargetTest(g);
	}
	
	@Test
	public void nodeIteratorTest() {
		UGraphTest.nodeIteratorTest(g);
	}
	
	@Test
	public void uEdgeIteratorTest() {
		UGraphTest.uEdgeIteratorTest(g);
	}
	
	@Test
	public void incidentUEdgeIteratorTest() {
		UGraphTest.incidentUEdgeIteratorTest(g);
	}
	
	@Test
	public void nodeMapTest() {
		UGraphTest.nodeMapTest(g);
	}
	
	@Test
	public void arcMapTest() {
		UGraphTest.uEdgeMapTest(g);
	}
	
	@Test
	public void edgeMapTest() {
		UGraphTest.edgeMapTest(g);
	}
	
	@Test
	public void removeNode1() {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		
		g.remove(n1);
		List<Node> nodes = new ArrayList<Node>();
		Iterators.addAll(nodes, g.getNodeIterator());
		assertEquals(1, nodes.size());
		assertEquals(n2, nodes.get(0));
		
		g.remove(n2);
		nodes = new ArrayList<Node>();
		Iterators.addAll(nodes, g.getNodeIterator());
		assertEquals(0, nodes.size());
	}
	
	@Test
	public void removeNode2() {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		
		g.remove(n2);
		List<Node> nodes = new ArrayList<Node>();
		Iterators.addAll(nodes, g.getNodeIterator());
		assertEquals(1, nodes.size());
		assertEquals(n1, nodes.get(0));
		
		g.remove(n1);
		nodes = new ArrayList<Node>();
		Iterators.addAll(nodes, g.getNodeIterator());
		assertEquals(0, nodes.size());
	}
	
	@Test
	public void removeUEdge1() {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		
		Edge e1 = g.addUEdge(n1, n2);
		Edge e2 = g.addUEdge(n2, n1);
		g.remove(e1);
		g.remove(e2);
		assertEquals(0, Iterators.size(g.getEdgeIterator()));
		assertEquals(0, Iterators.size(g.getUEdgeIterator()));
		assertEquals(0, Iterators.size(g.getIncidentUEdgeIterator(n1)));
		assertEquals(0, Iterators.size(g.getIncidentUEdgeIterator(n2)));
	}
	
	@Test
	public void removeUEdge2() {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		
		Edge e1 = g.addUEdge(n1, n2);
		Edge e2 = g.addUEdge(n2, n1);
		Edge e3 = g.addUEdge(n1, n2);
		Edge e4 = g.addUEdge(n2, n1);
		g.remove(e1);
		g.remove(e2);
		g.remove(e3);
		g.remove(e4);
		assertEquals(0, Iterators.size(g.getEdgeIterator()));
		assertEquals(0, Iterators.size(g.getUEdgeIterator()));
		assertEquals(0, Iterators.size(g.getIncidentUEdgeIterator(n1)));
		assertEquals(0, Iterators.size(g.getIncidentUEdgeIterator(n2)));
	}
	
	@Test
	public void removeUEdge3() {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		Edge e1 = g.addUEdge(n1, n2);
		Edge e2 = g.addUEdge(n2, n1);
		g.remove(e1);
		g.remove(e2);
		
		Edge e3 = g.addUEdge(n1, n2);
		Edge e4 = g.addUEdge(n2, n1);
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e3, e4).iterator(), g.getEdgeIterator()));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e3, e4).iterator(), g.getUEdgeIterator()));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e3, e4).iterator(), g.getIncidentUEdgeIterator(n1)));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e4, e3).iterator(), g.getIncidentUEdgeIterator(n2)));
		
		Edge e5 = g.addUEdge(n1, n2);
		Edge e6 = g.addUEdge(n2, n1);
		Edge e7 = g.addUEdge(n1, n2);
		Edge e8 = g.addUEdge(n2, n1);
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e3, e4, e5, e6, e7, e8).iterator(), g.getEdgeIterator()));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e3, e4, e5, e6, e7, e8).iterator(), g.getUEdgeIterator()));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e3, e5, e7, e4, e6, e8).iterator(), g.getIncidentUEdgeIterator(n1)));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e4, e6, e8, e3, e5, e7).iterator(), g.getIncidentUEdgeIterator(n2)));
		
		g.remove(e3);
		g.remove(e4);
		g.remove(e7);
		g.remove(e8);
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e5, e6).iterator(), g.getEdgeIterator()));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e5, e6).iterator(), g.getUEdgeIterator()));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e5, e6).iterator(), g.getIncidentUEdgeIterator(n1)));
		assertTrue(Iterators.elementsEqual(
				Arrays.asList(e6, e5).iterator(), g.getIncidentUEdgeIterator(n2)));
	}
	
	/**
	 * Test that the id of a removed node is reused, and the map entry is
	 * cleaned as well.
	 */
	public static void nodeMapMutationHandlerUGraphTest(UGraph g) {
		Node n1 = g.addNode();
		assertEquals(0, ((HasId) n1).getId());
		EntityMap<Node, Integer> map = g.createNodeMap();
		assertNull(map.get(n1));
		map.put(n1, 3);
		assertEquals(3, map.get(n1).intValue());
		
		g.remove(n1);
		Node n2 = g.addNode();
		assertEquals(0, ((HasId) n2).getId());
		assertNull(map.get(n2));
		map.put(n2, 5);
		assertEquals(5, map.get(n2).intValue());
	}
	
	@Test
	public void nodeMapMutationHandlerTest() {
		nodeMapMutationHandlerUGraphTest(g);
	}
	
	/**
	 * Test that the id of a removed undirected edge is reused, and the map entry is
	 * cleaned as well.
	 */
	public static void uEdgeMapMutationHandlerUGraphTest(UGraph g) {
		Node n1 = g.addNode();
		Node n2 = g.addNode();
		Edge e1 = g.addUEdge(n1, n2);
		assertEquals(0, ((HasId) e1).getId());
		EntityMap<Edge, Integer> map = g.createEdgeMap();
		assertNull(map.get(e1));
		map.put(e1, 3);
		assertEquals(3, map.get(e1).intValue());
		
		g.remove(e1);
		Edge e2 = g.addUEdge(n1, n2);
		assertEquals(0, ((HasId) e2).getId());
		assertNull(map.get(e2));
		map.put(e2, 5);
		assertEquals(5, map.get(e2).intValue());
	}
	
	
	@Test
	public void uEdgeMapMutationHandlerTest() {
		uEdgeMapMutationHandlerUGraphTest(g);
	}
	
	/**
	 * Serialize single edge undirected graph.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void serializationTest() throws IOException, ClassNotFoundException {
		EntityMap<Node, String> w = g.createNodeMap();
		Node n1 = g.addNode();
		w.put(n1, "n1");
		Node n2 = g.addNode();
		w.put(n2, "n2");
		Node n3 = g.addNode();
		w.put(n3, "n3");
		g.remove(n2);
		Edge a = g.addUEdge(n1, n3);
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytesOut);
		out.writeObject(g);
		out.writeObject(w);
		byte[] bytes = bytesOut.toByteArray();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(bytesIn);
		ListUGraph g1 = (ListUGraph) in.readObject();
		@SuppressWarnings("unchecked")
		EntityMap<Node, String> w1 = (EntityMap<Node, String>) in.readObject();
		g1.addNodeMutationHandler((NodeMutationHandler) w1);
		Iterator<Node> nodeIt = g1.getNodeIterator();
		Node n11 = nodeIt.next();
		Node n31 = nodeIt.next();
		assertEquals(((ListUGraph.N) n1).getId(), ((ListUGraph.N) n11).getId());
		assertEquals(((ListUGraph.N) n3).getId(), ((ListUGraph.N) n31).getId());
		assertFalse(nodeIt.hasNext());
		Iterator<Edge> arcIt = g1.getUEdgeIterator();
		Edge a2 = arcIt.next();
		assertEquals(((ListUGraph.U) a).getId(), ((ListUGraph.U) a2).getId());
		assertFalse(arcIt.hasNext());
		assertEquals(n11, g1.getSource(a2));
		assertEquals(n31, g1.getTarget(a2));
		assertEquals("n1", w1.get(n11));
		assertEquals("n3", w1.get(n31));
	}
}
