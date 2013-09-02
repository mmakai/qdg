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

import org.junit.Before;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import qdg.ListMixedGraph;
import qdg.api.Graph.Node;

public class IdenticalSpanningSubMixedGraphTest
		extends AbstractIdenticalSubMixedGraphTest {
	
	private Predicate<Node> allNodes = Predicates.alwaysTrue();
	
	@Before
	public void setUp() {
		original = new ListMixedGraph();
		identicalSubGraph = new SpanningSubMixedGraph(original, allNodes);
	}
}
