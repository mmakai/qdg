package qdg;

import java.util.Iterator;

import qdg.api.EntityMap;

public interface SparseArrayMap<E> extends EntityMap<Integer, E> {

	Iterator<Integer> keyIterator();
}
