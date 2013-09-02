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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import qdg.api.EntityMap;
import qdg.api.bits.HasId;
import qdg.api.bits.MutationHandler;

import com.google.common.base.Function;

public abstract class AbstractIdMap<K, V>
		implements EntityMap<K, V>, MutationHandler<K>, Serializable {
	
	private static final long serialVersionUID = -5082204439413140514L;
	
	private List<V> map = new ArrayList<V>();
	
	@Override
	public V put(K k, V v) {
		int id = ((HasId) k).getId();
		while (map.size() <= id) {
			map.add(null);
		}
		return map.set(id, v);
	}

	@Override
	public V get(Object k) {
		int id = ((HasId) k).getId();
		if (map.size() <= id) {
			return null;
		} else {
			return map.get(id);
		}
	}
	
	private class PrintEntry implements Function<K, String> {

		@Override
		public String apply(K k) {
			return "" + k + "=" + get(k);
		}
	};
	
	protected transient Function<K, String> f = new PrintEntry();

	@Override
	public void onAdd(K k) {
		// Adding nulls to the map are done lazily in put.
	}

	@Override
	public void onRemove(K k) {
		int id = ((HasId) k).getId();
		if (id < map.size()) {
			map.set(id, null);
		}
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		f = new PrintEntry();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}
}
