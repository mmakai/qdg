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

package qdg.api;

import qdg.api.bits.EdgeIterable;
import qdg.api.bits.EdgeMappable;
import qdg.api.bits.IncidentEdgeIterable;
import qdg.api.bits.NodeIterable;
import qdg.api.bits.NodeMappable;

public interface Graph extends NodeIterable<Graph.Node>,
		EdgeIterable<Graph.Edge>, IncidentEdgeIterable<Graph.Node, Graph.Edge>,
		NodeMappable<Graph.Node>, EdgeMappable<Graph.Edge> {
	
	interface Node {
	}
	
	interface Edge {
	}
	
	Node getSource(Edge edge);
	
	Node getTarget(Edge edge);
}
