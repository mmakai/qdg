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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import qdg.api.DiGraph;
import qdg.api.EntityMap;
import qdg.api.Graph.Edge;
import qdg.api.Graph.Node;

/**
 * Scaled dependency computations for the betweenness centrality computation
 * of:
 * 
 * @inproceedings{conf/alenex/GeisbergerSS08,
 *   added-at = {2009-03-02T00:00:00.000+0100},
 *   author = {Geisberger, Robert and Sanders, Peter and Schultes, Dominik},
 *   biburl = {http://www.bibsonomy.org/bibtex/25f3e512d901405adceba614cb24bb53a/dblp},
 *   booktitle = {ALENEX},
 *   crossref = {conf/alenex/2008},
 *   editor = {Munro, J. Ian and Wagner, Dorothea},
 *   ee = {http://www.siam.org/proceedings/alenex/2008/alx08_09geisbergerr.pdf},
 *   interhash = {272cf498dee8567047747b57d3d6927e},
 *   intrahash = {5f3e512d901405adceba614cb24bb53a},
 *   keywords = {dblp},
 *   pages = {90-100},
 *   publisher = {SIAM},
 *   timestamp = {2009-03-02T00:00:00.000+0100},
 *   title = {Better Approximation of Betweenness Centrality.},
 *   url = {http://dblp.uni-trier.de/db/conf/alenex/alenex2008.html#GeisbergerSS08},
 *   year = 2008
 * }
 * 
 * For efficiency reasons, nodes not receiving any contributions from any
 * shortest paths can have score value null, instead of 0.0.
 */
public class ScaledBetweennessCentrality {

	private final DiGraph g;
	
	private EntityMap<Node, Double> score;
	
	public ScaledBetweennessCentrality(DiGraph g) {
		this(g, g.<Double>createNodeMap());
	}
	
	public ScaledBetweennessCentrality(DiGraph g, EntityMap<Node, Double> score) {
		this.g = g;
		this.score = score;
	}
	
	protected class SingleSource {
		
		private Node s;
		
		private EntityMap<Node, Double> score;
		
		protected EntityMap<Node, Integer> distance = g.createNodeMap();
		
		protected EntityMap<Node, List<Edge>> tightEdges = g.createNodeMap();
		
		protected EntityMap<Node, Double> dependency = g.createNodeMap();
		
		/**
		 * Let $\sigma_{u, v}$ be the number of shortest paths from $u$ and $v$, and
		 * $\sigma_{u, v}(w)$ the number of shortest paths form $u$ to $v$ going through $w$.
		 * For $v$ the map stores $\sigma_{s, v}$
		 */
		protected EntityMap<Node, Integer> numShortestPaths = g.createNodeMap();
		
		protected Stack<Node> stack = new Stack<Node>();
		
		public SingleSource(Node s, EntityMap<Node, Double> score) {
			this.s = s;
			this.score = score;
		}
		
		protected void scan() {
			distance.put(s, 0);
			Queue<Node> queue = new ArrayDeque<Node>();
			queue.add(s);
			numShortestPaths.put(s, 1);
			while (!queue.isEmpty()) {
				Node u = queue.remove();
				stack.push(u);
				for (Edge e : g.getOutArcs(u)) {
					Node v = g.getTarget(e);
					if (distance.get(v) == null) {
						Integer dist = distance.get(u) + 1;
						distance.put(v, dist);
						queue.add(v);
					}
				}
				for (Edge e : g.getOutArcs(u)) {
					Node v = g.getTarget(e);
					if (distance.get(v).equals(distance.get(u) + 1)) {
						List<Edge> t = tightEdges.get(v);
						if (t == null) {
							t = new ArrayList<Edge>();
							tightEdges.put(v, t);
						}
						t.add(e);
						Integer i = numShortestPaths.get(v);
						if (i == null) {
							i = 0;
						}
						numShortestPaths.put(v, i + numShortestPaths.get(u));
					}
				}
			}
		}
		
		protected void finish() {
			while (!stack.isEmpty()) {
				Node v = stack.pop();
				Double vDependency = dependency.get(v);
				if (vDependency == null) {
					vDependency = 0.0;
				}
				List<Edge> t = tightEdges.get(v);
				if (t != null) {
					for (Edge e : t) {
						Node u = g.getSource(e);
						if (!u.equals(s)) {
							double partialDependency = (double) distance.get(u) * numShortestPaths.get(u) *
									(1 + vDependency) / distance.get(v) / numShortestPaths.get(v);
							Double dep = dependency.get(u);
							if (dep == null) {
								dep = 0.0;
							}
							dep += partialDependency;
							dependency.put(u, dep);
							Double s = score.get(u);
							if (s == null) {
								s = 0.0;
							}
							s += partialDependency;
							score.put(u, s);
						}
					}
				}
			}
		}
	}
	
	public void singleSourceCompute(Node s,
			EntityMap<Node, Double> score) {
		SingleSource singleSource = new SingleSource(s, score);
		singleSource.scan();
		singleSource.finish();
	}
	
	/**
	 * Half of the betweenness for undirected graphs.
	 */
	public void compute() {
		for (Node s : g.getNodes()) {
			singleSourceCompute(s, score);
		}
	}

	public EntityMap<Node, Double> getScore() {
		return score;
	}
}
