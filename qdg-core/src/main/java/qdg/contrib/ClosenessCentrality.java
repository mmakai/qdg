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

import java.util.ArrayDeque;
import java.util.Queue;

import com.google.common.collect.Iterators;

import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;

public class ClosenessCentrality {
	
	private final DiGraph g;
	
	private EntityMap<Node, Double> score;
	
	private Integer numOfNodes;
	
	private Double valueForUnreachableNode;
	
	public ClosenessCentrality(DiGraph g) {
		this(g, g.<Double>createNodeMap(), (double) Iterators.size(g.getNodeIterator()));
	}
	
	public ClosenessCentrality(DiGraph g, EntityMap<Node, Double> score, Double valueForUnreachableNode) {
		this.g = g;
		this.score = score;
		this.valueForUnreachableNode = valueForUnreachableNode;
	}
	
	protected class SingleSource {
		
		private Node s;
		
		private EntityMap<Node, Double> score;
		
		protected EntityMap<Node, Integer> distance = g.createNodeMap();
		
		public SingleSource(Node s, EntityMap<Node, Double> score) {
			this.s = s;
			this.score = score;
		}
		
		protected void scan() {
			distance.put(s, 0);
			Queue<Node> queue = new ArrayDeque<Node>();
			queue.add(s);
			Double ss = null;
			int count = 0;
			while (!queue.isEmpty()) {
				Node u = queue.remove();
				for (Edge e : g.getOutArcs(u)) {
					Node v = g.getTarget(e);
					if (distance.get(v) == null) {
						Integer dist = distance.get(u) + 1;
						distance.put(v, dist);
						queue.add(v);
						if (ss == null) {
							ss = 0.0;
						}
						ss += dist;
					}
				}
				++count;
			}
			if (valueForUnreachableNode != null) {
				if (ss == null) {
					ss = 0.0;
				}
				ss += (ensureNumOfNodes() - count) * valueForUnreachableNode;
			}
			if (ss != null) {
				score.put(s, 1.0 / ss);
			}
		}
	}
	
	private int ensureNumOfNodes() {
		if (numOfNodes == null) {
			numOfNodes = Iterators.size(g.getNodeIterator());
		}
		return numOfNodes;
	}
	
	public void singleSourceCompute(Node s,
			EntityMap<Node, Double> score) {
		SingleSource singleSource = new SingleSource(s, score);
		singleSource.scan();
	}
	
	/**
	 * Closeness centrality scores are computed for all nodes,
	 * i.e. the inverse of the sum of the distances to all the other nodes.
	 * There is a default distance taken for the unreachable nodes,
	 * with default value of the number of nodes. If this is set to null,
	 * then the unreachable nodes are simply ignored.
	 * If nothing is reached from a node, and the default value is null,
	 * then the score for the node is not changed.
	 */
	public void compute() {
		ensureNumOfNodes();
		for (Node s : g.getNodes()) {
			singleSourceCompute(s, score);
		}
	}

	public EntityMap<Node, Double> getScore() {
		return score;
	}
}
