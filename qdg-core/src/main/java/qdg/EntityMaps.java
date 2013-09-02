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

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import qdg.api.EntityMap;

public class EntityMaps {

	public static class NullMap<K, V> implements EntityMap<K, V> {

		@Override
		public V put(K k, V v) {
			return null;
		}

		@Override
		public V get(Object k) {
			return null;
		}
	}
	
	public static class ConstMap<K, V> implements EntityMap<K, V> {

		protected V value;
		
		public ConstMap(V value) {
			this.value = value;
		}
		
		@Override
		public V put(K k, V v) {
			throw new UnsupportedOperationException();
		}

		@Override
		public V get(Object k) {
			return value;
		}
	}
	
	public static class DefaultValueMap<K, V> implements EntityMap<K, V> {

		protected EntityMap<K, V> map;
		
		protected Function<?, V> factory;
		
		public DefaultValueMap(EntityMap<K, V> map, Function<?, V> factory) {
			this.map = map;
			this.factory = factory;
		}
		
		@Override
		public V put(K k, V v) {
			return map.put(k, v);
		}

		@SuppressWarnings("unchecked")
		@Override
		public V get(Object k) {
			V v = map.get(k);
			if (v == null) {
				v = factory.apply(null);
				map.put((K) k, v);
			}
			return v;
		}
	}
	
	public static class FunctionAsMap<K, V> implements EntityMap<K, V> {

		protected Function<K, V> function;
		
		public FunctionAsMap(Function<K, V> function) {
			this.function = function;
		}
		
		@Override
		public V put(K k, V v) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		@Override
		public V get(Object k) {
			return function.apply((K) k);
		}
	}
	
	public static class AsFunction<K, V> implements Function<K, V> {

		protected EntityMap<K, V> map;
		
		public AsFunction(EntityMap<K, V> map) {
			this.map = map;
		}

		@Override
		public V apply(K k) {
			return map.get(k);
		}
	}
	
	public static class PredicateAsMap<K> implements EntityMap<K, Boolean> {

		protected Predicate<K> predicate;
		
		public PredicateAsMap(Predicate<K> predicate) {
			this.predicate = predicate;
		}
		
		@Override
		public Boolean put(K k, Boolean v) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		@Override
		public Boolean get(Object k) {
			return predicate.apply((K) k);
		}
	}
	
	public static class AsPredicate<K> implements Predicate<K> {

		protected EntityMap<K, Boolean> map;
		
		public AsPredicate(EntityMap<K, Boolean> map) {
			this.map = map;
		}

		@Override
		public boolean apply(K k) {
			return map.get(k);
		}
	}
	
	public static class WrapperMap<K, L, V> implements EntityMap<K, V> {
		
		protected EntityMap<L, V> map;
		
		protected Function<K, L> wrappedEntityGetter;

		public WrapperMap(EntityMap<L, V> map, Function<K, L> wrappedEntityGetter) {
			this.map = map;
			this.wrappedEntityGetter = wrappedEntityGetter;
		}

		@Override
		public V put(K k, V v) {
			return map.put(wrappedEntityGetter.apply(k), v);
		}

		@SuppressWarnings("unchecked")
		@Override
		public V get(Object k) {
			return map.get(wrappedEntityGetter.apply((K) k));
		}
	}
}
