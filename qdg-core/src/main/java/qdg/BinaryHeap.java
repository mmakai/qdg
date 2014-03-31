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

package qdg;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import qdg.api.EntityMap;

public class BinaryHeap<K, V> {
	
	private EntityMap<K, Integer> map;
	
	private List<Entry<K, V>> heap = new ArrayList<Entry<K, V>>();
	
	public BinaryHeap(EntityMap<K, Integer> map) {
		this.map = map;
	}
	
	public void add(K k, V v) {
		if (map.get(k) == null) {
			int i = heap.size();
			map.put(k, i);
			heap.add(new AbstractMap.SimpleEntry<K, V>(k, v));
			bubbleUp(i);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void decrease(K k, V v) {
		int i = map.get(k);
		Entry<K, V> e = heap.get(i);
		e.setValue(v);
		bubbleUp(i);
	}
	
	public Entry<K, V> peek() {
		if (heap.isEmpty()) {
			return null;
		} else {
			return heap.get(0);
		}
	}
	
	public Entry<K, V> poll() {
		if (heap.isEmpty()) {
			return null;
		} else if (heap.size() == 1) {
			Entry<K, V> e = heap.remove(0);
			map.put(e.getKey(), null);
			return e;
		} else {
			Entry<K, V> e = heap.get(0);
			map.put(e.getKey(), null);
			Entry<K, V> f = heap.remove(heap.size() - 1);
			heap.set(0, f);
			map.put(f.getKey(), 0);
			bubbleDown();
			return e;
		}
	}
	
	public V get(Object k) {
		K key = (K) k;
		Integer i = map.get(key);
		if (i != null) {
			return heap.get(i).getValue();
		} else {
			return null;
		}
	}
	
	private void bubbleUp(int i) {
		while (i > 0) {
			int pi = (i - 1) / 2;
			Entry<K, V> parent = heap.get(pi);
			V v = heap.get(i).getValue();
			@SuppressWarnings("unchecked")
			int c = ((Comparable<V>) parent.getValue()).compareTo(v);
			if (c > 0) {
				swap(pi, i);
				i = pi;
			} else {
				break;
			}
		}
	}
	
	private void bubbleDown() {
		int i = 0;
		int l = 2 * i + 1;
		while (l < heap.size()) {
			V v = heap.get(i).getValue();
			int r = 2 * i + 2;
			V left = heap.get(l).getValue();
			if (r >= heap.size() || ((Comparable<V>) left).compareTo(heap.get(r).getValue()) <= 0) {
				@SuppressWarnings("unchecked")
				int c = ((Comparable<V>) v).compareTo(left);
				if (c > 0) {
					swap(i, l);
					i = l;
					l = 2 * i + 1;
				} else {
					break;
				}
			} else {
				V right = heap.get(r).getValue();
				@SuppressWarnings("unchecked")
				int c = ((Comparable<V>) v).compareTo(right);
				if (c > 0) {
					swap(i, r);
					i = r;
					l = 2 * i + 1;
				} else {
					break;
				}
			}
		}
	}
	
	private void swap(int i, int j) {
		Entry<K, V> ei = heap.get(i);
		Entry<K, V> ej = heap.get(j);
		map.put(ei.getKey(), j);
		map.put(ej.getKey(), i);
		heap.set(i, ej);
		heap.set(j, ei);
	}
	
	public boolean isEmpty() {
		return heap.isEmpty();
	}
}
