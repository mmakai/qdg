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

import com.google.common.collect.Iterators;

import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.Graph.Node;

public class DegreeCentrality {
	
	private final DiGraph g;
	
	private EntityMap<Node, Double> score;
	
	public DegreeCentrality(DiGraph g) {
		this(g, g.<Double>createNodeMap());
	}
	
	public DegreeCentrality(DiGraph g, EntityMap<Node, Double> score) {
		this.g = g;
		this.score = score;
	}
	
	/**
	 * Degree centrality scores are computed for all nodes.
	 */
	public void compute() {
		for (Node n : g.getNodes()) {
			score.put(n, (double) Iterators.size(g.getOutArcIterator(n)));
		}
	}

	public EntityMap<Node, Double> getScore() {
		return score;
	}
}