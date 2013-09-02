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

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.google.common.collect.Iterators;

public class ConcatIteratorTest {
	
	@Test
	public void test0() {
		Iterator<Integer> i0 = Iterators.emptyIterator();
		Iterator<Integer> i1 = Iterators.emptyIterator();
		assertFalse(new ConcatIterator<Integer>(i0, i1).hasNext());
	}
	
	@Test
	public void test1() {
		Iterator<Integer> i0 = Iterators.forArray(3, 5);
		Iterator<Integer> i1 = Iterators.emptyIterator();
		assertTrue(Iterators.elementsEqual(Iterators.forArray(3, 5),
				new ConcatIterator<Integer>(i0, i1)));
	}
	
	@Test
	public void test2() {
		Iterator<Integer> i0 = Iterators.emptyIterator();
		Iterator<Integer> i1 = Iterators.forArray(7, 9);
		assertTrue(Iterators.elementsEqual(Iterators.forArray(7, 9),
				new ConcatIterator<Integer>(i0, i1)));
	}
	
	@Test
	public void test3() {
		Iterator<Integer> i0 = Iterators.forArray(3, 5);
		Iterator<Integer> i1 = Iterators.forArray(7, 9);
		assertTrue(Iterators.elementsEqual(Iterators.forArray(3, 5, 7, 9),
				new ConcatIterator<Integer>(i0, i1)));
	}
}
