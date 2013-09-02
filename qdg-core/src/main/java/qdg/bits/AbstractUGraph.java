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

package qdg.bits;

import java.util.Iterator;

import qdg.api.EntityMap;
import qdg.api.UGraph;

public abstract class AbstractUGraph extends AbstractGraph implements UGraph {

	@Override
	public Iterable<Edge> getUEdges() {
		return new Iterable<Edge>() {

			@Override
			public Iterator<Edge> iterator() {
				return getUEdgeIterator();
			}
		};
	}

	@Override
	public Iterable<Edge> getIncidentUEdges(final Node node) {
		return new Iterable<Edge>() {

			@Override
			public Iterator<Edge> iterator() {
				return getIncidentUEdgeIterator(node);
			}
		};
	}
	
	@Override
	public Iterator<Edge> getEdgeIterator() {
		return getUEdgeIterator();
	}
	
	@Override
	public Iterator<Edge> getIncidentEdgeIterator(Node node) {
		return getIncidentUEdgeIterator(node);
	}
	
	@Override
	public <V> EntityMap<Edge, V> createEdgeMap() {
		return createUEdgeMap();
	}
}
