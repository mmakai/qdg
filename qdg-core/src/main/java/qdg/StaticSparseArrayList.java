/*
 * Copyright (C) 2013, 2014 Marton Makai
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

/**
 * An Integer -> E map stored in a sparse array. The integer keys serve as
 * indices of the array.
 * 
 * @author Marton Makai
 *
 * @param <E>
 */
public class StaticSparseArrayList<E> implements SparseArrayMap<E>, Serializable {
	
	private static final long serialVersionUID = -4891243247383504348L;

	private static class LinkedElement<F> implements Serializable {
		
		private static final long serialVersionUID = -539690895763695408L;

		private F f;
		
		private int previous = -1;
		
		private int next = -1;
	}
	
	protected List<LinkedElement<E>> container =
			new ArrayList<LinkedElement<E>>();
	
	protected int firstUsed = -1;
	
	// Free elements are not linked.
	protected int lastUsed = -1;
	
	public E get(int i) {
		if (i >= container.size()) {
			return null;
		} else {
			return container.get(i).f;
		}
	}

	@Override
	public E get(Object k) {
		int i = (Integer) k;
		return get(i);
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

	public E remove(int id) {
		if (id >= container.size()) {
			return null;
		}
		LinkedElement<E> removedElement = container.get(id);
		if (removedElement.f != null) {
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
			E oldValue = removedElement.f;
			removedElement.f = null;
			return oldValue;
		} else {
			return null;
		}
	}
	
	@Override
	public E put(Integer k, E v) {
		if (v == null) {
			throw new IllegalArgumentException();
		}
		while (k >= container.size()) {
			LinkedElement<E> newElement = new LinkedElement<E>();
			container.add(newElement);
		}
		LinkedElement<E> newElement = container.get(k);
		if (newElement.f != null) {
			E oldValue = newElement.f;
			newElement.f = v;
			return oldValue;
		} else {
			newElement.f = v;
			newElement.previous = lastUsed;
			newElement.next = -1;
			// firstUsed >= 0 and lastUsed >= 0 are equivalent.
			if (firstUsed >= 0) {
				container.get(lastUsed).next = k;
			} else {
				firstUsed = k;
			}
			lastUsed = k;
			return null;
		}
	}
}
