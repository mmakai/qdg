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

package qdg.contrib;

import static org.junit.Assert.*;

import org.junit.Test;

import qdg.StaticUGraph;
import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.Graph.Node;
import qdg.view.UGraphAsDiGraph;

public class ClosenessCentralityTest {
	
	@Test
	public void path1() {
		StaticUGraph g = new StaticUGraph();
		DiGraph di = new UGraphAsDiGraph(g);
		Node v0 = g.addNode();
		Node v1 = g.addNode();
		Node v2 = g.addNode();
		Node v3 = g.addNode();
		Node v4 = g.addNode();
		Node v5 = g.addNode();
		g.addUEdge(v1, v2);
		g.addUEdge(v3, v2);
		g.addUEdge(v4, v3);
		ClosenessCentrality c = new ClosenessCentrality(di);
		EntityMap<Node, Double> score = c.getScore();
		c.compute();
		// 5 unreachable nodes, the distance for each of them is set to 6.
		assertEquals(1.0 / (5 * 6), score.get(v0), 1e-8);
		// 2 unreachable nodes, 1 at distance 1, 1 at distance 2, 1 at distance 3.
		assertEquals(1.0 / (2 * 6 + 1 + 2 + 3), score.get(v1), 1e-8);
		// 2 unreachable nodes, 2 at distance 1, 1 at distance 2.
		assertEquals(1.0 / (2 * 6 + 2 + 2), score.get(v2), 1e-8);
		assertEquals(1.0 / (2 * 6 + 2 + 2), score.get(v3), 1e-8);
		assertEquals(1.0 / (2 * 6 + 1 + 2 + 3), score.get(v4), 1e-8);
		assertEquals(1.0 / (5 * 6), score.get(v5), 1e-8);
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
		g.addUEdge(v1, v2);
		g.addUEdge(v3, v2);
		g.addUEdge(v4, v2);
		ClosenessCentrality c = new ClosenessCentrality(di);
		EntityMap<Node, Double> score = c.getScore(); 
		c.compute();
		// 4 unreachable nodes, the distance for each of them is set to 6.
		assertEquals(1.0 / (4 * 5), score.get(v0), 1e-8);
		// 1 unreachable node, 1 at distance 1, 2 at distance 2.
		assertEquals(1.0 / (5 + 1 + 2 * 2), score.get(v1), 1e-8);
		// 1 unreachable node, 3 at distance 1.
		assertEquals(1.0 / (5 + 3), score.get(v2), 1e-8);
		// 1 unreachable node, 1 at distance 1, 2 at distance 2.
		assertEquals(1.0 / (5 + 1 + 2 * 2), score.get(v3), 1e-8);
		// 1 unreachable node, 1 at distance 1, 2 at distance 2.
		assertEquals(1.0 / (5 + 1 + 2 * 2), score.get(v4), 1e-8);
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
		g.addUEdge(v0, v1);
		g.addUEdge(v2, v0);
		g.addUEdge(v0, v3);
		g.addUEdge(v4, v1);
		g.addUEdge(v2, v4);
		g.addUEdge(v4, v3);
		ClosenessCentrality c = new ClosenessCentrality(di);
		EntityMap<Node, Double> score = c.getScore();
		c.compute();
		// 3 at distance 1, 1 at distance 2.
		assertEquals(1.0 / (3 * 1 + 2), score.get(v0), 1e-8);
		// 2 at distance 1, 2 at distance 2.
		assertEquals(1.0 / (2 * 1 + 2 * 2), score.get(v1), 1e-8);
		assertEquals(1.0 / (2 * 1 + 2 * 2), score.get(v2), 1e-8);
		assertEquals(1.0 / (2 * 1 + 2 * 2), score.get(v3), 1e-8);
		assertEquals(1.0 / (3 * 1 + 2), score.get(v4), 1e-8);
	}
}
