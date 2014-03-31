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

import java.util.Arrays;
import java.util.List;

import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;
import qdg.EntityMaps;
import qdg.EntityMaps.NullMap;
import qdg.StaticUGraph;
import qdg.contrib.WeightedBetweennessCentrality.SingleSource;
import qdg.view.UGraphAsDiGraph;

import org.junit.Test;

public class WeightedBetweennessCentralityTest {

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
		WeightedBetweennessCentrality b = new WeightedBetweennessCentrality(di);
		b.setWeights(new EntityMaps.ConstMap<Edge, Double>(1.0));
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
		assertEquals(2, dependency.get(v2), 1e-8);
		assertEquals(1, dependency.get(v3), 1e-8);
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
		WeightedBetweennessCentrality b = new WeightedBetweennessCentrality(di);
		b.setWeights(new EntityMaps.ConstMap<Edge, Double>(1.0));
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
		assertEquals(2, dependency.get(v2), 1e-8);
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
		WeightedBetweennessCentrality b = new WeightedBetweennessCentrality(di);
		b.setWeights(new EntityMaps.ConstMap<Edge, Double>(1.0));
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
				new UGraphAsDiGraph.A(e4, false),
				new UGraphAsDiGraph.A(e5, true)), tightEdges.get(v4));
		singleSource.finish();
		EntityMap<Node, Integer> numShortestPaths = singleSource.numShortestPaths;
		assertEquals(1, numShortestPaths.get(v0).intValue());
		assertEquals(1, numShortestPaths.get(v1).intValue());
		assertEquals(1, numShortestPaths.get(v2).intValue());
		assertEquals(1, numShortestPaths.get(v3).intValue());
		assertEquals(3, numShortestPaths.get(v4).intValue());
		EntityMap<Node, Double> dependency = singleSource.dependency;
		assertNull(dependency.get(v0));
		assertEquals(2.0 / 6, dependency.get(v1), 1e-8);
		assertEquals(2.0 / 6, dependency.get(v2), 1e-8);
		assertEquals(2.0 / 6, dependency.get(v3), 1e-8);
		assertNull(dependency.get(v4));
	}
	
	@Test
	public void zigZag() {
		StaticUGraph g = new StaticUGraph();
		UGraphAsDiGraph di = new UGraphAsDiGraph(g);
		EntityMap<Edge, Double> weights = g.createEdgeMap();
		Node v0 = g.addNode();
		Node v1 = g.addNode();
		Node v2 = g.addNode();
		Node v3 = g.addNode();
		Node v4 = g.addNode();
		Node v5 = g.addNode();
		Node v6 = g.addNode();
		Edge e0 = g.addUEdge(v0, v1);
		weights.put(e0, 1.0);
		Edge e1 = g.addUEdge(v1, v2);
		weights.put(e1, 1.0);
		Edge e2 = g.addUEdge(v0, v2);
		weights.put(e2, 2.0);
		Edge e3 = g.addUEdge(v2, v3);
		weights.put(e3, 1.0);
		Edge e4 = g.addUEdge(v3, v4);
		weights.put(e4, 1.0);
		Edge e5 = g.addUEdge(v2, v4);
		weights.put(e5, 3.0);
		Edge e6 = g.addUEdge(v4, v5);
		weights.put(e6, 1.0);
		Edge e7 = g.addUEdge(v5, v6);
		weights.put(e7, 1.0);
		Edge e8 = g.addUEdge(v4, v6);
		weights.put(e8, 1.0);
		WeightedBetweennessCentrality b = new WeightedBetweennessCentrality(di);
		b.setWeights(di.createArcMap(weights, weights));
		SingleSource singleSource = b.new SingleSource(v0,
				new NullMap<Node, Double>());
		singleSource.scan();
		EntityMap<Node, List<Edge>> tightEdges = singleSource.tightEdges;
		assertNull(tightEdges.get(v0));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e0, false)), tightEdges.get(v1));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e2, false), new UGraphAsDiGraph.A(e1, false)), tightEdges.get(v2));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e3, false)), tightEdges.get(v3));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e4, false)), tightEdges.get(v4));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e6, false)), tightEdges.get(v5));
		assertEquals(Arrays.asList(new UGraphAsDiGraph.A(e8, false)), tightEdges.get(v6));
		singleSource.finish();
		EntityMap<Node, Integer> numShortestPaths = singleSource.numShortestPaths;
		assertEquals(1, numShortestPaths.get(v0).intValue());
		assertEquals(1, numShortestPaths.get(v1).intValue());
		assertEquals(2, numShortestPaths.get(v2).intValue());
		assertEquals(2, numShortestPaths.get(v3).intValue());
		assertEquals(2, numShortestPaths.get(v4).intValue());
		assertEquals(2, numShortestPaths.get(v5).intValue());
		assertEquals(2, numShortestPaths.get(v6).intValue());
		EntityMap<Node, Double> dependency = singleSource.dependency;
		assertNull(dependency.get(v0));
		assertEquals(2.5, dependency.get(v1), 1e-8);
		assertEquals(4.0, dependency.get(v2), 1e-8);
		assertEquals(3.0, dependency.get(v3), 1e-8);
		assertEquals(2.0, dependency.get(v4), 1e-8);
		assertNull(dependency.get(v5));
		assertNull(dependency.get(v6));
	}
	
	@Test
	public void smallClique() {
		StaticUGraph g = new StaticUGraph();
		UGraphAsDiGraph di = new UGraphAsDiGraph(g);
		EntityMap<Edge, Double> weights = g.createEdgeMap();
		Node v0 = g.addNode();
		Node v1 = g.addNode();
		Node v2 = g.addNode();
		Node v3 = g.addNode();
		Node v4 = g.addNode();
		Edge e0 = g.addUEdge(v0, v1);
		weights.put(e0, 1.0);
		Edge e1 = g.addUEdge(v0, v2);
		weights.put(e1, .1);
		Edge e2 = g.addUEdge(v0, v3);
		weights.put(e2, 1.0);
		Edge e3 = g.addUEdge(v0, v4);
		weights.put(e3, 1.0);
		Edge e4 = g.addUEdge(v1, v2);
		weights.put(e4, 1.0);
		Edge e5 = g.addUEdge(v1, v3);
		weights.put(e5, 1.0);
		Edge e6 = g.addUEdge(v1, v4);
		weights.put(e6, .1);
		Edge e7 = g.addUEdge(v2, v3);
		weights.put(e7, .1);
		Edge e8 = g.addUEdge(v2, v4);
		weights.put(e8, .1);
		Edge e9 = g.addUEdge(v3, v4);
		weights.put(e9, 1.0);
		WeightedBetweennessCentrality b = new WeightedBetweennessCentrality(di);
		b.setWeights(di.createArcMap(weights, weights));
		b.compute();
		EntityMap<Node, Double> score = b.getScore();
		assertNull(score.get(v0));
		assertNull(score.get(v1));
		assertEquals(10.0, score.get(v2), 1e-8);
		assertNull(score.get(v3));
		assertEquals(6.0, score.get(v4), 1e-8);
	}
}
