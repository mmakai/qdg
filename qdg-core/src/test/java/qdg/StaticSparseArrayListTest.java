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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterators;

public class StaticSparseArrayListTest {
	
	private StaticSparseArrayList<Integer> s;
	
	@Before
	public void setUp() {
		s = new StaticSparseArrayList<Integer>();
	}
	
	@Test
	public void empty() {
		assertFalse(s.entryIterator().hasNext());
		assertFalse(s.keyIterator().hasNext());
		assertFalse(s.valueIterator().hasNext());
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
		
		assertNull(s.remove(0));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
		
		assertNull(s.get(100));
		assertNull(s.remove(100));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
	}

	@Test
	public void putAndRemove1() {
		assertNull(s.put(1, 5));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		
		assertEquals(new Integer(5), s.put(1, 6));
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		
		assertNull(s.remove(0));
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		
		assertEquals(new Integer(6), s.remove(1));
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
	}
	
	@Test
	public void putAndRemove1InOtherOrder() {
		assertNull(s.put(1, 5));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		
		assertNull(s.put(3, 6));
		assertEquals("[1=5, 3=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 3]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 6]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 3);
		
		assertNull(s.remove(2));
		assertEquals("[1=5, 3=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 3]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 6]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 3);
		
		assertEquals(new Integer(6), s.remove(3));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		
		assertEquals(new Integer(5), s.remove(1));
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
	}
	
	@Test
	public void putAndRemove1InSameOrder() {
		assertNull(s.put(3, 6));
		assertEquals("[3=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[3]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 3);
		assertEquals(s.lastUsed, 3);
		
		assertNull(s.put(1, 5));
		assertEquals("[3=6, 1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[3, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6, 5]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 3);
		assertEquals(s.lastUsed, 1);
		
		assertNull(s.remove(2));
		assertEquals("[3=6, 1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[3, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6, 5]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 3);
		assertEquals(s.lastUsed, 1);
		
		assertEquals(new Integer(6), s.remove(3));
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, 1);
		assertEquals(s.lastUsed, 1);
		
		assertEquals(new Integer(5), s.remove(1));
		assertEquals("[]", Iterators.toString(s.entryIterator()));
		assertEquals("[]", Iterators.toString(s.keyIterator()));
		assertEquals("[]", Iterators.toString(s.valueIterator()));
		assertEquals(s.firstUsed, -1);
		assertEquals(s.lastUsed, -1);
	}
}
