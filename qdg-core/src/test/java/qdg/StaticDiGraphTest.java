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

public class StaticDiGraphTest {

	private StaticDiGraph g;
	
	@Before
	public void setUp() {
		g = new StaticDiGraph();
	}
	
	@Test
	public void sourceAndTargetTest() {
		DiGraphTest.sourceAndTargetTest(g);
	}
	
	@Test
	public void nodeIteratorTest() {
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
	public void nodeMapTest() {
		DiGraphTest.nodeMapTest(g);
	}
	
	@Test
	public void arcMapTest() {
		DiGraphTest.arcMapTest(g);
	}
	
	@Test
	public void edgeMapTest() {
		DiGraphTest.edgeMapTest(g);
	}
}
