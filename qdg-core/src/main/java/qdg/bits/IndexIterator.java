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

import java.util.Collection;

import com.google.common.collect.UnmodifiableIterator;

public class IndexIterator<E> extends UnmodifiableIterator<Integer> {
	
	private int i = 0;
	
	private Collection<E> c;
	
	public IndexIterator(Collection<E> c) {
		this.c = c;
	}
	
	@Override
	public boolean hasNext() {
		return (i < c.size());
	}

	@Override
	public Integer next() {
		return i++;
	}
};