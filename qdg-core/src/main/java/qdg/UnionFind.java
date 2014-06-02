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

import qdg.api.EntityMap;

/**
 * Simple union-find datastructure without path compression.
 * 
 * With constant-time parent and rank map operations all union-find
 * operations are of logarithmic time complexity.
 *
 * @param <T>
 */
public class UnionFind<T> {
	
	public EntityMap<T, T> parent;
	
	public EntityMap<T, Integer> rank;
	
	public UnionFind(EntityMap<T, T> parent, EntityMap<T, Integer> rank) {
		this.parent = parent;
		this.rank = rank;
	}
	
	public void union(T t1, T t2) {
		T root1 = find(t1);
		T root2 = find(t2);
		if (root1.equals(root2)) {
			return;
		}
		int rank1 = rank.get(root1);
		if (rank1 < rank.get(root2)) {
			parent.put(root1, root2);
		} else if (rank1 > rank.get(root2)) {
			parent.put(root2, root1);
		} else {
			parent.put(root2, root1);
			rank.put(root1, rank1);
		}
	}
	
	public T find(T t) {
		while (true) {
			if (t == null) {
				return null;
			}
			T root = parent.get(t);
			if (t.equals(root)) {
				return root;
			} else {
				t = root;
			}
		}
	}
	
	public void add(T t) {
		parent.put(t, t);
		rank.put(t, 0);
	}
}
