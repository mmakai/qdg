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

public class StaticUGraphTest {
	
	private StaticUGraph g;
	
	@Before
	public void setUp() {
		g = new StaticUGraph();
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
}
