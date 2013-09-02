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

import org.junit.Before;
import org.junit.Test;

public class StaticMixedGraphTest {
	
	private StaticMixedGraph g;
	
	@Before
	public void setUp() {
		g = new StaticMixedGraph();
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
}
