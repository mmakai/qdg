/*
 * Copyright (C) 2014 Marton Makai
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

package qdg.contrib;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;
import qdg.EntityMaps.NullMap;
import qdg.StaticUGraph;
import qdg.contrib.ScaledBetweennessCentrality.SingleSource;
import qdg.view.UGraphAsDiGraph;

import org.junit.Test;

public class ScaledBetweennessCentralityTest {

	@Test
	public void path() {
		StaticUGraph g = new StaticUGraph();
		DiGraph di = new UGraphAsDiGraph(g);
		Node v0 = g.addNode();
		Node v1 = g.addNode();
		Node v2 = g.addNode();
		Node v3 = g.addNode();
		Node v4 = g.addNode();
		Node v5 = g.addNode();
		Edge e0 = g.addUEdge(v1, v2);
		Edge e1 = g.addUEdge(v3, v2);
		Edge e2 = g.addUEdge(v4, v3);
		ScaledBetweennessCentrality b = new ScaledBetweennessCentrality(di);
		SingleSource singleSource = b.new SingleSource(v1,
				new NullMap<Node, Double>());
		singleSource.scan();
		EntityMap<Node, List<Edge>> tightEdges = singleSource.tightEdges;
		assertNull(tightEdges.get(v0));
		assertNull(tightEdges.get(v1));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e0, false)), tightEdges.get(v2));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e1, true)), tightEdges.get(v3));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e2, true)), tightEdges.get(v4));
		assertNull(tightEdges.get(v5));
		singleSource.finish();
		EntityMap<Node, Integer> numShortestPaths = singleSource.numShortestPaths;
		assertNull(numShortestPaths.get(v0));
		assertEquals(1, numShortestPaths.get(v1).intValue());
		assertEquals(1, numShortestPaths.get(v2).intValue());
		assertEquals(1, numShortestPaths.get(v3).intValue());
		assertEquals(1, numShortestPaths.get(v4).intValue());
		assertNull(numShortestPaths.get(v5));
		EntityMap<Node, Double> dependency = singleSource.dependency;
		assertNull(dependency.get(v0));
		assertNull(dependency.get(v1));
		assertEquals(5.0 / 6, dependency.get(v2), 1e-8);
		assertEquals(2.0 / 3, dependency.get(v3), 1e-8);
		assertNull(dependency.get(v4));
		assertNull(dependency.get(v5));
	}
	
	@Test
	public void tree() {
		StaticUGraph g = new StaticUGraph();
		DiGraph di = new UGraphAsDiGraph(g);
		Node v0 = g.addNode();
		Node v1 = g.addNode();
		Node v2 = g.addNode();
		Node v3 = g.addNode();
		Node v4 = g.addNode();
		Edge e0 = g.addUEdge(v1, v2);
		Edge e1 = g.addUEdge(v3, v2);
		Edge e2 = g.addUEdge(v4, v2);
		ScaledBetweennessCentrality b = new ScaledBetweennessCentrality(di);
		SingleSource singleSource = b.new SingleSource(v1,
				new NullMap<Node, Double>());
		singleSource.scan();
		EntityMap<Node, List<Edge>> tightEdges = singleSource.tightEdges;
		assertNull(tightEdges.get(v0));
		assertNull(tightEdges.get(v1));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e0, false)), tightEdges.get(v2));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e1, true)), tightEdges.get(v3));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e2, true)), tightEdges.get(v4));
		singleSource.finish();
		EntityMap<Node, Integer> numShortestPaths = singleSource.numShortestPaths;
		assertNull(numShortestPaths.get(v0));
		assertEquals(1, numShortestPaths.get(v1).intValue());
		assertEquals(1, numShortestPaths.get(v2).intValue());
		assertEquals(1, numShortestPaths.get(v3).intValue());
		assertEquals(1, numShortestPaths.get(v4).intValue());
		EntityMap<Node, Double> dependency = singleSource.dependency;
		assertNull(dependency.get(v0));
		assertNull(dependency.get(v1));
		assertEquals(1, dependency.get(v2), 1e-8);
		assertNull(dependency.get(v3));
		assertNull(dependency.get(v4));
	}
	
	@Test
	public void theta() {
		StaticUGraph g = new StaticUGraph();
		DiGraph di = new UGraphAsDiGraph(g);
		Node v0 = g.addNode();
		Node v1 = g.addNode();
		Node v2 = g.addNode();
		Node v3 = g.addNode();
		Node v4 = g.addNode();
		Edge e0 = g.addUEdge(v0, v1);
		Edge e1 = g.addUEdge(v2, v0);
		Edge e2 = g.addUEdge(v0, v3);
		Edge e3 = g.addUEdge(v4, v1);
		Edge e4 = g.addUEdge(v2, v4);
		Edge e5 = g.addUEdge(v4, v3);
		ScaledBetweennessCentrality b = new ScaledBetweennessCentrality(di);
		SingleSource singleSource = b.new SingleSource(v0,
				new NullMap<Node, Double>());
		singleSource.scan();
		EntityMap<Node, List<Edge>> tightEdges = singleSource.tightEdges;
		assertNull(tightEdges.get(v0));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e0, false)), tightEdges.get(v1));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e1, true)), tightEdges.get(v2));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e2, false)), tightEdges.get(v3));
		// NOTE The ordering makes the following assert unstable, but there
		// might be no ordering for the edges.
		assertEquals(Arrays.asList(
				new UGraphAsDiGraph.A(e3, true),
				new UGraphAsDiGraph.A(e5, true),
				new UGraphAsDiGraph.A(e4, false)), tightEdges.get(v4));
		singleSource.finish();
		EntityMap<Node, Integer> numShortestPaths = singleSource.numShortestPaths;
		assertEquals(1, numShortestPaths.get(v0).intValue());
		assertEquals(1, numShortestPaths.get(v1).intValue());
		assertEquals(1, numShortestPaths.get(v2).intValue());
		assertEquals(1, numShortestPaths.get(v3).intValue());
		assertEquals(3, numShortestPaths.get(v4).intValue());
		EntityMap<Node, Double> dependency = singleSource.dependency;
		assertNull(dependency.get(v0));
		assertEquals(1.0 / 6, dependency.get(v1), 1e-8);
		assertEquals(1.0 / 6, dependency.get(v2), 1e-8);
		assertEquals(1.0 / 6, dependency.get(v3), 1e-8);
		assertNull(dependency.get(v4));
	}
	
	@Test
	public void longPath() {
		StaticUGraph g = new StaticUGraph();
		DiGraph di = new UGraphAsDiGraph(g);
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < 10; ++i) {
			nodes.add(g.addNode());
		}
		for (int i = 1; i < 10; ++i) {
			g.addUEdge(nodes.get(i - 1), nodes.get(i));
		}
		BetweennessCentrality b = new BetweennessCentrality(di);
		EntityMap<Node, Double> b1 = di.createNodeMap();
		b.singleSourceCompute(nodes.get(0), b1);
		EntityMap<Node, Double> c1 = di.createNodeMap();
		ScaledBetweennessCentrality c = new ScaledBetweennessCentrality(di);
		c.singleSourceCompute(nodes.get(0), c1);
		System.out.println(b1);
		System.out.println(c1);
	}
	
	@Test
	public void checkAgainstBrandesAlg() {
		StaticUGraph g = new StaticUGraph();
		DiGraph di = new UGraphAsDiGraph(g);
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < 10; ++i) {
			nodes.add(g.addNode());
		}
		Random u = new Random();
		for (int i = 0; i < nodes.size(); ++i) {
			for (int j = i + 1; j < nodes.size(); ++j) {
				if (u.nextDouble() > .1) {
					g.addUEdge(nodes.get(i), nodes.get(j));
				}
			}
		}
		BetweennessCentrality b = new BetweennessCentrality(di);
		b.compute();
		ScaledBetweennessCentrality c = new ScaledBetweennessCentrality(di);
		c.compute();
		// If applied to an undirected graph, forward and backward searches are the same,
		// hence the scaled version need two searches from each node.
		for (Node node : g.getNodes()) {
			assertEquals(b.getScore().get(node), 2 * c.getScore().get(node), 1e-8);
		}
	}
}
