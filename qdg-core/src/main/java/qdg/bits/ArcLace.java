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

import java.io.Serializable;
import java.util.Iterator;

import com.google.common.collect.UnmodifiableIterator;

import qdg.SparseArrayList;
import qdg.api.EntityMap;

public class ArcLace<N> {
	
	public static class NodeData implements Serializable {
		
		private static final long serialVersionUID = 850480654567551858L;

		protected int firstOut = -1;
		
		// Though the iteration could be performed without the index of the
		// last, with the pointer we can keep the insertion order.
		protected int lastOut = -1;
		
		protected int firstIn = -1;
		
		// Though the iteration could be performed without the index of the
		// last, with the pointer we can keep the insertion order.
		protected int lastIn = -1;
    	
		@Override
		public String toString() {
			return "NodeData [firstOut=" + firstOut + ", lastOut=" + lastOut
					+ ", firstIn=" + firstIn + ", lastIn=" + lastIn + "]";
		}
	}
	
	public static class ArcData<M> implements Serializable {
		
		private static final long serialVersionUID = 7780322224815348372L;

		protected M source, target;
		
		protected int prevOut = -1;
		
		protected int nextOut = -1;
		
		protected int prevIn = -1;
		
		protected int nextIn = -1;
		
		private ArcData(M source, M target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public String toString() {
			return "ArcData [source=" + source + ", target=" + target
					+ ", prevOut=" + prevOut + ", nextOut=" + nextOut
					+ ", prevIn=" + prevIn + ", nextIn=" + nextIn + "]";
		}
	}
	
	protected EntityMap<N, NodeData> nodeContainer;

	protected SparseArrayList<ArcData<N>> arcs;
	
	public ArcLace(EntityMap<N, NodeData> nodeContainer,
			SparseArrayList<ArcData<N>> arcs) {
		this.nodeContainer = nodeContainer;
		this.arcs = arcs;
	}
	
	public N getSource(int arcId) {
		return arcs.get(arcId).source;
	}
	
	public N getTarget(int arcId) {
		return arcs.get(arcId).target;
	}
	
	public Iterator<Integer> getArcIterator() {
		return arcs.keyIterator();
	}
	
	private class OutArcIterator extends UnmodifiableIterator<Integer> {

		private N n;
		
		private int lastReturnedId = -1;
		
		public OutArcIterator(N n) {
			this.n = n;
		}
		
		@Override
		public boolean hasNext() {
			if (lastReturnedId == -1) {
				return nodeContainer.get(n).firstOut >= 0;
			} else {
				return arcs.get(lastReturnedId).nextOut >= 0;
			}
		}

		@Override
		public Integer next() {
			if (lastReturnedId == -1) {
				lastReturnedId = nodeContainer.get(n).firstOut;
			} else {
				lastReturnedId = arcs.get(lastReturnedId).nextOut;
			}
			return lastReturnedId;
		}
	}
	
	public Iterator<Integer> getOutArcIterator(N node) {
		return new OutArcIterator(node);
	}
	
	private class InArcIterator extends UnmodifiableIterator<Integer> {

		private N n;
		
		private int lastReturnedId = -1;
		
		public InArcIterator(N n) {
			this.n = n;
		}
		
		@Override
		public boolean hasNext() {
			if (lastReturnedId == -1) {
				return nodeContainer.get(n).firstIn >= 0;
			} else {
				return arcs.get(lastReturnedId).nextIn >= 0;
			}
		}

		@Override
		public Integer next() {
			if (lastReturnedId == -1) {
				lastReturnedId = nodeContainer.get(n).firstIn;
			} else {
				lastReturnedId = arcs.get(lastReturnedId).nextIn;
			}
			return lastReturnedId;
		}
	}
	
	public Iterator<Integer> getInArcIterator(N node) {
		return new InArcIterator(node);
	}
	
	public Iterator<Integer> getIncidentArcIterator(N node) {
		return new ConcatIterator<Integer>(
				getOutArcIterator(node), getInArcIterator(node));
	}
	
	public Integer addArc(N source, N target) {
		N s = (N) source;
		N t = (N) target;
		ArcData<N> e = new ArcData<N>(s, t);
		int id = arcs.add(e);
		if (nodeContainer.get(s).lastOut >= 0) {
			arcs.get(nodeContainer.get(s).lastOut).nextOut = id;
			e.prevOut = nodeContainer.get(s).lastOut;
			nodeContainer.get(s).lastOut = id;
		} else {
			nodeContainer.get(s).firstOut = id;
			nodeContainer.get(s).lastOut = id;
		}
		if (nodeContainer.get(t).lastIn >= 0) {
			arcs.get(nodeContainer.get(t).lastIn).nextIn = id;
			e.prevIn = nodeContainer.get(t).lastIn;
			nodeContainer.get(t).lastIn = id;
		} else {
			nodeContainer.get(t).firstIn = id;
			nodeContainer.get(t).lastIn = id;
		}
		return id;
	}
	
	public void remove(Integer arcId) {
		ArcData<N> e = arcs.get(arcId);
		if (e.prevOut >= 0) {
			arcs.get(e.prevOut).nextOut = e.nextOut;
		} else {
			nodeContainer.get(e.source).firstOut = e.nextOut;
		}
		if (e.nextOut >= 0) {
			arcs.get(e.nextOut).prevOut = e.prevOut;
		} else {
			nodeContainer.get(e.source).lastOut = e.prevOut;
		}
		if (e.prevIn >= 0) {
			arcs.get(e.prevIn).nextIn = e.nextIn;
		} else {
			nodeContainer.get(e.target).firstIn = e.nextIn;
		}
		if (e.nextIn >= 0) {
			arcs.get(e.nextIn).prevIn = e.prevIn;
		} else {
			nodeContainer.get(e.target).lastIn = e.prevIn;
		}
		arcs.remove(arcId);
	}
}
