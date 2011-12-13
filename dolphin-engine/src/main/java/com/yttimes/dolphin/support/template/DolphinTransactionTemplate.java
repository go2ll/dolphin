/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/template/Attic/DolphinTransactionTemplate.java,v 1.1.2.1 2011/08/02 09:19:33 neo-cvs Exp $
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

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yttimes.dolphin.support.JPAService;

/**
 *
 * @author Neo
 * @version $Id: DolphinTransactionTemplate.java,v 1.1.2.1 2011/08/02 09:19:33 neo-cvs Exp $
 */
public class DolphinTransactionTemplate {

	private Logger logger = LoggerFactory.getLogger(DolphinTransactionTemplate.class);

	private UserTransaction transaction;

	public DolphinTransactionTemplate(UserTransaction ut) {
		this.transaction = ut;
	}

	public void execute(TransactionExecuteCallback callback) {
		EntityManager em = null;
		try {
			logger.debug("dolphin engine in JTA transaction..");
			transaction.begin();
			em = JPAService.getInstance().getEntityManager();
			callback.doInTransaction(em);
			transaction.commit();
		} catch (Exception ex) {
			logger.error("error occurs in JTA transaction, rollback...", ex);
			try {
				transaction.rollback();
			} catch (Exception e) {
				logger.error("error occurs when rollback the JTA transaction", ex);
			}
		} finally {
			logger.debug("JTA transaction end....");
			if (em != null)
				em.close();
		}
	}

	public <T> T query(TransactionQueryCallback<T> callback) {
		EntityManager em = null;
		T t = null;
		try {
			logger.debug("dolphin engine in JTA transaction..");
			transaction.begin();
			em = JPAService.getInstance().getEntityManager();
			t = callback.doInTransaction(em);
			transaction.commit();
		} catch (Exception ex) {
			logger.error("error occurs in JTA transaction, rollback...", ex);
			try {
				transaction.rollback();
			} catch (Exception e) {
				logger.error("error occurs when rollback the JTA transaction", ex);
			}
		} finally {
			logger.debug("JTA transaction end....");
			if (em != null)
				em.close();
		}
		return t;
	}

}
