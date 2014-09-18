/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/template/Attic/DolphinQueryTemplate.java,v 1.1.2.1 2011/08/02 09:19:33 neo-cvs Exp $
 * $Revision: 1.1.2.1 $
 * $Date: 2011/08/02 09:19:33 $
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
package com.yttimes.dolphin.support.template;

import com.yttimes.dolphin.support.JPAService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * @author Neo
 * @version $Id: DolphinQueryTemplate.java,v 1.1.2.1 2011/08/02 09:19:33 neo-cvs Exp $
 */
public class DolphinQueryTemplate {

  private Logger logger = LoggerFactory.getLogger(DolphinQueryTemplate.class);

  public <T> T query(TransactionQueryCallback<T> callback) {
    EntityManager em = null;
    T t = null;
    try {
      logger.debug("dolphin engine in query mode..");
      em = JPAService.getInstance().getEntityManager();
      t = callback.doInTransaction(em);
    } catch (Exception ex) {
      logger.error("error occurs in query mode..", ex);
    } finally {
      logger.debug("dolphin query end....");
      if (em != null) {
        em.close();
      }
    }
    return t;
  }
}
