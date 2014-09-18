/**
 * $Header: /home/cvsroot/dolphin/dolphin-engine/src/main/java/com/yttimes/dolphin/support/template/Attic/TransactionQueryCallback.java,v 1.1.2.1 2011/08/02 09:19:33 neo-cvs Exp $
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

/**
 * @author Neo
 * @version $Id: TransactionQueryCallback.java,v 1.1.2.1 2011/08/02 09:19:33 neo-cvs Exp $
 */
public interface TransactionQueryCallback<T> {

  public T doInTransaction(EntityManager em) throws Exception;
}
