/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/Attic/JPAService.java,v 1.1.2.8 2011/11/04 10:39:30 neo-cvs Exp $
 * $Revision: 1.1.2.8 $
 * $Date: 2011/11/04 10:39:30 $
 *
 * ==================================================================== 
 *
 * Copyright (c) 2011 YT Times Co., Ltd All Rights Reserved. 
 * This software is the confidential and proprietary information of 
 * YT Times Co., Ltd. You shall not disclose such Confidential 
 * Information. 
 *
 * ==================================================================== 
 *
 */
package com.yttimes.dolphin.support;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author Ivan Li
 * @version $Id: JPAService.java,v 1.1.2.8 2011/11/04 10:39:30 neo-cvs Exp $
 */
public class JPAService {

  private final ThreadLocal<EntityManager>
      entityManagerThreadLocal =
      new ThreadLocal<EntityManager>();

  private EntityManagerFactory emFactory;

  private JPAService() {
  }

  public static JPAService getInstance() {
    return JpaServiceHolder.JPA_SERVICE;
  }

  public void begin() {
    if (emFactory == null) {
      System.out.println("emFactory not initialized...");
      createJPAContext();
    }
    entityManagerThreadLocal.set(emFactory.createEntityManager());
  }

  public void end() {
    EntityManager em = entityManagerThreadLocal.get();
    if (null == em) {
      return;
    }
    em.close();
    entityManagerThreadLocal.remove();
  }

  public void flush() {
    getEntityManager().flush();
  }

  public void beginTransaction() {
    getEntityManager().getTransaction().begin();
  }

  public void setRollbackOnly() {
    EntityTransaction tx = getEntityManager().getTransaction();
    if (tx != null) {
      tx.setRollbackOnly();
    }
  }

  public void commitTransaction() {
    EntityTransaction tx = getEntityManager().getTransaction();
    if (tx != null) {
      if (tx.getRollbackOnly()) {
        tx.rollback();
      } else {
        tx.commit();
      }
    }
  }

  public void rollbackTransaction() {
    EntityTransaction tx = getEntityManager().getTransaction();
    if (tx != null) {
      tx.rollback();
    }
  }

  public void createJPAContext() {
    this.emFactory = Persistence.createEntityManagerFactory("dolphin");

  }

  public void createJPAContext(EntityManagerFactory emf) {
    this.emFactory = emf;
  }

  public void releaseJPAContext() {
    if (emFactory == null) {
      throw new RuntimeException("EntityManager factory has not been initialized!!!!");
    }
    entityManagerThreadLocal.set(null);
    emFactory.close();
    emFactory = null;
  }

  public EntityManager getEntityManager() {
    if (entityManagerThreadLocal.get() == null) {
      begin();
    }
    EntityManager em = entityManagerThreadLocal.get();
    return em;
  }

  public void releaseEntityManager() {
    getEntityManager().close();
    entityManagerThreadLocal.set(null);
  }

  private static class JpaServiceHolder {

    private static final JPAService JPA_SERVICE = new JPAService();
  }
}
