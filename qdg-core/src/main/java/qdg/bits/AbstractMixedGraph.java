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

import qdg.api.MixedGraph;

public abstract class AbstractMixedGraph extends AbstractGraph
		implements MixedGraph {

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
	public Iterable<Edge> getArcs() {
		return new Iterable<Edge>() {

			@Override
			public Iterator<Edge> iterator() {
				return getArcIterator();
			}
		};
	}

	@Override
	public Iterable<Edge> getOutArcs(final Node node) {
		return new Iterable<Edge>() {

			@Override
			public Iterator<Edge> iterator() {
				return getOutArcIterator(node);
			}
		};
	}

	@Override
	public Iterable<Edge> getInArcs(final Node node) {
		return new Iterable<Edge>() {

			@Override
			public Iterator<Edge> iterator() {
				return getInArcIterator(node);
			}
		};
	}

	@Override
	public Iterable<Edge> getIncidentArcs(final Node node) {
		return new Iterable<Edge>() {

			@Override
			public Iterator<Edge> iterator() {
				return getIncidentArcIterator(node);
			}
		};
	}
	
	@Override
	public Iterator<Edge> getEdgeIterator() {
		return new ConcatIterator<Edge>(getArcIterator(), getUEdgeIterator());
	}
	
	@Override
	public Iterator<Edge> getIncidentEdgeIterator(Node node) {
		return new ConcatIterator<Edge>(
				getIncidentArcIterator(node),
				getIncidentUEdgeIterator(node));
	}
}
