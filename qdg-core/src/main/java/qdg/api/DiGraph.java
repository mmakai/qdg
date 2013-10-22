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

import qdg.api.bits.ArcIterable;
import qdg.api.bits.ArcMappable;
import qdg.api.bits.HasEdgeMutationHandlers;
import qdg.api.bits.HasNodeMutationHandlers;
import qdg.api.bits.InArcIterable;
import qdg.api.bits.IncidentArcIterable;
import qdg.api.bits.OutArcIterable;

public interface DiGraph extends Graph,
		ArcIterable<Graph.Edge>,
		OutArcIterable<Graph.Node, Graph.Edge>,
		InArcIterable<Graph.Node, Graph.Edge>,
		IncidentArcIterable<Graph.Node, Graph.Edge>,
		ArcMappable<Graph.Edge>,
		HasNodeMutationHandlers, HasEdgeMutationHandlers {
	
	/**
	 * Optional operation.
	 */
	Node addNode();
	
	/**
	 * Optional operation.
	 */
	void remove(Node n);
	
	/**
	 * Optional operation.
	 */
	Edge addArc(Node source, Node target);
	
	/**
	 * Optional operation.
	 */
	void remove(Edge arc);
}
