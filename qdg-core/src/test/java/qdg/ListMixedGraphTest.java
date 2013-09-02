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
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;
import qdg.api.bits.EdgeMutationHandler;
import qdg.api.bits.NodeMutationHandler;

public class ListMixedGraphTest {
	
	private ListMixedGraph g;
	
	@Before
	public void setUp() {
		g = new ListMixedGraph();
	}
	
	// Everything from DiGraphTest.
	
	@Test
	public void sourceAndTargetDiTest() {
		DiGraphTest.sourceAndTargetTest(g);
	}
	
	@Test
	public void nodeIteratorDiTest() {
		DiGraphTest.nodeIteratorTest(g);
	}
	
	@Test
	public void arcIteratorTest() {
		DiGraphTest.arcIteratorTest(g);
	}
	
	@Test
	public void outArcIteratorTest() {
		DiGraphTest.outArcIteratorTest(g);
	}

	@Test
	public void inArcIteratorTest() {
		DiGraphTest.inArcIteratorTest(g);
	}
	
	@Test
	public void incidentArcIteratorTest() {
		DiGraphTest.incidentArcIteratorTest(g);
	}
	
	@Test
	public void nodeMapDiTest() {
		DiGraphTest.nodeMapTest(g);
	}
	
	@Test
	public void arcMapDiTest() {
		DiGraphTest.arcMapTest(g);
	}
	
	@Test
	public void edgeMapDiTest() {
		DiGraphTest.edgeMapTest(g);
	}
	
	// Everything from UGraphTest.
	
	@Test
	public void sourceAndTargetUTest() {
		UGraphTest.sourceAndTargetTest(g);
	}
	
	@Test
	public void nodeIteratorUTest() {
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
	public void nodeMapUTest() {
		UGraphTest.nodeMapTest(g);
	}
	
	@Test
	public void arcMapUTest() {
		UGraphTest.uEdgeMapTest(g);
	}
	
	@Test
	public void edgeMapUTest() {
		UGraphTest.edgeMapTest(g);
	}
	
	@Test
	public void nodeMapMutationHandlerTest() {
		ListDiGraphTest.nodeMapMutationHandlerDiGraphTest(g);
	}
	
	@Test
	public void arcMapMutationHandlerTest() {
		ListDiGraphTest.arcMapMutationHandlerDiGraphTest(g);
	}
	
	@Test
	public void uEdgeMapMutationHandlerTest() {
		ListUGraphTest.uEdgeMapMutationHandlerUGraphTest(g);
	}
	
	/**
	 * Serialize single edge mixed graph.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void serializationTest() throws IOException, ClassNotFoundException {
		EntityMap<Node, String> w = g.createNodeMap();
		EntityMap<Edge, String> c = g.createEdgeMap();
		Node n1 = g.addNode();
		w.put(n1, "n1");
		Node n2 = g.addNode();
		w.put(n2, "n2");
		Node n3 = g.addNode();
		w.put(n3, "n3");
		g.remove(n2);
		Edge a = g.addArc(n1, n3);
		Edge u = g.addUEdge(n3, n1);
		c.put(a, "a");
		c.put(u, "u");
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytesOut);
		out.writeObject(g);
		out.writeObject(w);
		out.writeObject(c);
		byte[] bytes = bytesOut.toByteArray();
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
		ObjectInputStream in = new ObjectInputStream(bytesIn);
		ListMixedGraph g1 = (ListMixedGraph) in.readObject();
		@SuppressWarnings("unchecked")
		EntityMap<Node, String> w2 = (EntityMap<Node, String>) in.readObject();
		g1.addNodeMutationHandler((NodeMutationHandler) w2);
		@SuppressWarnings("unchecked")
		EntityMap<Edge, String> c2 = (EntityMap<Edge, String>) in.readObject();
		g1.addEdgeMutationHandler((EdgeMutationHandler) c2);
		Iterator<Node> nodeIt = g1.getNodeIterator();
		Node n12 = nodeIt.next();
		Node n32 = nodeIt.next();
		assertEquals(((ListMixedGraph.N) n1).getId(), ((ListMixedGraph.N) n12).getId());
		assertEquals(((ListMixedGraph.N) n3).getId(), ((ListMixedGraph.N) n32).getId());
		assertFalse(nodeIt.hasNext());
		Iterator<Edge> arcIt = g1.getArcIterator();
		Edge a2 = arcIt.next();
		assertTrue(g1.isDirected(a2));
		assertEquals(((ListMixedGraph.E) a).getId(), ((ListMixedGraph.E) a2).getId());
		assertFalse(arcIt.hasNext());
		Iterator<Edge> uEdgeIt = g1.getUEdgeIterator();
		Edge u2 = uEdgeIt.next();
		assertFalse(g1.isDirected(u2));
		assertEquals(((ListMixedGraph.E) u).getId(), ((ListMixedGraph.E) u2).getId());
		assertFalse(uEdgeIt.hasNext());
		assertEquals(n32, g1.getSource(u2));
		assertEquals(n12, g1.getTarget(u2));
		assertEquals("n1", w2.get(n12));
		assertEquals("n3", w2.get(n32));
		assertEquals("a", c2.get(a2));
		assertEquals("u", c2.get(u2));
	}
}
