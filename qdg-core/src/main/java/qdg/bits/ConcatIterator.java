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

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.collect.UnmodifiableIterator;

/**
 * Combines multiple iterators into an single unmodifiable iterator.
 * 
 * Though com.google.common.collect.Iterators provides a general way to
 * combine iterators, its special case for combining 2, 3 or 4
 * iterators just calls the general implementation. A specialization
 * for 2 iterators can be slightly faster for several applications.
 *
 * @param <T>
 */
public class ConcatIterator<T> extends UnmodifiableIterator<T> {

	private Iterator<T> i0, i1;
	
	public ConcatIterator(Iterator<T> i0, Iterator<T> i1) {
		this.i0 = i0;
		this.i1 = i1;
	}

	@Override
	public boolean hasNext() {
		return i0.hasNext() || i1 != null && i1.hasNext();
	}

	@Override
	public T next() {
		if (i0.hasNext()) {
			return i0.next();
		} else if (i1 != null) {
			i0 = i1;
			i1 = null;
			if (i0.hasNext()) {
				return i0.next();
			} else {
				throw new NoSuchElementException();
			}
		} else {
			throw new NoSuchElementException();
		}
	}
}