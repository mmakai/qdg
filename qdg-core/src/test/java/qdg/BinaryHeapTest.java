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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Joiner;

import qdg.api.EntityMap;

public class BinaryHeapTest {

	@SuppressWarnings("serial")
	private static class Map<K, V> extends HashMap<K, V>
		implements EntityMap<K, V> {
	};
	
	private EntityMap<String, Integer> map = new Map<String, Integer>();
	
	private BinaryHeap<String, Double> heap = new BinaryHeap<String, Double>(map);
	
	private static <K, V> String peekAll(BinaryHeap<K, V> heap) {
		List<String> l = new ArrayList<String>();
		while (heap.peek() != null) {
			l.add(heap.peek().toString());
			heap.poll();
		}
		return "[" + Joiner.on(", ").join(l) + "]";
	}
	
	private static <K, V> String pollAll(BinaryHeap<K, V> heap) {
		List<String> l = new ArrayList<String>();
		while (heap.peek() != null) {
			l.add(heap.poll().toString());
		}
		return "[" + Joiner.on(", ").join(l) + "]";
	}
	
	@Test
	public void empty() {
		assertNull(heap.peek());
		assertNull(heap.poll());
	}
	
	@Test
	public void single1() {
		heap.add("A", 1.0);
		assertEquals("[A=1.0]", peekAll(heap).toString());
		heap.add("A", 2.0);
		assertEquals("[A=2.0]", peekAll(heap).toString());
	}
	
	@Test
	public void single2() {
		heap.add("A", 1.0);
		assertEquals("[A=1.0]", pollAll(heap).toString());
		heap.add("A", 2.0);
		assertEquals("[A=2.0]", pollAll(heap).toString());
	}
	
	@Test
	public void two1() {
		heap.add("A", 1.0);
		heap.add("B", 2.0);
		assertEquals("[A=1.0, B=2.0]", pollAll(heap).toString());
		
		heap.add("B", 2.0);
		heap.add("A", 1.0);
		assertEquals("[A=1.0, B=2.0]", pollAll(heap).toString());
	}
	
	@Test
	public void two2() {
		heap.add("A", 1.0);
		heap.add("B", 2.0);
		assertEquals("[A=1.0, B=2.0]", peekAll(heap).toString());
		
		heap.add("B", 2.0);
		heap.add("A", 1.0);
		assertEquals("[A=1.0, B=2.0]", peekAll(heap).toString());
	}
	
	@Test
	public void more1() {
		heap.add("A", 1.0);
		heap.add("B", 2.0);
		heap.add("C", 3.0);
		heap.add("D", 4.0);
		heap.add("E", 5.0);
		heap.add("F", 6.0);
		heap.add("G", 7.0);
		heap.add("H", 8.0);
		assertEquals("[A=1.0, B=2.0, C=3.0, D=4.0, E=5.0, F=6.0, G=7.0, H=8.0]", pollAll(heap).toString());
		
		heap.add("H", 8.0);
		heap.add("G", 7.0);
		heap.add("F", 6.0);
		heap.add("E", 5.0);
		heap.add("D", 4.0);
		heap.add("C", 3.0);
		heap.add("B", 2.0);
		heap.add("A", 1.0);
		assertEquals("[A=1.0, B=2.0, C=3.0, D=4.0, E=5.0, F=6.0, G=7.0, H=8.0]", pollAll(heap).toString());
	}
	
	@Test
	public void more2() {
		heap.add("A", 1.0);
		heap.add("B", 2.0);
		heap.add("C", 3.0);
		heap.add("D", 4.0);
		heap.add("E", 5.0);
		heap.add("F", 6.0);
		heap.add("G", 7.0);
		heap.add("H", 8.0);
		assertEquals("[A=1.0, B=2.0, C=3.0, D=4.0, E=5.0, F=6.0, G=7.0, H=8.0]", peekAll(heap).toString());
		
		heap.add("H", 8.0);
		heap.add("G", 7.0);
		heap.add("F", 6.0);
		heap.add("E", 5.0);
		heap.add("D", 4.0);
		heap.add("C", 3.0);
		heap.add("B", 2.0);
		heap.add("A", 1.0);
		assertEquals("[A=1.0, B=2.0, C=3.0, D=4.0, E=5.0, F=6.0, G=7.0, H=8.0]", peekAll(heap).toString());
	}
	
	@Test
	public void decrease() {
		heap.add("A", 81.0);
		heap.add("B", 82.0);
		heap.add("C", 83.0);
		heap.add("D", 84.0);
		heap.add("E", 85.0);
		heap.add("F", 86.0);
		heap.add("G", 87.0);
		heap.add("H", 88.0);
		heap.decrease("H", 8.0);
		heap.decrease("G", 7.0);
		heap.decrease("F", 6.0);
		heap.decrease("E", 5.0);
		heap.decrease("D", 4.0);
		heap.decrease("C", 3.0);
		heap.decrease("B", 2.0);
		heap.decrease("A", 1.0);
		assertEquals("[A=1.0, B=2.0, C=3.0, D=4.0, E=5.0, F=6.0, G=7.0, H=8.0]", peekAll(heap).toString());
		
		heap.add("A", 81.0);
		heap.add("B", 82.0);
		heap.add("C", 83.0);
		heap.add("D", 84.0);
		heap.add("E", 85.0);
		heap.add("F", 86.0);
		heap.add("G", 87.0);
		heap.add("H", 88.0);
		heap.decrease("H", 8.0);
		heap.decrease("G", 7.0);
		heap.decrease("F", 6.0);
		heap.decrease("E", 5.0);
		heap.decrease("D", 4.0);
		heap.decrease("C", 3.0);
		heap.decrease("B", 2.0);
		heap.decrease("A", 1.0);
		assertEquals("[A=1.0, B=2.0, C=3.0, D=4.0, E=5.0, F=6.0, G=7.0, H=8.0]", pollAll(heap).toString());
	}
}
