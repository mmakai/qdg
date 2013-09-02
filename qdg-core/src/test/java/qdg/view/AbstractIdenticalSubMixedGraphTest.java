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

import org.junit.Test;

import qdg.DiGraphTest;
import qdg.UGraphTest;
import qdg.api.MutableMixedGraph;

public abstract class AbstractIdenticalSubMixedGraphTest {
	
	protected MutableMixedGraph original;
	
	protected MutableMixedGraph identicalSubGraph;
	
	// Everything from DiGraphTest.
	
	@Test
	public void sourceAndTargetDiTest() {
		DiGraphTest.sourceAndTargetTest(identicalSubGraph);
	}
	
	@Test
	public void nodeIteratorDiTest() {
		DiGraphTest.nodeIteratorTest(identicalSubGraph);
	}
	
	@Test
	public void arcIteratorTest() {
		DiGraphTest.arcIteratorTest(identicalSubGraph);
	}
	
	@Test
	public void outArcIteratorTest() {
		DiGraphTest.outArcIteratorTest(identicalSubGraph);
	}

	@Test
	public void inArcIteratorTest() {
		DiGraphTest.inArcIteratorTest(identicalSubGraph);
	}
	
	@Test
	public void incidentArcIteratorTest() {
		DiGraphTest.incidentArcIteratorTest(identicalSubGraph);
	}
	
	@Test
	public void nodeMapDiTest() {
		DiGraphTest.nodeMapTest(identicalSubGraph);
	}
	
	@Test
	public void arcMapDiTest() {
		DiGraphTest.arcMapTest(identicalSubGraph);
	}
	
	@Test
	public void edgeMapDiTest() {
		DiGraphTest.edgeMapTest(identicalSubGraph);
	}
	
	// Everything from UGraphTest.
	
	@Test
	public void sourceAndTargetUTest() {
		UGraphTest.sourceAndTargetTest(identicalSubGraph);
	}
	
	@Test
	public void nodeIteratorUTest() {
		UGraphTest.nodeIteratorTest(identicalSubGraph);
	}
	
	@Test
	public void uEdgeIteratorTest() {
		UGraphTest.uEdgeIteratorTest(identicalSubGraph);
	}
	
	@Test
	public void incidentUEdgeIteratorTest() {
		UGraphTest.incidentUEdgeIteratorTest(identicalSubGraph);
	}
	
	@Test
	public void nodeMapUTest() {
		UGraphTest.nodeMapTest(identicalSubGraph);
	}
	
	@Test
	public void arcMapUTest() {
		UGraphTest.uEdgeMapTest(identicalSubGraph);
	}
	
	@Test
	public void edgeMapUTest() {
		UGraphTest.edgeMapTest(identicalSubGraph);
	}
}
