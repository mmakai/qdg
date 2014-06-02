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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import qdg.api.EntityMap;

public class UnionFindTest {

	private static class M<K, V> extends HashMap<K, V>
		implements EntityMap<K, V> {
	}
	
	private UnionFind<String> uf;
	
	@Before
	public void setUp() {
		M<String, String> parent = new M<String, String>();
		M<String, Integer> rank = new M<String, Integer>();
		uf = new UnionFind<String>(parent, rank);
	}
	
	@Test
	public void empty() {
		assertNull(uf.find("A"));
	}
	
	@Test
	public void singletons() {
		uf.add("A");
		uf.add("B");
		assertEquals("A", uf.find("A"));
		assertEquals("B", uf.find("B"));
	}
	
	@Test
	public void union() {
		uf.add("A");
		uf.add("B");
		uf.add("C");
		uf.add("D");
		uf.union("A", "B");
		assertEquals("A", uf.find("A"));
		assertEquals("A", uf.find("B"));
		uf.union("C", "D");
		assertEquals("C", uf.find("C"));
		assertEquals("C", uf.find("D"));
		uf.union("A", "D");
		assertEquals("A", uf.find("A"));
		assertEquals("A", uf.find("B"));
		assertEquals("A", uf.find("C"));
		assertEquals("A", uf.find("D"));
	}
}
