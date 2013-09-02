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

package qdg.jung;

import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * Unmodifiable collection wrapping an Iterable.
 * Methods which would result linear runtime and constant size output are
 * not implemented. 
 *
 * @param <E>
 */
public class IterableWrapper<E> extends AbstractCollection<E> {

	private Iterable<E> iterable;
	
	public IterableWrapper(Iterable<E> iterable) {
		this.iterable = iterable;
	}
	
	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> iterator() {
		return iterable.iterator();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}
}