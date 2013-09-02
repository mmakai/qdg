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

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.UnmodifiableIterator;

import qdg.api.EntityMap;

public class SparseArrayList<E> implements EntityMap<Integer, E>, Serializable {
	
	private static final long serialVersionUID = -4891243247383504348L;

	private static class LinkedElement<F> implements Serializable {
		
		private static final long serialVersionUID = -539690895763695408L;

		private F f;
		
		private int previous;
		
		private int next;
	}
	
	protected List<LinkedElement<E>> container =
			new ArrayList<LinkedElement<E>>();
	
	private int firstUsed = -1;
	
	// Though new elements could be inserted to the front, and then storing
	// the index of the last used position would not be necessary, it still
	// helps a bit in the debugging when the elements come in their original
	// order. Free elements are not double linked.
	private int lastUsed = -1;
	
	private int firstFree = -1;
	
	public E get(int i) {
		return container.get(i).f;
	}
	
	public int add(E e) {
		// New element is added as last in order.
		int newIndex;
		LinkedElement<E> newElement;
		if (firstFree >= 0) {
			newIndex = firstFree;
			newElement = container.get(newIndex);
			firstFree = newElement.next;
		} else {
			newIndex = container.size();
			newElement = new LinkedElement<E>();
			container.add(newElement);
		}
		newElement.f = e;
		newElement.next = -1;
		newElement.previous = lastUsed;
		if (firstUsed < 0) {
			firstUsed = newIndex;
		}
		if (lastUsed >= 0) {
			container.get(lastUsed).next = newIndex;
		}
		lastUsed = newIndex;
		return newIndex;
	}
	
	public void remove(int id) {
		LinkedElement<E> removedElement = container.get(id);
		if (removedElement.previous >= 0) {
			container.get(removedElement.previous).next = removedElement.next;
		} else {
			firstUsed = removedElement.next;
		}
		if (removedElement.next >= 0) {
			container.get(removedElement.next).previous = removedElement.previous;
		} else {
			lastUsed = removedElement.previous;
		}
		removedElement.f = null;
		removedElement.next = firstFree;
		firstFree = id;
	}

	private class KeyIterator extends UnmodifiableIterator<Integer> {

		private int id = -1;
		
		@Override
		public boolean hasNext() {
			if (id == -1) {
				return firstUsed >= 0;
			} else {
				return container.get(id).next >= 0;
			}
		}

		@Override
		public Integer next() {
			if (id == -1) {
				id = firstUsed;
			} else {
				id = container.get(id).next;
			}
			return id;
		}
	}
	
	public Iterator<Integer> keyIterator() {
		return new KeyIterator();
	}
	
	public Iterable<Integer> keys() {
		return new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				return new KeyIterator();
			}
		};
	}
	
	/**
	 * For testing purposes only.
	 */
	private class ValueIterator extends UnmodifiableIterator<E> {

		private Iterator<Integer> id = new KeyIterator();
		
		@Override
		public boolean hasNext() {
			return id.hasNext();
		}

		@Override
		public E next() {
			int nextId = id.next();
			return container.get(nextId).f;
		}
	}
	
	public Iterator<E> valueIterator() {
		return new ValueIterator();
	}
	
	public Iterable<E> values() {
		return new Iterable<E>() {

			@Override
			public Iterator<E> iterator() {
				return new ValueIterator();
			}
		};
	}
	
	private class EntryIterator extends UnmodifiableIterator<Map.Entry<Integer, E>> {

		private Iterator<Integer> id = new KeyIterator();
		
		@Override
		public boolean hasNext() {
			return id.hasNext();
		}
		
		@Override
		public Map.Entry<Integer, E> next() {
			Integer nextId = id.next();
			return new SimpleEntry<Integer, E>(nextId, container.get(nextId).f);
		}
	}
	
	public Iterator<Map.Entry<Integer, E>> entryIterator() {
		return new EntryIterator();
	}
	
	public Iterable<Map.Entry<Integer, E>> entries() {
		return new Iterable<Map.Entry<Integer, E>>() {

			@Override
			public Iterator<Map.Entry<Integer, E>> iterator() {
				return new EntryIterator();
			}
		};
	}
	
	/**
	 * For testing purposes only.
	 */
	private class FreeKeyIterator extends UnmodifiableIterator<Integer> {

		private int id = -1;
		
		@Override
		public boolean hasNext() {
			if (id == -1) {
				return firstFree >= 0;
			} else {
				return container.get(id).next >= 0;
			}
		}

		@Override
		public Integer next() {
			if (id == -1) {
				id = firstFree;
			} else {
				id = container.get(id).next;
			}
			return id;
		}
	}
	
	public Iterator<Integer> freeKeyIterator() {
		return new FreeKeyIterator();
	}
	
	public Iterable<Integer> freeKeys() {
		return new Iterable<Integer>() {

			@Override
			public Iterator<Integer> iterator() {
				return new FreeKeyIterator();
			}
		};
	}

	@Override
	public E put(Integer k, E v) {
		container.get(k).f = v;
		return v;
	}

	@Override
	public E get(Object k) {
		return container.get((Integer) k).f;
	}
}
