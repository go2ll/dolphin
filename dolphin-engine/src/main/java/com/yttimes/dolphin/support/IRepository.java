package com.yttimes.dolphin.support;

import java.io.Serializable;

public interface IRepository<K extends Serializable, E> {
	public void persist(E entity);

	public void remove(E entity);

	public E findById(K id);
}
