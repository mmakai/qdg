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
	public void empty() {
		assertFalse(s.entryIterator().hasNext());
		assertFalse(s.keyIterator().hasNext());
		assertFalse(s.valueIterator().hasNext());
		assertFalse(s.freeKeyIterator().hasNext());
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
		assertEquals(s.firstFree, -1);
		
		assertNull(s.remove(0));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
		assertEquals(s.firstFree, -1);
	}

	@Test
	public void putAndRemove1() {
		assertNull(s.put(1, 5));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 0);
		
		assertEquals(new Integer(5), s.put(1, 6));
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals("[0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 0);
		
		assertNull(s.remove(0));
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals("[0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 0);
		
		assertEquals(new Integer(6), s.remove(1));
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals("[1, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
		assertEquals(s.firstFree, 1);
	}
	
	@Test
	public void putAndRemove1InOtherOrder() {
		assertNull(s.put(1, 5));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 0);
		
		assertNull(s.put(3, 6));
		assertEquals("[1=5, 3=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 3]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 6]", Iterators.toString(s.valueIterator()));
		assertEquals("[2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 3);
		assertEquals(s.firstFree, 2);
		
		assertNull(s.remove(2));
		assertEquals("[1=5, 3=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 3]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 6]", Iterators.toString(s.valueIterator()));
		assertEquals("[2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 3);
		assertEquals(s.firstFree, 2);
		
		assertEquals(new Integer(6), s.remove(3));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[3, 2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 3);
		
		assertEquals(new Integer(5), s.remove(1));
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals("[1, 3, 2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
		assertEquals(s.firstFree, 1);
	}
	
	@Test
	public void putAndRemove1InSameOrder() {
		assertNull(s.put(3, 6));
		assertEquals("[3=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[3]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals("[2, 1, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 3);
		assertEquals(s.lastUsed, 3);
		assertEquals(s.firstFree, 2);
		
		assertNull(s.put(1, 5));
		assertEquals("[3=6, 1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[3, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6, 5]", Iterators.toString(s.valueIterator()));
		assertEquals("[2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 3);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 2);
		
		assertNull(s.remove(2));
		assertEquals("[3=6, 1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[3, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6, 5]", Iterators.toString(s.valueIterator()));
		assertEquals("[2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 3);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 2);
		
		assertEquals(new Integer(6), s.remove(3));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[3, 2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 3);
		
		assertEquals(new Integer(5), s.remove(1));
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals("[1, 3, 2, 0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
		assertEquals(s.firstFree, 1);
	}
	
	@Test
	public void reAdd1() {
		assertEquals(0, s.add(5));
		assertEquals(1, s.add(6));
		
		assertEquals(new Integer(5), s.remove(0));
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals("[0]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, 0);
		
		assertEquals(0, s.add(7));
		assertEquals("[1=6, 0=7]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 0]", Iterators.toString(s.keyIterator()));
		assertEquals("[6, 7]", Iterators.toString(s.valueIterator()));
		assertEquals("[]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 0);
		assertEquals(s.firstFree, -1);
	}
	
	@Test
	public void reAdd2() {
		assertEquals(0, s.add(5));
		assertEquals(1, s.add(6));
		
		assertEquals(new Integer(6), s.remove(1));
		assertEquals("[0=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[0]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals("[1]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 0);
		assertEquals(s.lastUsed, 0);
		assertEquals(s.firstFree, 1);
		
		assertEquals(1, s.add(7));
		assertEquals("[0=5, 1=7]", Iterators.toString(s.entryIterator()));
		assertEquals("[0, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 7]", Iterators.toString(s.valueIterator()));
		assertEquals("[]", Iterators.toString(s.freeKeyIterator()));
		assertEquals(s.firstUsed, 0);
		assertEquals(s.lastUsed, 1);
		assertEquals(s.firstFree, -1);
	}
}
