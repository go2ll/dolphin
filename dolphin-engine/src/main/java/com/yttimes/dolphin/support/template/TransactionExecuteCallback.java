package com.yttimes.dolphin.support.template;

import javax.persistence.EntityManager;

public interface TransactionExecuteCallback {
	public void doInTransaction(EntityManager em) throws Exception;
}
