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

public class StaticSparseArrayListTest {
	
	private StaticSparseArrayList<Integer> s;
	
	@Before
	public void setUp() {
		s = new StaticSparseArrayList<Integer>();
	}
	
	public void empty() {
		assertFalse(s.entryIterator().hasNext());
		assertFalse(s.keyIterator().hasNext());
		assertFalse(s.valueIterator().hasNext());
	}
	
	@Test
	public void add1() {
		s.put(0, 5);
		assertEquals("[0=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[0]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
				
		s.put(1, 6);
		assertEquals("[0=5, 1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[0, 1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 6]", Iterators.toString(s.valueIterator()));
	}
	
	@Test
	public void add2() {
		s.put(1, 6);
		assertEquals("[1=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[6]", Iterators.toString(s.valueIterator()));
				
		s.put(0, 5);
		assertEquals("[1=6, 0=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 0]", Iterators.toString(s.keyIterator()));
		assertEquals("[6, 5]", Iterators.toString(s.valueIterator()));
	}
	
	@Test
	public void add3() {
		s.put(1, 5);
		assertEquals("[1=5]", Iterators.toString(s.entryIterator()));
		assertEquals("[1]", Iterators.toString(s.keyIterator()));
		assertEquals("[5]", Iterators.toString(s.valueIterator()));
				
		s.put(2, 6);
		assertEquals("[1=5, 2=6]", Iterators.toString(s.entryIterator()));
		assertEquals("[1, 2]", Iterators.toString(s.keyIterator()));
		assertEquals("[5, 6]", Iterators.toString(s.valueIterator()));
	}
}
