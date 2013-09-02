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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterators;

public class SparseArrayListTest {
	
	private SparseArrayList<Integer> s;
	
	@Before
	public void setUp() {
		s = new SparseArrayList<Integer>();
	}
	
	@Test
	public void add() {
		assertFalse(s.entryIterator().hasNext());
		assertFalse(s.keyIterator().hasNext());
		assertFalse(s.valueIterator().hasNext());
		assertFalse(s.freeKeyIterator().hasNext());
		
		assertEquals(0, s.add(5));
		assertEquals("[0=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[0]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[]", Iterators.toString(s.freeKeyIterator()));
		
		assertEquals(1, s.add(6));
		assertEquals("[0=5, 1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[0, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 6]", Iterators.toString(s.valueIterator()));
		assertEquals("[]", Iterators.toString(s.freeKeyIterator()));
	}
	
	@Test
	public void remove1() {
		int i0 = s.add(5);
		int i1 = s.add(6);
		
		s.remove(i0);
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals("[0]", Iterators.toString(s.freeKeyIterator()));
		
		s.remove(i1);
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals("[1, 0]", Iterators.toString(s.freeKeyIterator()));
	}
	
	@Test
	public void remove2() {
		int i0 = s.add(5);
		int i1 = s.add(6);
		
		s.remove(i1);
		assertEquals("[0=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[0]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[1]", Iterators.toString(s.freeKeyIterator()));
		
		s.remove(i0);
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals("[0, 1]", Iterators.toString(s.freeKeyIterator()));
	}
	
	@Test
	public void reAdd1() {
		int i0 = s.add(5);
		s.add(6);
		
		s.remove(i0);
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals("[0]", Iterators.toString(s.freeKeyIterator()));
		
		s.add(7);
		assertEquals("[1=6, 0=7]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 0]", Iterators.toString(s.keyIterator()));
		assertEquals("[6, 7]", Iterators.toString(s.valueIterator()));
		assertEquals("[]", Iterators.toString(s.freeKeyIterator()));
	}
	
	@Test
	public void reAdd2() {
		s.add(5);
		int i1 = s.add(6);
		
		s.remove(i1);
		assertEquals("[0=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[0]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[1]", Iterators.toString(s.freeKeyIterator()));
		
		s.add(7);
		assertEquals("[0=5, 1=7]", Iterators.toString(s.entryIterator()));
		assertEquals("[0, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 7]", Iterators.toString(s.valueIterator()));
		assertEquals("[]", Iterators.toString(s.freeKeyIterator()));
	}
}
