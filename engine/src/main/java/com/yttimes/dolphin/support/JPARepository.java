package com.yttimes.dolphin.support;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class JPARepository<E, K extends Serializable> implements IRepository<K, E> {

  @SuppressWarnings("unused")
  @PersistenceContext
  private EntityManager entityManager;

  protected Class<E> entityClass;

  public void persist(E entity) {
    getEntityManager().persist(entity);
  }

  public void remove(E entity) {
    getEntityManager().remove(entity);
  }

  public E findById(K id) {
    return getEntityManager().find(entityClass, id);
  }

  public E refresh(E entity) {
    getEntityManager().refresh(entity);
    return entity;
  }

  public void refresh(List<E> entities) {
    for (E entity : entities) {
      getEntityManager().refresh(entity);
    }
  }

  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public EntityManager getEntityManager() {
    return JPAService.getInstance().getEntityManager();
  }
}
